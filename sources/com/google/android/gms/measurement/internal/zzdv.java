package com.google.android.gms.measurement.internal;

final class zzdv implements Runnable {
    private final /* synthetic */ long zzba;
    private final /* synthetic */ zzdd zzpm;

    zzdv(zzdd zzdd, long j) {
        this.zzpm = zzdd;
        this.zzba = j;
    }

    public final void run() {
        this.zzpm.zzae().zzlm.set(this.zzba);
        this.zzpm.zzad().zzdh().zza("Minimum session duration set", Long.valueOf(this.zzba));
    }
}
