package com.google.android.gms.measurement.internal;

import android.content.Intent;

final /* synthetic */ class zzff implements Runnable {
    private final zzfe zzrc;
    private final int zzrd;
    private final zzau zzre;
    private final Intent zzrf;

    zzff(zzfe zzfe, int i, zzau zzau, Intent intent) {
        this.zzrc = zzfe;
        this.zzrd = i;
        this.zzre = zzau;
        this.zzrf = intent;
    }

    public final void run() {
        this.zzrc.zza(this.zzrd, this.zzre, this.zzrf);
    }
}
