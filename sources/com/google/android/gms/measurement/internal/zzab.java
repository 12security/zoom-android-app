package com.google.android.gms.measurement.internal;

import android.os.Handler;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.internal.measurement.zzk;

abstract class zzab {
    private static volatile Handler handler;
    private final zzcv zzer;
    private final Runnable zzes;
    /* access modifiers changed from: private */
    public volatile long zzet;

    zzab(zzcv zzcv) {
        Preconditions.checkNotNull(zzcv);
        this.zzer = zzcv;
        this.zzes = new zzac(this, zzcv);
    }

    public abstract void run();

    public final void zzv(long j) {
        cancel();
        if (j >= 0) {
            this.zzet = this.zzer.zzz().currentTimeMillis();
            if (!getHandler().postDelayed(this.zzes, j)) {
                this.zzer.zzad().zzda().zza("Failed to schedule delayed post. time", Long.valueOf(j));
            }
        }
    }

    public final boolean zzcn() {
        return this.zzet != 0;
    }

    /* access modifiers changed from: 0000 */
    public final void cancel() {
        this.zzet = 0;
        getHandler().removeCallbacks(this.zzes);
    }

    private final Handler getHandler() {
        Handler handler2;
        if (handler != null) {
            return handler;
        }
        synchronized (zzab.class) {
            if (handler == null) {
                handler = new zzk(this.zzer.getContext().getMainLooper());
            }
            handler2 = handler;
        }
        return handler2;
    }
}
