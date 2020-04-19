package com.google.android.gms.measurement.internal;

final class zzcb implements Runnable {
    private final /* synthetic */ zzm zzos;
    private final /* synthetic */ zzca zzot;

    zzcb(zzca zzca, zzm zzm) {
        this.zzot = zzca;
        this.zzos = zzm;
    }

    public final void run() {
        this.zzot.zzkt.zzgf();
        this.zzot.zzkt.zze(this.zzos);
    }
}
