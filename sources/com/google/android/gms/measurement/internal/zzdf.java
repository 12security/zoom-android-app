package com.google.android.gms.measurement.internal;

import android.os.Bundle;

final class zzdf implements Runnable {
    private final /* synthetic */ String zzao;
    private final /* synthetic */ String zzbn;
    private final /* synthetic */ boolean zzbq;
    private final /* synthetic */ String zzdk;
    private final /* synthetic */ zzdd zzpm;
    private final /* synthetic */ long zzpn;
    private final /* synthetic */ Bundle zzpo;
    private final /* synthetic */ boolean zzpp;
    private final /* synthetic */ boolean zzpq;

    zzdf(zzdd zzdd, String str, String str2, long j, Bundle bundle, boolean z, boolean z2, boolean z3, String str3) {
        this.zzpm = zzdd;
        this.zzao = str;
        this.zzbn = str2;
        this.zzpn = j;
        this.zzpo = bundle;
        this.zzbq = z;
        this.zzpp = z2;
        this.zzpq = z3;
        this.zzdk = str3;
    }

    public final void run() {
        this.zzpm.zza(this.zzao, this.zzbn, this.zzpn, this.zzpo, this.zzbq, this.zzpp, this.zzpq, this.zzdk);
    }
}
