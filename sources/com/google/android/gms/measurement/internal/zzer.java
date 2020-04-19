package com.google.android.gms.measurement.internal;

import android.os.RemoteException;

final class zzer implements Runnable {
    private final /* synthetic */ zzm zzos;
    private final /* synthetic */ zzeg zzqq;

    zzer(zzeg zzeg, zzm zzm) {
        this.zzqq = zzeg;
        this.zzos = zzm;
    }

    public final void run() {
        zzam zzd = this.zzqq.zzqk;
        if (zzd == null) {
            this.zzqq.zzad().zzda().zzaq("Failed to send measurementEnabled to service");
            return;
        }
        try {
            zzd.zzb(this.zzos);
            this.zzqq.zzfg();
        } catch (RemoteException e) {
            this.zzqq.zzad().zzda().zza("Failed to send measurementEnabled to the service", e);
        }
    }
}
