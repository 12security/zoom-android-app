package com.google.android.gms.measurement.internal;

final class zzcs implements Runnable {
    private final /* synthetic */ String zzax;
    private final /* synthetic */ String zzdk;
    private final /* synthetic */ zzca zzot;
    private final /* synthetic */ String zzow;
    private final /* synthetic */ long zzox;

    zzcs(zzca zzca, String str, String str2, String str3, long j) {
        this.zzot = zzca;
        this.zzow = str;
        this.zzdk = str2;
        this.zzax = str3;
        this.zzox = j;
    }

    public final void run() {
        String str = this.zzow;
        if (str == null) {
            this.zzot.zzkt.zzgi().zzv().zza(this.zzdk, (zzec) null);
            return;
        }
        this.zzot.zzkt.zzgi().zzv().zza(this.zzdk, new zzec(this.zzax, str, this.zzox));
    }
}
