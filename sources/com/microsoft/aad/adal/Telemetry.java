package com.microsoft.aad.adal;

import android.util.Pair;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class Telemetry {
    private static final Telemetry INSTANCE = new Telemetry();
    private static final String TAG = "Telemetry";
    private static boolean sAllowPii = false;
    private DefaultDispatcher mDispatcher = null;
    private final Map<Pair<String, String>, String> mEventTracking = new ConcurrentHashMap();

    public static synchronized Telemetry getInstance() {
        Telemetry telemetry;
        synchronized (Telemetry.class) {
            telemetry = INSTANCE;
        }
        return telemetry;
    }

    public static void setAllowPii(boolean z) {
        sAllowPii = z;
    }

    public static boolean getAllowPii() {
        return sAllowPii;
    }

    public synchronized void registerDispatcher(IDispatcher iDispatcher, boolean z) {
        if (z) {
            this.mDispatcher = new AggregatedDispatcher(iDispatcher);
        } else {
            this.mDispatcher = new DefaultDispatcher(iDispatcher);
        }
    }

    static String registerNewRequest() {
        return UUID.randomUUID().toString();
    }

    /* access modifiers changed from: 0000 */
    public void startEvent(String str, String str2) {
        if (this.mDispatcher != null) {
            this.mEventTracking.put(new Pair(str, str2), Long.toString(System.currentTimeMillis()));
        }
    }

    /* access modifiers changed from: 0000 */
    public void stopEvent(String str, IEvents iEvents, String str2) {
        if (this.mDispatcher != null) {
            String str3 = (String) this.mEventTracking.remove(new Pair(str, str2));
            if (StringExtensions.isNullOrBlank(str3)) {
                Logger.m239w(TAG, "Stop Event called without a corresponding start_event", "", null);
                return;
            }
            long parseLong = Long.parseLong(str3);
            long currentTimeMillis = System.currentTimeMillis();
            long j = currentTimeMillis - parseLong;
            String l = Long.toString(currentTimeMillis);
            iEvents.setProperty("Microsoft.ADAL.start_time", str3);
            iEvents.setProperty("Microsoft.ADAL.stop_time", l);
            iEvents.setProperty("Microsoft.ADAL.response_time", Long.toString(j));
            this.mDispatcher.receive(str, iEvents);
        }
    }

    /* access modifiers changed from: 0000 */
    public void flush(String str) {
        DefaultDispatcher defaultDispatcher = this.mDispatcher;
        if (defaultDispatcher != null) {
            defaultDispatcher.flush(str);
        }
    }
}
