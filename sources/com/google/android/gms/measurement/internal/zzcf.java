package com.google.android.gms.measurement.internal;

final class zzcf implements Runnable {
    private final /* synthetic */ zzca zzot;
    private final /* synthetic */ zzr zzou;

    zzcf(zzca zzca, zzr zzr) {
        this.zzot = zzca;
        this.zzou = zzr;
    }

    public final void run() {
        this.zzot.zzkt.zzgf();
        this.zzot.zzkt.zze(this.zzou);
    }
}
