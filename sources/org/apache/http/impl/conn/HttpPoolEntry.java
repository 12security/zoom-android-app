package org.apache.http.impl.conn;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.apache.commons.logging.Log;
import org.apache.http.conn.OperatedClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.routing.RouteTracker;
import org.apache.http.pool.PoolEntry;

@Deprecated
class HttpPoolEntry extends PoolEntry<HttpRoute, OperatedClientConnection> {
    private final Log log;
    private final RouteTracker tracker;

    public HttpPoolEntry(Log log2, String str, HttpRoute httpRoute, OperatedClientConnection operatedClientConnection, long j, TimeUnit timeUnit) {
        super(str, httpRoute, operatedClientConnection, j, timeUnit);
        this.log = log2;
        this.tracker = new RouteTracker(httpRoute);
    }

    public boolean isExpired(long j) {
        boolean isExpired = super.isExpired(j);
        if (isExpired && this.log.isDebugEnabled()) {
            Log log2 = this.log;
            StringBuilder sb = new StringBuilder();
            sb.append("Connection ");
            sb.append(this);
            sb.append(" expired @ ");
            sb.append(new Date(getExpiry()));
            log2.debug(sb.toString());
        }
        return isExpired;
    }

    /* access modifiers changed from: 0000 */
    public RouteTracker getTracker() {
        return this.tracker;
    }

    /* access modifiers changed from: 0000 */
    public HttpRoute getPlannedRoute() {
        return (HttpRoute) getRoute();
    }

    /* access modifiers changed from: 0000 */
    public HttpRoute getEffectiveRoute() {
        return this.tracker.toRoute();
    }

    public boolean isClosed() {
        return !((OperatedClientConnection) getConnection()).isOpen();
    }

    public void close() {
        try {
            ((OperatedClientConnection) getConnection()).close();
        } catch (IOException e) {
            this.log.debug("I/O error closing connection", e);
        }
    }
}
