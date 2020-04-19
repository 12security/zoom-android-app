package com.google.android.gms.measurement.internal;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.Size;
import androidx.annotation.WorkerThread;
import androidx.collection.ArrayMap;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.Clock;
import com.google.android.gms.common.util.VisibleForTesting;
import java.util.Map;

public final class zzed extends zzf {
    @VisibleForTesting
    protected zzec zzpy;
    private volatile zzec zzpz;
    private zzec zzqa;
    private final Map<Activity, zzec> zzqb = new ArrayMap();
    private zzec zzqc;
    private String zzqd;

    public zzed(zzby zzby) {
        super(zzby);
    }

    /* access modifiers changed from: protected */
    public final boolean zzak() {
        return false;
    }

    @WorkerThread
    public final zzec zzfc() {
        zzah();
        zzq();
        return this.zzpy;
    }

    public final void setCurrentScreen(@NonNull Activity activity, @Size(max = 36, min = 1) @Nullable String str, @Size(max = 36, min = 1) @Nullable String str2) {
        if (this.zzpz == null) {
            zzad().zzdd().zzaq("setCurrentScreen cannot be called while no activity active");
        } else if (this.zzqb.get(activity) == null) {
            zzad().zzdd().zzaq("setCurrentScreen must be called with an activity in the activity lifecycle");
        } else {
            if (str2 == null) {
                str2 = zzbj(activity.getClass().getCanonicalName());
            }
            boolean equals = this.zzpz.zzpv.equals(str2);
            boolean zzs = zzgd.zzs(this.zzpz.zzpu, str);
            if (equals && zzs) {
                zzad().zzdf().zzaq("setCurrentScreen cannot be called with the same class and name");
            } else if (str != null && (str.length() <= 0 || str.length() > 100)) {
                zzad().zzdd().zza("Invalid screen name length in setCurrentScreen. Length", Integer.valueOf(str.length()));
            } else if (str2 == null || (str2.length() > 0 && str2.length() <= 100)) {
                zzad().zzdi().zza("Setting current screen to name, class", str == null ? "null" : str, str2);
                zzec zzec = new zzec(str, str2, zzab().zzgk());
                this.zzqb.put(activity, zzec);
                zza(activity, zzec, true);
            } else {
                zzad().zzdd().zza("Invalid class name length in setCurrentScreen. Length", Integer.valueOf(str2.length()));
            }
        }
    }

    public final zzec zzfd() {
        zzo();
        return this.zzpz;
    }

    @MainThread
    private final void zza(Activity activity, zzec zzec, boolean z) {
        zzec zzec2 = this.zzpz == null ? this.zzqa : this.zzpz;
        if (zzec.zzpv == null) {
            zzec = new zzec(zzec.zzpu, zzbj(activity.getClass().getCanonicalName()), zzec.zzpw);
        }
        this.zzqa = this.zzpz;
        this.zzpz = zzec;
        zzac().zza((Runnable) new zzee(this, z, zzec2, zzec));
    }

    /* access modifiers changed from: private */
    @WorkerThread
    public final void zza(@NonNull zzec zzec, boolean z) {
        zzr().zzc(zzz().elapsedRealtime());
        if (zzx().zza(zzec.zzpx, z)) {
            zzec.zzpx = false;
        }
    }

    public static void zza(zzec zzec, Bundle bundle, boolean z) {
        if (bundle == null || zzec == null || (bundle.containsKey("_sc") && !z)) {
            if (bundle != null && zzec == null && z) {
                bundle.remove("_sn");
                bundle.remove("_sc");
                bundle.remove("_si");
            }
            return;
        }
        if (zzec.zzpu != null) {
            bundle.putString("_sn", zzec.zzpu);
        } else {
            bundle.remove("_sn");
        }
        bundle.putString("_sc", zzec.zzpv);
        bundle.putLong("_si", zzec.zzpw);
    }

    @WorkerThread
    public final void zza(String str, zzec zzec) {
        zzq();
        synchronized (this) {
            if (this.zzqd == null || this.zzqd.equals(str) || zzec != null) {
                this.zzqd = str;
                this.zzqc = zzec;
            }
        }
    }

    @VisibleForTesting
    private static String zzbj(String str) {
        String[] split = str.split("\\.");
        String str2 = split.length > 0 ? split[split.length - 1] : "";
        return str2.length() > 100 ? str2.substring(0, 100) : str2;
    }

    @MainThread
    private final zzec zza(@NonNull Activity activity) {
        Preconditions.checkNotNull(activity);
        zzec zzec = (zzec) this.zzqb.get(activity);
        if (zzec != null) {
            return zzec;
        }
        zzec zzec2 = new zzec(null, zzbj(activity.getClass().getCanonicalName()), zzab().zzgk());
        this.zzqb.put(activity, zzec2);
        return zzec2;
    }

    @MainThread
    public final void onActivityCreated(Activity activity, Bundle bundle) {
        if (bundle != null) {
            Bundle bundle2 = bundle.getBundle("com.google.app_measurement.screen_service");
            if (bundle2 != null) {
                this.zzqb.put(activity, new zzec(bundle2.getString("name"), bundle2.getString("referrer_name"), bundle2.getLong("id")));
            }
        }
    }

    @MainThread
    public final void onActivityResumed(Activity activity) {
        zza(activity, zza(activity), false);
        zza zzr = zzr();
        zzr.zzac().zza((Runnable) new zzd(zzr, zzr.zzz().elapsedRealtime()));
    }

    @MainThread
    public final void onActivityPaused(Activity activity) {
        zzec zza = zza(activity);
        this.zzqa = this.zzpz;
        this.zzpz = null;
        zzac().zza((Runnable) new zzef(this, zza));
    }

    @MainThread
    public final void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        if (bundle != null) {
            zzec zzec = (zzec) this.zzqb.get(activity);
            if (zzec != null) {
                Bundle bundle2 = new Bundle();
                bundle2.putLong("id", zzec.zzpw);
                bundle2.putString("name", zzec.zzpu);
                bundle2.putString("referrer_name", zzec.zzpv);
                bundle.putBundle("com.google.app_measurement.screen_service", bundle2);
            }
        }
    }

    @MainThread
    public final void onActivityDestroyed(Activity activity) {
        this.zzqb.remove(activity);
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
