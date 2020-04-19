package com.google.android.gms.measurement.internal;

final class zzcm implements Runnable {
    private final /* synthetic */ zzaj zzdj;
    private final /* synthetic */ String zzdk;
    private final /* synthetic */ zzca zzot;

    zzcm(zzca zzca, zzaj zzaj, String str) {
        this.zzot = zzca;
        this.zzdj = zzaj;
        this.zzdk = str;
    }

    public final void run() {
        this.zzot.zzkt.zzgf();
        this.zzot.zzkt.zzd(this.zzdj, this.zzdk);
    }
}
