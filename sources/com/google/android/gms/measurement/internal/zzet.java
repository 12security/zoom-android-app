package com.google.android.gms.measurement.internal;

import android.os.RemoteException;
import android.text.TextUtils;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;

final class zzet implements Runnable {
    private final /* synthetic */ zzm zzos;
    private final /* synthetic */ zzeg zzqq;
    private final /* synthetic */ boolean zzqr;
    private final /* synthetic */ boolean zzqt;
    private final /* synthetic */ zzr zzqu;
    private final /* synthetic */ zzr zzqv;

    zzet(zzeg zzeg, boolean z, boolean z2, zzr zzr, zzm zzm, zzr zzr2) {
        this.zzqq = zzeg;
        this.zzqt = z;
        this.zzqr = z2;
        this.zzqu = zzr;
        this.zzos = zzm;
        this.zzqv = zzr2;
    }

    public final void run() {
        zzam zzd = this.zzqq.zzqk;
        if (zzd == null) {
            this.zzqq.zzad().zzda().zzaq("Discarding data. Failed to send conditional user property to service");
            return;
        }
        if (this.zzqt) {
            this.zzqq.zza(zzd, (AbstractSafeParcelable) this.zzqr ? null : this.zzqu, this.zzos);
        } else {
            try {
                if (TextUtils.isEmpty(this.zzqv.packageName)) {
                    zzd.zza(this.zzqu, this.zzos);
                } else {
                    zzd.zzb(this.zzqu);
                }
            } catch (RemoteException e) {
                this.zzqq.zzad().zzda().zza("Failed to send conditional user property to the service", e);
            }
        }
        this.zzqq.zzfg();
    }
}
