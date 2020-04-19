package com.google.android.gms.measurement.internal;

import android.content.Context;
import androidx.annotation.WorkerThread;
import com.google.android.gms.common.util.Clock;
import com.google.android.gms.common.util.VisibleForTesting;

public final class zzap extends zzf {
    private String zzcf;
    private String zzch;
    private String zzcn;
    private String zzcp;
    private long zzcs;
    private String zzcv;
    private int zzdq;
    private int zzjg;
    private String zzjh;
    private long zzji;
    private long zzu;

    zzap(zzby zzby, long j) {
        super(zzby);
        this.zzu = j;
    }

    /* access modifiers changed from: protected */
    public final boolean zzak() {
        return true;
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:65:0x019b A[Catch:{ IllegalStateException -> 0x01cc }] */
    /* JADX WARNING: Removed duplicated region for block: B:66:0x019e A[Catch:{ IllegalStateException -> 0x01cc }] */
    /* JADX WARNING: Removed duplicated region for block: B:69:0x01a7 A[Catch:{ IllegalStateException -> 0x01cc }] */
    /* JADX WARNING: Removed duplicated region for block: B:71:0x01ba A[Catch:{ IllegalStateException -> 0x01cc }] */
    /* JADX WARNING: Removed duplicated region for block: B:76:0x01e4  */
    /* JADX WARNING: Removed duplicated region for block: B:78:0x01ef  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void zzal() {
        /*
            r10 = this;
            java.lang.String r0 = "unknown"
            java.lang.String r1 = "Unknown"
            java.lang.String r2 = "Unknown"
            android.content.Context r3 = r10.getContext()
            java.lang.String r3 = r3.getPackageName()
            android.content.Context r4 = r10.getContext()
            android.content.pm.PackageManager r4 = r4.getPackageManager()
            r5 = 0
            r6 = -2147483648(0xffffffff80000000, float:-0.0)
            if (r4 != 0) goto L_0x002d
            com.google.android.gms.measurement.internal.zzau r4 = r10.zzad()
            com.google.android.gms.measurement.internal.zzaw r4 = r4.zzda()
            java.lang.String r7 = "PackageManager is null, app identity information might be inaccurate. appId"
            java.lang.Object r8 = com.google.android.gms.measurement.internal.zzau.zzao(r3)
            r4.zza(r7, r8)
            goto L_0x0087
        L_0x002d:
            java.lang.String r0 = r4.getInstallerPackageName(r3)     // Catch:{ IllegalArgumentException -> 0x0032 }
            goto L_0x0043
        L_0x0032:
            com.google.android.gms.measurement.internal.zzau r7 = r10.zzad()
            com.google.android.gms.measurement.internal.zzaw r7 = r7.zzda()
            java.lang.String r8 = "Error retrieving app installer package name. appId"
            java.lang.Object r9 = com.google.android.gms.measurement.internal.zzau.zzao(r3)
            r7.zza(r8, r9)
        L_0x0043:
            if (r0 != 0) goto L_0x0048
            java.lang.String r0 = "manual_install"
            goto L_0x0052
        L_0x0048:
            java.lang.String r7 = "com.android.vending"
            boolean r7 = r7.equals(r0)
            if (r7 == 0) goto L_0x0052
            java.lang.String r0 = ""
        L_0x0052:
            android.content.Context r7 = r10.getContext()     // Catch:{ NameNotFoundException -> 0x0076 }
            java.lang.String r7 = r7.getPackageName()     // Catch:{ NameNotFoundException -> 0x0076 }
            android.content.pm.PackageInfo r7 = r4.getPackageInfo(r7, r5)     // Catch:{ NameNotFoundException -> 0x0076 }
            if (r7 == 0) goto L_0x0087
            android.content.pm.ApplicationInfo r8 = r7.applicationInfo     // Catch:{ NameNotFoundException -> 0x0076 }
            java.lang.CharSequence r4 = r4.getApplicationLabel(r8)     // Catch:{ NameNotFoundException -> 0x0076 }
            boolean r8 = android.text.TextUtils.isEmpty(r4)     // Catch:{ NameNotFoundException -> 0x0076 }
            if (r8 != 0) goto L_0x0070
            java.lang.String r2 = r4.toString()     // Catch:{ NameNotFoundException -> 0x0076 }
        L_0x0070:
            java.lang.String r1 = r7.versionName     // Catch:{ NameNotFoundException -> 0x0076 }
            int r4 = r7.versionCode     // Catch:{ NameNotFoundException -> 0x0076 }
            r6 = r4
            goto L_0x0087
        L_0x0076:
            com.google.android.gms.measurement.internal.zzau r4 = r10.zzad()
            com.google.android.gms.measurement.internal.zzaw r4 = r4.zzda()
            java.lang.String r7 = "Error retrieving package info. appId, appName"
            java.lang.Object r8 = com.google.android.gms.measurement.internal.zzau.zzao(r3)
            r4.zza(r7, r8, r2)
        L_0x0087:
            r10.zzcf = r3
            r10.zzcp = r0
            r10.zzcn = r1
            r10.zzjg = r6
            r10.zzjh = r2
            r0 = 0
            r10.zzji = r0
            r10.zzag()
            android.content.Context r2 = r10.getContext()
            com.google.android.gms.common.api.Status r2 = com.google.android.gms.common.api.internal.GoogleServices.initialize(r2)
            r4 = 1
            if (r2 == 0) goto L_0x00ab
            boolean r6 = r2.isSuccess()
            if (r6 == 0) goto L_0x00ab
            r6 = 1
            goto L_0x00ac
        L_0x00ab:
            r6 = 0
        L_0x00ac:
            com.google.android.gms.measurement.internal.zzby r7 = r10.zzl
            java.lang.String r7 = r7.zzem()
            boolean r7 = android.text.TextUtils.isEmpty(r7)
            if (r7 != 0) goto L_0x00c8
            java.lang.String r7 = "am"
            com.google.android.gms.measurement.internal.zzby r8 = r10.zzl
            java.lang.String r8 = r8.zzen()
            boolean r7 = r7.equals(r8)
            if (r7 == 0) goto L_0x00c8
            r7 = 1
            goto L_0x00c9
        L_0x00c8:
            r7 = 0
        L_0x00c9:
            r6 = r6 | r7
            if (r6 != 0) goto L_0x00f5
            if (r2 != 0) goto L_0x00dc
            com.google.android.gms.measurement.internal.zzau r2 = r10.zzad()
            com.google.android.gms.measurement.internal.zzaw r2 = r2.zzda()
            java.lang.String r7 = "GoogleService failed to initialize (no status)"
            r2.zzaq(r7)
            goto L_0x00f5
        L_0x00dc:
            com.google.android.gms.measurement.internal.zzau r7 = r10.zzad()
            com.google.android.gms.measurement.internal.zzaw r7 = r7.zzda()
            java.lang.String r8 = "GoogleService failed to initialize, status"
            int r9 = r2.getStatusCode()
            java.lang.Integer r9 = java.lang.Integer.valueOf(r9)
            java.lang.String r2 = r2.getStatusMessage()
            r7.zza(r8, r9, r2)
        L_0x00f5:
            if (r6 == 0) goto L_0x0161
            com.google.android.gms.measurement.internal.zzt r2 = r10.zzaf()
            java.lang.Boolean r2 = r2.zzbr()
            com.google.android.gms.measurement.internal.zzt r6 = r10.zzaf()
            boolean r6 = r6.zzbq()
            if (r6 == 0) goto L_0x011f
            com.google.android.gms.measurement.internal.zzby r2 = r10.zzl
            boolean r2 = r2.zzel()
            if (r2 == 0) goto L_0x0161
            com.google.android.gms.measurement.internal.zzau r2 = r10.zzad()
            com.google.android.gms.measurement.internal.zzaw r2 = r2.zzdg()
            java.lang.String r4 = "Collection disabled with firebase_analytics_collection_deactivated=1"
            r2.zzaq(r4)
            goto L_0x0161
        L_0x011f:
            if (r2 == 0) goto L_0x013d
            boolean r6 = r2.booleanValue()
            if (r6 != 0) goto L_0x013d
            com.google.android.gms.measurement.internal.zzby r2 = r10.zzl
            boolean r2 = r2.zzel()
            if (r2 == 0) goto L_0x0161
            com.google.android.gms.measurement.internal.zzau r2 = r10.zzad()
            com.google.android.gms.measurement.internal.zzaw r2 = r2.zzdg()
            java.lang.String r4 = "Collection disabled with firebase_analytics_collection_enabled=0"
            r2.zzaq(r4)
            goto L_0x0161
        L_0x013d:
            if (r2 != 0) goto L_0x0153
            boolean r2 = com.google.android.gms.common.api.internal.GoogleServices.isMeasurementExplicitlyDisabled()
            if (r2 == 0) goto L_0x0153
            com.google.android.gms.measurement.internal.zzau r2 = r10.zzad()
            com.google.android.gms.measurement.internal.zzaw r2 = r2.zzdg()
            java.lang.String r4 = "Collection disabled with google_app_measurement_enable=0"
            r2.zzaq(r4)
            goto L_0x0161
        L_0x0153:
            com.google.android.gms.measurement.internal.zzau r2 = r10.zzad()
            com.google.android.gms.measurement.internal.zzaw r2 = r2.zzdi()
            java.lang.String r6 = "Collection enabled"
            r2.zzaq(r6)
            goto L_0x0162
        L_0x0161:
            r4 = 0
        L_0x0162:
            java.lang.String r2 = ""
            r10.zzch = r2
            java.lang.String r2 = ""
            r10.zzcv = r2
            r10.zzcs = r0
            r10.zzag()
            com.google.android.gms.measurement.internal.zzby r0 = r10.zzl
            java.lang.String r0 = r0.zzem()
            boolean r0 = android.text.TextUtils.isEmpty(r0)
            if (r0 != 0) goto L_0x0191
            java.lang.String r0 = "am"
            com.google.android.gms.measurement.internal.zzby r1 = r10.zzl
            java.lang.String r1 = r1.zzen()
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x0191
            com.google.android.gms.measurement.internal.zzby r0 = r10.zzl
            java.lang.String r0 = r0.zzem()
            r10.zzcv = r0
        L_0x0191:
            java.lang.String r0 = com.google.android.gms.common.api.internal.GoogleServices.getGoogleAppId()     // Catch:{ IllegalStateException -> 0x01cc }
            boolean r1 = android.text.TextUtils.isEmpty(r0)     // Catch:{ IllegalStateException -> 0x01cc }
            if (r1 == 0) goto L_0x019e
            java.lang.String r1 = ""
            goto L_0x019f
        L_0x019e:
            r1 = r0
        L_0x019f:
            r10.zzch = r1     // Catch:{ IllegalStateException -> 0x01cc }
            boolean r0 = android.text.TextUtils.isEmpty(r0)     // Catch:{ IllegalStateException -> 0x01cc }
            if (r0 != 0) goto L_0x01b8
            com.google.android.gms.common.internal.StringResourceValueReader r0 = new com.google.android.gms.common.internal.StringResourceValueReader     // Catch:{ IllegalStateException -> 0x01cc }
            android.content.Context r1 = r10.getContext()     // Catch:{ IllegalStateException -> 0x01cc }
            r0.<init>(r1)     // Catch:{ IllegalStateException -> 0x01cc }
            java.lang.String r1 = "admob_app_id"
            java.lang.String r0 = r0.getString(r1)     // Catch:{ IllegalStateException -> 0x01cc }
            r10.zzcv = r0     // Catch:{ IllegalStateException -> 0x01cc }
        L_0x01b8:
            if (r4 == 0) goto L_0x01de
            com.google.android.gms.measurement.internal.zzau r0 = r10.zzad()     // Catch:{ IllegalStateException -> 0x01cc }
            com.google.android.gms.measurement.internal.zzaw r0 = r0.zzdi()     // Catch:{ IllegalStateException -> 0x01cc }
            java.lang.String r1 = "App package, google app id"
            java.lang.String r2 = r10.zzcf     // Catch:{ IllegalStateException -> 0x01cc }
            java.lang.String r4 = r10.zzch     // Catch:{ IllegalStateException -> 0x01cc }
            r0.zza(r1, r2, r4)     // Catch:{ IllegalStateException -> 0x01cc }
            goto L_0x01de
        L_0x01cc:
            r0 = move-exception
            com.google.android.gms.measurement.internal.zzau r1 = r10.zzad()
            com.google.android.gms.measurement.internal.zzaw r1 = r1.zzda()
            java.lang.String r2 = "getGoogleAppId or isMeasurementEnabled failed with exception. appId"
            java.lang.Object r3 = com.google.android.gms.measurement.internal.zzau.zzao(r3)
            r1.zza(r2, r3, r0)
        L_0x01de:
            int r0 = android.os.Build.VERSION.SDK_INT
            r1 = 16
            if (r0 < r1) goto L_0x01ef
            android.content.Context r0 = r10.getContext()
            boolean r0 = com.google.android.gms.common.wrappers.InstantApps.isInstantApp(r0)
            r10.zzdq = r0
            return
        L_0x01ef:
            r10.zzdq = r5
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.measurement.internal.zzap.zzal():void");
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    public final zzm zzak(String str) {
        String str2;
        Boolean bool;
        zzq();
        zzo();
        String zzan = zzan();
        String gmpAppId = getGmpAppId();
        zzah();
        String str3 = this.zzcn;
        long zzcx = (long) zzcx();
        zzah();
        String str4 = this.zzcp;
        long zzav = zzaf().zzav();
        zzah();
        zzq();
        if (this.zzji == 0) {
            this.zzji = this.zzl.zzab().zzc(getContext(), getContext().getPackageName());
        }
        long j = this.zzji;
        boolean isEnabled = this.zzl.isEnabled();
        boolean z = !zzae().zzlu;
        zzq();
        zzo();
        if (!zzaf().zzr(this.zzcf) || this.zzl.isEnabled()) {
            str2 = zzcw();
        } else {
            str2 = null;
        }
        zzah();
        boolean z2 = isEnabled;
        long j2 = this.zzcs;
        long zzer = this.zzl.zzer();
        int zzcy = zzcy();
        zzt zzaf = zzaf();
        zzaf.zzo();
        Boolean zzj = zzaf.zzj("google_analytics_adid_collection_enabled");
        boolean booleanValue = Boolean.valueOf(zzj == null || zzj.booleanValue()).booleanValue();
        zzt zzaf2 = zzaf();
        zzaf2.zzo();
        Boolean zzj2 = zzaf2.zzj("google_analytics_ssaid_collection_enabled");
        boolean booleanValue2 = Boolean.valueOf(zzj2 == null || zzj2.booleanValue()).booleanValue();
        boolean zzdy = zzae().zzdy();
        String zzao = zzao();
        long j3 = j2;
        if (zzaf().zze(zzan(), zzal.zzin)) {
            Boolean zzj3 = zzaf().zzj("google_analytics_default_allow_ad_personalization_signals");
            if (zzj3 != null) {
                bool = Boolean.valueOf(!zzj3.booleanValue());
                zzm zzm = new zzm(zzan, gmpAppId, str3, zzcx, str4, zzav, j, str, z2, z, str2, j3, zzer, zzcy, booleanValue, booleanValue2, zzdy, zzao, bool, this.zzu);
                return zzm;
            }
        }
        bool = null;
        zzm zzm2 = new zzm(zzan, gmpAppId, str3, zzcx, str4, zzav, j, str, z2, z, str2, j3, zzer, zzcy, booleanValue, booleanValue2, zzdy, zzao, bool, this.zzu);
        return zzm2;
    }

    @WorkerThread
    @VisibleForTesting
    private final String zzcw() {
        try {
            Class loadClass = getContext().getClassLoader().loadClass("com.google.firebase.analytics.FirebaseAnalytics");
            if (loadClass == null) {
                return null;
            }
            try {
                Object invoke = loadClass.getDeclaredMethod("getInstance", new Class[]{Context.class}).invoke(null, new Object[]{getContext()});
                if (invoke == null) {
                    return null;
                }
                try {
                    return (String) loadClass.getDeclaredMethod("getFirebaseInstanceId", new Class[0]).invoke(invoke, new Object[0]);
                } catch (Exception unused) {
                    zzad().zzdf().zzaq("Failed to retrieve Firebase Instance Id");
                    return null;
                }
            } catch (Exception unused2) {
                zzad().zzde().zzaq("Failed to obtain Firebase Analytics instance");
                return null;
            }
        } catch (ClassNotFoundException unused3) {
            return null;
        }
    }

    /* access modifiers changed from: 0000 */
    public final String zzan() {
        zzah();
        return this.zzcf;
    }

    /* access modifiers changed from: 0000 */
    public final String getGmpAppId() {
        zzah();
        return this.zzch;
    }

    /* access modifiers changed from: 0000 */
    public final String zzao() {
        zzah();
        return this.zzcv;
    }

    /* access modifiers changed from: 0000 */
    public final int zzcx() {
        zzah();
        return this.zzjg;
    }

    /* access modifiers changed from: 0000 */
    public final int zzcy() {
        zzah();
        return this.zzdq;
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
