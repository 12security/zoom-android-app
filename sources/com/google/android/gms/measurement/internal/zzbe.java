package com.google.android.gms.measurement.internal;

final class zzbe implements Runnable {
    private final /* synthetic */ boolean zzkw;
    private final /* synthetic */ zzbd zzkx;

    zzbe(zzbd zzbd, boolean z) {
        this.zzkx = zzbd;
        this.zzkw = z;
    }

    public final void run() {
        this.zzkx.zzkt.zzj(this.zzkw);
    }
}
