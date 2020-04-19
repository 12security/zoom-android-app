package com.google.android.gms.measurement.internal;

import java.util.concurrent.atomic.AtomicReference;

final class zzdi implements Runnable {
    private final /* synthetic */ AtomicReference zzpl;
    private final /* synthetic */ zzdd zzpm;

    zzdi(zzdd zzdd, AtomicReference atomicReference) {
        this.zzpm = zzdd;
        this.zzpl = atomicReference;
    }

    public final void run() {
        this.zzpm.zzu().zza(this.zzpl);
    }
}
