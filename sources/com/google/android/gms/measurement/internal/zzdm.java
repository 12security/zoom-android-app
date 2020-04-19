package com.google.android.gms.measurement.internal;

import android.os.Bundle;

final class zzdm implements Runnable {
    private final /* synthetic */ zzdd zzpm;
    private final /* synthetic */ Bundle zzpt;

    zzdm(zzdd zzdd, Bundle bundle) {
        this.zzpm = zzdd;
        this.zzpt = bundle;
    }

    public final void run() {
        this.zzpm.zzf(this.zzpt);
    }
}
