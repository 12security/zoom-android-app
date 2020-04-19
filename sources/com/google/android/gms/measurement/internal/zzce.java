package com.google.android.gms.measurement.internal;

final class zzce implements Runnable {
    private final /* synthetic */ zzca zzot;
    private final /* synthetic */ zzr zzou;

    zzce(zzca zzca, zzr zzr) {
        this.zzot = zzca;
        this.zzou = zzr;
    }

    public final void run() {
        this.zzot.zzkt.zzgf();
        this.zzot.zzkt.zzf(this.zzou);
    }
}
