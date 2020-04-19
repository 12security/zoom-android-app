package com.google.android.gms.measurement.internal;

import android.os.RemoteException;

final class zzek implements Runnable {
    private final /* synthetic */ zzm zzos;
    private final /* synthetic */ zzeg zzqq;

    zzek(zzeg zzeg, zzm zzm) {
        this.zzqq = zzeg;
        this.zzos = zzm;
    }

    public final void run() {
        zzam zzd = this.zzqq.zzqk;
        if (zzd == null) {
            this.zzqq.zzad().zzda().zzaq("Failed to reset data on the service; null service");
            return;
        }
        try {
            zzd.zzd(this.zzos);
        } catch (RemoteException e) {
            this.zzqq.zzad().zzda().zza("Failed to reset data on the service", e);
        }
        this.zzqq.zzfg();
    }
}
