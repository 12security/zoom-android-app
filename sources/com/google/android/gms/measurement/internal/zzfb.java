package com.google.android.gms.measurement.internal;

final class zzfb implements Runnable {
    private final /* synthetic */ zzey zzqz;
    private final /* synthetic */ zzam zzra;

    zzfb(zzey zzey, zzam zzam) {
        this.zzqz = zzey;
        this.zzra = zzam;
    }

    public final void run() {
        synchronized (this.zzqz) {
            this.zzqz.zzqw = false;
            if (!this.zzqz.zzqq.isConnected()) {
                this.zzqz.zzqq.zzad().zzdh().zzaq("Connected to remote service");
                this.zzqz.zzqq.zza(this.zzra);
            }
        }
    }
}
