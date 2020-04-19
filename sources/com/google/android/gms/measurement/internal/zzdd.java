package com.google.android.gms.measurement.internal;

import android.app.Application;
import android.content.Context;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import androidx.collection.ArrayMap;
import com.google.android.gms.common.api.internal.GoogleServices;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.Clock;
import com.google.android.gms.common.util.CollectionUtils;
import com.google.android.gms.common.util.VisibleForTesting;
import com.google.android.gms.measurement.api.AppMeasurementSdk.ConditionalUserProperty;
import com.google.firebase.analytics.FirebaseAnalytics.Param;
import com.zipow.videobox.sip.SipCallTimeoutHelper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicReference;

public final class zzdd extends zzf {
    @VisibleForTesting
    protected zzdx zzpf;
    private zzda zzpg;
    private final Set<zzdb> zzph = new CopyOnWriteArraySet();
    private boolean zzpi;
    private final AtomicReference<String> zzpj = new AtomicReference<>();
    @VisibleForTesting
    protected boolean zzpk = true;

    protected zzdd(zzby zzby) {
        super(zzby);
    }

    /* access modifiers changed from: protected */
    public final boolean zzak() {
        return false;
    }

    public final void zzeu() {
        if (getContext().getApplicationContext() instanceof Application) {
            ((Application) getContext().getApplicationContext()).unregisterActivityLifecycleCallbacks(this.zzpf);
        }
    }

    public final Boolean zzev() {
        AtomicReference atomicReference = new AtomicReference();
        return (Boolean) zzac().zza(atomicReference, SipCallTimeoutHelper.LOCAL_CALLOUT_TIMEOUT, "boolean test flag value", new zzde(this, atomicReference));
    }

    public final String zzew() {
        AtomicReference atomicReference = new AtomicReference();
        return (String) zzac().zza(atomicReference, SipCallTimeoutHelper.LOCAL_CALLOUT_TIMEOUT, "String test flag value", new zzdo(this, atomicReference));
    }

    public final Long zzex() {
        AtomicReference atomicReference = new AtomicReference();
        return (Long) zzac().zza(atomicReference, SipCallTimeoutHelper.LOCAL_CALLOUT_TIMEOUT, "long test flag value", new zzdq(this, atomicReference));
    }

    public final Integer zzey() {
        AtomicReference atomicReference = new AtomicReference();
        return (Integer) zzac().zza(atomicReference, SipCallTimeoutHelper.LOCAL_CALLOUT_TIMEOUT, "int test flag value", new zzdr(this, atomicReference));
    }

    public final Double zzez() {
        AtomicReference atomicReference = new AtomicReference();
        return (Double) zzac().zza(atomicReference, SipCallTimeoutHelper.LOCAL_CALLOUT_TIMEOUT, "double test flag value", new zzds(this, atomicReference));
    }

    public final void setMeasurementEnabled(boolean z) {
        zzah();
        zzo();
        zzac().zza((Runnable) new zzdt(this, z));
    }

    public final void zza(boolean z) {
        zzah();
        zzo();
        zzac().zza((Runnable) new zzdu(this, z));
    }

    /* access modifiers changed from: private */
    @WorkerThread
    public final void zzg(boolean z) {
        zzq();
        zzo();
        zzah();
        zzad().zzdh().zza("Setting app measurement enabled (FE)", Boolean.valueOf(z));
        zzae().setMeasurementEnabled(z);
        zzfa();
    }

    /* access modifiers changed from: private */
    @WorkerThread
    public final void zzfa() {
        if (zzaf().zze(zzt().zzan(), zzal.zzin)) {
            zzq();
            String zzed = zzae().zzlp.zzed();
            if (zzed != null) {
                if ("unset".equals(zzed)) {
                    zza("app", "_npa", (Object) null, zzz().currentTimeMillis());
                } else {
                    zza("app", "_npa", (Object) Long.valueOf("true".equals(zzed) ? 1 : 0), zzz().currentTimeMillis());
                }
            }
        }
        if (!zzaf().zzs(zzt().zzan()) || !this.zzl.isEnabled() || !this.zzpk) {
            zzad().zzdh().zzaq("Updating Scion state (FE)");
            zzu().zzfe();
            return;
        }
        zzad().zzdh().zzaq("Recording app launch after enabling measurement for the first time (FE)");
        zzfb();
    }

    public final void setMinimumSessionDuration(long j) {
        zzo();
        zzac().zza((Runnable) new zzdv(this, j));
    }

    public final void setSessionTimeoutDuration(long j) {
        zzo();
        zzac().zza((Runnable) new zzdw(this, j));
    }

    public final void zza(String str, String str2, Bundle bundle, boolean z) {
        logEvent(str, str2, bundle, false, true, zzz().currentTimeMillis());
    }

    public final void logEvent(String str, String str2, Bundle bundle) {
        logEvent(str, str2, bundle, true, true, zzz().currentTimeMillis());
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    public final void zza(String str, String str2, Bundle bundle) {
        zzo();
        zzq();
        zza(str, str2, zzz().currentTimeMillis(), bundle);
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    public final void zza(String str, String str2, long j, Bundle bundle) {
        zzo();
        zzq();
        zza(str, str2, j, bundle, true, this.zzpg == null || zzgd.zzbs(str2), false, null);
    }

    /* access modifiers changed from: private */
    @WorkerThread
    public final void zza(String str, String str2, long j, Bundle bundle, boolean z, boolean z2, boolean z3, String str3) {
        zzdd zzdd;
        List list;
        String[] strArr;
        zzec zzec;
        int i;
        long j2;
        ArrayList arrayList;
        Bundle bundle2;
        Class cls;
        String str4 = str;
        String str5 = str2;
        long j3 = j;
        Bundle bundle3 = bundle;
        String str6 = str3;
        Preconditions.checkNotEmpty(str);
        if (!zzaf().zze(str6, zzal.zzis)) {
            Preconditions.checkNotEmpty(str2);
        }
        Preconditions.checkNotNull(bundle);
        zzq();
        zzah();
        if (!this.zzl.isEnabled()) {
            zzad().zzdh().zzaq("Event not sent since app measurement is disabled");
            return;
        }
        int i2 = 0;
        if (!this.zzpi) {
            this.zzpi = true;
            try {
                if (!this.zzl.zzep()) {
                    cls = Class.forName("com.google.android.gms.tagmanager.TagManagerService", true, getContext().getClassLoader());
                } else {
                    cls = Class.forName("com.google.android.gms.tagmanager.TagManagerService");
                }
                try {
                    cls.getDeclaredMethod("initialize", new Class[]{Context.class}).invoke(null, new Object[]{getContext()});
                } catch (Exception e) {
                    zzad().zzdd().zza("Failed to invoke Tag Manager's initialize() method", e);
                }
            } catch (ClassNotFoundException unused) {
                zzad().zzdg().zzaq("Tag Manager is not found and thus will not be used");
            }
        }
        if (z3) {
            zzag();
            if (!"_iap".equals(str5)) {
                zzgd zzab = this.zzl.zzab();
                int i3 = 2;
                if (zzab.zzp("event", str5)) {
                    if (!zzab.zza("event", zzcx.zzoy, str5)) {
                        i3 = 13;
                    } else if (zzab.zza("event", 40, str5)) {
                        i3 = 0;
                    }
                }
                if (i3 != 0) {
                    zzad().zzdc().zza("Invalid public event name. Event will not be logged (FE)", zzaa().zzal(str5));
                    this.zzl.zzab();
                    this.zzl.zzab().zza(i3, "_ev", zzgd.zza(str5, 40, true), str5 != null ? str2.length() : 0);
                    return;
                }
            }
        }
        zzag();
        zzec zzfc = zzv().zzfc();
        if (zzfc != null && !bundle3.containsKey("_sc")) {
            zzfc.zzpx = true;
        }
        zzed.zza(zzfc, bundle3, z && z3);
        boolean equals = "am".equals(str4);
        boolean zzbs = zzgd.zzbs(str2);
        if (z && this.zzpg != null && !zzbs && !equals) {
            zzad().zzdh().zza("Passing event to registered event handler (FE)", zzaa().zzal(str5), zzaa().zzc(bundle3));
            this.zzpg.interceptEvent(str, str2, bundle, j);
        } else if (this.zzl.zzet()) {
            int zzbn = zzab().zzbn(str5);
            if (zzbn != 0) {
                zzad().zzdc().zza("Invalid event name. Event will not be logged (FE)", zzaa().zzal(str5));
                zzab();
                String zza = zzgd.zza(str5, 40, true);
                if (str5 != null) {
                    i2 = str2.length();
                }
                this.zzl.zzab().zza(str3, zzbn, "_ev", zza, i2);
                return;
            }
            List listOf = CollectionUtils.listOf((T[]) new String[]{"_o", "_sn", "_sc", "_si"});
            long j4 = j3;
            Bundle zza2 = zzab().zza(str3, str2, bundle, listOf, z3, true);
            zzec zzec2 = (zza2 == null || !zza2.containsKey("_sc") || !zza2.containsKey("_si")) ? null : new zzec(zza2.getString("_sn"), zza2.getString("_sc"), Long.valueOf(zza2.getLong("_si")).longValue());
            zzec zzec3 = zzec2 == null ? zzfc : zzec2;
            if (zzaf().zzac(str6)) {
                zzag();
                if (zzv().zzfc() != null && "_ae".equals(str5)) {
                    long zzfq = zzx().zzfq();
                    if (zzfq > 0) {
                        zzab().zzb(zza2, zzfq);
                    }
                }
            }
            ArrayList arrayList2 = new ArrayList();
            arrayList2.add(zza2);
            long nextLong = zzab().zzgl().nextLong();
            if (zzaf().zze(zzt().zzan(), zzal.zzih) && zzae().zzls.get() > 0 && zzae().zzx(j4) && zzae().zzlv.get()) {
                zzad().zzdi().zzaq("Current session is expired, remove the session number and Id");
                if (zzaf().zze(zzt().zzan(), zzal.zzid)) {
                    zza("auto", "_sid", (Object) null, zzz().currentTimeMillis());
                }
                if (zzaf().zze(zzt().zzan(), zzal.zzie)) {
                    zza("auto", "_sno", (Object) null, zzz().currentTimeMillis());
                }
            }
            if (!zzaf().zzab(zzt().zzan())) {
                long j5 = j4;
                zzdd = this;
            } else if (zza2.getLong(Param.EXTEND_SESSION, 0) == 1) {
                zzad().zzdi().zzaq("EXTEND_SESSION param attached: initiate a new session or extend the current active session");
                long j6 = j4;
                zzdd = this;
                zzdd.zzl.zzx().zza(j6, true);
            } else {
                long j7 = j4;
                zzdd = this;
            }
            String[] strArr2 = (String[]) zza2.keySet().toArray(new String[bundle.size()]);
            Arrays.sort(strArr2);
            int length = strArr2.length;
            int i4 = 0;
            int i5 = 0;
            while (i4 < length) {
                String str7 = strArr2[i4];
                Object obj = zza2.get(str7);
                zzab();
                Bundle[] zzb = zzgd.zzb(obj);
                if (zzb != null) {
                    zza2.putInt(str7, zzb.length);
                    strArr = strArr2;
                    int i6 = 0;
                    while (i6 < zzb.length) {
                        Bundle bundle4 = zzb[i6];
                        int i7 = length;
                        zzed.zza(zzec3, bundle4, true);
                        Bundle[] bundleArr = zzb;
                        String str8 = str7;
                        long j8 = nextLong;
                        Bundle bundle5 = bundle4;
                        ArrayList arrayList3 = arrayList2;
                        zzec zzec4 = zzec3;
                        List list2 = listOf;
                        Bundle bundle6 = zza2;
                        Bundle zza3 = zzab().zza(str3, "_ep", bundle5, listOf, z3, false);
                        zza3.putString("_en", str2);
                        zza3.putLong("_eid", j8);
                        zza3.putString("_gn", str8);
                        Bundle[] bundleArr2 = bundleArr;
                        zza3.putInt("_ll", bundleArr2.length);
                        zza3.putInt("_i", i6);
                        arrayList3.add(zza3);
                        i6++;
                        zza2 = bundle6;
                        arrayList2 = arrayList3;
                        str7 = str8;
                        zzb = bundleArr2;
                        length = i7;
                        zzec3 = zzec4;
                        listOf = list2;
                        nextLong = j8;
                        long j9 = j;
                    }
                    list = listOf;
                    i = length;
                    j2 = nextLong;
                    arrayList = arrayList2;
                    zzec = zzec3;
                    bundle2 = zza2;
                    String str9 = str2;
                    i5 += zzb.length;
                } else {
                    list = listOf;
                    strArr = strArr2;
                    i = length;
                    j2 = nextLong;
                    arrayList = arrayList2;
                    zzec = zzec3;
                    bundle2 = zza2;
                    String str10 = str2;
                }
                i4++;
                zza2 = bundle2;
                arrayList2 = arrayList;
                nextLong = j2;
                length = i;
                zzec3 = zzec;
                long j10 = j;
                strArr2 = strArr;
                listOf = list;
            }
            long j11 = nextLong;
            ArrayList arrayList4 = arrayList2;
            Bundle bundle7 = zza2;
            String str11 = str2;
            if (i5 != 0) {
                bundle7.putLong("_eid", j11);
                bundle7.putInt("_epc", i5);
            }
            int i8 = 0;
            while (i8 < arrayList4.size()) {
                Bundle bundle8 = (Bundle) arrayList4.get(i8);
                String str12 = i8 != 0 ? "_ep" : str11;
                bundle8.putString("_o", str);
                Bundle zzg = z2 ? zzab().zzg(bundle8) : bundle8;
                zzad().zzdh().zza("Logging event (FE)", zzaa().zzal(str11), zzaa().zzc(zzg));
                ArrayList arrayList5 = arrayList4;
                zzaj zzaj = new zzaj(str12, new zzag(zzg), str, j);
                zzu().zzc(zzaj, str3);
                if (!equals) {
                    for (zzdb onEvent : zzdd.zzph) {
                        onEvent.onEvent(str, str2, new Bundle(zzg), j);
                    }
                }
                i8++;
                arrayList4 = arrayList5;
            }
            zzag();
            if (zzv().zzfc() != null && "_ae".equals(str11)) {
                zzx().zza(true, true);
            }
        }
    }

    public final void logEvent(String str, String str2, Bundle bundle, boolean z, boolean z2, long j) {
        boolean z3;
        zzo();
        String str3 = str == null ? "app" : str;
        Bundle bundle2 = bundle == null ? new Bundle() : bundle;
        if (z2) {
            if (this.zzpg != null && !zzgd.zzbs(str2)) {
                z3 = false;
                zzb(str3, str2, j, bundle2, z2, z3, !z, null);
            }
        }
        z3 = true;
        zzb(str3, str2, j, bundle2, z2, z3, !z, null);
    }

    private final void zzb(String str, String str2, long j, Bundle bundle, boolean z, boolean z2, boolean z3, String str3) {
        Bundle zzh = zzgd.zzh(bundle);
        zzbt zzac = zzac();
        zzdf zzdf = new zzdf(this, str, str2, j, zzh, z, z2, z3, str3);
        zzac.zza((Runnable) zzdf);
    }

    public final void zzb(String str, String str2, Object obj, boolean z) {
        zza(str, str2, obj, z, zzz().currentTimeMillis());
    }

    public final void zza(String str, String str2, Object obj, boolean z, long j) {
        String str3 = str == null ? "app" : str;
        int i = 6;
        int i2 = 0;
        if (z) {
            i = zzab().zzbo(str2);
        } else {
            zzgd zzab = zzab();
            if (zzab.zzp("user property", str2)) {
                if (!zzab.zza("user property", zzcz.zzpc, str2)) {
                    i = 15;
                } else if (zzab.zza("user property", 24, str2)) {
                    i = 0;
                }
            }
        }
        if (i != 0) {
            zzab();
            String zza = zzgd.zza(str2, 24, true);
            if (str2 != null) {
                i2 = str2.length();
            }
            this.zzl.zzab().zza(i, "_ev", zza, i2);
        } else if (obj != null) {
            int zzc = zzab().zzc(str2, obj);
            if (zzc != 0) {
                zzab();
                String zza2 = zzgd.zza(str2, 24, true);
                if ((obj instanceof String) || (obj instanceof CharSequence)) {
                    i2 = String.valueOf(obj).length();
                }
                this.zzl.zzab().zza(zzc, "_ev", zza2, i2);
                return;
            }
            Object zzd = zzab().zzd(str2, obj);
            if (zzd != null) {
                zza(str3, str2, j, zzd);
            }
        } else {
            zza(str3, str2, j, (Object) null);
        }
    }

    private final void zza(String str, String str2, long j, Object obj) {
        zzbt zzac = zzac();
        zzdg zzdg = new zzdg(this, str, str2, obj, j);
        zzac.zza((Runnable) zzdg);
    }

    /* access modifiers changed from: 0000 */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x008a  */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x0098  */
    @androidx.annotation.WorkerThread
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void zza(java.lang.String r9, java.lang.String r10, java.lang.Object r11, long r12) {
        /*
            r8 = this;
            com.google.android.gms.common.internal.Preconditions.checkNotEmpty(r9)
            com.google.android.gms.common.internal.Preconditions.checkNotEmpty(r10)
            r8.zzq()
            r8.zzo()
            r8.zzah()
            com.google.android.gms.measurement.internal.zzt r0 = r8.zzaf()
            com.google.android.gms.measurement.internal.zzap r1 = r8.zzt()
            java.lang.String r1 = r1.zzan()
            com.google.android.gms.measurement.internal.zzal$zza<java.lang.Boolean> r2 = com.google.android.gms.measurement.internal.zzal.zzin
            boolean r0 = r0.zze(r1, r2)
            if (r0 == 0) goto L_0x0080
            java.lang.String r0 = "allow_personalized_ads"
            boolean r0 = r0.equals(r10)
            if (r0 == 0) goto L_0x0080
            boolean r0 = r11 instanceof java.lang.String
            if (r0 == 0) goto L_0x006e
            r0 = r11
            java.lang.String r0 = (java.lang.String) r0
            boolean r1 = android.text.TextUtils.isEmpty(r0)
            if (r1 != 0) goto L_0x006e
            java.lang.String r10 = "false"
            java.util.Locale r11 = java.util.Locale.ENGLISH
            java.lang.String r11 = r0.toLowerCase(r11)
            boolean r10 = r10.equals(r11)
            r0 = 1
            if (r10 == 0) goto L_0x004a
            r10 = r0
            goto L_0x004c
        L_0x004a:
            r10 = 0
        L_0x004c:
            java.lang.Long r11 = java.lang.Long.valueOf(r10)
            java.lang.String r10 = "_npa"
            com.google.android.gms.measurement.internal.zzbf r2 = r8.zzae()
            com.google.android.gms.measurement.internal.zzbk r2 = r2.zzlp
            r3 = r11
            java.lang.Long r3 = (java.lang.Long) r3
            long r3 = r3.longValue()
            int r0 = (r3 > r0 ? 1 : (r3 == r0 ? 0 : -1))
            if (r0 != 0) goto L_0x0066
            java.lang.String r0 = "true"
            goto L_0x0068
        L_0x0066:
            java.lang.String r0 = "false"
        L_0x0068:
            r2.zzav(r0)
            r3 = r10
            r6 = r11
            goto L_0x0082
        L_0x006e:
            if (r11 != 0) goto L_0x0080
            java.lang.String r10 = "_npa"
            com.google.android.gms.measurement.internal.zzbf r0 = r8.zzae()
            com.google.android.gms.measurement.internal.zzbk r0 = r0.zzlp
            java.lang.String r1 = "unset"
            r0.zzav(r1)
            r3 = r10
            r6 = r11
            goto L_0x0082
        L_0x0080:
            r3 = r10
            r6 = r11
        L_0x0082:
            com.google.android.gms.measurement.internal.zzby r10 = r8.zzl
            boolean r10 = r10.isEnabled()
            if (r10 != 0) goto L_0x0098
            com.google.android.gms.measurement.internal.zzau r9 = r8.zzad()
            com.google.android.gms.measurement.internal.zzaw r9 = r9.zzdh()
            java.lang.String r10 = "User property not set since app measurement is disabled"
            r9.zzaq(r10)
            return
        L_0x0098:
            com.google.android.gms.measurement.internal.zzby r10 = r8.zzl
            boolean r10 = r10.zzet()
            if (r10 != 0) goto L_0x00a1
            return
        L_0x00a1:
            com.google.android.gms.measurement.internal.zzau r10 = r8.zzad()
            com.google.android.gms.measurement.internal.zzaw r10 = r10.zzdh()
            java.lang.String r11 = "Setting user property (FE)"
            com.google.android.gms.measurement.internal.zzas r0 = r8.zzaa()
            java.lang.String r0 = r0.zzal(r3)
            r10.zza(r11, r0, r6)
            com.google.android.gms.measurement.internal.zzga r10 = new com.google.android.gms.measurement.internal.zzga
            r2 = r10
            r4 = r12
            r7 = r9
            r2.<init>(r3, r4, r6, r7)
            com.google.android.gms.measurement.internal.zzeg r9 = r8.zzu()
            r9.zzb(r10)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.measurement.internal.zzdd.zza(java.lang.String, java.lang.String, java.lang.Object, long):void");
    }

    public final List<zzga> zzh(boolean z) {
        zzo();
        zzah();
        zzad().zzdh().zzaq("Fetching user attributes (FE)");
        if (zzac().zzef()) {
            zzad().zzda().zzaq("Cannot get all user properties from analytics worker thread");
            return Collections.emptyList();
        } else if (zzq.isMainThread()) {
            zzad().zzda().zzaq("Cannot get all user properties from main thread");
            return Collections.emptyList();
        } else {
            AtomicReference atomicReference = new AtomicReference();
            synchronized (atomicReference) {
                this.zzl.zzac().zza((Runnable) new zzdh(this, atomicReference, z));
                try {
                    atomicReference.wait(5000);
                } catch (InterruptedException e) {
                    zzad().zzdd().zza("Interrupted waiting for get user properties", e);
                }
            }
            List<zzga> list = (List) atomicReference.get();
            if (list != null) {
                return list;
            }
            zzad().zzdd().zzaq("Timed out waiting for get user properties");
            return Collections.emptyList();
        }
    }

    @Nullable
    public final String zzj() {
        zzo();
        return (String) this.zzpj.get();
    }

    @Nullable
    public final String zzy(long j) {
        if (zzac().zzef()) {
            zzad().zzda().zzaq("Cannot retrieve app instance id from analytics worker thread");
            return null;
        } else if (zzq.isMainThread()) {
            zzad().zzda().zzaq("Cannot retrieve app instance id from main thread");
            return null;
        } else {
            long elapsedRealtime = zzz().elapsedRealtime();
            String zzz = zzz(120000);
            long elapsedRealtime2 = zzz().elapsedRealtime() - elapsedRealtime;
            if (zzz == null && elapsedRealtime2 < 120000) {
                zzz = zzz(120000 - elapsedRealtime2);
            }
            return zzz;
        }
    }

    /* access modifiers changed from: 0000 */
    public final void zzbi(@Nullable String str) {
        this.zzpj.set(str);
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(4:10|11|12|13) */
    /* JADX WARNING: Code restructure failed: missing block: B:11:?, code lost:
        zzad().zzdd().zzaq("Interrupted waiting for app instance id");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x002c, code lost:
        return null;
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:10:0x001d */
    @androidx.annotation.Nullable
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final java.lang.String zzz(long r4) {
        /*
            r3 = this;
            java.util.concurrent.atomic.AtomicReference r0 = new java.util.concurrent.atomic.AtomicReference
            r0.<init>()
            monitor-enter(r0)
            com.google.android.gms.measurement.internal.zzbt r1 = r3.zzac()     // Catch:{ all -> 0x002d }
            com.google.android.gms.measurement.internal.zzdi r2 = new com.google.android.gms.measurement.internal.zzdi     // Catch:{ all -> 0x002d }
            r2.<init>(r3, r0)     // Catch:{ all -> 0x002d }
            r1.zza(r2)     // Catch:{ all -> 0x002d }
            r0.wait(r4)     // Catch:{ InterruptedException -> 0x001d }
            monitor-exit(r0)     // Catch:{ all -> 0x002d }
            java.lang.Object r4 = r0.get()
            java.lang.String r4 = (java.lang.String) r4
            return r4
        L_0x001d:
            com.google.android.gms.measurement.internal.zzau r4 = r3.zzad()     // Catch:{ all -> 0x002d }
            com.google.android.gms.measurement.internal.zzaw r4 = r4.zzdd()     // Catch:{ all -> 0x002d }
            java.lang.String r5 = "Interrupted waiting for app instance id"
            r4.zzaq(r5)     // Catch:{ all -> 0x002d }
            r4 = 0
            monitor-exit(r0)     // Catch:{ all -> 0x002d }
            return r4
        L_0x002d:
            r4 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x002d }
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.measurement.internal.zzdd.zzz(long):java.lang.String");
    }

    public final void resetAnalyticsData(long j) {
        zzbi(null);
        zzac().zza((Runnable) new zzdj(this, j));
    }

    @WorkerThread
    public final void zzfb() {
        zzq();
        zzo();
        zzah();
        if (this.zzl.zzet()) {
            zzu().zzfb();
            this.zzpk = false;
            String zzdx = zzae().zzdx();
            if (!TextUtils.isEmpty(zzdx)) {
                zzy().zzah();
                if (!zzdx.equals(VERSION.RELEASE)) {
                    Bundle bundle = new Bundle();
                    bundle.putString("_po", zzdx);
                    logEvent("auto", "_ou", bundle);
                }
            }
        }
    }

    @WorkerThread
    public final void zza(zzda zzda) {
        zzq();
        zzo();
        zzah();
        if (zzda != null) {
            zzda zzda2 = this.zzpg;
            if (zzda != zzda2) {
                Preconditions.checkState(zzda2 == null, "EventInterceptor already set.");
            }
        }
        this.zzpg = zzda;
    }

    public final void zza(zzdb zzdb) {
        zzo();
        zzah();
        Preconditions.checkNotNull(zzdb);
        if (!this.zzph.add(zzdb)) {
            zzad().zzdd().zzaq("OnEventListener already registered");
        }
    }

    public final void zzb(zzdb zzdb) {
        zzo();
        zzah();
        Preconditions.checkNotNull(zzdb);
        if (!this.zzph.remove(zzdb)) {
            zzad().zzdd().zzaq("OnEventListener had not been registered");
        }
    }

    public final void setConditionalUserProperty(Bundle bundle) {
        setConditionalUserProperty(bundle, zzz().currentTimeMillis());
    }

    public final void setConditionalUserProperty(Bundle bundle, long j) {
        Preconditions.checkNotNull(bundle);
        zzo();
        Bundle bundle2 = new Bundle(bundle);
        if (!TextUtils.isEmpty(bundle2.getString("app_id"))) {
            zzad().zzdd().zzaq("Package name should be null when calling setConditionalUserProperty");
        }
        bundle2.remove("app_id");
        zza(bundle2, j);
    }

    public final void zzd(Bundle bundle) {
        Preconditions.checkNotNull(bundle);
        Preconditions.checkNotEmpty(bundle.getString("app_id"));
        zzn();
        zza(new Bundle(bundle), zzz().currentTimeMillis());
    }

    private final void zza(Bundle bundle, long j) {
        Preconditions.checkNotNull(bundle);
        zzcw.zza(bundle, "app_id", String.class, null);
        zzcw.zza(bundle, "origin", String.class, null);
        zzcw.zza(bundle, "name", String.class, null);
        zzcw.zza(bundle, "value", Object.class, null);
        zzcw.zza(bundle, ConditionalUserProperty.TRIGGER_EVENT_NAME, String.class, null);
        zzcw.zza(bundle, ConditionalUserProperty.TRIGGER_TIMEOUT, Long.class, Long.valueOf(0));
        zzcw.zza(bundle, ConditionalUserProperty.TIMED_OUT_EVENT_NAME, String.class, null);
        zzcw.zza(bundle, ConditionalUserProperty.TIMED_OUT_EVENT_PARAMS, Bundle.class, null);
        zzcw.zza(bundle, ConditionalUserProperty.TRIGGERED_EVENT_NAME, String.class, null);
        zzcw.zza(bundle, ConditionalUserProperty.TRIGGERED_EVENT_PARAMS, Bundle.class, null);
        zzcw.zza(bundle, ConditionalUserProperty.TIME_TO_LIVE, Long.class, Long.valueOf(0));
        zzcw.zza(bundle, ConditionalUserProperty.EXPIRED_EVENT_NAME, String.class, null);
        zzcw.zza(bundle, ConditionalUserProperty.EXPIRED_EVENT_PARAMS, Bundle.class, null);
        Preconditions.checkNotEmpty(bundle.getString("name"));
        Preconditions.checkNotEmpty(bundle.getString("origin"));
        Preconditions.checkNotNull(bundle.get("value"));
        bundle.putLong(ConditionalUserProperty.CREATION_TIMESTAMP, j);
        String string = bundle.getString("name");
        Object obj = bundle.get("value");
        if (zzab().zzbo(string) != 0) {
            zzad().zzda().zza("Invalid conditional user property name", zzaa().zzan(string));
        } else if (zzab().zzc(string, obj) != 0) {
            zzad().zzda().zza("Invalid conditional user property value", zzaa().zzan(string), obj);
        } else {
            Object zzd = zzab().zzd(string, obj);
            if (zzd == null) {
                zzad().zzda().zza("Unable to normalize conditional user property value", zzaa().zzan(string), obj);
                return;
            }
            zzcw.zza(bundle, zzd);
            long j2 = bundle.getLong(ConditionalUserProperty.TRIGGER_TIMEOUT);
            if (TextUtils.isEmpty(bundle.getString(ConditionalUserProperty.TRIGGER_EVENT_NAME)) || (j2 <= 15552000000L && j2 >= 1)) {
                long j3 = bundle.getLong(ConditionalUserProperty.TIME_TO_LIVE);
                if (j3 > 15552000000L || j3 < 1) {
                    zzad().zzda().zza("Invalid conditional user property time to live", zzaa().zzan(string), Long.valueOf(j3));
                } else {
                    zzac().zza((Runnable) new zzdl(this, bundle));
                }
            } else {
                zzad().zzda().zza("Invalid conditional user property timeout", zzaa().zzan(string), Long.valueOf(j2));
            }
        }
    }

    public final void clearConditionalUserProperty(String str, String str2, Bundle bundle) {
        zzo();
        zza((String) null, str, str2, bundle);
    }

    public final void clearConditionalUserPropertyAs(String str, String str2, String str3, Bundle bundle) {
        Preconditions.checkNotEmpty(str);
        zzn();
        zza(str, str2, str3, bundle);
    }

    private final void zza(String str, String str2, String str3, Bundle bundle) {
        long currentTimeMillis = zzz().currentTimeMillis();
        Preconditions.checkNotEmpty(str2);
        Bundle bundle2 = new Bundle();
        if (str != null) {
            bundle2.putString("app_id", str);
        }
        bundle2.putString("name", str2);
        bundle2.putLong(ConditionalUserProperty.CREATION_TIMESTAMP, currentTimeMillis);
        if (str3 != null) {
            bundle2.putString(ConditionalUserProperty.EXPIRED_EVENT_NAME, str3);
            bundle2.putBundle(ConditionalUserProperty.EXPIRED_EVENT_PARAMS, bundle);
        }
        zzac().zza((Runnable) new zzdm(this, bundle2));
    }

    /* access modifiers changed from: private */
    @WorkerThread
    public final void zze(Bundle bundle) {
        Bundle bundle2 = bundle;
        zzq();
        zzah();
        Preconditions.checkNotNull(bundle);
        Preconditions.checkNotEmpty(bundle2.getString("name"));
        Preconditions.checkNotEmpty(bundle2.getString("origin"));
        Preconditions.checkNotNull(bundle2.get("value"));
        if (!this.zzl.isEnabled()) {
            zzad().zzdh().zzaq("Conditional property not sent since collection is disabled");
            return;
        }
        zzga zzga = new zzga(bundle2.getString("name"), bundle2.getLong(ConditionalUserProperty.TRIGGERED_TIMESTAMP), bundle2.get("value"), bundle2.getString("origin"));
        try {
            zzaj zza = zzab().zza(bundle2.getString("app_id"), bundle2.getString(ConditionalUserProperty.TRIGGERED_EVENT_NAME), bundle2.getBundle(ConditionalUserProperty.TRIGGERED_EVENT_PARAMS), bundle2.getString("origin"), 0, true, false);
            zzaj zza2 = zzab().zza(bundle2.getString("app_id"), bundle2.getString(ConditionalUserProperty.TIMED_OUT_EVENT_NAME), bundle2.getBundle(ConditionalUserProperty.TIMED_OUT_EVENT_PARAMS), bundle2.getString("origin"), 0, true, false);
            zzaj zza3 = zzab().zza(bundle2.getString("app_id"), bundle2.getString(ConditionalUserProperty.EXPIRED_EVENT_NAME), bundle2.getBundle(ConditionalUserProperty.EXPIRED_EVENT_PARAMS), bundle2.getString("origin"), 0, true, false);
            String string = bundle2.getString("app_id");
            String string2 = bundle2.getString("origin");
            long j = bundle2.getLong(ConditionalUserProperty.CREATION_TIMESTAMP);
            String string3 = bundle2.getString(ConditionalUserProperty.TRIGGER_EVENT_NAME);
            long j2 = bundle2.getLong(ConditionalUserProperty.TRIGGER_TIMEOUT);
            long j3 = bundle2.getLong(ConditionalUserProperty.TIME_TO_LIVE);
            zzr zzr = r3;
            zzr zzr2 = new zzr(string, string2, zzga, j, false, string3, zza2, j2, zza, j3, zza3);
            zzu().zzd(zzr);
        } catch (IllegalArgumentException unused) {
        }
    }

    /* access modifiers changed from: private */
    @WorkerThread
    public final void zzf(Bundle bundle) {
        Bundle bundle2 = bundle;
        zzq();
        zzah();
        Preconditions.checkNotNull(bundle);
        Preconditions.checkNotEmpty(bundle2.getString("name"));
        if (!this.zzl.isEnabled()) {
            zzad().zzdh().zzaq("Conditional property not cleared since collection is disabled");
            return;
        }
        zzga zzga = new zzga(bundle2.getString("name"), 0, null, null);
        try {
            zzaj zza = zzab().zza(bundle2.getString("app_id"), bundle2.getString(ConditionalUserProperty.EXPIRED_EVENT_NAME), bundle2.getBundle(ConditionalUserProperty.EXPIRED_EVENT_PARAMS), bundle2.getString("origin"), bundle2.getLong(ConditionalUserProperty.CREATION_TIMESTAMP), true, false);
            String string = bundle2.getString("app_id");
            String string2 = bundle2.getString("origin");
            long j = bundle2.getLong(ConditionalUserProperty.CREATION_TIMESTAMP);
            boolean z = bundle2.getBoolean(ConditionalUserProperty.ACTIVE);
            String string3 = bundle2.getString(ConditionalUserProperty.TRIGGER_EVENT_NAME);
            long j2 = bundle2.getLong(ConditionalUserProperty.TRIGGER_TIMEOUT);
            long j3 = bundle2.getLong(ConditionalUserProperty.TIME_TO_LIVE);
            zzr zzr = r3;
            zzr zzr2 = new zzr(string, string2, zzga, j, z, string3, null, j2, null, j3, zza);
            zzu().zzd(zzr);
        } catch (IllegalArgumentException unused) {
        }
    }

    public final ArrayList<Bundle> zzn(String str, String str2) {
        zzo();
        return zzf(null, str, str2);
    }

    public final ArrayList<Bundle> zze(String str, String str2, String str3) {
        Preconditions.checkNotEmpty(str);
        zzn();
        return zzf(str, str2, str3);
    }

    @VisibleForTesting
    private final ArrayList<Bundle> zzf(String str, String str2, String str3) {
        if (zzac().zzef()) {
            zzad().zzda().zzaq("Cannot get conditional user properties from analytics worker thread");
            return new ArrayList<>(0);
        } else if (zzq.isMainThread()) {
            zzad().zzda().zzaq("Cannot get conditional user properties from main thread");
            return new ArrayList<>(0);
        } else {
            AtomicReference atomicReference = new AtomicReference();
            synchronized (atomicReference) {
                zzbt zzac = this.zzl.zzac();
                zzdn zzdn = new zzdn(this, atomicReference, str, str2, str3);
                zzac.zza((Runnable) zzdn);
                try {
                    atomicReference.wait(5000);
                } catch (InterruptedException e) {
                    zzad().zzdd().zza("Interrupted waiting for get conditional user properties", str, e);
                }
            }
            List list = (List) atomicReference.get();
            if (list != null) {
                return zzgd.zzc(list);
            }
            zzad().zzdd().zza("Timed out waiting for get conditional user properties", str);
            return new ArrayList<>();
        }
    }

    public final Map<String, Object> getUserProperties(String str, String str2, boolean z) {
        zzo();
        return zzb((String) null, str, str2, z);
    }

    public final Map<String, Object> getUserPropertiesAs(String str, String str2, String str3, boolean z) {
        Preconditions.checkNotEmpty(str);
        zzn();
        return zzb(str, str2, str3, z);
    }

    @VisibleForTesting
    private final Map<String, Object> zzb(String str, String str2, String str3, boolean z) {
        if (zzac().zzef()) {
            zzad().zzda().zzaq("Cannot get user properties from analytics worker thread");
            return Collections.emptyMap();
        } else if (zzq.isMainThread()) {
            zzad().zzda().zzaq("Cannot get user properties from main thread");
            return Collections.emptyMap();
        } else {
            AtomicReference atomicReference = new AtomicReference();
            synchronized (atomicReference) {
                zzbt zzac = this.zzl.zzac();
                zzdp zzdp = new zzdp(this, atomicReference, str, str2, str3, z);
                zzac.zza((Runnable) zzdp);
                try {
                    atomicReference.wait(5000);
                } catch (InterruptedException e) {
                    zzad().zzdd().zza("Interrupted waiting for get user properties", e);
                }
            }
            List<zzga> list = (List) atomicReference.get();
            if (list == null) {
                zzad().zzdd().zzaq("Timed out waiting for get user properties");
                return Collections.emptyMap();
            }
            ArrayMap arrayMap = new ArrayMap(list.size());
            for (zzga zzga : list) {
                arrayMap.put(zzga.name, zzga.getValue());
            }
            return arrayMap;
        }
    }

    @Nullable
    public final String getCurrentScreenName() {
        zzec zzfd = this.zzl.zzv().zzfd();
        if (zzfd != null) {
            return zzfd.zzpu;
        }
        return null;
    }

    @Nullable
    public final String getCurrentScreenClass() {
        zzec zzfd = this.zzl.zzv().zzfd();
        if (zzfd != null) {
            return zzfd.zzpv;
        }
        return null;
    }

    @Nullable
    public final String getGmpAppId() {
        if (this.zzl.zzem() != null) {
            return this.zzl.zzem();
        }
        try {
            return GoogleServices.getGoogleAppId();
        } catch (IllegalStateException e) {
            this.zzl.zzad().zzda().zza("getGoogleAppId failed with exception", e);
            return null;
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
