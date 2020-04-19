package com.google.android.gms.measurement.internal;

final class zzcr implements Runnable {
    private final /* synthetic */ zzm zzos;
    private final /* synthetic */ zzca zzot;

    zzcr(zzca zzca, zzm zzm) {
        this.zzot = zzca;
        this.zzos = zzm;
    }

    public final void run() {
        this.zzot.zzkt.zzgf();
        this.zzot.zzkt.zzf(this.zzos);
    }
}
