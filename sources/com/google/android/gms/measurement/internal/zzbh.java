package com.google.android.gms.measurement.internal;

import android.content.SharedPreferences.Editor;
import androidx.annotation.WorkerThread;
import com.google.android.gms.common.internal.Preconditions;

public final class zzbh {
    private boolean value;
    private final String zzjf;
    private final boolean zzlw;
    private boolean zzlx;
    private final /* synthetic */ zzbf zzly;

    public zzbh(zzbf zzbf, String str, boolean z) {
        this.zzly = zzbf;
        Preconditions.checkNotEmpty(str);
        this.zzjf = str;
        this.zzlw = z;
    }

    @WorkerThread
    public final boolean get() {
        if (!this.zzlx) {
            this.zzlx = true;
            this.value = this.zzly.zzdr().getBoolean(this.zzjf, this.zzlw);
        }
        return this.value;
    }

    @WorkerThread
    public final void set(boolean z) {
        Editor edit = this.zzly.zzdr().edit();
        edit.putBoolean(this.zzjf, z);
        edit.apply();
        this.value = z;
    }
}
