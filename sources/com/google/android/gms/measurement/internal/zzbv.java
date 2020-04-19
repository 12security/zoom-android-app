package com.google.android.gms.measurement.internal;

import com.google.android.gms.common.internal.Preconditions;
import java.lang.Thread.UncaughtExceptionHandler;

final class zzbv implements UncaughtExceptionHandler {
    private final String zznh;
    private final /* synthetic */ zzbt zzni;

    public zzbv(zzbt zzbt, String str) {
        this.zzni = zzbt;
        Preconditions.checkNotNull(str);
        this.zznh = str;
    }

    public final synchronized void uncaughtException(Thread thread, Throwable th) {
        this.zzni.zzad().zzda().zza(this.zznh, th);
    }
}
