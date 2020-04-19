package com.google.android.gms.measurement.internal;

import java.util.concurrent.atomic.AtomicReference;

final class zzdh implements Runnable {
    private final /* synthetic */ boolean zzbd;
    private final /* synthetic */ AtomicReference zzpl;
    private final /* synthetic */ zzdd zzpm;

    zzdh(zzdd zzdd, AtomicReference atomicReference, boolean z) {
        this.zzpm = zzdd;
        this.zzpl = atomicReference;
        this.zzbd = z;
    }

    public final void run() {
        this.zzpm.zzu().zza(this.zzpl, this.zzbd);
    }
}
