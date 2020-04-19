package com.google.android.gms.measurement.internal;

import java.util.concurrent.atomic.AtomicReference;

final class zzds implements Runnable {
    private final /* synthetic */ AtomicReference zzpl;
    private final /* synthetic */ zzdd zzpm;

    zzds(zzdd zzdd, AtomicReference atomicReference) {
        this.zzpm = zzdd;
        this.zzpl = atomicReference;
    }

    public final void run() {
        synchronized (this.zzpl) {
            try {
                this.zzpl.set(Double.valueOf(this.zzpm.zzaf().zzc(this.zzpm.zzt().zzan(), zzal.zzho)));
                this.zzpl.notify();
            } catch (Throwable th) {
                this.zzpl.notify();
                throw th;
            }
        }
    }
}
