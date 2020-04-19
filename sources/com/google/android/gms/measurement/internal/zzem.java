package com.google.android.gms.measurement.internal;

import android.os.RemoteException;
import com.google.android.gms.internal.measurement.zzq;

final class zzem implements Runnable {
    private final /* synthetic */ zzq zzdh;
    private final /* synthetic */ zzm zzos;
    private final /* synthetic */ zzeg zzqq;

    zzem(zzeg zzeg, zzm zzm, zzq zzq) {
        this.zzqq = zzeg;
        this.zzos = zzm;
        this.zzdh = zzq;
    }

    public final void run() {
        try {
            zzam zzd = this.zzqq.zzqk;
            if (zzd == null) {
                this.zzqq.zzad().zzda().zzaq("Failed to get app instance id");
                return;
            }
            String zzc = zzd.zzc(this.zzos);
            if (zzc != null) {
                this.zzqq.zzs().zzbi(zzc);
                this.zzqq.zzae().zzli.zzav(zzc);
            }
            this.zzqq.zzfg();
            this.zzqq.zzab().zzb(this.zzdh, zzc);
        } catch (RemoteException e) {
            this.zzqq.zzad().zzda().zza("Failed to get app instance id", e);
        } finally {
            this.zzqq.zzab().zzb(this.zzdh, (String) null);
        }
    }
}
