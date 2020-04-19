package com.google.android.gms.measurement.internal;

import java.util.concurrent.atomic.AtomicReference;

final class zzdr implements Runnable {
    private final /* synthetic */ AtomicReference zzpl;
    private final /* synthetic */ zzdd zzpm;

    zzdr(zzdd zzdd, AtomicReference atomicReference) {
        this.zzpm = zzdd;
        this.zzpl = atomicReference;
    }

    public final void run() {
        synchronized (this.zzpl) {
            try {
                this.zzpl.set(Integer.valueOf(this.zzpm.zzaf().zzb(this.zzpm.zzt().zzan(), zzal.zzhn)));
                this.zzpl.notify();
            } catch (Throwable th) {
                this.zzpl.notify();
                throw th;
            }
        }
    }
}
