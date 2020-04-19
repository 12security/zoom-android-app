package com.google.android.gms.measurement.internal;

final class zzcc implements Runnable {
    private final /* synthetic */ zzm zzos;
    private final /* synthetic */ zzca zzot;
    private final /* synthetic */ zzr zzou;

    zzcc(zzca zzca, zzr zzr, zzm zzm) {
        this.zzot = zzca;
        this.zzou = zzr;
        this.zzos = zzm;
    }

    public final void run() {
        this.zzot.zzkt.zzgf();
        this.zzot.zzkt.zzc(this.zzou, this.zzos);
    }
}
