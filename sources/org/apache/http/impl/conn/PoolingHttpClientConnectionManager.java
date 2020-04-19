package org.apache.http.impl.conn;

import java.io.Closeable;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpClientConnection;
import org.apache.http.HttpHost;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.Lookup;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.conn.ConnectionRequest;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.HttpClientConnectionOperator;
import org.apache.http.conn.HttpConnectionFactory;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.conn.SchemePortResolver;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.pool.ConnFactory;
import org.apache.http.pool.ConnPoolControl;
import org.apache.http.pool.PoolEntryCallback;
import org.apache.http.pool.PoolStats;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;

@Contract(threading = ThreadingBehavior.SAFE_CONDITIONAL)
public class PoolingHttpClientConnectionManager implements HttpClientConnectionManager, ConnPoolControl<HttpRoute>, Closeable {
    private final ConfigData configData;
    private final HttpClientConnectionOperator connectionOperator;
    private final AtomicBoolean isShutDown;
    private final Log log;
    private final CPool pool;

    static class ConfigData {
        private final Map<HttpHost, ConnectionConfig> connectionConfigMap = new ConcurrentHashMap();
        private volatile ConnectionConfig defaultConnectionConfig;
        private volatile SocketConfig defaultSocketConfig;
        private final Map<HttpHost, SocketConfig> socketConfigMap = new ConcurrentHashMap();

        ConfigData() {
        }

        public SocketConfig getDefaultSocketConfig() {
            return this.defaultSocketConfig;
        }

        public void setDefaultSocketConfig(SocketConfig socketConfig) {
            this.defaultSocketConfig = socketConfig;
        }

        public ConnectionConfig getDefaultConnectionConfig() {
            return this.defaultConnectionConfig;
        }

        public void setDefaultConnectionConfig(ConnectionConfig connectionConfig) {
            this.defaultConnectionConfig = connectionConfig;
        }

        public SocketConfig getSocketConfig(HttpHost httpHost) {
            return (SocketConfig) this.socketConfigMap.get(httpHost);
        }

        public void setSocketConfig(HttpHost httpHost, SocketConfig socketConfig) {
            this.socketConfigMap.put(httpHost, socketConfig);
        }

        public ConnectionConfig getConnectionConfig(HttpHost httpHost) {
            return (ConnectionConfig) this.connectionConfigMap.get(httpHost);
        }

        public void setConnectionConfig(HttpHost httpHost, ConnectionConfig connectionConfig) {
            this.connectionConfigMap.put(httpHost, connectionConfig);
        }
    }

    static class InternalConnectionFactory implements ConnFactory<HttpRoute, ManagedHttpClientConnection> {
        private final ConfigData configData;
        private final HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connFactory;

        InternalConnectionFactory(ConfigData configData2, HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> httpConnectionFactory) {
            if (configData2 == null) {
                configData2 = new ConfigData();
            }
            this.configData = configData2;
            if (httpConnectionFactory == null) {
                httpConnectionFactory = ManagedHttpClientConnectionFactory.INSTANCE;
            }
            this.connFactory = httpConnectionFactory;
        }

        public ManagedHttpClientConnection create(HttpRoute httpRoute) throws IOException {
            ConnectionConfig connectionConfig = httpRoute.getProxyHost() != null ? this.configData.getConnectionConfig(httpRoute.getProxyHost()) : null;
            if (connectionConfig == null) {
                connectionConfig = this.configData.getConnectionConfig(httpRoute.getTargetHost());
            }
            if (connectionConfig == null) {
                connectionConfig = this.configData.getDefaultConnectionConfig();
            }
            if (connectionConfig == null) {
                connectionConfig = ConnectionConfig.DEFAULT;
            }
            return (ManagedHttpClientConnection) this.connFactory.create(httpRoute, connectionConfig);
        }
    }

    private static Registry<ConnectionSocketFactory> getDefaultRegistry() {
        return RegistryBuilder.create().register(HttpHost.DEFAULT_SCHEME_NAME, PlainConnectionSocketFactory.getSocketFactory()).register("https", SSLConnectionSocketFactory.getSocketFactory()).build();
    }

    public PoolingHttpClientConnectionManager() {
        this(getDefaultRegistry());
    }

    public PoolingHttpClientConnectionManager(long j, TimeUnit timeUnit) {
        this(getDefaultRegistry(), null, null, null, j, timeUnit);
    }

    public PoolingHttpClientConnectionManager(Registry<ConnectionSocketFactory> registry) {
        this(registry, null, null);
    }

    public PoolingHttpClientConnectionManager(Registry<ConnectionSocketFactory> registry, DnsResolver dnsResolver) {
        this(registry, null, dnsResolver);
    }

    public PoolingHttpClientConnectionManager(Registry<ConnectionSocketFactory> registry, HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> httpConnectionFactory) {
        this(registry, httpConnectionFactory, null);
    }

    public PoolingHttpClientConnectionManager(HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> httpConnectionFactory) {
        this(getDefaultRegistry(), httpConnectionFactory, null);
    }

    public PoolingHttpClientConnectionManager(Registry<ConnectionSocketFactory> registry, HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> httpConnectionFactory, DnsResolver dnsResolver) {
        this(registry, httpConnectionFactory, null, dnsResolver, -1, TimeUnit.MILLISECONDS);
    }

    public PoolingHttpClientConnectionManager(Registry<ConnectionSocketFactory> registry, HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> httpConnectionFactory, SchemePortResolver schemePortResolver, DnsResolver dnsResolver, long j, TimeUnit timeUnit) {
        this((HttpClientConnectionOperator) new DefaultHttpClientConnectionOperator(registry, schemePortResolver, dnsResolver), httpConnectionFactory, j, timeUnit);
    }

    public PoolingHttpClientConnectionManager(HttpClientConnectionOperator httpClientConnectionOperator, HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> httpConnectionFactory, long j, TimeUnit timeUnit) {
        this.log = LogFactory.getLog(getClass());
        this.configData = new ConfigData();
        CPool cPool = new CPool(new InternalConnectionFactory(this.configData, httpConnectionFactory), 2, 20, j, timeUnit);
        this.pool = cPool;
        this.pool.setValidateAfterInactivity(2000);
        this.connectionOperator = (HttpClientConnectionOperator) Args.notNull(httpClientConnectionOperator, "HttpClientConnectionOperator");
        this.isShutDown = new AtomicBoolean(false);
    }

    PoolingHttpClientConnectionManager(CPool cPool, Lookup<ConnectionSocketFactory> lookup, SchemePortResolver schemePortResolver, DnsResolver dnsResolver) {
        this.log = LogFactory.getLog(getClass());
        this.configData = new ConfigData();
        this.pool = cPool;
        this.connectionOperator = new DefaultHttpClientConnectionOperator(lookup, schemePortResolver, dnsResolver);
        this.isShutDown = new AtomicBoolean(false);
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        try {
            shutdown();
        } finally {
            super.finalize();
        }
    }

    public void close() {
        shutdown();
    }

    private String format(HttpRoute httpRoute, Object obj) {
        StringBuilder sb = new StringBuilder();
        sb.append("[route: ");
        sb.append(httpRoute);
        sb.append("]");
        if (obj != null) {
            sb.append("[state: ");
            sb.append(obj);
            sb.append("]");
        }
        return sb.toString();
    }

    private String formatStats(HttpRoute httpRoute) {
        StringBuilder sb = new StringBuilder();
        PoolStats totalStats = this.pool.getTotalStats();
        PoolStats stats = this.pool.getStats(httpRoute);
        sb.append("[total kept alive: ");
        sb.append(totalStats.getAvailable());
        sb.append("; ");
        sb.append("route allocated: ");
        sb.append(stats.getLeased() + stats.getAvailable());
        sb.append(" of ");
        sb.append(stats.getMax());
        sb.append("; ");
        sb.append("total allocated: ");
        sb.append(totalStats.getLeased() + totalStats.getAvailable());
        sb.append(" of ");
        sb.append(totalStats.getMax());
        sb.append("]");
        return sb.toString();
    }

    private String format(CPoolEntry cPoolEntry) {
        StringBuilder sb = new StringBuilder();
        sb.append("[id: ");
        sb.append(cPoolEntry.getId());
        sb.append("]");
        sb.append("[route: ");
        sb.append(cPoolEntry.getRoute());
        sb.append("]");
        Object state = cPoolEntry.getState();
        if (state != null) {
            sb.append("[state: ");
            sb.append(state);
            sb.append("]");
        }
        return sb.toString();
    }

    /* access modifiers changed from: private */
    public SocketConfig resolveSocketConfig(HttpHost httpHost) {
        SocketConfig socketConfig = this.configData.getSocketConfig(httpHost);
        if (socketConfig == null) {
            socketConfig = this.configData.getDefaultSocketConfig();
        }
        return socketConfig == null ? SocketConfig.DEFAULT : socketConfig;
    }

    public ConnectionRequest requestConnection(final HttpRoute httpRoute, Object obj) {
        Args.notNull(httpRoute, "HTTP route");
        if (this.log.isDebugEnabled()) {
            Log log2 = this.log;
            StringBuilder sb = new StringBuilder();
            sb.append("Connection request: ");
            sb.append(format(httpRoute, obj));
            sb.append(formatStats(httpRoute));
            log2.debug(sb.toString());
        }
        final Future lease = this.pool.lease(httpRoute, obj, null);
        return new ConnectionRequest() {
            public boolean cancel() {
                return lease.cancel(true);
            }

            public HttpClientConnection get(long j, TimeUnit timeUnit) throws InterruptedException, ExecutionException, ConnectionPoolTimeoutException {
                HttpHost httpHost;
                HttpClientConnection leaseConnection = PoolingHttpClientConnectionManager.this.leaseConnection(lease, j, timeUnit);
                if (leaseConnection.isOpen()) {
                    if (httpRoute.getProxyHost() != null) {
                        httpHost = httpRoute.getProxyHost();
                    } else {
                        httpHost = httpRoute.getTargetHost();
                    }
                    leaseConnection.setSocketTimeout(PoolingHttpClientConnectionManager.this.resolveSocketConfig(httpHost).getSoTimeout());
                }
                return leaseConnection;
            }
        };
    }

    /* access modifiers changed from: protected */
    public HttpClientConnection leaseConnection(Future<CPoolEntry> future, long j, TimeUnit timeUnit) throws InterruptedException, ExecutionException, ConnectionPoolTimeoutException {
        try {
            CPoolEntry cPoolEntry = (CPoolEntry) future.get(j, timeUnit);
            if (cPoolEntry == null || future.isCancelled()) {
                throw new InterruptedException();
            }
            Asserts.check(cPoolEntry.getConnection() != null, "Pool entry with no connection");
            if (this.log.isDebugEnabled()) {
                Log log2 = this.log;
                StringBuilder sb = new StringBuilder();
                sb.append("Connection leased: ");
                sb.append(format(cPoolEntry));
                sb.append(formatStats((HttpRoute) cPoolEntry.getRoute()));
                log2.debug(sb.toString());
            }
            return CPoolProxy.newProxy(cPoolEntry);
        } catch (TimeoutException unused) {
            throw new ConnectionPoolTimeoutException("Timeout waiting for connection from pool");
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:34:0x00c1, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void releaseConnection(org.apache.http.HttpClientConnection r7, java.lang.Object r8, long r9, java.util.concurrent.TimeUnit r11) {
        /*
            r6 = this;
            java.lang.String r0 = "Managed connection"
            org.apache.http.util.Args.notNull(r7, r0)
            monitor-enter(r7)
            org.apache.http.impl.conn.CPoolEntry r0 = org.apache.http.impl.conn.CPoolProxy.detach(r7)     // Catch:{ all -> 0x0106 }
            if (r0 != 0) goto L_0x000e
            monitor-exit(r7)     // Catch:{ all -> 0x0106 }
            return
        L_0x000e:
            java.lang.Object r1 = r0.getConnection()     // Catch:{ all -> 0x0106 }
            org.apache.http.conn.ManagedHttpClientConnection r1 = (org.apache.http.conn.ManagedHttpClientConnection) r1     // Catch:{ all -> 0x0106 }
            r2 = 1
            r3 = 0
            boolean r4 = r1.isOpen()     // Catch:{ all -> 0x00c2 }
            if (r4 == 0) goto L_0x007e
            if (r11 == 0) goto L_0x001f
            goto L_0x0021
        L_0x001f:
            java.util.concurrent.TimeUnit r11 = java.util.concurrent.TimeUnit.MILLISECONDS     // Catch:{ all -> 0x00c2 }
        L_0x0021:
            r0.setState(r8)     // Catch:{ all -> 0x00c2 }
            r0.updateExpiry(r9, r11)     // Catch:{ all -> 0x00c2 }
            org.apache.commons.logging.Log r8 = r6.log     // Catch:{ all -> 0x00c2 }
            boolean r8 = r8.isDebugEnabled()     // Catch:{ all -> 0x00c2 }
            if (r8 == 0) goto L_0x007b
            r4 = 0
            int r8 = (r9 > r4 ? 1 : (r9 == r4 ? 0 : -1))
            if (r8 <= 0) goto L_0x0057
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ all -> 0x00c2 }
            r8.<init>()     // Catch:{ all -> 0x00c2 }
            java.lang.String r4 = "for "
            r8.append(r4)     // Catch:{ all -> 0x00c2 }
            long r9 = r11.toMillis(r9)     // Catch:{ all -> 0x00c2 }
            double r9 = (double) r9     // Catch:{ all -> 0x00c2 }
            r4 = 4652007308841189376(0x408f400000000000, double:1000.0)
            double r9 = r9 / r4
            r8.append(r9)     // Catch:{ all -> 0x00c2 }
            java.lang.String r9 = " seconds"
            r8.append(r9)     // Catch:{ all -> 0x00c2 }
            java.lang.String r8 = r8.toString()     // Catch:{ all -> 0x00c2 }
            goto L_0x0059
        L_0x0057:
            java.lang.String r8 = "indefinitely"
        L_0x0059:
            org.apache.commons.logging.Log r9 = r6.log     // Catch:{ all -> 0x00c2 }
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ all -> 0x00c2 }
            r10.<init>()     // Catch:{ all -> 0x00c2 }
            java.lang.String r11 = "Connection "
            r10.append(r11)     // Catch:{ all -> 0x00c2 }
            java.lang.String r11 = r6.format(r0)     // Catch:{ all -> 0x00c2 }
            r10.append(r11)     // Catch:{ all -> 0x00c2 }
            java.lang.String r11 = " can be kept alive "
            r10.append(r11)     // Catch:{ all -> 0x00c2 }
            r10.append(r8)     // Catch:{ all -> 0x00c2 }
            java.lang.String r8 = r10.toString()     // Catch:{ all -> 0x00c2 }
            r9.debug(r8)     // Catch:{ all -> 0x00c2 }
        L_0x007b:
            r1.setSocketTimeout(r3)     // Catch:{ all -> 0x00c2 }
        L_0x007e:
            org.apache.http.impl.conn.CPool r8 = r6.pool     // Catch:{ all -> 0x0106 }
            boolean r9 = r1.isOpen()     // Catch:{ all -> 0x0106 }
            if (r9 == 0) goto L_0x008d
            boolean r9 = r0.isRouteComplete()     // Catch:{ all -> 0x0106 }
            if (r9 == 0) goto L_0x008d
            goto L_0x008e
        L_0x008d:
            r2 = 0
        L_0x008e:
            r8.release(r0, r2)     // Catch:{ all -> 0x0106 }
            org.apache.commons.logging.Log r8 = r6.log     // Catch:{ all -> 0x0106 }
            boolean r8 = r8.isDebugEnabled()     // Catch:{ all -> 0x0106 }
            if (r8 == 0) goto L_0x00c0
            org.apache.commons.logging.Log r8 = r6.log     // Catch:{ all -> 0x0106 }
            java.lang.StringBuilder r9 = new java.lang.StringBuilder     // Catch:{ all -> 0x0106 }
            r9.<init>()     // Catch:{ all -> 0x0106 }
            java.lang.String r10 = "Connection released: "
            r9.append(r10)     // Catch:{ all -> 0x0106 }
            java.lang.String r10 = r6.format(r0)     // Catch:{ all -> 0x0106 }
            r9.append(r10)     // Catch:{ all -> 0x0106 }
            java.lang.Object r10 = r0.getRoute()     // Catch:{ all -> 0x0106 }
            org.apache.http.conn.routing.HttpRoute r10 = (org.apache.http.conn.routing.HttpRoute) r10     // Catch:{ all -> 0x0106 }
            java.lang.String r10 = r6.formatStats(r10)     // Catch:{ all -> 0x0106 }
            r9.append(r10)     // Catch:{ all -> 0x0106 }
            java.lang.String r9 = r9.toString()     // Catch:{ all -> 0x0106 }
            r8.debug(r9)     // Catch:{ all -> 0x0106 }
        L_0x00c0:
            monitor-exit(r7)     // Catch:{ all -> 0x0106 }
            return
        L_0x00c2:
            r8 = move-exception
            org.apache.http.impl.conn.CPool r9 = r6.pool     // Catch:{ all -> 0x0106 }
            boolean r10 = r1.isOpen()     // Catch:{ all -> 0x0106 }
            if (r10 == 0) goto L_0x00d2
            boolean r10 = r0.isRouteComplete()     // Catch:{ all -> 0x0106 }
            if (r10 == 0) goto L_0x00d2
            goto L_0x00d3
        L_0x00d2:
            r2 = 0
        L_0x00d3:
            r9.release(r0, r2)     // Catch:{ all -> 0x0106 }
            org.apache.commons.logging.Log r9 = r6.log     // Catch:{ all -> 0x0106 }
            boolean r9 = r9.isDebugEnabled()     // Catch:{ all -> 0x0106 }
            if (r9 == 0) goto L_0x0105
            org.apache.commons.logging.Log r9 = r6.log     // Catch:{ all -> 0x0106 }
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ all -> 0x0106 }
            r10.<init>()     // Catch:{ all -> 0x0106 }
            java.lang.String r11 = "Connection released: "
            r10.append(r11)     // Catch:{ all -> 0x0106 }
            java.lang.String r11 = r6.format(r0)     // Catch:{ all -> 0x0106 }
            r10.append(r11)     // Catch:{ all -> 0x0106 }
            java.lang.Object r11 = r0.getRoute()     // Catch:{ all -> 0x0106 }
            org.apache.http.conn.routing.HttpRoute r11 = (org.apache.http.conn.routing.HttpRoute) r11     // Catch:{ all -> 0x0106 }
            java.lang.String r11 = r6.formatStats(r11)     // Catch:{ all -> 0x0106 }
            r10.append(r11)     // Catch:{ all -> 0x0106 }
            java.lang.String r10 = r10.toString()     // Catch:{ all -> 0x0106 }
            r9.debug(r10)     // Catch:{ all -> 0x0106 }
        L_0x0105:
            throw r8     // Catch:{ all -> 0x0106 }
        L_0x0106:
            r8 = move-exception
            monitor-exit(r7)     // Catch:{ all -> 0x0106 }
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.http.impl.conn.PoolingHttpClientConnectionManager.releaseConnection(org.apache.http.HttpClientConnection, java.lang.Object, long, java.util.concurrent.TimeUnit):void");
    }

    public void connect(HttpClientConnection httpClientConnection, HttpRoute httpRoute, int i, HttpContext httpContext) throws IOException {
        ManagedHttpClientConnection managedHttpClientConnection;
        HttpHost httpHost;
        Args.notNull(httpClientConnection, "Managed Connection");
        Args.notNull(httpRoute, "HTTP route");
        synchronized (httpClientConnection) {
            managedHttpClientConnection = (ManagedHttpClientConnection) CPoolProxy.getPoolEntry(httpClientConnection).getConnection();
        }
        if (httpRoute.getProxyHost() != null) {
            httpHost = httpRoute.getProxyHost();
        } else {
            httpHost = httpRoute.getTargetHost();
        }
        this.connectionOperator.connect(managedHttpClientConnection, httpHost, httpRoute.getLocalSocketAddress(), i, resolveSocketConfig(httpHost), httpContext);
    }

    public void upgrade(HttpClientConnection httpClientConnection, HttpRoute httpRoute, HttpContext httpContext) throws IOException {
        ManagedHttpClientConnection managedHttpClientConnection;
        Args.notNull(httpClientConnection, "Managed Connection");
        Args.notNull(httpRoute, "HTTP route");
        synchronized (httpClientConnection) {
            managedHttpClientConnection = (ManagedHttpClientConnection) CPoolProxy.getPoolEntry(httpClientConnection).getConnection();
        }
        this.connectionOperator.upgrade(managedHttpClientConnection, httpRoute.getTargetHost(), httpContext);
    }

    public void routeComplete(HttpClientConnection httpClientConnection, HttpRoute httpRoute, HttpContext httpContext) throws IOException {
        Args.notNull(httpClientConnection, "Managed Connection");
        Args.notNull(httpRoute, "HTTP route");
        synchronized (httpClientConnection) {
            CPoolProxy.getPoolEntry(httpClientConnection).markRouteComplete();
        }
    }

    public void shutdown() {
        if (this.isShutDown.compareAndSet(false, true)) {
            this.log.debug("Connection manager is shutting down");
            try {
                this.pool.shutdown();
            } catch (IOException e) {
                this.log.debug("I/O exception shutting down connection manager", e);
            }
            this.log.debug("Connection manager shut down");
        }
    }

    public void closeIdleConnections(long j, TimeUnit timeUnit) {
        if (this.log.isDebugEnabled()) {
            Log log2 = this.log;
            StringBuilder sb = new StringBuilder();
            sb.append("Closing connections idle longer than ");
            sb.append(j);
            sb.append(OAuth.SCOPE_DELIMITER);
            sb.append(timeUnit);
            log2.debug(sb.toString());
        }
        this.pool.closeIdle(j, timeUnit);
    }

    public void closeExpiredConnections() {
        this.log.debug("Closing expired connections");
        this.pool.closeExpired();
    }

    /* access modifiers changed from: protected */
    public void enumAvailable(PoolEntryCallback<HttpRoute, ManagedHttpClientConnection> poolEntryCallback) {
        this.pool.enumAvailable(poolEntryCallback);
    }

    /* access modifiers changed from: protected */
    public void enumLeased(PoolEntryCallback<HttpRoute, ManagedHttpClientConnection> poolEntryCallback) {
        this.pool.enumLeased(poolEntryCallback);
    }

    public int getMaxTotal() {
        return this.pool.getMaxTotal();
    }

    public void setMaxTotal(int i) {
        this.pool.setMaxTotal(i);
    }

    public int getDefaultMaxPerRoute() {
        return this.pool.getDefaultMaxPerRoute();
    }

    public void setDefaultMaxPerRoute(int i) {
        this.pool.setDefaultMaxPerRoute(i);
    }

    public int getMaxPerRoute(HttpRoute httpRoute) {
        return this.pool.getMaxPerRoute(httpRoute);
    }

    public void setMaxPerRoute(HttpRoute httpRoute, int i) {
        this.pool.setMaxPerRoute(httpRoute, i);
    }

    public PoolStats getTotalStats() {
        return this.pool.getTotalStats();
    }

    public PoolStats getStats(HttpRoute httpRoute) {
        return this.pool.getStats(httpRoute);
    }

    public Set<HttpRoute> getRoutes() {
        return this.pool.getRoutes();
    }

    public SocketConfig getDefaultSocketConfig() {
        return this.configData.getDefaultSocketConfig();
    }

    public void setDefaultSocketConfig(SocketConfig socketConfig) {
        this.configData.setDefaultSocketConfig(socketConfig);
    }

    public ConnectionConfig getDefaultConnectionConfig() {
        return this.configData.getDefaultConnectionConfig();
    }

    public void setDefaultConnectionConfig(ConnectionConfig connectionConfig) {
        this.configData.setDefaultConnectionConfig(connectionConfig);
    }

    public SocketConfig getSocketConfig(HttpHost httpHost) {
        return this.configData.getSocketConfig(httpHost);
    }

    public void setSocketConfig(HttpHost httpHost, SocketConfig socketConfig) {
        this.configData.setSocketConfig(httpHost, socketConfig);
    }

    public ConnectionConfig getConnectionConfig(HttpHost httpHost) {
        return this.configData.getConnectionConfig(httpHost);
    }

    public void setConnectionConfig(HttpHost httpHost, ConnectionConfig connectionConfig) {
        this.configData.setConnectionConfig(httpHost, connectionConfig);
    }

    public int getValidateAfterInactivity() {
        return this.pool.getValidateAfterInactivity();
    }

    public void setValidateAfterInactivity(int i) {
        this.pool.setValidateAfterInactivity(i);
    }
}
