package com.google.android.gms.measurement.internal;

final class zzfm implements Runnable {
    private final /* synthetic */ long zzcd;
    private final /* synthetic */ zzfj zzro;

    zzfm(zzfj zzfj, long j) {
        this.zzro = zzfj;
        this.zzcd = j;
    }

    public final void run() {
        this.zzro.zzaa(this.zzcd);
    }
}
