package com.google.android.gms.measurement.internal;

import java.util.concurrent.atomic.AtomicReference;

final class zzdq implements Runnable {
    private final /* synthetic */ AtomicReference zzpl;
    private final /* synthetic */ zzdd zzpm;

    zzdq(zzdd zzdd, AtomicReference atomicReference) {
        this.zzpm = zzdd;
        this.zzpl = atomicReference;
    }

    public final void run() {
        synchronized (this.zzpl) {
            try {
                this.zzpl.set(Long.valueOf(this.zzpm.zzaf().zza(this.zzpm.zzt().zzan(), zzal.zzhm)));
                this.zzpl.notify();
            } catch (Throwable th) {
                this.zzpl.notify();
                throw th;
            }
        }
    }
}
