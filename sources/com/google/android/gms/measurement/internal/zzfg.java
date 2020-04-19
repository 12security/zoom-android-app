package com.google.android.gms.measurement.internal;

import android.app.job.JobParameters;

final /* synthetic */ class zzfg implements Runnable {
    private final zzfe zzrc;
    private final zzau zzrg;
    private final JobParameters zzrh;

    zzfg(zzfe zzfe, zzau zzau, JobParameters jobParameters) {
        this.zzrc = zzfe;
        this.zzrg = zzau;
        this.zzrh = jobParameters;
    }

    public final void run() {
        this.zzrc.zza(this.zzrg, this.zzrh);
    }
}
