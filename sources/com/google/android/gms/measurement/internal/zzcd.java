package com.google.android.gms.measurement.internal;

final class zzcd implements Runnable {
    private final /* synthetic */ zzm zzos;
    private final /* synthetic */ zzca zzot;
    private final /* synthetic */ zzr zzou;

    zzcd(zzca zzca, zzr zzr, zzm zzm) {
        this.zzot = zzca;
        this.zzou = zzr;
        this.zzos = zzm;
    }

    public final void run() {
        this.zzot.zzkt.zzgf();
        this.zzot.zzkt.zzb(this.zzou, this.zzos);
    }
}
