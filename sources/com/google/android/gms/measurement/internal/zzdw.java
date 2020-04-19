package com.google.android.gms.measurement.internal;

final class zzdw implements Runnable {
    private final /* synthetic */ long zzba;
    private final /* synthetic */ zzdd zzpm;

    zzdw(zzdd zzdd, long j) {
        this.zzpm = zzdd;
        this.zzba = j;
    }

    public final void run() {
        this.zzpm.zzae().zzln.set(this.zzba);
        this.zzpm.zzad().zzdh().zza("Session timeout duration set", Long.valueOf(this.zzba));
    }
}
