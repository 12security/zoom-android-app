package com.google.android.gms.measurement.internal;

final class zzac implements Runnable {
    private final /* synthetic */ zzcv zzeu;
    private final /* synthetic */ zzab zzev;

    zzac(zzab zzab, zzcv zzcv) {
        this.zzev = zzab;
        this.zzeu = zzcv;
    }

    public final void run() {
        this.zzeu.zzag();
        if (zzq.isMainThread()) {
            this.zzeu.zzac().zza((Runnable) this);
            return;
        }
        boolean zzcn = this.zzev.zzcn();
        this.zzev.zzet = 0;
        if (zzcn) {
            this.zzev.run();
        }
    }
}
