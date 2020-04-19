package com.google.android.gms.measurement.internal;

final class zzdk implements Runnable {
    private final /* synthetic */ zzda zzbc;
    private final /* synthetic */ zzdd zzpm;

    zzdk(zzdd zzdd, zzda zzda) {
        this.zzpm = zzdd;
        this.zzbc = zzda;
    }

    public final void run() {
        this.zzpm.zza(this.zzbc);
    }
}
