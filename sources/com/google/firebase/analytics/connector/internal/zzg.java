package com.google.firebase.analytics.connector.internal;

import android.os.Bundle;
import com.google.android.gms.measurement.AppMeasurement.OnEventListener;

final class zzg implements OnEventListener {
    private final /* synthetic */ zzf zzabl;

    public zzg(zzf zzf) {
        this.zzabl = zzf;
    }

    public final void onEvent(String str, String str2, Bundle bundle, long j) {
        if (str != null && !str.equals("crash") && zzc.zzch(str2)) {
            Bundle bundle2 = new Bundle();
            bundle2.putString("name", str2);
            bundle2.putLong("timestampInMillis", j);
            bundle2.putBundle("params", bundle);
            this.zzabl.zzabh.onMessageTriggered(3, bundle2);
        }
    }
}
