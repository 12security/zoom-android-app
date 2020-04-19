package com.google.android.gms.internal.measurement;

import android.app.Activity;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import androidx.annotation.Size;
import androidx.annotation.WorkerThread;
import com.google.android.gms.common.api.internal.GoogleServices;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.Clock;
import com.google.android.gms.common.util.DefaultClock;
import com.google.android.gms.common.util.VisibleForTesting;
import com.google.android.gms.common.wrappers.Wrappers;
import com.google.android.gms.dynamite.DynamiteModule;
import com.google.android.gms.dynamite.DynamiteModule.LoadingException;
import com.google.android.gms.dynamite.DynamiteModule.VersionPolicy;
import com.google.android.gms.dynamite.descriptors.com.google.android.gms.measurement.dynamite.ModuleDescriptor;
import com.google.android.gms.measurement.api.AppMeasurementSdk;
import com.google.android.gms.measurement.internal.zzda;
import com.google.android.gms.measurement.internal.zzdb;
import com.zipow.videobox.sip.SipCallTimeoutHelper;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nonnull;

public class zzaa {
    private static Boolean zzaf = null;
    /* access modifiers changed from: private */
    public static Boolean zzag = null;
    @VisibleForTesting
    private static String zzah = "use_dynamite_api";
    @VisibleForTesting
    private static String zzai = "allow_remote_dynamite";
    private static boolean zzaj = false;
    private static boolean zzak = false;
    private static volatile zzaa zzz;
    protected final Clock zzaa;
    private final ExecutorService zzab;
    private final AppMeasurementSdk zzac;
    /* access modifiers changed from: private */
    public Map<zzdb, zzc> zzad;
    private int zzae;
    /* access modifiers changed from: private */
    public boolean zzal;
    private String zzam;
    /* access modifiers changed from: private */
    public zzn zzan;
    /* access modifiers changed from: private */
    public final String zzw;

    abstract class zza implements Runnable {
        final long timestamp;
        final long zzbs;
        private final boolean zzbt;

        zza(zzaa zzaa) {
            this(true);
        }

        /* access modifiers changed from: 0000 */
        public abstract void zzl() throws RemoteException;

        /* access modifiers changed from: protected */
        public void zzm() {
        }

        zza(boolean z) {
            this.timestamp = zzaa.this.zzaa.currentTimeMillis();
            this.zzbs = zzaa.this.zzaa.elapsedRealtime();
            this.zzbt = z;
        }

        public void run() {
            if (zzaa.this.zzal) {
                zzm();
                return;
            }
            try {
                zzl();
            } catch (Exception e) {
                zzaa.this.zza(e, false, this.zzbt);
                zzm();
            }
        }
    }

    static class zzb extends zzu {
        private final zzda zzbu;

        zzb(zzda zzda) {
            this.zzbu = zzda;
        }

        public final void onEvent(String str, String str2, Bundle bundle, long j) {
            this.zzbu.interceptEvent(str, str2, bundle, j);
        }

        /* renamed from: id */
        public final int mo23964id() {
            return this.zzbu.hashCode();
        }
    }

    static class zzc extends zzu {
        private final zzdb zzbv;

        zzc(zzdb zzdb) {
            this.zzbv = zzdb;
        }

        public final void onEvent(String str, String str2, Bundle bundle, long j) {
            this.zzbv.onEvent(str, str2, bundle, j);
        }

        /* renamed from: id */
        public final int mo23964id() {
            return this.zzbv.hashCode();
        }
    }

    class zzd implements ActivityLifecycleCallbacks {
        zzd() {
        }

        public final void onActivityCreated(Activity activity, Bundle bundle) {
            zzaa.this.zza((zza) new zzbe(this, activity, bundle));
        }

        public final void onActivityStarted(Activity activity) {
            zzaa.this.zza((zza) new zzbf(this, activity));
        }

        public final void onActivityResumed(Activity activity) {
            zzaa.this.zza((zza) new zzbg(this, activity));
        }

        public final void onActivityPaused(Activity activity) {
            zzaa.this.zza((zza) new zzbh(this, activity));
        }

        public final void onActivityStopped(Activity activity) {
            zzaa.this.zza((zza) new zzbi(this, activity));
        }

        public final void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
            zzm zzm = new zzm();
            zzaa.this.zza((zza) new zzbj(this, activity, zzm));
            Bundle zzb = zzm.zzb(50);
            if (zzb != null) {
                bundle.putAll(zzb);
            }
        }

        public final void onActivityDestroyed(Activity activity) {
            zzaa.this.zza((zza) new zzbk(this, activity));
        }
    }

    public static zzaa zza(@Nonnull Context context) {
        return zza(context, (String) null, (String) null, (String) null, (Bundle) null);
    }

    public static zzaa zza(Context context, String str, String str2, String str3, Bundle bundle) {
        Preconditions.checkNotNull(context);
        if (zzz == null) {
            synchronized (zzaa.class) {
                if (zzz == null) {
                    zzaa zzaa2 = new zzaa(context, str, str2, str3, bundle);
                    zzz = zzaa2;
                }
            }
        }
        return zzz;
    }

    public final AppMeasurementSdk zzh() {
        return this.zzac;
    }

    private zzaa(Context context, String str, String str2, String str3, Bundle bundle) {
        if (str == null || !zza(str2, str3)) {
            this.zzw = "FA";
        } else {
            this.zzw = str;
        }
        this.zzaa = DefaultClock.getInstance();
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(0, 1, 30, TimeUnit.SECONDS, new LinkedBlockingQueue());
        this.zzab = threadPoolExecutor;
        this.zzac = new AppMeasurementSdk(this);
        boolean z = false;
        if (!(!zzb(context) || zzi())) {
            this.zzam = null;
            this.zzal = true;
            Log.w(this.zzw, "Disabling data collection. Found google_app_id in strings.xml but Google Analytics for Firebase is missing. Remove this value or add Google Analytics for Firebase to resume data collection.");
            return;
        }
        if (!zza(str2, str3)) {
            this.zzam = "fa";
            if (str2 == null || str3 == null) {
                boolean z2 = str2 == null;
                if (str3 == null) {
                    z = true;
                }
                if (z2 ^ z) {
                    Log.w(this.zzw, "Specified origin or custom app id is null. Both parameters will be ignored.");
                }
            } else {
                Log.v(this.zzw, "Deferring to Google Analytics for Firebase for event data collection. https://goo.gl/J1sWQy");
                this.zzal = true;
                return;
            }
        } else {
            this.zzam = str2;
        }
        zzab zzab2 = new zzab(this, str2, str3, context, bundle);
        zza((zza) zzab2);
        Application application = (Application) context.getApplicationContext();
        if (application == null) {
            Log.w(this.zzw, "Unable to register lifecycle notifications. Application null.");
        } else {
            application.registerActivityLifecycleCallbacks(new zzd());
        }
    }

    private static boolean zzb(Context context) {
        try {
            GoogleServices.initialize(context);
            if (GoogleServices.getGoogleAppId() != null) {
                return true;
            }
            return false;
        } catch (IllegalStateException unused) {
            return false;
        }
    }

    /* access modifiers changed from: private */
    public static boolean zza(String str, String str2) {
        return (str2 == null || str == null || zzi()) ? false : true;
    }

    /* access modifiers changed from: private */
    public final void zza(zza zza2) {
        this.zzab.execute(zza2);
    }

    /* access modifiers changed from: protected */
    public final zzn zza(Context context, boolean z) {
        VersionPolicy versionPolicy;
        if (z) {
            try {
                versionPolicy = DynamiteModule.PREFER_HIGHEST_OR_REMOTE_VERSION;
            } catch (LoadingException e) {
                zza((Exception) e, true, false);
                return null;
            }
        } else {
            versionPolicy = DynamiteModule.PREFER_LOCAL;
        }
        return zzo.asInterface(DynamiteModule.load(context, versionPolicy, ModuleDescriptor.MODULE_ID).instantiate("com.google.android.gms.measurement.internal.AppMeasurementDynamiteService"));
    }

    /* access modifiers changed from: private */
    public static int zzc(Context context) {
        return DynamiteModule.getRemoteVersion(context, ModuleDescriptor.MODULE_ID);
    }

    /* access modifiers changed from: private */
    public static int zzd(Context context) {
        return DynamiteModule.getLocalVersion(context, ModuleDescriptor.MODULE_ID);
    }

    /* access modifiers changed from: private */
    public final void zza(Exception exc, boolean z, boolean z2) {
        this.zzal |= z;
        if (z) {
            Log.w(this.zzw, "Data collection startup failed. No data will be collected.", exc);
            return;
        }
        String str = "Error with data collection. Data lost.";
        if (z2) {
            zza(5, str, (Object) exc, (Object) null, (Object) null);
        }
        Log.w(this.zzw, str, exc);
    }

    private static boolean zzi() {
        try {
            Class.forName("com.google.firebase.analytics.FirebaseAnalytics");
            return true;
        } catch (ClassNotFoundException unused) {
            return false;
        }
    }

    public final void zza(zzda zzda) {
        zza((zza) new zzam(this, zzda));
    }

    public final void zza(zzdb zzdb) {
        zza((zza) new zzax(this, zzdb));
    }

    public final void zzb(zzdb zzdb) {
        zza((zza) new zzba(this, zzdb));
    }

    public final void logEvent(@Nonnull String str, Bundle bundle) {
        zza(null, str, bundle, false, true, null);
    }

    public final void logEventInternal(String str, String str2, Bundle bundle) {
        zza(str, str2, bundle, true, true, null);
    }

    public final void logEventInternalNoInterceptor(String str, String str2, Bundle bundle, long j) {
        zza(str, str2, bundle, true, false, Long.valueOf(j));
    }

    private final void zza(String str, String str2, Bundle bundle, boolean z, boolean z2, Long l) {
        zzbb zzbb = new zzbb(this, l, str, str2, bundle, z, z2);
        zza((zza) zzbb);
    }

    public final void setUserProperty(String str, String str2) {
        zza((String) null, str, (Object) str2, false);
    }

    public final void setUserPropertyInternal(String str, String str2, Object obj) {
        zza(str, str2, obj, true);
    }

    private final void zza(String str, String str2, Object obj, boolean z) {
        zzbc zzbc = new zzbc(this, str, str2, obj, z);
        zza((zza) zzbc);
    }

    public final void setConditionalUserProperty(Bundle bundle) {
        zza((zza) new zzbd(this, bundle));
    }

    public final void clearConditionalUserProperty(String str, String str2, Bundle bundle) {
        zza((zza) new zzac(this, str, str2, bundle));
    }

    public final List<Bundle> getConditionalUserProperties(String str, String str2) {
        zzm zzm = new zzm();
        zza((zza) new zzad(this, str, str2, zzm));
        List<Bundle> list = (List) zzm.zza(zzm.zzb(5000), List.class);
        return list == null ? Collections.emptyList() : list;
    }

    public final void setUserId(String str) {
        zza((zza) new zzae(this, str));
    }

    public final void setCurrentScreen(Activity activity, String str, String str2) {
        zza((zza) new zzaf(this, activity, str, str2));
    }

    public final void setMeasurementEnabled(boolean z) {
        zza((zza) new zzag(this, z));
    }

    public final void resetAnalyticsData() {
        zza((zza) new zzah(this));
    }

    public final void setMinimumSessionDuration(long j) {
        zza((zza) new zzai(this, j));
    }

    public final void setSessionTimeoutDuration(long j) {
        zza((zza) new zzaj(this, j));
    }

    public final void beginAdUnitExposure(String str) {
        zza((zza) new zzak(this, str));
    }

    public final void endAdUnitExposure(String str) {
        zza((zza) new zzal(this, str));
    }

    public final String getGmpAppId() {
        zzm zzm = new zzm();
        zza((zza) new zzan(this, zzm));
        return zzm.zza(500);
    }

    public final String zzj() {
        zzm zzm = new zzm();
        zza((zza) new zzao(this, zzm));
        return zzm.zza(50);
    }

    public final long generateEventId() {
        zzm zzm = new zzm();
        zza((zza) new zzap(this, zzm));
        Long l = (Long) zzm.zza(zzm.zzb(500), Long.class);
        if (l != null) {
            return l.longValue();
        }
        long nextLong = new Random(System.nanoTime() ^ this.zzaa.currentTimeMillis()).nextLong();
        int i = this.zzae + 1;
        this.zzae = i;
        return nextLong + ((long) i);
    }

    public final String getCurrentScreenName() {
        zzm zzm = new zzm();
        zza((zza) new zzaq(this, zzm));
        return zzm.zza(500);
    }

    public final String getCurrentScreenClass() {
        zzm zzm = new zzm();
        zza((zza) new zzar(this, zzm));
        return zzm.zza(500);
    }

    public final Map<String, Object> getUserProperties(String str, String str2, boolean z) {
        zzm zzm = new zzm();
        zzas zzas = new zzas(this, str, str2, z, zzm);
        zza((zza) zzas);
        Bundle zzb2 = zzm.zzb(5000);
        if (zzb2 == null || zzb2.size() == 0) {
            return Collections.emptyMap();
        }
        HashMap hashMap = new HashMap(zzb2.size());
        for (String str3 : zzb2.keySet()) {
            Object obj = zzb2.get(str3);
            if ((obj instanceof Double) || (obj instanceof Long) || (obj instanceof String)) {
                hashMap.put(str3, obj);
            }
        }
        return hashMap;
    }

    public final void zza(int i, String str, Object obj, Object obj2, Object obj3) {
        zzat zzat = new zzat(this, false, 5, str, obj, null, null);
        zza((zza) zzat);
    }

    public final Bundle zza(Bundle bundle, boolean z) {
        zzm zzm = new zzm();
        zza((zza) new zzau(this, bundle, zzm));
        if (z) {
            return zzm.zzb(5000);
        }
        return null;
    }

    public final int getMaxUserProperties(String str) {
        zzm zzm = new zzm();
        zza((zza) new zzav(this, str, zzm));
        Integer num = (Integer) zzm.zza(zzm.zzb(10000), Integer.class);
        if (num == null) {
            return 25;
        }
        return num.intValue();
    }

    @WorkerThread
    public final String getAppInstanceId() {
        zzm zzm = new zzm();
        zza((zza) new zzaw(this, zzm));
        return zzm.zza(120000);
    }

    public final String getAppIdOrigin() {
        return this.zzam;
    }

    public final Object zzb(int i) {
        zzm zzm = new zzm();
        zza((zza) new zzay(this, zzm, i));
        return zzm.zza(zzm.zzb((long) SipCallTimeoutHelper.LOCAL_CALLOUT_TIMEOUT), Object.class);
    }

    public final void setDataCollectionEnabled(boolean z) {
        zza((zza) new zzaz(this, z));
    }

    /* access modifiers changed from: private */
    public static void zze(Context context) {
        synchronized (zzaa.class) {
            try {
                if (zzaf != null && zzag != null) {
                    return;
                }
                if (zza(context, "app_measurement_internal_disable_startup_flags")) {
                    zzaf = Boolean.valueOf(false);
                    zzag = Boolean.valueOf(false);
                    return;
                }
                SharedPreferences sharedPreferences = context.getSharedPreferences("com.google.android.gms.measurement.prefs", 0);
                zzaf = Boolean.valueOf(sharedPreferences.getBoolean(zzah, false));
                zzag = Boolean.valueOf(sharedPreferences.getBoolean(zzai, false));
                Editor edit = sharedPreferences.edit();
                edit.remove(zzah);
                edit.remove(zzai);
                edit.apply();
            } catch (ClassCastException | IllegalStateException | NullPointerException e) {
                Log.e("FA", "Exception reading flag from SharedPreferences.", e);
                zzaf = Boolean.valueOf(false);
                zzag = Boolean.valueOf(false);
            }
        }
    }

    public static boolean zzf(Context context) {
        zze(context);
        return zzaf.booleanValue();
    }

    private static boolean zza(Context context, @Size(min = 1) String str) {
        Preconditions.checkNotEmpty(str);
        try {
            ApplicationInfo applicationInfo = Wrappers.packageManager(context).getApplicationInfo(context.getPackageName(), 128);
            if (applicationInfo != null) {
                if (applicationInfo.metaData != null) {
                    return applicationInfo.metaData.getBoolean(str);
                }
            }
            return false;
        } catch (NameNotFoundException unused) {
            return false;
        }
    }
}
