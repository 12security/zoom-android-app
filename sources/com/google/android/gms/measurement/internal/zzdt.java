package com.google.android.gms.measurement.internal;

final class zzdt implements Runnable {
    private final /* synthetic */ boolean zzaz;
    private final /* synthetic */ zzdd zzpm;

    zzdt(zzdd zzdd, boolean z) {
        this.zzpm = zzdd;
        this.zzaz = z;
    }

    public final void run() {
        this.zzpm.zzg(this.zzaz);
    }
}
