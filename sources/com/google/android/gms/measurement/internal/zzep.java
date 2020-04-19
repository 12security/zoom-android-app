package com.google.android.gms.measurement.internal;

import android.os.RemoteException;
import com.google.android.gms.internal.measurement.zzq;

final class zzep implements Runnable {
    private final /* synthetic */ zzq zzdh;
    private final /* synthetic */ zzaj zzdj;
    private final /* synthetic */ String zzdk;
    private final /* synthetic */ zzeg zzqq;

    zzep(zzeg zzeg, zzaj zzaj, String str, zzq zzq) {
        this.zzqq = zzeg;
        this.zzdj = zzaj;
        this.zzdk = str;
        this.zzdh = zzq;
    }

    public final void run() {
        byte[] bArr = null;
        try {
            zzam zzd = this.zzqq.zzqk;
            if (zzd == null) {
                this.zzqq.zzad().zzda().zzaq("Discarding data. Failed to send event to service to bundle");
                return;
            }
            bArr = zzd.zza(this.zzdj, this.zzdk);
            this.zzqq.zzfg();
            this.zzqq.zzab().zza(this.zzdh, bArr);
        } catch (RemoteException e) {
            this.zzqq.zzad().zzda().zza("Failed to send event to the service to bundle", e);
        } finally {
            this.zzqq.zzab().zza(this.zzdh, bArr);
        }
    }
}
