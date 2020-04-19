package org.apache.http.impl.conn;

import java.io.Closeable;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;
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
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;
import org.apache.http.util.LangUtils;

@Contract(threading = ThreadingBehavior.SAFE_CONDITIONAL)
public class BasicHttpClientConnectionManager implements HttpClientConnectionManager, Closeable {
    private ManagedHttpClientConnection conn;
    private ConnectionConfig connConfig;
    private final HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connFactory;
    private final HttpClientConnectionOperator connectionOperator;
    private long expiry;
    private final AtomicBoolean isShutdown;
    private boolean leased;
    private final Log log;
    private HttpRoute route;
    private SocketConfig socketConfig;
    private Object state;
    private long updated;

    public void routeComplete(HttpClientConnection httpClientConnection, HttpRoute httpRoute, HttpContext httpContext) throws IOException {
    }

    private static Registry<ConnectionSocketFactory> getDefaultRegistry() {
        return RegistryBuilder.create().register(HttpHost.DEFAULT_SCHEME_NAME, PlainConnectionSocketFactory.getSocketFactory()).register("https", SSLConnectionSocketFactory.getSocketFactory()).build();
    }

    public BasicHttpClientConnectionManager(Lookup<ConnectionSocketFactory> lookup, HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> httpConnectionFactory, SchemePortResolver schemePortResolver, DnsResolver dnsResolver) {
        this((HttpClientConnectionOperator) new DefaultHttpClientConnectionOperator(lookup, schemePortResolver, dnsResolver), httpConnectionFactory);
    }

    public BasicHttpClientConnectionManager(HttpClientConnectionOperator httpClientConnectionOperator, HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> httpConnectionFactory) {
        this.log = LogFactory.getLog(getClass());
        this.connectionOperator = (HttpClientConnectionOperator) Args.notNull(httpClientConnectionOperator, "Connection operator");
        if (httpConnectionFactory == null) {
            httpConnectionFactory = ManagedHttpClientConnectionFactory.INSTANCE;
        }
        this.connFactory = httpConnectionFactory;
        this.expiry = Long.MAX_VALUE;
        this.socketConfig = SocketConfig.DEFAULT;
        this.connConfig = ConnectionConfig.DEFAULT;
        this.isShutdown = new AtomicBoolean(false);
    }

    public BasicHttpClientConnectionManager(Lookup<ConnectionSocketFactory> lookup, HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> httpConnectionFactory) {
        this(lookup, httpConnectionFactory, null, null);
    }

    public BasicHttpClientConnectionManager(Lookup<ConnectionSocketFactory> lookup) {
        this(lookup, null, null, null);
    }

    public BasicHttpClientConnectionManager() {
        this(getDefaultRegistry(), null, null, null);
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

    /* access modifiers changed from: 0000 */
    public HttpRoute getRoute() {
        return this.route;
    }

    /* access modifiers changed from: 0000 */
    public Object getState() {
        return this.state;
    }

    public synchronized SocketConfig getSocketConfig() {
        return this.socketConfig;
    }

    public synchronized void setSocketConfig(SocketConfig socketConfig2) {
        if (socketConfig2 == null) {
            socketConfig2 = SocketConfig.DEFAULT;
        }
        this.socketConfig = socketConfig2;
    }

    public synchronized ConnectionConfig getConnectionConfig() {
        return this.connConfig;
    }

    public synchronized void setConnectionConfig(ConnectionConfig connectionConfig) {
        if (connectionConfig == null) {
            connectionConfig = ConnectionConfig.DEFAULT;
        }
        this.connConfig = connectionConfig;
    }

    public final ConnectionRequest requestConnection(final HttpRoute httpRoute, final Object obj) {
        Args.notNull(httpRoute, "Route");
        return new ConnectionRequest() {
            public boolean cancel() {
                return false;
            }

            public HttpClientConnection get(long j, TimeUnit timeUnit) {
                return BasicHttpClientConnectionManager.this.getConnection(httpRoute, obj);
            }
        };
    }

    private void closeConnection() {
        if (this.conn != null) {
            this.log.debug("Closing connection");
            try {
                this.conn.close();
            } catch (IOException e) {
                if (this.log.isDebugEnabled()) {
                    this.log.debug("I/O exception closing connection", e);
                }
            }
            this.conn = null;
        }
    }

    private void shutdownConnection() {
        if (this.conn != null) {
            this.log.debug("Shutting down connection");
            try {
                this.conn.shutdown();
            } catch (IOException e) {
                if (this.log.isDebugEnabled()) {
                    this.log.debug("I/O exception shutting down connection", e);
                }
            }
            this.conn = null;
        }
    }

    private void checkExpiry() {
        if (this.conn != null && System.currentTimeMillis() >= this.expiry) {
            if (this.log.isDebugEnabled()) {
                Log log2 = this.log;
                StringBuilder sb = new StringBuilder();
                sb.append("Connection expired @ ");
                sb.append(new Date(this.expiry));
                log2.debug(sb.toString());
            }
            closeConnection();
        }
    }

    /* access modifiers changed from: 0000 */
    public synchronized HttpClientConnection getConnection(HttpRoute httpRoute, Object obj) {
        boolean z = false;
        Asserts.check(!this.isShutdown.get(), "Connection manager has been shut down");
        if (this.log.isDebugEnabled()) {
            Log log2 = this.log;
            StringBuilder sb = new StringBuilder();
            sb.append("Get connection for route ");
            sb.append(httpRoute);
            log2.debug(sb.toString());
        }
        if (!this.leased) {
            z = true;
        }
        Asserts.check(z, "Connection is still allocated");
        if (!LangUtils.equals((Object) this.route, (Object) httpRoute) || !LangUtils.equals(this.state, obj)) {
            closeConnection();
        }
        this.route = httpRoute;
        this.state = obj;
        checkExpiry();
        if (this.conn == null) {
            this.conn = (ManagedHttpClientConnection) this.connFactory.create(httpRoute, this.connConfig);
        }
        this.conn.setSocketTimeout(this.socketConfig.getSoTimeout());
        this.leased = true;
        return this.conn;
    }

    public synchronized void releaseConnection(HttpClientConnection httpClientConnection, Object obj, long j, TimeUnit timeUnit) {
        String str;
        Args.notNull(httpClientConnection, "Connection");
        Asserts.check(httpClientConnection == this.conn, "Connection not obtained from this manager");
        if (this.log.isDebugEnabled()) {
            Log log2 = this.log;
            StringBuilder sb = new StringBuilder();
            sb.append("Releasing connection ");
            sb.append(httpClientConnection);
            log2.debug(sb.toString());
        }
        if (!this.isShutdown.get()) {
            try {
                this.updated = System.currentTimeMillis();
                if (!this.conn.isOpen()) {
                    this.conn = null;
                    this.route = null;
                    this.conn = null;
                    this.expiry = Long.MAX_VALUE;
                } else {
                    this.state = obj;
                    this.conn.setSocketTimeout(0);
                    if (this.log.isDebugEnabled()) {
                        if (j > 0) {
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("for ");
                            sb2.append(j);
                            sb2.append(OAuth.SCOPE_DELIMITER);
                            sb2.append(timeUnit);
                            str = sb2.toString();
                        } else {
                            str = "indefinitely";
                        }
                        Log log3 = this.log;
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append("Connection can be kept alive ");
                        sb3.append(str);
                        log3.debug(sb3.toString());
                    }
                    if (j > 0) {
                        this.expiry = this.updated + timeUnit.toMillis(j);
                    } else {
                        this.expiry = Long.MAX_VALUE;
                    }
                }
            } finally {
                this.leased = false;
            }
        }
    }

    public void connect(HttpClientConnection httpClientConnection, HttpRoute httpRoute, int i, HttpContext httpContext) throws IOException {
        HttpHost httpHost;
        Args.notNull(httpClientConnection, "Connection");
        Args.notNull(httpRoute, "HTTP route");
        Asserts.check(httpClientConnection == this.conn, "Connection not obtained from this manager");
        if (httpRoute.getProxyHost() != null) {
            httpHost = httpRoute.getProxyHost();
        } else {
            httpHost = httpRoute.getTargetHost();
        }
        this.connectionOperator.connect(this.conn, httpHost, httpRoute.getLocalSocketAddress(), i, this.socketConfig, httpContext);
    }

    public void upgrade(HttpClientConnection httpClientConnection, HttpRoute httpRoute, HttpContext httpContext) throws IOException {
        Args.notNull(httpClientConnection, "Connection");
        Args.notNull(httpRoute, "HTTP route");
        Asserts.check(httpClientConnection == this.conn, "Connection not obtained from this manager");
        this.connectionOperator.upgrade(this.conn, httpRoute.getTargetHost(), httpContext);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0013, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void closeExpiredConnections() {
        /*
            r1 = this;
            monitor-enter(r1)
            java.util.concurrent.atomic.AtomicBoolean r0 = r1.isShutdown     // Catch:{ all -> 0x0014 }
            boolean r0 = r0.get()     // Catch:{ all -> 0x0014 }
            if (r0 == 0) goto L_0x000b
            monitor-exit(r1)
            return
        L_0x000b:
            boolean r0 = r1.leased     // Catch:{ all -> 0x0014 }
            if (r0 != 0) goto L_0x0012
            r1.checkExpiry()     // Catch:{ all -> 0x0014 }
        L_0x0012:
            monitor-exit(r1)
            return
        L_0x0014:
            r0 = move-exception
            monitor-exit(r1)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.http.impl.conn.BasicHttpClientConnectionManager.closeExpiredConnections():void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:17:0x002e, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void closeIdleConnections(long r3, java.util.concurrent.TimeUnit r5) {
        /*
            r2 = this;
            monitor-enter(r2)
            java.lang.String r0 = "Time unit"
            org.apache.http.util.Args.notNull(r5, r0)     // Catch:{ all -> 0x002f }
            java.util.concurrent.atomic.AtomicBoolean r0 = r2.isShutdown     // Catch:{ all -> 0x002f }
            boolean r0 = r0.get()     // Catch:{ all -> 0x002f }
            if (r0 == 0) goto L_0x0010
            monitor-exit(r2)
            return
        L_0x0010:
            boolean r0 = r2.leased     // Catch:{ all -> 0x002f }
            if (r0 != 0) goto L_0x002d
            long r3 = r5.toMillis(r3)     // Catch:{ all -> 0x002f }
            r0 = 0
            int r5 = (r3 > r0 ? 1 : (r3 == r0 ? 0 : -1))
            if (r5 >= 0) goto L_0x001f
            r3 = r0
        L_0x001f:
            long r0 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x002f }
            long r0 = r0 - r3
            long r3 = r2.updated     // Catch:{ all -> 0x002f }
            int r3 = (r3 > r0 ? 1 : (r3 == r0 ? 0 : -1))
            if (r3 > 0) goto L_0x002d
            r2.closeConnection()     // Catch:{ all -> 0x002f }
        L_0x002d:
            monitor-exit(r2)
            return
        L_0x002f:
            r3 = move-exception
            monitor-exit(r2)
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.http.impl.conn.BasicHttpClientConnectionManager.closeIdleConnections(long, java.util.concurrent.TimeUnit):void");
    }

    public synchronized void shutdown() {
        if (this.isShutdown.compareAndSet(false, true)) {
            shutdownConnection();
        }
    }
}
