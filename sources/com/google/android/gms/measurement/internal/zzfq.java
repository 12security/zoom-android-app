package com.google.android.gms.measurement.internal;

final class zzfq extends zzab {
    private final /* synthetic */ zzft zzri;
    private final /* synthetic */ zzfp zzrs;

    zzfq(zzfp zzfp, zzcv zzcv, zzft zzft) {
        this.zzrs = zzfp;
        this.zzri = zzft;
        super(zzcv);
    }

    public final void run() {
        this.zzrs.cancel();
        this.zzrs.zzad().zzdi().zzaq("Starting upload from DelayedRunnable");
        this.zzri.zzga();
    }
}
