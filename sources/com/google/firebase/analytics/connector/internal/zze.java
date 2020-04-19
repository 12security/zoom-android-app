package com.google.firebase.analytics.connector.internal;

import android.os.Bundle;
import com.google.android.gms.measurement.AppMeasurement.OnEventListener;

final class zze implements OnEventListener {
    private final /* synthetic */ zzd zzabj;

    public zze(zzd zzd) {
        this.zzabj = zzd;
    }

    public final void onEvent(String str, String str2, Bundle bundle, long j) {
        if (this.zzabj.zzabg.contains(str2)) {
            Bundle bundle2 = new Bundle();
            bundle2.putString("events", zzc.zzck(str2));
            this.zzabj.zzabh.onMessageTriggered(2, bundle2);
        }
    }
}
