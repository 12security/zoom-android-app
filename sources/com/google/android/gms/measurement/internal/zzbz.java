package com.google.android.gms.measurement.internal;

final class zzbz implements Runnable {
    private final /* synthetic */ zzdc zzoo;
    private final /* synthetic */ zzby zzop;

    zzbz(zzby zzby, zzdc zzdc) {
        this.zzop = zzby;
        this.zzoo = zzdc;
    }

    public final void run() {
        this.zzop.zza(this.zzoo);
        this.zzop.start();
    }
}
