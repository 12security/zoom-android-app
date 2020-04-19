package com.google.android.gms.measurement.internal;

import android.os.Bundle;
import android.os.RemoteException;
import com.google.android.gms.internal.measurement.zzq;

final class zzex implements Runnable {
    private final /* synthetic */ String zzao;
    private final /* synthetic */ String zzav;
    private final /* synthetic */ boolean zzbd;
    private final /* synthetic */ zzq zzdh;
    private final /* synthetic */ zzm zzos;
    private final /* synthetic */ zzeg zzqq;

    zzex(zzeg zzeg, String str, String str2, boolean z, zzm zzm, zzq zzq) {
        this.zzqq = zzeg;
        this.zzao = str;
        this.zzav = str2;
        this.zzbd = z;
        this.zzos = zzm;
        this.zzdh = zzq;
    }

    public final void run() {
        Bundle bundle = new Bundle();
        try {
            zzam zzd = this.zzqq.zzqk;
            if (zzd == null) {
                this.zzqq.zzad().zzda().zza("Failed to get user properties", this.zzao, this.zzav);
                return;
            }
            bundle = zzgd.zzb(zzd.zza(this.zzao, this.zzav, this.zzbd, this.zzos));
            this.zzqq.zzfg();
            this.zzqq.zzab().zza(this.zzdh, bundle);
        } catch (RemoteException e) {
            this.zzqq.zzad().zzda().zza("Failed to get user properties", this.zzao, e);
        } finally {
            this.zzqq.zzab().zza(this.zzdh, bundle);
        }
    }
}
