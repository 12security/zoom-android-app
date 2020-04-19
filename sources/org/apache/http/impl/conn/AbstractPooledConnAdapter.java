package org.apache.http.impl.conn;

import java.io.IOException;
import org.apache.http.HttpHost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.OperatedClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

@Deprecated
public abstract class AbstractPooledConnAdapter extends AbstractClientConnAdapter {
    protected volatile AbstractPoolEntry poolEntry;

    public String getId() {
        return null;
    }

    protected AbstractPooledConnAdapter(ClientConnectionManager clientConnectionManager, AbstractPoolEntry abstractPoolEntry) {
        super(clientConnectionManager, abstractPoolEntry.connection);
        this.poolEntry = abstractPoolEntry;
    }

    /* access modifiers changed from: protected */
    @Deprecated
    public AbstractPoolEntry getPoolEntry() {
        return this.poolEntry;
    }

    /* access modifiers changed from: protected */
    public void assertValid(AbstractPoolEntry abstractPoolEntry) {
        if (isReleased() || abstractPoolEntry == null) {
            throw new ConnectionShutdownException();
        }
    }

    /* access modifiers changed from: protected */
    @Deprecated
    public final void assertAttached() {
        if (this.poolEntry == null) {
            throw new ConnectionShutdownException();
        }
    }

    /* access modifiers changed from: protected */
    public synchronized void detach() {
        this.poolEntry = null;
        super.detach();
    }

    public HttpRoute getRoute() {
        AbstractPoolEntry poolEntry2 = getPoolEntry();
        assertValid(poolEntry2);
        if (poolEntry2.tracker == null) {
            return null;
        }
        return poolEntry2.tracker.toRoute();
    }

    public void open(HttpRoute httpRoute, HttpContext httpContext, HttpParams httpParams) throws IOException {
        AbstractPoolEntry poolEntry2 = getPoolEntry();
        assertValid(poolEntry2);
        poolEntry2.open(httpRoute, httpContext, httpParams);
    }

    public void tunnelTarget(boolean z, HttpParams httpParams) throws IOException {
        AbstractPoolEntry poolEntry2 = getPoolEntry();
        assertValid(poolEntry2);
        poolEntry2.tunnelTarget(z, httpParams);
    }

    public void tunnelProxy(HttpHost httpHost, boolean z, HttpParams httpParams) throws IOException {
        AbstractPoolEntry poolEntry2 = getPoolEntry();
        assertValid(poolEntry2);
        poolEntry2.tunnelProxy(httpHost, z, httpParams);
    }

    public void layerProtocol(HttpContext httpContext, HttpParams httpParams) throws IOException {
        AbstractPoolEntry poolEntry2 = getPoolEntry();
        assertValid(poolEntry2);
        poolEntry2.layerProtocol(httpContext, httpParams);
    }

    public void close() throws IOException {
        AbstractPoolEntry poolEntry2 = getPoolEntry();
        if (poolEntry2 != null) {
            poolEntry2.shutdownEntry();
        }
        OperatedClientConnection wrappedConnection = getWrappedConnection();
        if (wrappedConnection != null) {
            wrappedConnection.close();
        }
    }

    public void shutdown() throws IOException {
        AbstractPoolEntry poolEntry2 = getPoolEntry();
        if (poolEntry2 != null) {
            poolEntry2.shutdownEntry();
        }
        OperatedClientConnection wrappedConnection = getWrappedConnection();
        if (wrappedConnection != null) {
            wrappedConnection.shutdown();
        }
    }

    public Object getState() {
        AbstractPoolEntry poolEntry2 = getPoolEntry();
        assertValid(poolEntry2);
        return poolEntry2.getState();
    }

    public void setState(Object obj) {
        AbstractPoolEntry poolEntry2 = getPoolEntry();
        assertValid(poolEntry2);
        poolEntry2.setState(obj);
    }
}
