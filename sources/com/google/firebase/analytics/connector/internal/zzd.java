package com.google.firebase.analytics.connector.internal;

import com.google.android.gms.measurement.AppMeasurement;
import com.google.firebase.analytics.connector.AnalyticsConnector.AnalyticsConnectorListener;
import java.util.HashSet;
import java.util.Set;

public final class zzd implements zza {
    private AppMeasurement zzaau;
    Set<String> zzabg;
    /* access modifiers changed from: private */
    public AnalyticsConnectorListener zzabh;
    private zze zzabi = new zze(this);

    public zzd(AppMeasurement appMeasurement, AnalyticsConnectorListener analyticsConnectorListener) {
        this.zzabh = analyticsConnectorListener;
        this.zzaau = appMeasurement;
        this.zzaau.registerOnMeasurementEventListener(this.zzabi);
        this.zzabg = new HashSet();
    }

    public final AnalyticsConnectorListener zzju() {
        return this.zzabh;
    }

    public final void registerEventNames(Set<String> set) {
        this.zzabg.clear();
        Set<String> set2 = this.zzabg;
        HashSet hashSet = new HashSet();
        for (String str : set) {
            if (hashSet.size() >= 50) {
                break;
            } else if (zzc.zzcj(str) && zzc.zzci(str)) {
                hashSet.add(zzc.zzcl(str));
            }
        }
        set2.addAll(hashSet);
    }

    public final void unregisterEventNames() {
        this.zzabg.clear();
    }
}
