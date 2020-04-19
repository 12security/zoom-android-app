package org.apache.http.impl.conn.tsccm;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.conn.ClientConnectionOperator;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.conn.OperatedClientConnection;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRoute;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;

@Deprecated
public class ConnPoolByRoute extends AbstractConnPool {
    protected final ConnPerRoute connPerRoute;
    private final long connTTL;
    private final TimeUnit connTTLTimeUnit;
    protected final Queue<BasicPoolEntry> freeConnections;
    protected final Set<BasicPoolEntry> leasedConnections;
    private final Log log;
    protected volatile int maxTotalConnections;
    protected volatile int numConnections;
    protected final ClientConnectionOperator operator;
    /* access modifiers changed from: private */
    public final Lock poolLock;
    protected final Map<HttpRoute, RouteSpecificPool> routeToPool;
    protected volatile boolean shutdown;
    protected final Queue<WaitingThread> waitingThreads;

    public ConnPoolByRoute(ClientConnectionOperator clientConnectionOperator, ConnPerRoute connPerRoute2, int i) {
        this(clientConnectionOperator, connPerRoute2, i, -1, TimeUnit.MILLISECONDS);
    }

    public ConnPoolByRoute(ClientConnectionOperator clientConnectionOperator, ConnPerRoute connPerRoute2, int i, long j, TimeUnit timeUnit) {
        this.log = LogFactory.getLog(getClass());
        Args.notNull(clientConnectionOperator, "Connection operator");
        Args.notNull(connPerRoute2, "Connections per route");
        this.poolLock = this.poolLock;
        this.leasedConnections = this.leasedConnections;
        this.operator = clientConnectionOperator;
        this.connPerRoute = connPerRoute2;
        this.maxTotalConnections = i;
        this.freeConnections = createFreeConnQueue();
        this.waitingThreads = createWaitingThreadQueue();
        this.routeToPool = createRouteToPoolMap();
        this.connTTL = j;
        this.connTTLTimeUnit = timeUnit;
    }

    /* access modifiers changed from: protected */
    public Lock getLock() {
        return this.poolLock;
    }

    @Deprecated
    public ConnPoolByRoute(ClientConnectionOperator clientConnectionOperator, HttpParams httpParams) {
        this(clientConnectionOperator, ConnManagerParams.getMaxConnectionsPerRoute(httpParams), ConnManagerParams.getMaxTotalConnections(httpParams));
    }

    /* access modifiers changed from: protected */
    public Queue<BasicPoolEntry> createFreeConnQueue() {
        return new LinkedList();
    }

    /* access modifiers changed from: protected */
    public Queue<WaitingThread> createWaitingThreadQueue() {
        return new LinkedList();
    }

    /* access modifiers changed from: protected */
    public Map<HttpRoute, RouteSpecificPool> createRouteToPoolMap() {
        return new HashMap();
    }

    /* access modifiers changed from: protected */
    public RouteSpecificPool newRouteSpecificPool(HttpRoute httpRoute) {
        return new RouteSpecificPool(httpRoute, this.connPerRoute);
    }

    /* access modifiers changed from: protected */
    public WaitingThread newWaitingThread(Condition condition, RouteSpecificPool routeSpecificPool) {
        return new WaitingThread(condition, routeSpecificPool);
    }

    private void closeConnection(BasicPoolEntry basicPoolEntry) {
        OperatedClientConnection connection = basicPoolEntry.getConnection();
        if (connection != null) {
            try {
                connection.close();
            } catch (IOException e) {
                this.log.debug("I/O error closing connection", e);
            }
        }
    }

    /* access modifiers changed from: protected */
    public RouteSpecificPool getRoutePool(HttpRoute httpRoute, boolean z) {
        this.poolLock.lock();
        try {
            RouteSpecificPool routeSpecificPool = (RouteSpecificPool) this.routeToPool.get(httpRoute);
            if (routeSpecificPool == null && z) {
                routeSpecificPool = newRouteSpecificPool(httpRoute);
                this.routeToPool.put(httpRoute, routeSpecificPool);
            }
            return routeSpecificPool;
        } finally {
            this.poolLock.unlock();
        }
    }

    public int getConnectionsInPool(HttpRoute httpRoute) {
        this.poolLock.lock();
        int i = 0;
        try {
            RouteSpecificPool routePool = getRoutePool(httpRoute, false);
            if (routePool != null) {
                i = routePool.getEntryCount();
            }
            return i;
        } finally {
            this.poolLock.unlock();
        }
    }

    public int getConnectionsInPool() {
        this.poolLock.lock();
        try {
            return this.numConnections;
        } finally {
            this.poolLock.unlock();
        }
    }

    public PoolEntryRequest requestPoolEntry(final HttpRoute httpRoute, final Object obj) {
        final WaitingThreadAborter waitingThreadAborter = new WaitingThreadAborter();
        return new PoolEntryRequest() {
            public void abortRequest() {
                ConnPoolByRoute.this.poolLock.lock();
                try {
                    waitingThreadAborter.abort();
                } finally {
                    ConnPoolByRoute.this.poolLock.unlock();
                }
            }

            public BasicPoolEntry getPoolEntry(long j, TimeUnit timeUnit) throws InterruptedException, ConnectionPoolTimeoutException {
                return ConnPoolByRoute.this.getEntryBlocking(httpRoute, obj, j, timeUnit, waitingThreadAborter);
            }
        };
    }

    /* access modifiers changed from: protected */
    public BasicPoolEntry getEntryBlocking(HttpRoute httpRoute, Object obj, long j, TimeUnit timeUnit, WaitingThreadAborter waitingThreadAborter) throws ConnectionPoolTimeoutException, InterruptedException {
        RouteSpecificPool routePool;
        WaitingThread waitingThread;
        BasicPoolEntry basicPoolEntry = null;
        Date date = j > 0 ? new Date(System.currentTimeMillis() + timeUnit.toMillis(j)) : null;
        this.poolLock.lock();
        try {
            routePool = getRoutePool(httpRoute, true);
            waitingThread = null;
            while (true) {
                if (basicPoolEntry != null) {
                    break;
                }
                boolean z = false;
                Asserts.check(!this.shutdown, "Connection pool shut down");
                if (this.log.isDebugEnabled()) {
                    Log log2 = this.log;
                    StringBuilder sb = new StringBuilder();
                    sb.append("[");
                    sb.append(httpRoute);
                    sb.append("] total kept alive: ");
                    sb.append(this.freeConnections.size());
                    sb.append(", total issued: ");
                    sb.append(this.leasedConnections.size());
                    sb.append(", total allocated: ");
                    sb.append(this.numConnections);
                    sb.append(" out of ");
                    sb.append(this.maxTotalConnections);
                    log2.debug(sb.toString());
                }
                basicPoolEntry = getFreeEntry(routePool, obj);
                if (basicPoolEntry != null) {
                    break;
                }
                if (routePool.getCapacity() > 0) {
                    z = true;
                }
                if (this.log.isDebugEnabled()) {
                    Log log3 = this.log;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("Available capacity: ");
                    sb2.append(routePool.getCapacity());
                    sb2.append(" out of ");
                    sb2.append(routePool.getMaxEntries());
                    sb2.append(" [");
                    sb2.append(httpRoute);
                    sb2.append("][");
                    sb2.append(obj);
                    sb2.append("]");
                    log3.debug(sb2.toString());
                }
                if (z && this.numConnections < this.maxTotalConnections) {
                    basicPoolEntry = createEntry(routePool, this.operator);
                } else if (!z || this.freeConnections.isEmpty()) {
                    if (this.log.isDebugEnabled()) {
                        Log log4 = this.log;
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append("Need to wait for connection [");
                        sb3.append(httpRoute);
                        sb3.append("][");
                        sb3.append(obj);
                        sb3.append("]");
                        log4.debug(sb3.toString());
                    }
                    if (waitingThread == null) {
                        waitingThread = newWaitingThread(this.poolLock.newCondition(), routePool);
                        waitingThreadAborter.setWaitingThread(waitingThread);
                    }
                    routePool.queueThread(waitingThread);
                    this.waitingThreads.add(waitingThread);
                    boolean await = waitingThread.await(date);
                    routePool.removeThread(waitingThread);
                    this.waitingThreads.remove(waitingThread);
                    if (!await && date != null) {
                        if (date.getTime() <= System.currentTimeMillis()) {
                            throw new ConnectionPoolTimeoutException("Timeout waiting for connection from pool");
                        }
                    }
                } else {
                    deleteLeastUsedEntry();
                    routePool = getRoutePool(httpRoute, true);
                    basicPoolEntry = createEntry(routePool, this.operator);
                }
            }
            this.poolLock.unlock();
            return basicPoolEntry;
        } catch (Throwable th) {
            this.poolLock.unlock();
            throw th;
        }
    }

    public void freeEntry(BasicPoolEntry basicPoolEntry, boolean z, long j, TimeUnit timeUnit) {
        String str;
        HttpRoute plannedRoute = basicPoolEntry.getPlannedRoute();
        if (this.log.isDebugEnabled()) {
            Log log2 = this.log;
            StringBuilder sb = new StringBuilder();
            sb.append("Releasing connection [");
            sb.append(plannedRoute);
            sb.append("][");
            sb.append(basicPoolEntry.getState());
            sb.append("]");
            log2.debug(sb.toString());
        }
        this.poolLock.lock();
        try {
            if (this.shutdown) {
                closeConnection(basicPoolEntry);
                return;
            }
            this.leasedConnections.remove(basicPoolEntry);
            RouteSpecificPool routePool = getRoutePool(plannedRoute, true);
            if (!z || routePool.getCapacity() < 0) {
                closeConnection(basicPoolEntry);
                routePool.dropEntry();
                this.numConnections--;
            } else {
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
                    sb3.append("Pooling connection [");
                    sb3.append(plannedRoute);
                    sb3.append("][");
                    sb3.append(basicPoolEntry.getState());
                    sb3.append("]; keep alive ");
                    sb3.append(str);
                    log3.debug(sb3.toString());
                }
                routePool.freeEntry(basicPoolEntry);
                basicPoolEntry.updateExpiry(j, timeUnit);
                this.freeConnections.add(basicPoolEntry);
            }
            notifyWaitingThread(routePool);
            this.poolLock.unlock();
        } finally {
            this.poolLock.unlock();
        }
    }

    /* access modifiers changed from: protected */
    public BasicPoolEntry getFreeEntry(RouteSpecificPool routeSpecificPool, Object obj) {
        this.poolLock.lock();
        boolean z = false;
        BasicPoolEntry basicPoolEntry = null;
        while (!z) {
            try {
                basicPoolEntry = routeSpecificPool.allocEntry(obj);
                if (basicPoolEntry != null) {
                    if (this.log.isDebugEnabled()) {
                        Log log2 = this.log;
                        StringBuilder sb = new StringBuilder();
                        sb.append("Getting free connection [");
                        sb.append(routeSpecificPool.getRoute());
                        sb.append("][");
                        sb.append(obj);
                        sb.append("]");
                        log2.debug(sb.toString());
                    }
                    this.freeConnections.remove(basicPoolEntry);
                    if (basicPoolEntry.isExpired(System.currentTimeMillis())) {
                        if (this.log.isDebugEnabled()) {
                            Log log3 = this.log;
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("Closing expired free connection [");
                            sb2.append(routeSpecificPool.getRoute());
                            sb2.append("][");
                            sb2.append(obj);
                            sb2.append("]");
                            log3.debug(sb2.toString());
                        }
                        closeConnection(basicPoolEntry);
                        routeSpecificPool.dropEntry();
                        this.numConnections--;
                    } else {
                        this.leasedConnections.add(basicPoolEntry);
                        z = true;
                    }
                } else {
                    if (this.log.isDebugEnabled()) {
                        Log log4 = this.log;
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append("No free connections [");
                        sb3.append(routeSpecificPool.getRoute());
                        sb3.append("][");
                        sb3.append(obj);
                        sb3.append("]");
                        log4.debug(sb3.toString());
                    }
                    z = true;
                }
            } catch (Throwable th) {
                this.poolLock.unlock();
                throw th;
            }
        }
        this.poolLock.unlock();
        return basicPoolEntry;
    }

    /* access modifiers changed from: protected */
    public BasicPoolEntry createEntry(RouteSpecificPool routeSpecificPool, ClientConnectionOperator clientConnectionOperator) {
        if (this.log.isDebugEnabled()) {
            Log log2 = this.log;
            StringBuilder sb = new StringBuilder();
            sb.append("Creating new connection [");
            sb.append(routeSpecificPool.getRoute());
            sb.append("]");
            log2.debug(sb.toString());
        }
        BasicPoolEntry basicPoolEntry = new BasicPoolEntry(clientConnectionOperator, routeSpecificPool.getRoute(), this.connTTL, this.connTTLTimeUnit);
        this.poolLock.lock();
        try {
            routeSpecificPool.createdEntry(basicPoolEntry);
            this.numConnections++;
            this.leasedConnections.add(basicPoolEntry);
            return basicPoolEntry;
        } finally {
            this.poolLock.unlock();
        }
    }

    /* access modifiers changed from: protected */
    public void deleteEntry(BasicPoolEntry basicPoolEntry) {
        HttpRoute plannedRoute = basicPoolEntry.getPlannedRoute();
        if (this.log.isDebugEnabled()) {
            Log log2 = this.log;
            StringBuilder sb = new StringBuilder();
            sb.append("Deleting connection [");
            sb.append(plannedRoute);
            sb.append("][");
            sb.append(basicPoolEntry.getState());
            sb.append("]");
            log2.debug(sb.toString());
        }
        this.poolLock.lock();
        try {
            closeConnection(basicPoolEntry);
            RouteSpecificPool routePool = getRoutePool(plannedRoute, true);
            routePool.deleteEntry(basicPoolEntry);
            this.numConnections--;
            if (routePool.isUnused()) {
                this.routeToPool.remove(plannedRoute);
            }
        } finally {
            this.poolLock.unlock();
        }
    }

    /* access modifiers changed from: protected */
    public void deleteLeastUsedEntry() {
        this.poolLock.lock();
        try {
            BasicPoolEntry basicPoolEntry = (BasicPoolEntry) this.freeConnections.remove();
            if (basicPoolEntry != null) {
                deleteEntry(basicPoolEntry);
            } else if (this.log.isDebugEnabled()) {
                this.log.debug("No free connection to delete");
            }
        } finally {
            this.poolLock.unlock();
        }
    }

    /* access modifiers changed from: protected */
    public void handleLostEntry(HttpRoute httpRoute) {
        this.poolLock.lock();
        try {
            RouteSpecificPool routePool = getRoutePool(httpRoute, true);
            routePool.dropEntry();
            if (routePool.isUnused()) {
                this.routeToPool.remove(httpRoute);
            }
            this.numConnections--;
            notifyWaitingThread(routePool);
        } finally {
            this.poolLock.unlock();
        }
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x006b A[Catch:{ all -> 0x0074 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void notifyWaitingThread(org.apache.http.impl.conn.tsccm.RouteSpecificPool r4) {
        /*
            r3 = this;
            java.util.concurrent.locks.Lock r0 = r3.poolLock
            r0.lock()
            if (r4 == 0) goto L_0x0039
            boolean r0 = r4.hasThread()     // Catch:{ all -> 0x0074 }
            if (r0 == 0) goto L_0x0039
            org.apache.commons.logging.Log r0 = r3.log     // Catch:{ all -> 0x0074 }
            boolean r0 = r0.isDebugEnabled()     // Catch:{ all -> 0x0074 }
            if (r0 == 0) goto L_0x0034
            org.apache.commons.logging.Log r0 = r3.log     // Catch:{ all -> 0x0074 }
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ all -> 0x0074 }
            r1.<init>()     // Catch:{ all -> 0x0074 }
            java.lang.String r2 = "Notifying thread waiting on pool ["
            r1.append(r2)     // Catch:{ all -> 0x0074 }
            org.apache.http.conn.routing.HttpRoute r2 = r4.getRoute()     // Catch:{ all -> 0x0074 }
            r1.append(r2)     // Catch:{ all -> 0x0074 }
            java.lang.String r2 = "]"
            r1.append(r2)     // Catch:{ all -> 0x0074 }
            java.lang.String r1 = r1.toString()     // Catch:{ all -> 0x0074 }
            r0.debug(r1)     // Catch:{ all -> 0x0074 }
        L_0x0034:
            org.apache.http.impl.conn.tsccm.WaitingThread r4 = r4.nextThread()     // Catch:{ all -> 0x0074 }
            goto L_0x0069
        L_0x0039:
            java.util.Queue<org.apache.http.impl.conn.tsccm.WaitingThread> r4 = r3.waitingThreads     // Catch:{ all -> 0x0074 }
            boolean r4 = r4.isEmpty()     // Catch:{ all -> 0x0074 }
            if (r4 != 0) goto L_0x0059
            org.apache.commons.logging.Log r4 = r3.log     // Catch:{ all -> 0x0074 }
            boolean r4 = r4.isDebugEnabled()     // Catch:{ all -> 0x0074 }
            if (r4 == 0) goto L_0x0050
            org.apache.commons.logging.Log r4 = r3.log     // Catch:{ all -> 0x0074 }
            java.lang.String r0 = "Notifying thread waiting on any pool"
            r4.debug(r0)     // Catch:{ all -> 0x0074 }
        L_0x0050:
            java.util.Queue<org.apache.http.impl.conn.tsccm.WaitingThread> r4 = r3.waitingThreads     // Catch:{ all -> 0x0074 }
            java.lang.Object r4 = r4.remove()     // Catch:{ all -> 0x0074 }
            org.apache.http.impl.conn.tsccm.WaitingThread r4 = (org.apache.http.impl.conn.tsccm.WaitingThread) r4     // Catch:{ all -> 0x0074 }
            goto L_0x0069
        L_0x0059:
            org.apache.commons.logging.Log r4 = r3.log     // Catch:{ all -> 0x0074 }
            boolean r4 = r4.isDebugEnabled()     // Catch:{ all -> 0x0074 }
            if (r4 == 0) goto L_0x0068
            org.apache.commons.logging.Log r4 = r3.log     // Catch:{ all -> 0x0074 }
            java.lang.String r0 = "Notifying no-one, there are no waiting threads"
            r4.debug(r0)     // Catch:{ all -> 0x0074 }
        L_0x0068:
            r4 = 0
        L_0x0069:
            if (r4 == 0) goto L_0x006e
            r4.wakeup()     // Catch:{ all -> 0x0074 }
        L_0x006e:
            java.util.concurrent.locks.Lock r4 = r3.poolLock
            r4.unlock()
            return
        L_0x0074:
            r4 = move-exception
            java.util.concurrent.locks.Lock r0 = r3.poolLock
            r0.unlock()
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.http.impl.conn.tsccm.ConnPoolByRoute.notifyWaitingThread(org.apache.http.impl.conn.tsccm.RouteSpecificPool):void");
    }

    public void deleteClosedConnections() {
        this.poolLock.lock();
        try {
            Iterator it = this.freeConnections.iterator();
            while (it.hasNext()) {
                BasicPoolEntry basicPoolEntry = (BasicPoolEntry) it.next();
                if (!basicPoolEntry.getConnection().isOpen()) {
                    it.remove();
                    deleteEntry(basicPoolEntry);
                }
            }
        } finally {
            this.poolLock.unlock();
        }
    }

    public void closeIdleConnections(long j, TimeUnit timeUnit) {
        Args.notNull(timeUnit, "Time unit");
        if (j <= 0) {
            j = 0;
        }
        if (this.log.isDebugEnabled()) {
            Log log2 = this.log;
            StringBuilder sb = new StringBuilder();
            sb.append("Closing connections idle longer than ");
            sb.append(j);
            sb.append(OAuth.SCOPE_DELIMITER);
            sb.append(timeUnit);
            log2.debug(sb.toString());
        }
        long currentTimeMillis = System.currentTimeMillis() - timeUnit.toMillis(j);
        this.poolLock.lock();
        try {
            Iterator it = this.freeConnections.iterator();
            while (it.hasNext()) {
                BasicPoolEntry basicPoolEntry = (BasicPoolEntry) it.next();
                if (basicPoolEntry.getUpdated() <= currentTimeMillis) {
                    if (this.log.isDebugEnabled()) {
                        Log log3 = this.log;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("Closing connection last used @ ");
                        sb2.append(new Date(basicPoolEntry.getUpdated()));
                        log3.debug(sb2.toString());
                    }
                    it.remove();
                    deleteEntry(basicPoolEntry);
                }
            }
        } finally {
            this.poolLock.unlock();
        }
    }

    public void closeExpiredConnections() {
        this.log.debug("Closing expired connections");
        long currentTimeMillis = System.currentTimeMillis();
        this.poolLock.lock();
        try {
            Iterator it = this.freeConnections.iterator();
            while (it.hasNext()) {
                BasicPoolEntry basicPoolEntry = (BasicPoolEntry) it.next();
                if (basicPoolEntry.isExpired(currentTimeMillis)) {
                    if (this.log.isDebugEnabled()) {
                        Log log2 = this.log;
                        StringBuilder sb = new StringBuilder();
                        sb.append("Closing connection expired @ ");
                        sb.append(new Date(basicPoolEntry.getExpiry()));
                        log2.debug(sb.toString());
                    }
                    it.remove();
                    deleteEntry(basicPoolEntry);
                }
            }
        } finally {
            this.poolLock.unlock();
        }
    }

    public void shutdown() {
        this.poolLock.lock();
        try {
            if (!this.shutdown) {
                this.shutdown = true;
                Iterator it = this.leasedConnections.iterator();
                while (it.hasNext()) {
                    BasicPoolEntry basicPoolEntry = (BasicPoolEntry) it.next();
                    it.remove();
                    closeConnection(basicPoolEntry);
                }
                Iterator it2 = this.freeConnections.iterator();
                while (it2.hasNext()) {
                    BasicPoolEntry basicPoolEntry2 = (BasicPoolEntry) it2.next();
                    it2.remove();
                    if (this.log.isDebugEnabled()) {
                        Log log2 = this.log;
                        StringBuilder sb = new StringBuilder();
                        sb.append("Closing connection [");
                        sb.append(basicPoolEntry2.getPlannedRoute());
                        sb.append("][");
                        sb.append(basicPoolEntry2.getState());
                        sb.append("]");
                        log2.debug(sb.toString());
                    }
                    closeConnection(basicPoolEntry2);
                }
                Iterator it3 = this.waitingThreads.iterator();
                while (it3.hasNext()) {
                    WaitingThread waitingThread = (WaitingThread) it3.next();
                    it3.remove();
                    waitingThread.wakeup();
                }
                this.routeToPool.clear();
                this.poolLock.unlock();
            }
        } finally {
            this.poolLock.unlock();
        }
    }

    public void setMaxTotalConnections(int i) {
        this.poolLock.lock();
        try {
            this.maxTotalConnections = i;
        } finally {
            this.poolLock.unlock();
        }
    }

    public int getMaxTotalConnections() {
        return this.maxTotalConnections;
    }
}
