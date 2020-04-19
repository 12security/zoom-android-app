package com.google.android.gms.measurement.internal;

import android.content.SharedPreferences.Editor;
import androidx.annotation.WorkerThread;
import com.google.android.gms.common.internal.Preconditions;

public final class zzbk {
    private String value;
    private final String zzjf;
    private boolean zzlx;
    private final /* synthetic */ zzbf zzly;
    private final String zzme = null;

    public zzbk(zzbf zzbf, String str, String str2) {
        this.zzly = zzbf;
        Preconditions.checkNotEmpty(str);
        this.zzjf = str;
    }

    @WorkerThread
    public final String zzed() {
        if (!this.zzlx) {
            this.zzlx = true;
            this.value = this.zzly.zzdr().getString(this.zzjf, null);
        }
        return this.value;
    }

    @WorkerThread
    public final void zzav(String str) {
        if (!zzgd.zzs(str, this.value)) {
            Editor edit = this.zzly.zzdr().edit();
            edit.putString(this.zzjf, str);
            edit.apply();
            this.value = str;
        }
    }
}
