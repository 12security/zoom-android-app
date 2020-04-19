package com.google.android.gms.measurement.internal;

final class zzfu implements Runnable {
    private final /* synthetic */ zzfy zzsq;
    private final /* synthetic */ zzft zzsr;

    zzfu(zzft zzft, zzfy zzfy) {
        this.zzsr = zzft;
        this.zzsq = zzfy;
    }

    public final void run() {
        this.zzsr.zza(this.zzsq);
        this.zzsr.start();
    }
}
