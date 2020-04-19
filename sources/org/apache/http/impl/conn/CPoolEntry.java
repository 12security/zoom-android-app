package org.apache.http.impl.conn;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.apache.commons.logging.Log;
import org.apache.http.HttpClientConnection;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.pool.PoolEntry;

@Contract(threading = ThreadingBehavior.SAFE)
class CPoolEntry extends PoolEntry<HttpRoute, ManagedHttpClientConnection> {
    private final Log log;
    private volatile boolean routeComplete;

    public CPoolEntry(Log log2, String str, HttpRoute httpRoute, ManagedHttpClientConnection managedHttpClientConnection, long j, TimeUnit timeUnit) {
        super(str, httpRoute, managedHttpClientConnection, j, timeUnit);
        this.log = log2;
    }

    public void markRouteComplete() {
        this.routeComplete = true;
    }

    public boolean isRouteComplete() {
        return this.routeComplete;
    }

    public void closeConnection() throws IOException {
        ((HttpClientConnection) getConnection()).close();
    }

    public void shutdownConnection() throws IOException {
        ((HttpClientConnection) getConnection()).shutdown();
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

    public boolean isClosed() {
        return !((HttpClientConnection) getConnection()).isOpen();
    }

    public void close() {
        try {
            closeConnection();
        } catch (IOException e) {
            this.log.debug("I/O error closing connection", e);
        }
    }
}
