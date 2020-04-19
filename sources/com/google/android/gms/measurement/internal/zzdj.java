package com.google.android.gms.measurement.internal;

import java.util.concurrent.atomic.AtomicReference;

final class zzdj implements Runnable {
    private final /* synthetic */ zzdd zzpm;
    private final /* synthetic */ long zzps;

    zzdj(zzdd zzdd, long j) {
        this.zzpm = zzdd;
        this.zzps = j;
    }

    public final void run() {
        zzdd zzdd = this.zzpm;
        long j = this.zzps;
        zzdd.zzq();
        zzdd.zzo();
        zzdd.zzah();
        zzdd.zzad().zzdh().zzaq("Resetting analytics data (FE)");
        zzdd.zzx().zzfo();
        if (zzdd.zzaf().zzu(zzdd.zzt().zzan())) {
            zzdd.zzae().zzlg.set(j);
        }
        boolean isEnabled = zzdd.zzl.isEnabled();
        if (!zzdd.zzaf().zzbq()) {
            zzdd.zzae().zzf(!isEnabled);
        }
        zzdd.zzu().resetAnalyticsData();
        zzdd.zzpk = !isEnabled;
        this.zzpm.zzu().zza(new AtomicReference<>());
    }
}
