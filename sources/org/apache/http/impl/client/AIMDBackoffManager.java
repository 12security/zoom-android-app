package org.apache.http.impl.client;

import java.util.HashMap;
import java.util.Map;
import org.apache.http.client.BackoffManager;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.pool.ConnPoolControl;
import org.apache.http.util.Args;

public class AIMDBackoffManager implements BackoffManager {
    private double backoffFactor;
    private int cap;
    private final Clock clock;
    private final ConnPoolControl<HttpRoute> connPerRoute;
    private long coolDown;
    private final Map<HttpRoute, Long> lastRouteBackoffs;
    private final Map<HttpRoute, Long> lastRouteProbes;

    public AIMDBackoffManager(ConnPoolControl<HttpRoute> connPoolControl) {
        this(connPoolControl, new SystemClock());
    }

    AIMDBackoffManager(ConnPoolControl<HttpRoute> connPoolControl, Clock clock2) {
        this.coolDown = 5000;
        this.backoffFactor = 0.5d;
        this.cap = 2;
        this.clock = clock2;
        this.connPerRoute = connPoolControl;
        this.lastRouteProbes = new HashMap();
        this.lastRouteBackoffs = new HashMap();
    }

    public void backOff(HttpRoute httpRoute) {
        synchronized (this.connPerRoute) {
            int maxPerRoute = this.connPerRoute.getMaxPerRoute(httpRoute);
            Long lastUpdate = getLastUpdate(this.lastRouteBackoffs, httpRoute);
            long currentTime = this.clock.getCurrentTime();
            if (currentTime - lastUpdate.longValue() >= this.coolDown) {
                this.connPerRoute.setMaxPerRoute(httpRoute, getBackedOffPoolSize(maxPerRoute));
                this.lastRouteBackoffs.put(httpRoute, Long.valueOf(currentTime));
            }
        }
    }

    private int getBackedOffPoolSize(int i) {
        if (i <= 1) {
            return 1;
        }
        return (int) Math.floor(this.backoffFactor * ((double) i));
    }

    /* JADX WARNING: Code restructure failed: missing block: B:16:0x004e, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void probe(org.apache.http.conn.routing.HttpRoute r11) {
        /*
            r10 = this;
            org.apache.http.pool.ConnPoolControl<org.apache.http.conn.routing.HttpRoute> r0 = r10.connPerRoute
            monitor-enter(r0)
            org.apache.http.pool.ConnPoolControl<org.apache.http.conn.routing.HttpRoute> r1 = r10.connPerRoute     // Catch:{ all -> 0x004f }
            int r1 = r1.getMaxPerRoute(r11)     // Catch:{ all -> 0x004f }
            int r2 = r10.cap     // Catch:{ all -> 0x004f }
            if (r1 < r2) goto L_0x0010
            int r1 = r10.cap     // Catch:{ all -> 0x004f }
            goto L_0x0012
        L_0x0010:
            int r1 = r1 + 1
        L_0x0012:
            java.util.Map<org.apache.http.conn.routing.HttpRoute, java.lang.Long> r2 = r10.lastRouteProbes     // Catch:{ all -> 0x004f }
            java.lang.Long r2 = r10.getLastUpdate(r2, r11)     // Catch:{ all -> 0x004f }
            java.util.Map<org.apache.http.conn.routing.HttpRoute, java.lang.Long> r3 = r10.lastRouteBackoffs     // Catch:{ all -> 0x004f }
            java.lang.Long r3 = r10.getLastUpdate(r3, r11)     // Catch:{ all -> 0x004f }
            org.apache.http.impl.client.Clock r4 = r10.clock     // Catch:{ all -> 0x004f }
            long r4 = r4.getCurrentTime()     // Catch:{ all -> 0x004f }
            long r6 = r2.longValue()     // Catch:{ all -> 0x004f }
            long r6 = r4 - r6
            long r8 = r10.coolDown     // Catch:{ all -> 0x004f }
            int r2 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1))
            if (r2 < 0) goto L_0x004d
            long r2 = r3.longValue()     // Catch:{ all -> 0x004f }
            long r2 = r4 - r2
            long r6 = r10.coolDown     // Catch:{ all -> 0x004f }
            int r2 = (r2 > r6 ? 1 : (r2 == r6 ? 0 : -1))
            if (r2 >= 0) goto L_0x003d
            goto L_0x004d
        L_0x003d:
            org.apache.http.pool.ConnPoolControl<org.apache.http.conn.routing.HttpRoute> r2 = r10.connPerRoute     // Catch:{ all -> 0x004f }
            r2.setMaxPerRoute(r11, r1)     // Catch:{ all -> 0x004f }
            java.util.Map<org.apache.http.conn.routing.HttpRoute, java.lang.Long> r1 = r10.lastRouteProbes     // Catch:{ all -> 0x004f }
            java.lang.Long r2 = java.lang.Long.valueOf(r4)     // Catch:{ all -> 0x004f }
            r1.put(r11, r2)     // Catch:{ all -> 0x004f }
            monitor-exit(r0)     // Catch:{ all -> 0x004f }
            return
        L_0x004d:
            monitor-exit(r0)     // Catch:{ all -> 0x004f }
            return
        L_0x004f:
            r11 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x004f }
            throw r11
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.http.impl.client.AIMDBackoffManager.probe(org.apache.http.conn.routing.HttpRoute):void");
    }

    private Long getLastUpdate(Map<HttpRoute, Long> map, HttpRoute httpRoute) {
        Long l = (Long) map.get(httpRoute);
        return l == null ? Long.valueOf(0) : l;
    }

    public void setBackoffFactor(double d) {
        Args.check(d > 0.0d && d < 1.0d, "Backoff factor must be 0.0 < f < 1.0");
        this.backoffFactor = d;
    }

    public void setCooldownMillis(long j) {
        Args.positive(this.coolDown, "Cool down");
        this.coolDown = j;
    }

    public void setPerHostConnectionCap(int i) {
        Args.positive(i, "Per host connection cap");
        this.cap = i;
    }
}
