package com.google.android.gms.measurement.internal;

import android.os.RemoteException;

final class zzeo implements Runnable {
    private final /* synthetic */ zzec zzqi;
    private final /* synthetic */ zzeg zzqq;

    zzeo(zzeg zzeg, zzec zzec) {
        this.zzqq = zzeg;
        this.zzqi = zzec;
    }

    public final void run() {
        zzam zzd = this.zzqq.zzqk;
        if (zzd == null) {
            this.zzqq.zzad().zzda().zzaq("Failed to send current screen to service");
            return;
        }
        try {
            if (this.zzqi == null) {
                zzd.zza(0, (String) null, (String) null, this.zzqq.getContext().getPackageName());
            } else {
                zzd.zza(this.zzqi.zzpw, this.zzqi.zzpu, this.zzqi.zzpv, this.zzqq.getContext().getPackageName());
            }
            this.zzqq.zzfg();
        } catch (RemoteException e) {
            this.zzqq.zzad().zzda().zza("Failed to send current screen to the service", e);
        }
    }
}
