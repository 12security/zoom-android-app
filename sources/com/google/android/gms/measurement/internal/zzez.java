package com.google.android.gms.measurement.internal;

final class zzez implements Runnable {
    private final /* synthetic */ zzam zzqy;
    private final /* synthetic */ zzey zzqz;

    zzez(zzey zzey, zzam zzam) {
        this.zzqz = zzey;
        this.zzqy = zzam;
    }

    public final void run() {
        synchronized (this.zzqz) {
            this.zzqz.zzqw = false;
            if (!this.zzqz.zzqq.isConnected()) {
                this.zzqz.zzqq.zzad().zzdi().zzaq("Connected to service");
                this.zzqz.zzqq.zza(this.zzqy);
            }
        }
    }
}
