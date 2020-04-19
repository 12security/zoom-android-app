package com.google.android.gms.measurement.internal;

import android.content.ComponentName;

final class zzfa implements Runnable {
    private final /* synthetic */ ComponentName val$name;
    private final /* synthetic */ zzey zzqz;

    zzfa(zzey zzey, ComponentName componentName) {
        this.zzqz = zzey;
        this.val$name = componentName;
    }

    public final void run() {
        this.zzqz.zzqq.onServiceDisconnected(this.val$name);
    }
}
