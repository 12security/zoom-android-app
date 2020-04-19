package com.google.android.gms.measurement.internal;

import android.os.RemoteException;
import com.google.android.gms.internal.measurement.zzq;
import java.util.ArrayList;

final class zzev implements Runnable {
    private final /* synthetic */ String zzao;
    private final /* synthetic */ String zzav;
    private final /* synthetic */ zzq zzdh;
    private final /* synthetic */ zzm zzos;
    private final /* synthetic */ zzeg zzqq;

    zzev(zzeg zzeg, String str, String str2, zzm zzm, zzq zzq) {
        this.zzqq = zzeg;
        this.zzao = str;
        this.zzav = str2;
        this.zzos = zzm;
        this.zzdh = zzq;
    }

    public final void run() {
        ArrayList arrayList = new ArrayList();
        try {
            zzam zzd = this.zzqq.zzqk;
            if (zzd == null) {
                this.zzqq.zzad().zzda().zza("Failed to get conditional properties", this.zzao, this.zzav);
                return;
            }
            arrayList = zzgd.zzc(zzd.zza(this.zzao, this.zzav, this.zzos));
            this.zzqq.zzfg();
            this.zzqq.zzab().zza(this.zzdh, arrayList);
        } catch (RemoteException e) {
            this.zzqq.zzad().zzda().zza("Failed to get conditional properties", this.zzao, this.zzav, e);
        } finally {
            this.zzqq.zzab().zza(this.zzdh, arrayList);
        }
    }
}
