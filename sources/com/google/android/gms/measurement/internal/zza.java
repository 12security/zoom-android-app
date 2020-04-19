package com.google.android.gms.measurement.internal;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.WorkerThread;
import androidx.collection.ArrayMap;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.Clock;
import java.util.Map;

public final class zza extends zze {
    private final Map<String, Long> zzby = new ArrayMap();
    private final Map<String, Integer> zzbz = new ArrayMap();
    private long zzca;

    public zza(zzby zzby2) {
        super(zzby2);
    }

    public final void beginAdUnitExposure(String str, long j) {
        if (str == null || str.length() == 0) {
            zzad().zzda().zzaq("Ad unit id must be a non-empty string");
        } else {
            zzac().zza((Runnable) new zzb(this, str, j));
        }
    }

    /* access modifiers changed from: private */
    @WorkerThread
    public final void zza(String str, long j) {
        zzo();
        zzq();
        Preconditions.checkNotEmpty(str);
        if (this.zzbz.isEmpty()) {
            this.zzca = j;
        }
        Integer num = (Integer) this.zzbz.get(str);
        if (num != null) {
            this.zzbz.put(str, Integer.valueOf(num.intValue() + 1));
        } else if (this.zzbz.size() >= 100) {
            zzad().zzdd().zzaq("Too many ads visible");
        } else {
            this.zzbz.put(str, Integer.valueOf(1));
            this.zzby.put(str, Long.valueOf(j));
        }
    }

    public final void endAdUnitExposure(String str, long j) {
        if (str == null || str.length() == 0) {
            zzad().zzda().zzaq("Ad unit id must be a non-empty string");
        } else {
            zzac().zza((Runnable) new zzc(this, str, j));
        }
    }

    /* access modifiers changed from: private */
    @WorkerThread
    public final void zzb(String str, long j) {
        zzo();
        zzq();
        Preconditions.checkNotEmpty(str);
        Integer num = (Integer) this.zzbz.get(str);
        if (num != null) {
            zzec zzfc = zzv().zzfc();
            int intValue = num.intValue() - 1;
            if (intValue == 0) {
                this.zzbz.remove(str);
                Long l = (Long) this.zzby.get(str);
                if (l == null) {
                    zzad().zzda().zzaq("First ad unit exposure time was never set");
                } else {
                    long longValue = j - l.longValue();
                    this.zzby.remove(str);
                    zza(str, longValue, zzfc);
                }
                if (this.zzbz.isEmpty()) {
                    long j2 = this.zzca;
                    if (j2 == 0) {
                        zzad().zzda().zzaq("First ad exposure time was never set");
                        return;
                    } else {
                        zza(j - j2, zzfc);
                        this.zzca = 0;
                    }
                }
                return;
            }
            this.zzbz.put(str, Integer.valueOf(intValue));
            return;
        }
        zzad().zzda().zza("Call to endAdUnitExposure for unknown ad unit id", str);
    }

    @WorkerThread
    private final void zza(long j, zzec zzec) {
        if (zzec == null) {
            zzad().zzdi().zzaq("Not logging ad exposure. No active activity");
        } else if (j < 1000) {
            zzad().zzdi().zza("Not logging ad exposure. Less than 1000 ms. exposure", Long.valueOf(j));
        } else {
            Bundle bundle = new Bundle();
            bundle.putLong("_xt", j);
            zzed.zza(zzec, bundle, true);
            zzs().logEvent("am", "_xa", bundle);
        }
    }

    @WorkerThread
    private final void zza(String str, long j, zzec zzec) {
        if (zzec == null) {
            zzad().zzdi().zzaq("Not logging ad unit exposure. No active activity");
        } else if (j < 1000) {
            zzad().zzdi().zza("Not logging ad unit exposure. Less than 1000 ms. exposure", Long.valueOf(j));
        } else {
            Bundle bundle = new Bundle();
            bundle.putString("_ai", str);
            bundle.putLong("_xt", j);
            zzed.zza(zzec, bundle, true);
            zzs().logEvent("am", "_xu", bundle);
        }
    }

    @WorkerThread
    public final void zzc(long j) {
        zzec zzfc = zzv().zzfc();
        for (String str : this.zzby.keySet()) {
            zza(str, j - ((Long) this.zzby.get(str)).longValue(), zzfc);
        }
        if (!this.zzby.isEmpty()) {
            zza(j - this.zzca, zzfc);
        }
        zzd(j);
    }

    /* access modifiers changed from: private */
    @WorkerThread
    public final void zzd(long j) {
        for (String put : this.zzby.keySet()) {
            this.zzby.put(put, Long.valueOf(j));
        }
        if (!this.zzby.isEmpty()) {
            this.zzca = j;
        }
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
