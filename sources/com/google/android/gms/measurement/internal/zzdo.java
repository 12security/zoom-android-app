package com.google.android.gms.measurement.internal;

import java.util.concurrent.atomic.AtomicReference;

final class zzdo implements Runnable {
    private final /* synthetic */ AtomicReference zzpl;
    private final /* synthetic */ zzdd zzpm;

    zzdo(zzdd zzdd, AtomicReference atomicReference) {
        this.zzpm = zzdd;
        this.zzpl = atomicReference;
    }

    public final void run() {
        synchronized (this.zzpl) {
            try {
                this.zzpl.set(this.zzpm.zzaf().zzq(this.zzpm.zzt().zzan()));
                this.zzpl.notify();
            } catch (Throwable th) {
                this.zzpl.notify();
                throw th;
            }
        }
    }
}
