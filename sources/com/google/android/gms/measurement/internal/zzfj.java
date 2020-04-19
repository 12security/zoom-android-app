package com.google.android.gms.measurement.internal;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.WorkerThread;
import com.google.android.gms.common.util.Clock;
import com.google.android.gms.common.util.VisibleForTesting;
import com.google.android.gms.internal.measurement.zzk;

public final class zzfj extends zzf {
    private Handler handler;
    @VisibleForTesting
    private long zzrk = zzz().elapsedRealtime();
    @VisibleForTesting
    private long zzrl = this.zzrk;
    private final zzab zzrm = new zzfk(this, this.zzl);
    private final zzab zzrn = new zzfl(this, this.zzl);

    zzfj(zzby zzby) {
        super(zzby);
    }

    /* access modifiers changed from: protected */
    public final boolean zzak() {
        return false;
    }

    private final void zzfn() {
        synchronized (this) {
            if (this.handler == null) {
                this.handler = new zzk(Looper.getMainLooper());
            }
        }
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    public final void zzfo() {
        zzq();
        this.zzrm.cancel();
        this.zzrn.cancel();
        this.zzrk = 0;
        this.zzrl = this.zzrk;
    }

    /* access modifiers changed from: private */
    @WorkerThread
    public final void zzaa(long j) {
        zzq();
        zzfn();
        if (zzaf().zze(zzt().zzan(), zzal.zzih)) {
            zzae().zzlv.set(false);
        }
        zzad().zzdi().zza("Activity resumed, time", Long.valueOf(j));
        this.zzrk = j;
        this.zzrl = this.zzrk;
        if (zzaf().zzaa(zzt().zzan())) {
            zzab(zzz().currentTimeMillis());
            return;
        }
        this.zzrm.cancel();
        this.zzrn.cancel();
        if (zzae().zzx(zzz().currentTimeMillis())) {
            zzae().zzlo.set(true);
            zzae().zzlt.set(0);
        }
        if (zzae().zzlo.get()) {
            this.zzrm.zzv(Math.max(0, zzae().zzlm.get() - zzae().zzlt.get()));
        } else {
            this.zzrn.zzv(Math.max(0, 3600000 - zzae().zzlt.get()));
        }
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    public final void zzab(long j) {
        zzq();
        zzfn();
        zza(j, false);
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    public final void zza(long j, boolean z) {
        zzq();
        zzfn();
        this.zzrm.cancel();
        this.zzrn.cancel();
        if (zzae().zzx(j)) {
            zzae().zzlo.set(true);
            zzae().zzlt.set(0);
        }
        if (z && zzaf().zzab(zzt().zzan())) {
            zzae().zzls.set(j);
        }
        if (zzae().zzlo.get()) {
            zzad(j);
        } else {
            this.zzrn.zzv(Math.max(0, 3600000 - zzae().zzlt.get()));
        }
    }

    /* access modifiers changed from: private */
    @WorkerThread
    public final void zzac(long j) {
        zzq();
        zzfn();
        if (zzaf().zze(zzt().zzan(), zzal.zzih)) {
            zzae().zzlv.set(true);
        }
        this.zzrm.cancel();
        this.zzrn.cancel();
        zzad().zzdi().zza("Activity paused, time", Long.valueOf(j));
        if (this.zzrk != 0) {
            zzae().zzlt.set(zzae().zzlt.get() + (j - this.zzrk));
        }
    }

    @WorkerThread
    private final void zzad(long j) {
        zzq();
        zzad().zzdi().zza("Session started, time", Long.valueOf(zzz().elapsedRealtime()));
        Long valueOf = zzaf().zzy(zzt().zzan()) ? Long.valueOf(j / 1000) : null;
        zzs().zza("auto", "_sid", (Object) valueOf, j);
        zzae().zzlo.set(false);
        Bundle bundle = new Bundle();
        if (zzaf().zzy(zzt().zzan())) {
            bundle.putLong("_sid", valueOf.longValue());
        }
        zzs().zza("auto", "_s", j, bundle);
        zzae().zzls.set(j);
    }

    /* access modifiers changed from: protected */
    @WorkerThread
    @VisibleForTesting
    public final void zzfp() {
        zzq();
        zzad(zzz().currentTimeMillis());
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    @VisibleForTesting
    public final long zzfq() {
        long elapsedRealtime = zzz().elapsedRealtime();
        long j = elapsedRealtime - this.zzrl;
        this.zzrl = elapsedRealtime;
        return j;
    }

    @WorkerThread
    public final boolean zza(boolean z, boolean z2) {
        zzq();
        zzah();
        long elapsedRealtime = zzz().elapsedRealtime();
        zzae().zzls.set(zzz().currentTimeMillis());
        long j = elapsedRealtime - this.zzrk;
        if (z || j >= 1000) {
            zzae().zzlt.set(j);
            zzad().zzdi().zza("Recording user engagement, ms", Long.valueOf(j));
            Bundle bundle = new Bundle();
            bundle.putLong("_et", j);
            zzed.zza(zzv().zzfc(), bundle, true);
            if (zzaf().zzac(zzt().zzan())) {
                if (zzaf().zze(zzt().zzan(), zzal.zzim)) {
                    if (!z2) {
                        zzfq();
                    }
                } else if (z2) {
                    bundle.putLong("_fr", 1);
                } else {
                    zzfq();
                }
            }
            if (!zzaf().zze(zzt().zzan(), zzal.zzim) || !z2) {
                zzs().logEvent("auto", "_e", bundle);
            }
            this.zzrk = elapsedRealtime;
            this.zzrn.cancel();
            this.zzrn.zzv(Math.max(0, 3600000 - zzae().zzlt.get()));
            return true;
        }
        zzad().zzdi().zza("Screen exposed for less than 1000 ms. Event not sent. time", Long.valueOf(j));
        return false;
    }

    /* access modifiers changed from: private */
    @WorkerThread
    public final void zzfr() {
        zzq();
        zza(false, false);
        zzr().zzc(zzz().elapsedRealtime());
    }

    public final /* bridge */ /* synthetic */ void zzn() {
        super.zzn();
    }

    public final /* bridge */ /* synthetic */ void zzo() {
        super.zzo();
    }

    public final /* bridge */ /* synthetic */ void zzp() {
        super.zzp();
    }

    public final /* bridge */ /* synthetic */ void zzq() {
        super.zzq();
    }

    public final /* bridge */ /* synthetic */ zza zzr() {
        return super.zzr();
    }

    public final /* bridge */ /* synthetic */ zzdd zzs() {
        return super.zzs();
    }

    public final /* bridge */ /* synthetic */ zzap zzt() {
        return super.zzt();
    }

    public final /* bridge */ /* synthetic */ zzeg zzu() {
        return super.zzu();
    }

    public final /* bridge */ /* synthetic */ zzed zzv() {
        return super.zzv();
    }

    public final /* bridge */ /* synthetic */ zzaq zzw() {
        return super.zzw();
    }

    public final /* bridge */ /* synthetic */ zzfj zzx() {
        return super.zzx();
    }

    public final /* bridge */ /* synthetic */ zzad zzy() {
        return super.zzy();
    }

    public final /* bridge */ /* synthetic */ Clock zzz() {
        return super.zzz();
    }

    public final /* bridge */ /* synthetic */ Context getContext() {
        return super.getContext();
    }

    public final /* bridge */ /* synthetic */ zzas zzaa() {
        return super.zzaa();
    }

    public final /* bridge */ /* synthetic */ zzgd zzab() {
        return super.zzab();
    }

    public final /* bridge */ /* synthetic */ zzbt zzac() {
        return super.zzac();
    }

    public final /* bridge */ /* synthetic */ zzau zzad() {
        return super.zzad();
    }

    public final /* bridge */ /* synthetic */ zzbf zzae() {
        return super.zzae();
    }

    public final /* bridge */ /* synthetic */ zzt zzaf() {
        return super.zzaf();
    }

    public final /* bridge */ /* synthetic */ zzq zzag() {
        return super.zzag();
    }
}
