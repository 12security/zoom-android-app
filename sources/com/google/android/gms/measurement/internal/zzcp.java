package com.google.android.gms.measurement.internal;

final class zzcp implements Runnable {
    private final /* synthetic */ zzm zzos;
    private final /* synthetic */ zzca zzot;
    private final /* synthetic */ zzga zzov;

    zzcp(zzca zzca, zzga zzga, zzm zzm) {
        this.zzot = zzca;
        this.zzov = zzga;
        this.zzos = zzm;
    }

    public final void run() {
        this.zzot.zzkt.zzgf();
        this.zzot.zzkt.zzb(this.zzov, this.zzos);
    }
}
