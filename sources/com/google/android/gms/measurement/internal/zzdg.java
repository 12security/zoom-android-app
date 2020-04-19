package com.google.android.gms.measurement.internal;

final class zzdg implements Runnable {
    private final /* synthetic */ String zzao;
    private final /* synthetic */ String zzbn;
    private final /* synthetic */ zzdd zzpm;
    private final /* synthetic */ long zzpn;
    private final /* synthetic */ Object zzpr;

    zzdg(zzdd zzdd, String str, String str2, Object obj, long j) {
        this.zzpm = zzdd;
        this.zzao = str;
        this.zzbn = str2;
        this.zzpr = obj;
        this.zzpn = j;
    }

    public final void run() {
        this.zzpm.zza(this.zzao, this.zzbn, this.zzpr, this.zzpn);
    }
}
