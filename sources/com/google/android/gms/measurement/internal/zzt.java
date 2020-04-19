package com.google.android.gms.measurement.internal;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.Size;
import androidx.annotation.WorkerThread;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.Clock;
import com.google.android.gms.common.util.ProcessUtils;
import com.google.android.gms.common.util.VisibleForTesting;
import com.google.android.gms.common.wrappers.Wrappers;
import com.google.android.gms.measurement.internal.zzal.zza;
import java.lang.reflect.InvocationTargetException;

public final class zzt extends zzct {
    private Boolean zzdz;
    @NonNull
    private zzv zzea = zzu.zzec;
    private Boolean zzeb;

    zzt(zzby zzby) {
        super(zzby);
        zzal.zza(zzby);
    }

    /* access modifiers changed from: 0000 */
    public final void zza(@NonNull zzv zzv) {
        this.zzea = zzv;
    }

    static String zzbo() {
        return (String) zzal.zzgd.get(null);
    }

    @WorkerThread
    public final int zzi(@Size(min = 1) String str) {
        return zzb(str, zzal.zzgr);
    }

    public final long zzav() {
        zzag();
        return 15300;
    }

    public final boolean zzbp() {
        if (this.zzeb == null) {
            synchronized (this) {
                if (this.zzeb == null) {
                    ApplicationInfo applicationInfo = getContext().getApplicationInfo();
                    String myProcessName = ProcessUtils.getMyProcessName();
                    if (applicationInfo != null) {
                        String str = applicationInfo.processName;
                        this.zzeb = Boolean.valueOf(str != null && str.equals(myProcessName));
                    }
                    if (this.zzeb == null) {
                        this.zzeb = Boolean.TRUE;
                        zzad().zzda().zzaq("My process not in the list of running processes");
                    }
                }
            }
        }
        return this.zzeb.booleanValue();
    }

    @WorkerThread
    public final long zza(String str, @NonNull zza<Long> zza) {
        if (str == null) {
            return ((Long) zza.get(null)).longValue();
        }
        String zzb = this.zzea.zzb(str, zza.getKey());
        if (TextUtils.isEmpty(zzb)) {
            return ((Long) zza.get(null)).longValue();
        }
        try {
            return ((Long) zza.get(Long.valueOf(Long.parseLong(zzb)))).longValue();
        } catch (NumberFormatException unused) {
            return ((Long) zza.get(null)).longValue();
        }
    }

    @WorkerThread
    public final int zzb(String str, @NonNull zza<Integer> zza) {
        if (str == null) {
            return ((Integer) zza.get(null)).intValue();
        }
        String zzb = this.zzea.zzb(str, zza.getKey());
        if (TextUtils.isEmpty(zzb)) {
            return ((Integer) zza.get(null)).intValue();
        }
        try {
            return ((Integer) zza.get(Integer.valueOf(Integer.parseInt(zzb)))).intValue();
        } catch (NumberFormatException unused) {
            return ((Integer) zza.get(null)).intValue();
        }
    }

    @WorkerThread
    public final double zzc(String str, @NonNull zza<Double> zza) {
        if (str == null) {
            return ((Double) zza.get(null)).doubleValue();
        }
        String zzb = this.zzea.zzb(str, zza.getKey());
        if (TextUtils.isEmpty(zzb)) {
            return ((Double) zza.get(null)).doubleValue();
        }
        try {
            return ((Double) zza.get(Double.valueOf(Double.parseDouble(zzb)))).doubleValue();
        } catch (NumberFormatException unused) {
            return ((Double) zza.get(null)).doubleValue();
        }
    }

    @WorkerThread
    public final boolean zzd(String str, @NonNull zza<Boolean> zza) {
        if (str == null) {
            return ((Boolean) zza.get(null)).booleanValue();
        }
        String zzb = this.zzea.zzb(str, zza.getKey());
        if (TextUtils.isEmpty(zzb)) {
            return ((Boolean) zza.get(null)).booleanValue();
        }
        return ((Boolean) zza.get(Boolean.valueOf(Boolean.parseBoolean(zzb)))).booleanValue();
    }

    public final boolean zze(String str, zza<Boolean> zza) {
        return zzd(str, zza);
    }

    public final boolean zza(zza<Boolean> zza) {
        return zzd(null, zza);
    }

    /* access modifiers changed from: 0000 */
    @Nullable
    @VisibleForTesting
    public final Boolean zzj(@Size(min = 1) String str) {
        Preconditions.checkNotEmpty(str);
        try {
            if (getContext().getPackageManager() == null) {
                zzad().zzda().zzaq("Failed to load metadata: PackageManager is null");
                return null;
            }
            ApplicationInfo applicationInfo = Wrappers.packageManager(getContext()).getApplicationInfo(getContext().getPackageName(), 128);
            if (applicationInfo == null) {
                zzad().zzda().zzaq("Failed to load metadata: ApplicationInfo is null");
                return null;
            } else if (applicationInfo.metaData == null) {
                zzad().zzda().zzaq("Failed to load metadata: Metadata bundle is null");
                return null;
            } else if (!applicationInfo.metaData.containsKey(str)) {
                return null;
            } else {
                return Boolean.valueOf(applicationInfo.metaData.getBoolean(str));
            }
        } catch (NameNotFoundException e) {
            zzad().zzda().zza("Failed to load metadata: Package name not found", e);
            return null;
        }
    }

    public final boolean zzbq() {
        zzag();
        Boolean zzj = zzj("firebase_analytics_collection_deactivated");
        return zzj != null && zzj.booleanValue();
    }

    public final Boolean zzbr() {
        zzag();
        return zzj("firebase_analytics_collection_enabled");
    }

    public static long zzbs() {
        return ((Long) zzal.zzhg.get(null)).longValue();
    }

    public static long zzbt() {
        return ((Long) zzal.zzgg.get(null)).longValue();
    }

    public final String zzbu() {
        try {
            return (String) Class.forName("android.os.SystemProperties").getMethod("get", new Class[]{String.class, String.class}).invoke(null, new Object[]{"debug.firebase.analytics.app", ""});
        } catch (ClassNotFoundException e) {
            zzad().zzda().zza("Could not find SystemProperties class", e);
            return "";
        } catch (NoSuchMethodException e2) {
            zzad().zzda().zza("Could not find SystemProperties.get() method", e2);
            return "";
        } catch (IllegalAccessException e3) {
            zzad().zzda().zza("Could not access SystemProperties.get()", e3);
            return "";
        } catch (InvocationTargetException e4) {
            zzad().zzda().zza("SystemProperties.get() threw an exception", e4);
            return "";
        }
    }

    public static boolean zzbv() {
        return ((Boolean) zzal.zzgc.get(null)).booleanValue();
    }

    public final boolean zzk(String str) {
        return "1".equals(this.zzea.zzb(str, "gaia_collection_enabled"));
    }

    public final boolean zzl(String str) {
        return "1".equals(this.zzea.zzb(str, "measurement.event_sampling_enabled"));
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    public final boolean zzm(String str) {
        return zzd(str, zzal.zzhq);
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    public final boolean zzn(String str) {
        return zzd(str, zzal.zzhs);
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    public final boolean zzo(String str) {
        return zzd(str, zzal.zzht);
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    public final boolean zzp(String str) {
        return zzd(str, zzal.zzhk);
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    public final String zzq(String str) {
        zza<String> zza = zzal.zzhl;
        if (str == null) {
            return (String) zza.get(null);
        }
        return (String) zza.get(this.zzea.zzb(str, zza.getKey()));
    }

    /* access modifiers changed from: 0000 */
    public final boolean zzr(String str) {
        return zzd(str, zzal.zzhu);
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    public final boolean zzs(String str) {
        return zzd(str, zzal.zzhv);
    }

    /* access modifiers changed from: 0000 */
    public final boolean zzt(String str) {
        return zzd(str, zzal.zzhx);
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    public final boolean zzu(String str) {
        return zzd(str, zzal.zzhy);
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    public final boolean zzv(String str) {
        return zzd(str, zzal.zzhz);
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    public final boolean zzw(String str) {
        return zzd(str, zzal.zzib);
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    public final boolean zzbw() {
        if (this.zzdz == null) {
            this.zzdz = zzj("app_measurement_lite");
            if (this.zzdz == null) {
                this.zzdz = Boolean.valueOf(false);
            }
        }
        if (this.zzdz.booleanValue() || !this.zzl.zzep()) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    public final boolean zzx(String str) {
        return zzd(str, zzal.zzia);
    }

    @WorkerThread
    static boolean zzbx() {
        return ((Boolean) zzal.zzic.get(null)).booleanValue();
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    public final boolean zzy(String str) {
        return zzd(str, zzal.zzid);
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    public final boolean zzz(String str) {
        return zzd(str, zzal.zzie);
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    public final boolean zzaa(String str) {
        return zzd(str, zzal.zzif);
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    public final boolean zzab(String str) {
        return zzd(str, zzal.zzig);
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    public final boolean zzac(String str) {
        return zzd(str, zzal.zzil);
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
