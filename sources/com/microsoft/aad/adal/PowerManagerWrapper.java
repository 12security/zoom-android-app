package com.microsoft.aad.adal;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.PowerManager;

public class PowerManagerWrapper {
    private static PowerManagerWrapper sInstance;

    static void setInstance(PowerManagerWrapper powerManagerWrapper) {
        sInstance = powerManagerWrapper;
    }

    public static synchronized PowerManagerWrapper getInstance() {
        PowerManagerWrapper powerManagerWrapper;
        synchronized (PowerManagerWrapper.class) {
            if (sInstance == null) {
                sInstance = new PowerManagerWrapper();
            }
            powerManagerWrapper = sInstance;
        }
        return powerManagerWrapper;
    }

    @TargetApi(23)
    public boolean isDeviceIdleMode(Context context) {
        return ((PowerManager) context.getSystemService("power")).isDeviceIdleMode();
    }

    @TargetApi(23)
    public boolean isIgnoringBatteryOptimizations(Context context) {
        return ((PowerManager) context.getSystemService("power")).isIgnoringBatteryOptimizations(context.getPackageName());
    }
}
