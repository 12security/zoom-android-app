package com.google.firebase.analytics.connector.internal;

import com.google.android.gms.measurement.AppMeasurement;
import com.google.firebase.analytics.connector.AnalyticsConnector.AnalyticsConnectorListener;
import java.util.Set;

public final class zzf implements zza {
    private AppMeasurement zzaau;
    /* access modifiers changed from: private */
    public AnalyticsConnectorListener zzabh;
    private zzg zzabk = new zzg(this);

    public zzf(AppMeasurement appMeasurement, AnalyticsConnectorListener analyticsConnectorListener) {
        this.zzabh = analyticsConnectorListener;
        this.zzaau = appMeasurement;
        this.zzaau.registerOnMeasurementEventListener(this.zzabk);
    }

    public final void registerEventNames(Set<String> set) {
    }

    public final void unregisterEventNames() {
    }

    public final AnalyticsConnectorListener zzju() {
        return this.zzabh;
    }
}
