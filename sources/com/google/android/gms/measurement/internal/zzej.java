package com.google.android.gms.measurement.internal;

import android.os.RemoteException;
import java.util.concurrent.atomic.AtomicReference;

final class zzej implements Runnable {
    private final /* synthetic */ boolean zzbd;
    private final /* synthetic */ zzm zzos;
    private final /* synthetic */ zzeg zzqq;
    private final /* synthetic */ AtomicReference zzqs;

    zzej(zzeg zzeg, AtomicReference atomicReference, zzm zzm, boolean z) {
        this.zzqq = zzeg;
        this.zzqs = atomicReference;
        this.zzos = zzm;
        this.zzbd = z;
    }

    public final void run() {
        synchronized (this.zzqs) {
            try {
                zzam zzd = this.zzqq.zzqk;
                if (zzd == null) {
                    this.zzqq.zzad().zzda().zzaq("Failed to get user properties");
                    this.zzqs.notify();
                    return;
                }
                this.zzqs.set(zzd.zza(this.zzos, this.zzbd));
                this.zzqq.zzfg();
                this.zzqs.notify();
            } catch (RemoteException e) {
                try {
                    this.zzqq.zzad().zzda().zza("Failed to get user properties", e);
                    this.zzqs.notify();
                } catch (Throwable th) {
                    this.zzqs.notify();
                    throw th;
                }
            }
        }
    }
}
