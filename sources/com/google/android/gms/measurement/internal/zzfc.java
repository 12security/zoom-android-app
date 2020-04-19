package com.google.android.gms.measurement.internal;

import android.content.ComponentName;
import android.content.Context;

final class zzfc implements Runnable {
    private final /* synthetic */ zzey zzqz;

    zzfc(zzey zzey) {
        this.zzqz = zzey;
    }

    public final void run() {
        zzeg zzeg = this.zzqz.zzqq;
        Context context = this.zzqz.zzqq.getContext();
        this.zzqz.zzqq.zzag();
        zzeg.onServiceDisconnected(new ComponentName(context, "com.google.android.gms.measurement.AppMeasurementService"));
    }
}
