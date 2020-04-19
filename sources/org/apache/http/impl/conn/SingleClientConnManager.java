package org.apache.http.impl.conn;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ClientConnectionOperator;
import org.apache.http.conn.ClientConnectionRequest;
import org.apache.http.conn.ManagedClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.routing.RouteTracker;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;

@Contract(threading = ThreadingBehavior.SAFE)
@Deprecated
public class SingleClientConnManager implements ClientConnectionManager {
    public static final String MISUSE_MESSAGE = "Invalid use of SingleClientConnManager: connection still allocated.\nMake sure to release the connection before allocating another one.";
    protected final boolean alwaysShutDown;
    protected final ClientConnectionOperator connOperator;
    protected volatile long connectionExpiresTime;
    protected volatile boolean isShutDown;
    protected volatile long lastReleaseTime;
    private final Log log;
    protected volatile ConnAdapter managedConn;
    protected final SchemeRegistry schemeRegistry;
    protected volatile PoolEntry uniquePoolEntry;

    protected class ConnAdapter extends AbstractPooledConnAdapter {
        protected ConnAdapter(PoolEntry poolEntry, HttpRoute httpRoute) {
            super(SingleClientConnManager.this, poolEntry);
            markReusable();
            poolEntry.route = httpRoute;
        }
    }

    protected class PoolEntry extends AbstractPoolEntry {
        protected PoolEntry() {
            super(SingleClientConnManager.this.connOperator, null);
        }

        /* access modifiers changed from: protected */
        public void close() throws IOException {
            shutdownEntry();
            if (this.connection.isOpen()) {
                this.connection.close();
            }
        }

        /* access modifiers changed from: protected */
        public void shutdown() throws IOException {
            shutdownEntry();
            if (this.connection.isOpen()) {
                this.connection.shutdown();
            }
        }
    }

    @Deprecated
    public SingleClientConnManager(HttpParams httpParams, SchemeRegistry schemeRegistry2) {
        this(schemeRegistry2);
    }

    public SingleClientConnManager(SchemeRegistry schemeRegistry2) {
        this.log = LogFactory.getLog(getClass());
        Args.notNull(schemeRegistry2, "Scheme registry");
        this.schemeRegistry = schemeRegistry2;
        this.connOperator = createConnectionOperator(schemeRegistry2);
        this.uniquePoolEntry = new PoolEntry();
        this.managedConn = null;
        this.lastReleaseTime = -1;
        this.alwaysShutDown = false;
        this.isShutDown = false;
    }

    public SingleClientConnManager() {
        this(SchemeRegistryFactory.createDefault());
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        try {
            shutdown();
        } finally {
            super.finalize();
        }
    }

    public SchemeRegistry getSchemeRegistry() {
        return this.schemeRegistry;
    }

    /* access modifiers changed from: protected */
    public ClientConnectionOperator createConnectionOperator(SchemeRegistry schemeRegistry2) {
        return new DefaultClientConnectionOperator(schemeRegistry2);
    }

    /* access modifiers changed from: protected */
    public final void assertStillUp() throws IllegalStateException {
        Asserts.check(!this.isShutDown, "Manager is shut down");
    }

    public final ClientConnectionRequest requestConnection(final HttpRoute httpRoute, final Object obj) {
        return new ClientConnectionRequest() {
            public void abortRequest() {
            }

            public ManagedClientConnection getConnection(long j, TimeUnit timeUnit) {
                return SingleClientConnManager.this.getConnection(httpRoute, obj);
            }
        };
    }

    public ManagedClientConnection getConnection(HttpRoute httpRoute, Object obj) {
        boolean z;
        ConnAdapter connAdapter;
        Args.notNull(httpRoute, "Route");
        assertStillUp();
        if (this.log.isDebugEnabled()) {
            Log log2 = this.log;
            StringBuilder sb = new StringBuilder();
            sb.append("Get connection for route ");
            sb.append(httpRoute);
            log2.debug(sb.toString());
        }
        synchronized (this) {
            boolean z2 = true;
            boolean z3 = false;
            Asserts.check(this.managedConn == null, MISUSE_MESSAGE);
            closeExpiredConnections();
            if (this.uniquePoolEntry.connection.isOpen()) {
                RouteTracker routeTracker = this.uniquePoolEntry.tracker;
                z = routeTracker == null || !routeTracker.toRoute().equals(httpRoute);
            } else {
                z = false;
                z3 = true;
            }
            if (z) {
                try {
                    this.uniquePoolEntry.shutdown();
                } catch (IOException e) {
                    this.log.debug("Problem shutting down connection.", e);
                }
            } else {
                z2 = z3;
            }
            if (z2) {
                this.uniquePoolEntry = new PoolEntry();
            }
            this.managedConn = new ConnAdapter(this.uniquePoolEntry, httpRoute);
            connAdapter = this.managedConn;
        }
        return connAdapter;
    }

    /* JADX WARNING: Unknown top exception splitter block from list: {B:27:0x0069=Splitter:B:27:0x0069, B:56:0x00b9=Splitter:B:56:0x00b9, B:47:0x009c=Splitter:B:47:0x009c} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void releaseConnection(org.apache.http.conn.ManagedClientConnection r9, long r10, java.util.concurrent.TimeUnit r12) {
        /*
            r8 = this;
            boolean r0 = r9 instanceof org.apache.http.impl.conn.SingleClientConnManager.ConnAdapter
            java.lang.String r1 = "Connection class mismatch, connection not obtained from this manager"
            org.apache.http.util.Args.check(r0, r1)
            r8.assertStillUp()
            org.apache.commons.logging.Log r0 = r8.log
            boolean r0 = r0.isDebugEnabled()
            if (r0 == 0) goto L_0x0028
            org.apache.commons.logging.Log r0 = r8.log
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "Releasing connection "
            r1.append(r2)
            r1.append(r9)
            java.lang.String r1 = r1.toString()
            r0.debug(r1)
        L_0x0028:
            org.apache.http.impl.conn.SingleClientConnManager$ConnAdapter r9 = (org.apache.http.impl.conn.SingleClientConnManager.ConnAdapter) r9
            monitor-enter(r9)
            org.apache.http.impl.conn.AbstractPoolEntry r0 = r9.poolEntry     // Catch:{ all -> 0x00df }
            if (r0 != 0) goto L_0x0031
            monitor-exit(r9)     // Catch:{ all -> 0x00df }
            return
        L_0x0031:
            org.apache.http.conn.ClientConnectionManager r0 = r9.getManager()     // Catch:{ all -> 0x00df }
            if (r0 != r8) goto L_0x0039
            r0 = 1
            goto L_0x003a
        L_0x0039:
            r0 = 0
        L_0x003a:
            java.lang.String r1 = "Connection not obtained from this manager"
            org.apache.http.util.Asserts.check(r0, r1)     // Catch:{ all -> 0x00df }
            r0 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
            r2 = 0
            r4 = 0
            boolean r5 = r9.isOpen()     // Catch:{ IOException -> 0x008c }
            if (r5 == 0) goto L_0x0069
            boolean r5 = r8.alwaysShutDown     // Catch:{ IOException -> 0x008c }
            if (r5 != 0) goto L_0x0057
            boolean r5 = r9.isMarkedReusable()     // Catch:{ IOException -> 0x008c }
            if (r5 != 0) goto L_0x0069
        L_0x0057:
            org.apache.commons.logging.Log r5 = r8.log     // Catch:{ IOException -> 0x008c }
            boolean r5 = r5.isDebugEnabled()     // Catch:{ IOException -> 0x008c }
            if (r5 == 0) goto L_0x0066
            org.apache.commons.logging.Log r5 = r8.log     // Catch:{ IOException -> 0x008c }
            java.lang.String r6 = "Released connection open but not reusable."
            r5.debug(r6)     // Catch:{ IOException -> 0x008c }
        L_0x0066:
            r9.shutdown()     // Catch:{ IOException -> 0x008c }
        L_0x0069:
            r9.detach()     // Catch:{ all -> 0x00df }
            monitor-enter(r8)     // Catch:{ all -> 0x00df }
            r8.managedConn = r4     // Catch:{ all -> 0x0087 }
            long r4 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x0087 }
            r8.lastReleaseTime = r4     // Catch:{ all -> 0x0087 }
            int r2 = (r10 > r2 ? 1 : (r10 == r2 ? 0 : -1))
            if (r2 <= 0) goto L_0x0083
            long r10 = r12.toMillis(r10)     // Catch:{ all -> 0x0087 }
            long r0 = r8.lastReleaseTime     // Catch:{ all -> 0x0087 }
            long r10 = r10 + r0
            r8.connectionExpiresTime = r10     // Catch:{ all -> 0x0087 }
            goto L_0x0085
        L_0x0083:
            r8.connectionExpiresTime = r0     // Catch:{ all -> 0x0087 }
        L_0x0085:
            monitor-exit(r8)     // Catch:{ all -> 0x0087 }
            goto L_0x00b9
        L_0x0087:
            r10 = move-exception
            monitor-exit(r8)     // Catch:{ all -> 0x0087 }
            throw r10     // Catch:{ all -> 0x00df }
        L_0x008a:
            r5 = move-exception
            goto L_0x00be
        L_0x008c:
            r5 = move-exception
            org.apache.commons.logging.Log r6 = r8.log     // Catch:{ all -> 0x008a }
            boolean r6 = r6.isDebugEnabled()     // Catch:{ all -> 0x008a }
            if (r6 == 0) goto L_0x009c
            org.apache.commons.logging.Log r6 = r8.log     // Catch:{ all -> 0x008a }
            java.lang.String r7 = "Exception shutting down released connection."
            r6.debug(r7, r5)     // Catch:{ all -> 0x008a }
        L_0x009c:
            r9.detach()     // Catch:{ all -> 0x00df }
            monitor-enter(r8)     // Catch:{ all -> 0x00df }
            r8.managedConn = r4     // Catch:{ all -> 0x00bb }
            long r4 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x00bb }
            r8.lastReleaseTime = r4     // Catch:{ all -> 0x00bb }
            int r2 = (r10 > r2 ? 1 : (r10 == r2 ? 0 : -1))
            if (r2 <= 0) goto L_0x00b6
            long r10 = r12.toMillis(r10)     // Catch:{ all -> 0x00bb }
            long r0 = r8.lastReleaseTime     // Catch:{ all -> 0x00bb }
            long r10 = r10 + r0
            r8.connectionExpiresTime = r10     // Catch:{ all -> 0x00bb }
            goto L_0x00b8
        L_0x00b6:
            r8.connectionExpiresTime = r0     // Catch:{ all -> 0x00bb }
        L_0x00b8:
            monitor-exit(r8)     // Catch:{ all -> 0x00bb }
        L_0x00b9:
            monitor-exit(r9)     // Catch:{ all -> 0x00df }
            return
        L_0x00bb:
            r10 = move-exception
            monitor-exit(r8)     // Catch:{ all -> 0x00bb }
            throw r10     // Catch:{ all -> 0x00df }
        L_0x00be:
            r9.detach()     // Catch:{ all -> 0x00df }
            monitor-enter(r8)     // Catch:{ all -> 0x00df }
            r8.managedConn = r4     // Catch:{ all -> 0x00dc }
            long r6 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x00dc }
            r8.lastReleaseTime = r6     // Catch:{ all -> 0x00dc }
            int r2 = (r10 > r2 ? 1 : (r10 == r2 ? 0 : -1))
            if (r2 <= 0) goto L_0x00d8
            long r10 = r12.toMillis(r10)     // Catch:{ all -> 0x00dc }
            long r0 = r8.lastReleaseTime     // Catch:{ all -> 0x00dc }
            long r10 = r10 + r0
            r8.connectionExpiresTime = r10     // Catch:{ all -> 0x00dc }
            goto L_0x00da
        L_0x00d8:
            r8.connectionExpiresTime = r0     // Catch:{ all -> 0x00dc }
        L_0x00da:
            monitor-exit(r8)     // Catch:{ all -> 0x00dc }
            throw r5     // Catch:{ all -> 0x00df }
        L_0x00dc:
            r10 = move-exception
            monitor-exit(r8)     // Catch:{ all -> 0x00dc }
            throw r10     // Catch:{ all -> 0x00df }
        L_0x00df:
            r10 = move-exception
            monitor-exit(r9)     // Catch:{ all -> 0x00df }
            throw r10
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.http.impl.conn.SingleClientConnManager.releaseConnection(org.apache.http.conn.ManagedClientConnection, long, java.util.concurrent.TimeUnit):void");
    }

    public void closeExpiredConnections() {
        if (System.currentTimeMillis() >= this.connectionExpiresTime) {
            closeIdleConnections(0, TimeUnit.MILLISECONDS);
        }
    }

    public void closeIdleConnections(long j, TimeUnit timeUnit) {
        assertStillUp();
        Args.notNull(timeUnit, "Time unit");
        synchronized (this) {
            if (this.managedConn == null && this.uniquePoolEntry.connection.isOpen()) {
                if (this.lastReleaseTime <= System.currentTimeMillis() - timeUnit.toMillis(j)) {
                    try {
                        this.uniquePoolEntry.close();
                    } catch (IOException e) {
                        this.log.debug("Problem closing idle connection.", e);
                    }
                }
            }
        }
    }

    public void shutdown() {
        this.isShutDown = true;
        synchronized (this) {
            try {
                if (this.uniquePoolEntry != null) {
                    this.uniquePoolEntry.shutdown();
                }
                this.uniquePoolEntry = null;
            } catch (IOException e) {
                try {
                    this.log.debug("Problem while shutting down manager.", e);
                    this.uniquePoolEntry = null;
                } catch (Throwable th) {
                    this.uniquePoolEntry = null;
                    this.managedConn = null;
                    throw th;
                }
            }
            this.managedConn = null;
        }
    }

    /* access modifiers changed from: protected */
    public void revokeConnection() {
        ConnAdapter connAdapter = this.managedConn;
        if (connAdapter != null) {
            connAdapter.detach();
            synchronized (this) {
                try {
                    this.uniquePoolEntry.shutdown();
                } catch (IOException e) {
                    this.log.debug("Problem while shutting down connection.", e);
                }
            }
        }
    }
}
