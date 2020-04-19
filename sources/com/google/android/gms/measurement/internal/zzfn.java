package com.google.android.gms.measurement.internal;

final class zzfn implements Runnable {
    private final /* synthetic */ long zzcd;
    private final /* synthetic */ zzfj zzro;

    zzfn(zzfj zzfj, long j) {
        this.zzro = zzfj;
        this.zzcd = j;
    }

    public final void run() {
        this.zzro.zzac(this.zzcd);
    }
}
