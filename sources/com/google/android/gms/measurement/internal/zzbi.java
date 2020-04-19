package com.google.android.gms.measurement.internal;

import android.content.SharedPreferences.Editor;
import androidx.annotation.WorkerThread;
import com.google.android.gms.common.internal.Preconditions;

public final class zzbi {
    private long value;
    private final String zzjf;
    private boolean zzlx;
    private final /* synthetic */ zzbf zzly;
    private final long zzlz;

    public zzbi(zzbf zzbf, String str, long j) {
        this.zzly = zzbf;
        Preconditions.checkNotEmpty(str);
        this.zzjf = str;
        this.zzlz = j;
    }

    @WorkerThread
    public final long get() {
        if (!this.zzlx) {
            this.zzlx = true;
            this.value = this.zzly.zzdr().getLong(this.zzjf, this.zzlz);
        }
        return this.value;
    }

    @WorkerThread
    public final void set(long j) {
        Editor edit = this.zzly.zzdr().edit();
        edit.putLong(this.zzjf, j);
        edit.apply();
        this.value = j;
    }
}
