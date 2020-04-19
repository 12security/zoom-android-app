package com.google.android.gms.measurement.internal;

final class zzd implements Runnable {
    private final /* synthetic */ zza zzcc;
    private final /* synthetic */ long zzcd;

    zzd(zza zza, long j) {
        this.zzcc = zza;
        this.zzcd = j;
    }

    public final void run() {
        this.zzcc.zzd(this.zzcd);
    }
}
