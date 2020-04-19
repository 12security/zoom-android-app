package com.microsoft.aad.adal;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build.VERSION;

class DefaultConnectionService implements IConnectionService {
    private static final String TAG = "DefaultConnectionService";
    private final Context mConnectionContext;

    DefaultConnectionService(Context context) {
        this.mConnectionContext = context;
    }

    public boolean isConnectionAvailable() {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) this.mConnectionContext.getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting() && !isNetworkDisabledFromOptimizations();
    }

    @TargetApi(23)
    public boolean isNetworkDisabledFromOptimizations() {
        if (VERSION.SDK_INT >= 23) {
            if (UsageStatsManagerWrapper.getInstance().isAppInactive(this.mConnectionContext)) {
                Logger.m239w(TAG, "Client app is inactive. Network is disabled.", "", ADALError.NO_NETWORK_CONNECTION_POWER_OPTIMIZATION);
                return true;
            }
            PowerManagerWrapper instance = PowerManagerWrapper.getInstance();
            if (instance.isDeviceIdleMode(this.mConnectionContext) && !instance.isIgnoringBatteryOptimizations(this.mConnectionContext)) {
                Logger.m239w(TAG, "Device is dozing. Network is disabled.", "", ADALError.NO_NETWORK_CONNECTION_POWER_OPTIMIZATION);
                return true;
            }
        }
        return false;
    }
}
