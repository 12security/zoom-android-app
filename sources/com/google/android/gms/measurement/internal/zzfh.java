package com.google.android.gms.measurement.internal;

final class zzfh implements Runnable {
    private final /* synthetic */ zzft zzri;
    private final /* synthetic */ Runnable zzrj;

    zzfh(zzfe zzfe, zzft zzft, Runnable runnable) {
        this.zzri = zzft;
        this.zzrj = runnable;
    }

    public final void run() {
        this.zzri.zzgf();
        this.zzri.zzf(this.zzrj);
        this.zzri.zzga();
    }
}
