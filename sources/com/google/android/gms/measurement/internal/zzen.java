package com.google.android.gms.measurement.internal;

import android.os.RemoteException;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;

final class zzen implements Runnable {
    private final /* synthetic */ zzm zzos;
    private final /* synthetic */ zzeg zzqq;

    zzen(zzeg zzeg, zzm zzm) {
        this.zzqq = zzeg;
        this.zzos = zzm;
    }

    public final void run() {
        zzam zzd = this.zzqq.zzqk;
        if (zzd == null) {
            this.zzqq.zzad().zzda().zzaq("Discarding data. Failed to send app launch");
            return;
        }
        try {
            zzd.zza(this.zzos);
            this.zzqq.zza(zzd, (AbstractSafeParcelable) null, this.zzos);
            this.zzqq.zzfg();
        } catch (RemoteException e) {
            this.zzqq.zzad().zzda().zza("Failed to send app launch to the service", e);
        }
    }
}
