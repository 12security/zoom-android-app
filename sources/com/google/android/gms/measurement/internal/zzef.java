package com.google.android.gms.measurement.internal;

final class zzef implements Runnable {
    private final /* synthetic */ zzed zzqh;
    private final /* synthetic */ zzec zzqi;

    zzef(zzed zzed, zzec zzec) {
        this.zzqh = zzed;
        this.zzqi = zzec;
    }

    public final void run() {
        this.zzqh.zza(this.zzqi, false);
        zzed zzed = this.zzqh;
        zzed.zzpy = null;
        zzed.zzu().zza((zzec) null);
    }
}
