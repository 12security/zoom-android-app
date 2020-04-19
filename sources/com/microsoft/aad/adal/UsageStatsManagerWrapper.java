package com.microsoft.aad.adal;

import android.annotation.TargetApi;
import android.app.usage.UsageStatsManager;
import android.content.Context;

public class UsageStatsManagerWrapper {
    private static UsageStatsManagerWrapper sInstance;

    static synchronized void setInstance(UsageStatsManagerWrapper usageStatsManagerWrapper) {
        synchronized (UsageStatsManagerWrapper.class) {
            sInstance = usageStatsManagerWrapper;
        }
    }

    public static synchronized UsageStatsManagerWrapper getInstance() {
        UsageStatsManagerWrapper usageStatsManagerWrapper;
        synchronized (UsageStatsManagerWrapper.class) {
            if (sInstance == null) {
                sInstance = new UsageStatsManagerWrapper();
            }
            usageStatsManagerWrapper = sInstance;
        }
        return usageStatsManagerWrapper;
    }

    @TargetApi(23)
    public boolean isAppInactive(Context context) {
        return ((UsageStatsManager) context.getSystemService("usagestats")).isAppInactive(context.getPackageName());
    }
}
