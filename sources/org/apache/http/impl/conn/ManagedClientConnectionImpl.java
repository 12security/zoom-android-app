package org.apache.http.impl.conn;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import org.apache.http.HttpConnectionMetrics;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ClientConnectionOperator;
import org.apache.http.conn.ManagedClientConnection;
import org.apache.http.conn.OperatedClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.routing.RouteTracker;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;

@Deprecated
class ManagedClientConnectionImpl implements ManagedClientConnection {
    private volatile long duration = Long.MAX_VALUE;
    private final ClientConnectionManager manager;
    private final ClientConnectionOperator operator;
    private volatile HttpPoolEntry poolEntry;
    private volatile boolean reusable = false;

    public String getId() {
        return null;
    }

    ManagedClientConnectionImpl(ClientConnectionManager clientConnectionManager, ClientConnectionOperator clientConnectionOperator, HttpPoolEntry httpPoolEntry) {
        Args.notNull(clientConnectionManager, "Connection manager");
        Args.notNull(clientConnectionOperator, "Connection operator");
        Args.notNull(httpPoolEntry, "HTTP pool entry");
        this.manager = clientConnectionManager;
        this.operator = clientConnectionOperator;
        this.poolEntry = httpPoolEntry;
    }

    /* access modifiers changed from: 0000 */
    public HttpPoolEntry getPoolEntry() {
        return this.poolEntry;
    }

    /* access modifiers changed from: 0000 */
    public HttpPoolEntry detach() {
        HttpPoolEntry httpPoolEntry = this.poolEntry;
        this.poolEntry = null;
        return httpPoolEntry;
    }

    public ClientConnectionManager getManager() {
        return this.manager;
    }

    private OperatedClientConnection getConnection() {
        HttpPoolEntry httpPoolEntry = this.poolEntry;
        if (httpPoolEntry == null) {
            return null;
        }
        return (OperatedClientConnection) httpPoolEntry.getConnection();
    }

    private OperatedClientConnection ensureConnection() {
        HttpPoolEntry httpPoolEntry = this.poolEntry;
        if (httpPoolEntry != null) {
            return (OperatedClientConnection) httpPoolEntry.getConnection();
        }
        throw new ConnectionShutdownException();
    }

    private HttpPoolEntry ensurePoolEntry() {
        HttpPoolEntry httpPoolEntry = this.poolEntry;
        if (httpPoolEntry != null) {
            return httpPoolEntry;
        }
        throw new ConnectionShutdownException();
    }

    public void close() throws IOException {
        HttpPoolEntry httpPoolEntry = this.poolEntry;
        if (httpPoolEntry != null) {
            OperatedClientConnection operatedClientConnection = (OperatedClientConnection) httpPoolEntry.getConnection();
            httpPoolEntry.getTracker().reset();
            operatedClientConnection.close();
        }
    }

    public void shutdown() throws IOException {
        HttpPoolEntry httpPoolEntry = this.poolEntry;
        if (httpPoolEntry != null) {
            OperatedClientConnection operatedClientConnection = (OperatedClientConnection) httpPoolEntry.getConnection();
            httpPoolEntry.getTracker().reset();
            operatedClientConnection.shutdown();
        }
    }

    public boolean isOpen() {
        OperatedClientConnection connection = getConnection();
        if (connection != null) {
            return connection.isOpen();
        }
        return false;
    }

    public boolean isStale() {
        OperatedClientConnection connection = getConnection();
        if (connection != null) {
            return connection.isStale();
        }
        return true;
    }

    public void setSocketTimeout(int i) {
        ensureConnection().setSocketTimeout(i);
    }

    public int getSocketTimeout() {
        return ensureConnection().getSocketTimeout();
    }

    public HttpConnectionMetrics getMetrics() {
        return ensureConnection().getMetrics();
    }

    public void flush() throws IOException {
        ensureConnection().flush();
    }

    public boolean isResponseAvailable(int i) throws IOException {
        return ensureConnection().isResponseAvailable(i);
    }

    public void receiveResponseEntity(HttpResponse httpResponse) throws HttpException, IOException {
        ensureConnection().receiveResponseEntity(httpResponse);
    }

    public HttpResponse receiveResponseHeader() throws HttpException, IOException {
        return ensureConnection().receiveResponseHeader();
    }

    public void sendRequestEntity(HttpEntityEnclosingRequest httpEntityEnclosingRequest) throws HttpException, IOException {
        ensureConnection().sendRequestEntity(httpEntityEnclosingRequest);
    }

    public void sendRequestHeader(HttpRequest httpRequest) throws HttpException, IOException {
        ensureConnection().sendRequestHeader(httpRequest);
    }

    public InetAddress getLocalAddress() {
        return ensureConnection().getLocalAddress();
    }

    public int getLocalPort() {
        return ensureConnection().getLocalPort();
    }

    public InetAddress getRemoteAddress() {
        return ensureConnection().getRemoteAddress();
    }

    public int getRemotePort() {
        return ensureConnection().getRemotePort();
    }

    public boolean isSecure() {
        return ensureConnection().isSecure();
    }

    public void bind(Socket socket) throws IOException {
        throw new UnsupportedOperationException();
    }

    public Socket getSocket() {
        return ensureConnection().getSocket();
    }

    public SSLSession getSSLSession() {
        Socket socket = ensureConnection().getSocket();
        if (socket instanceof SSLSocket) {
            return ((SSLSocket) socket).getSession();
        }
        return null;
    }

    public Object getAttribute(String str) {
        OperatedClientConnection ensureConnection = ensureConnection();
        if (ensureConnection instanceof HttpContext) {
            return ((HttpContext) ensureConnection).getAttribute(str);
        }
        return null;
    }

    public Object removeAttribute(String str) {
        OperatedClientConnection ensureConnection = ensureConnection();
        if (ensureConnection instanceof HttpContext) {
            return ((HttpContext) ensureConnection).removeAttribute(str);
        }
        return null;
    }

    public void setAttribute(String str, Object obj) {
        OperatedClientConnection ensureConnection = ensureConnection();
        if (ensureConnection instanceof HttpContext) {
            ((HttpContext) ensureConnection).setAttribute(str, obj);
        }
    }

    public HttpRoute getRoute() {
        return ensurePoolEntry().getEffectiveRoute();
    }

    public void open(HttpRoute httpRoute, HttpContext httpContext, HttpParams httpParams) throws IOException {
        OperatedClientConnection operatedClientConnection;
        Args.notNull(httpRoute, "Route");
        Args.notNull(httpParams, "HTTP parameters");
        synchronized (this) {
            if (this.poolEntry != null) {
                RouteTracker tracker = this.poolEntry.getTracker();
                Asserts.notNull(tracker, "Route tracker");
                Asserts.check(!tracker.isConnected(), "Connection already open");
                operatedClientConnection = (OperatedClientConnection) this.poolEntry.getConnection();
            } else {
                throw new ConnectionShutdownException();
            }
        }
        HttpHost proxyHost = httpRoute.getProxyHost();
        this.operator.openConnection(operatedClientConnection, proxyHost != null ? proxyHost : httpRoute.getTargetHost(), httpRoute.getLocalAddress(), httpContext, httpParams);
        synchronized (this) {
            if (this.poolEntry != null) {
                RouteTracker tracker2 = this.poolEntry.getTracker();
                if (proxyHost == null) {
                    tracker2.connectTarget(operatedClientConnection.isSecure());
                } else {
                    tracker2.connectProxy(proxyHost, operatedClientConnection.isSecure());
                }
            } else {
                throw new InterruptedIOException();
            }
        }
    }

    public void tunnelTarget(boolean z, HttpParams httpParams) throws IOException {
        HttpHost targetHost;
        OperatedClientConnection operatedClientConnection;
        Args.notNull(httpParams, "HTTP parameters");
        synchronized (this) {
            if (this.poolEntry != null) {
                RouteTracker tracker = this.poolEntry.getTracker();
                Asserts.notNull(tracker, "Route tracker");
                Asserts.check(tracker.isConnected(), "Connection not open");
                Asserts.check(!tracker.isTunnelled(), "Connection is already tunnelled");
                targetHost = tracker.getTargetHost();
                operatedClientConnection = (OperatedClientConnection) this.poolEntry.getConnection();
            } else {
                throw new ConnectionShutdownException();
            }
        }
        operatedClientConnection.update(null, targetHost, z, httpParams);
        synchronized (this) {
            if (this.poolEntry != null) {
                this.poolEntry.getTracker().tunnelTarget(z);
            } else {
                throw new InterruptedIOException();
            }
        }
    }

    public void tunnelProxy(HttpHost httpHost, boolean z, HttpParams httpParams) throws IOException {
        OperatedClientConnection operatedClientConnection;
        Args.notNull(httpHost, "Next proxy");
        Args.notNull(httpParams, "HTTP parameters");
        synchronized (this) {
            if (this.poolEntry != null) {
                RouteTracker tracker = this.poolEntry.getTracker();
                Asserts.notNull(tracker, "Route tracker");
                Asserts.check(tracker.isConnected(), "Connection not open");
                operatedClientConnection = (OperatedClientConnection) this.poolEntry.getConnection();
            } else {
                throw new ConnectionShutdownException();
            }
        }
        operatedClientConnection.update(null, httpHost, z, httpParams);
        synchronized (this) {
            if (this.poolEntry != null) {
                this.poolEntry.getTracker().tunnelProxy(httpHost, z);
            } else {
                throw new InterruptedIOException();
            }
        }
    }

    public void layerProtocol(HttpContext httpContext, HttpParams httpParams) throws IOException {
        HttpHost targetHost;
        OperatedClientConnection operatedClientConnection;
        Args.notNull(httpParams, "HTTP parameters");
        synchronized (this) {
            if (this.poolEntry != null) {
                RouteTracker tracker = this.poolEntry.getTracker();
                Asserts.notNull(tracker, "Route tracker");
                Asserts.check(tracker.isConnected(), "Connection not open");
                Asserts.check(tracker.isTunnelled(), "Protocol layering without a tunnel not supported");
                Asserts.check(!tracker.isLayered(), "Multiple protocol layering not supported");
                targetHost = tracker.getTargetHost();
                operatedClientConnection = (OperatedClientConnection) this.poolEntry.getConnection();
            } else {
                throw new ConnectionShutdownException();
            }
        }
        this.operator.updateSecureConnection(operatedClientConnection, targetHost, httpContext, httpParams);
        synchronized (this) {
            if (this.poolEntry != null) {
                this.poolEntry.getTracker().layerProtocol(operatedClientConnection.isSecure());
            } else {
                throw new InterruptedIOException();
            }
        }
    }

    public Object getState() {
        return ensurePoolEntry().getState();
    }

    public void setState(Object obj) {
        ensurePoolEntry().setState(obj);
    }

    public void markReusable() {
        this.reusable = true;
    }

    public void unmarkReusable() {
        this.reusable = false;
    }

    public boolean isMarkedReusable() {
        return this.reusable;
    }

    public void setIdleDuration(long j, TimeUnit timeUnit) {
        if (j > 0) {
            this.duration = timeUnit.toMillis(j);
        } else {
            this.duration = -1;
        }
    }

    public void releaseConnection() {
        synchronized (this) {
            if (this.poolEntry != null) {
                this.manager.releaseConnection(this, this.duration, TimeUnit.MILLISECONDS);
                this.poolEntry = null;
            }
        }
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(7:6|7|8|9|10|11|12) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0015 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void abortConnection() {
        /*
            r4 = this;
            monitor-enter(r4)
            org.apache.http.impl.conn.HttpPoolEntry r0 = r4.poolEntry     // Catch:{ all -> 0x0023 }
            if (r0 != 0) goto L_0x0007
            monitor-exit(r4)     // Catch:{ all -> 0x0023 }
            return
        L_0x0007:
            r0 = 0
            r4.reusable = r0     // Catch:{ all -> 0x0023 }
            org.apache.http.impl.conn.HttpPoolEntry r0 = r4.poolEntry     // Catch:{ all -> 0x0023 }
            java.lang.Object r0 = r0.getConnection()     // Catch:{ all -> 0x0023 }
            org.apache.http.conn.OperatedClientConnection r0 = (org.apache.http.conn.OperatedClientConnection) r0     // Catch:{ all -> 0x0023 }
            r0.shutdown()     // Catch:{ IOException -> 0x0015 }
        L_0x0015:
            org.apache.http.conn.ClientConnectionManager r0 = r4.manager     // Catch:{ all -> 0x0023 }
            long r1 = r4.duration     // Catch:{ all -> 0x0023 }
            java.util.concurrent.TimeUnit r3 = java.util.concurrent.TimeUnit.MILLISECONDS     // Catch:{ all -> 0x0023 }
            r0.releaseConnection(r4, r1, r3)     // Catch:{ all -> 0x0023 }
            r0 = 0
            r4.poolEntry = r0     // Catch:{ all -> 0x0023 }
            monitor-exit(r4)     // Catch:{ all -> 0x0023 }
            return
        L_0x0023:
            r0 = move-exception
            monitor-exit(r4)     // Catch:{ all -> 0x0023 }
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.http.impl.conn.ManagedClientConnectionImpl.abortConnection():void");
    }
}
