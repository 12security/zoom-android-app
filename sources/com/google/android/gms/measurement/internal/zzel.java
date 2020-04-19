package com.google.android.gms.measurement.internal;

import android.os.RemoteException;
import java.util.concurrent.atomic.AtomicReference;

final class zzel implements Runnable {
    private final /* synthetic */ zzm zzos;
    private final /* synthetic */ zzeg zzqq;
    private final /* synthetic */ AtomicReference zzqs;

    zzel(zzeg zzeg, AtomicReference atomicReference, zzm zzm) {
        this.zzqq = zzeg;
        this.zzqs = atomicReference;
        this.zzos = zzm;
    }

    public final void run() {
        synchronized (this.zzqs) {
            try {
                zzam zzd = this.zzqq.zzqk;
                if (zzd == null) {
                    this.zzqq.zzad().zzda().zzaq("Failed to get app instance id");
                    this.zzqs.notify();
                    return;
                }
                this.zzqs.set(zzd.zzc(this.zzos));
                String str = (String) this.zzqs.get();
                if (str != null) {
                    this.zzqq.zzs().zzbi(str);
                    this.zzqq.zzae().zzli.zzav(str);
                }
                this.zzqq.zzfg();
                this.zzqs.notify();
            } catch (RemoteException e) {
                try {
                    this.zzqq.zzad().zzda().zza("Failed to get app instance id", e);
                    this.zzqs.notify();
                } catch (Throwable th) {
                    this.zzqs.notify();
                    throw th;
                }
            }
        }
    }
}
