package com.google.android.gms.measurement.internal;

import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;

final class zzei implements Runnable {
    private final /* synthetic */ zzm zzos;
    private final /* synthetic */ zzga zzov;
    private final /* synthetic */ zzeg zzqq;
    private final /* synthetic */ boolean zzqr;

    zzei(zzeg zzeg, boolean z, zzga zzga, zzm zzm) {
        this.zzqq = zzeg;
        this.zzqr = z;
        this.zzov = zzga;
        this.zzos = zzm;
    }

    public final void run() {
        zzam zzd = this.zzqq.zzqk;
        if (zzd == null) {
            this.zzqq.zzad().zzda().zzaq("Discarding data. Failed to set user attribute");
            return;
        }
        this.zzqq.zza(zzd, (AbstractSafeParcelable) this.zzqr ? null : this.zzov, this.zzos);
        this.zzqq.zzfg();
    }
}
