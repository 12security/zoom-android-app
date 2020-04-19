package com.google.android.gms.measurement.internal;

import android.os.RemoteException;
import android.text.TextUtils;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;

final class zzes implements Runnable {
    private final /* synthetic */ zzaj zzdj;
    private final /* synthetic */ String zzdk;
    private final /* synthetic */ zzm zzos;
    private final /* synthetic */ zzeg zzqq;
    private final /* synthetic */ boolean zzqr;
    private final /* synthetic */ boolean zzqt;

    zzes(zzeg zzeg, boolean z, boolean z2, zzaj zzaj, zzm zzm, String str) {
        this.zzqq = zzeg;
        this.zzqt = z;
        this.zzqr = z2;
        this.zzdj = zzaj;
        this.zzos = zzm;
        this.zzdk = str;
    }

    public final void run() {
        zzam zzd = this.zzqq.zzqk;
        if (zzd == null) {
            this.zzqq.zzad().zzda().zzaq("Discarding data. Failed to send event to service");
            return;
        }
        if (this.zzqt) {
            this.zzqq.zza(zzd, (AbstractSafeParcelable) this.zzqr ? null : this.zzdj, this.zzos);
        } else {
            try {
                if (TextUtils.isEmpty(this.zzdk)) {
                    zzd.zza(this.zzdj, this.zzos);
                } else {
                    zzd.zza(this.zzdj, this.zzdk, this.zzqq.zzad().zzdk());
                }
            } catch (RemoteException e) {
                this.zzqq.zzad().zzda().zza("Failed to send event to the service", e);
            }
        }
        this.zzqq.zzfg();
    }
}
