package com.google.android.gms.measurement.internal;

final class zzbp implements Runnable {
    private final /* synthetic */ zzby zzmk;
    private final /* synthetic */ zzau zzml;

    zzbp(zzbo zzbo, zzby zzby, zzau zzau) {
        this.zzmk = zzby;
        this.zzml = zzau;
    }

    public final void run() {
        if (this.zzmk.zzej() == null) {
            this.zzml.zzda().zzaq("Install Referrer Reporter is null");
            return;
        }
        zzbl zzej = this.zzmk.zzej();
        zzej.zzl.zzo();
        zzej.zzaw(zzej.zzl.getContext().getPackageName());
    }
}
