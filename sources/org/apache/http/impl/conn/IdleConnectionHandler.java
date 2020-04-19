package org.apache.http.impl.conn;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpConnection;

@Deprecated
public class IdleConnectionHandler {
    private final Map<HttpConnection, TimeValues> connectionToTimes = new HashMap();
    private final Log log = LogFactory.getLog(getClass());

    private static class TimeValues {
        /* access modifiers changed from: private */
        public final long timeAdded;
        /* access modifiers changed from: private */
        public final long timeExpires;

        TimeValues(long j, long j2, TimeUnit timeUnit) {
            this.timeAdded = j;
            if (j2 > 0) {
                this.timeExpires = j + timeUnit.toMillis(j2);
            } else {
                this.timeExpires = Long.MAX_VALUE;
            }
        }
    }

    public void add(HttpConnection httpConnection, long j, TimeUnit timeUnit) {
        long currentTimeMillis = System.currentTimeMillis();
        if (this.log.isDebugEnabled()) {
            Log log2 = this.log;
            StringBuilder sb = new StringBuilder();
            sb.append("Adding connection at: ");
            sb.append(currentTimeMillis);
            log2.debug(sb.toString());
        }
        Map<HttpConnection, TimeValues> map = this.connectionToTimes;
        TimeValues timeValues = new TimeValues(currentTimeMillis, j, timeUnit);
        map.put(httpConnection, timeValues);
    }

    public boolean remove(HttpConnection httpConnection) {
        TimeValues timeValues = (TimeValues) this.connectionToTimes.remove(httpConnection);
        boolean z = true;
        if (timeValues == null) {
            this.log.warn("Removing a connection that never existed!");
            return true;
        }
        if (System.currentTimeMillis() > timeValues.timeExpires) {
            z = false;
        }
        return z;
    }

    public void removeAll() {
        this.connectionToTimes.clear();
    }

    public void closeIdleConnections(long j) {
        long currentTimeMillis = System.currentTimeMillis() - j;
        if (this.log.isDebugEnabled()) {
            Log log2 = this.log;
            StringBuilder sb = new StringBuilder();
            sb.append("Checking for connections, idle timeout: ");
            sb.append(currentTimeMillis);
            log2.debug(sb.toString());
        }
        for (Entry entry : this.connectionToTimes.entrySet()) {
            HttpConnection httpConnection = (HttpConnection) entry.getKey();
            long access$100 = ((TimeValues) entry.getValue()).timeAdded;
            if (access$100 <= currentTimeMillis) {
                if (this.log.isDebugEnabled()) {
                    Log log3 = this.log;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("Closing idle connection, connection time: ");
                    sb2.append(access$100);
                    log3.debug(sb2.toString());
                }
                try {
                    httpConnection.close();
                } catch (IOException e) {
                    this.log.debug("I/O error closing connection", e);
                }
            }
        }
    }

    public void closeExpiredConnections() {
        long currentTimeMillis = System.currentTimeMillis();
        if (this.log.isDebugEnabled()) {
            Log log2 = this.log;
            StringBuilder sb = new StringBuilder();
            sb.append("Checking for expired connections, now: ");
            sb.append(currentTimeMillis);
            log2.debug(sb.toString());
        }
        for (Entry entry : this.connectionToTimes.entrySet()) {
            HttpConnection httpConnection = (HttpConnection) entry.getKey();
            TimeValues timeValues = (TimeValues) entry.getValue();
            if (timeValues.timeExpires <= currentTimeMillis) {
                if (this.log.isDebugEnabled()) {
                    Log log3 = this.log;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("Closing connection, expired @: ");
                    sb2.append(timeValues.timeExpires);
                    log3.debug(sb2.toString());
                }
                try {
                    httpConnection.close();
                } catch (IOException e) {
                    this.log.debug("I/O error closing connection", e);
                }
            }
        }
    }
}
