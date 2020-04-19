package com.google.android.gms.measurement.internal;

import android.os.RemoteException;
import android.text.TextUtils;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicReference;

final class zzeu implements Runnable {
    private final /* synthetic */ String zzao;
    private final /* synthetic */ String zzav;
    private final /* synthetic */ String zzdk;
    private final /* synthetic */ zzm zzos;
    private final /* synthetic */ zzeg zzqq;
    private final /* synthetic */ AtomicReference zzqs;

    zzeu(zzeg zzeg, AtomicReference atomicReference, String str, String str2, String str3, zzm zzm) {
        this.zzqq = zzeg;
        this.zzqs = atomicReference;
        this.zzdk = str;
        this.zzao = str2;
        this.zzav = str3;
        this.zzos = zzm;
    }

    public final void run() {
        synchronized (this.zzqs) {
            try {
                zzam zzd = this.zzqq.zzqk;
                if (zzd == null) {
                    this.zzqq.zzad().zzda().zza("Failed to get conditional properties", zzau.zzao(this.zzdk), this.zzao, this.zzav);
                    this.zzqs.set(Collections.emptyList());
                    this.zzqs.notify();
                    return;
                }
                if (TextUtils.isEmpty(this.zzdk)) {
                    this.zzqs.set(zzd.zza(this.zzao, this.zzav, this.zzos));
                } else {
                    this.zzqs.set(zzd.zzd(this.zzdk, this.zzao, this.zzav));
                }
                this.zzqq.zzfg();
                this.zzqs.notify();
            } catch (RemoteException e) {
                try {
                    this.zzqq.zzad().zzda().zza("Failed to get conditional properties", zzau.zzao(this.zzdk), this.zzao, e);
                    this.zzqs.set(Collections.emptyList());
                    this.zzqs.notify();
                } catch (Throwable th) {
                    this.zzqs.notify();
                    throw th;
                }
            }
        }
    }
}
