package com.google.android.gms.measurement.internal;

import androidx.annotation.NonNull;
import com.google.android.gms.common.internal.Preconditions;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

final class zzbw<V> extends FutureTask<V> implements Comparable<zzbw> {
    private final String zznh;
    private final /* synthetic */ zzbt zzni;
    private final long zznj = zzbt.zzng.getAndIncrement();
    final boolean zznk;

    zzbw(zzbt zzbt, Callable<V> callable, boolean z, String str) {
        this.zzni = zzbt;
        super(callable);
        Preconditions.checkNotNull(str);
        this.zznh = str;
        this.zznk = z;
        if (this.zznj == Long.MAX_VALUE) {
            zzbt.zzad().zzda().zzaq("Tasks index overflow");
        }
    }

    zzbw(zzbt zzbt, Runnable runnable, boolean z, String str) {
        this.zzni = zzbt;
        super(runnable, null);
        Preconditions.checkNotNull(str);
        this.zznh = str;
        this.zznk = false;
        if (this.zznj == Long.MAX_VALUE) {
            zzbt.zzad().zzda().zzaq("Tasks index overflow");
        }
    }

    /* access modifiers changed from: protected */
    public final void setException(Throwable th) {
        this.zzni.zzad().zzda().zza(this.zznh, th);
        if (th instanceof zzbu) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
        super.setException(th);
    }

    public final /* synthetic */ int compareTo(@NonNull Object obj) {
        zzbw zzbw = (zzbw) obj;
        boolean z = this.zznk;
        if (z != zzbw.zznk) {
            return z ? -1 : 1;
        }
        long j = this.zznj;
        long j2 = zzbw.zznj;
        if (j < j2) {
            return -1;
        }
        if (j > j2) {
            return 1;
        }
        this.zzni.zzad().zzdb().zza("Two tasks share the same index. index", Long.valueOf(this.zznj));
        return 0;
    }
}
