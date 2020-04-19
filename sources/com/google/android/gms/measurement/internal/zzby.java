package com.google.android.gms.measurement.internal;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.annotation.WorkerThread;
import com.google.android.gms.common.api.internal.GoogleServices;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.Clock;
import com.google.android.gms.common.util.DefaultClock;
import com.google.android.gms.common.util.VisibleForTesting;
import com.google.android.gms.common.wrappers.Wrappers;
import com.google.android.gms.internal.measurement.zzcw;
import com.google.android.gms.internal.measurement.zzy;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class zzby implements zzcv {
    private static volatile zzby zznn;
    private final Clock zzaa;
    private boolean zzce = false;
    private final long zzdp;
    private final zzq zzfq;
    private final Context zzno;
    private final String zznp;
    private final String zznq;
    private final zzt zznr;
    private final zzbf zzns;
    private final zzau zznt;
    private final zzbt zznu;
    private final zzfj zznv;
    private final zzgd zznw;
    private final zzas zznx;
    private final zzed zzny;
    private final zzdd zznz;
    private final zza zzoa;
    private final zzdz zzob;
    private zzaq zzoc;
    private zzeg zzod;
    private zzad zzoe;
    private zzap zzof;
    private zzbl zzog;
    private Boolean zzoh;
    private long zzoi;
    private volatile Boolean zzoj;
    @VisibleForTesting
    private Boolean zzok;
    @VisibleForTesting
    private Boolean zzol;
    private int zzom;
    private AtomicInteger zzon = new AtomicInteger(0);
    private final boolean zzv;
    private final String zzx;

    private zzby(zzdc zzdc) {
        boolean z = false;
        Preconditions.checkNotNull(zzdc);
        this.zzfq = new zzq(zzdc.zzno);
        zzal.zza(this.zzfq);
        this.zzno = zzdc.zzno;
        this.zzx = zzdc.zzx;
        this.zznp = zzdc.zznp;
        this.zznq = zzdc.zznq;
        this.zzv = zzdc.zzv;
        this.zzoj = zzdc.zzoj;
        zzy zzy = zzdc.zzpe;
        if (!(zzy == null || zzy.zzy == null)) {
            Object obj = zzy.zzy.get("measurementEnabled");
            if (obj instanceof Boolean) {
                this.zzok = (Boolean) obj;
            }
            Object obj2 = zzy.zzy.get("measurementDeactivated");
            if (obj2 instanceof Boolean) {
                this.zzol = (Boolean) obj2;
            }
        }
        zzcw.zzq(this.zzno);
        this.zzaa = DefaultClock.getInstance();
        this.zzdp = this.zzaa.currentTimeMillis();
        this.zznr = new zzt(this);
        zzbf zzbf = new zzbf(this);
        zzbf.zzai();
        this.zzns = zzbf;
        zzau zzau = new zzau(this);
        zzau.zzai();
        this.zznt = zzau;
        zzgd zzgd = new zzgd(this);
        zzgd.zzai();
        this.zznw = zzgd;
        zzas zzas = new zzas(this);
        zzas.zzai();
        this.zznx = zzas;
        this.zzoa = new zza(this);
        zzed zzed = new zzed(this);
        zzed.zzai();
        this.zzny = zzed;
        zzdd zzdd = new zzdd(this);
        zzdd.zzai();
        this.zznz = zzdd;
        zzfj zzfj = new zzfj(this);
        zzfj.zzai();
        this.zznv = zzfj;
        zzdz zzdz = new zzdz(this);
        zzdz.zzai();
        this.zzob = zzdz;
        zzbt zzbt = new zzbt(this);
        zzbt.zzai();
        this.zznu = zzbt;
        if (!(zzdc.zzpe == null || zzdc.zzpe.zzu == 0)) {
            z = true;
        }
        boolean z2 = !z;
        zzq zzq = this.zzfq;
        if (this.zzno.getApplicationContext() instanceof Application) {
            zzdd zzs = zzs();
            if (zzs.getContext().getApplicationContext() instanceof Application) {
                Application application = (Application) zzs.getContext().getApplicationContext();
                if (zzs.zzpf == null) {
                    zzs.zzpf = new zzdx(zzs, null);
                }
                if (z2) {
                    application.unregisterActivityLifecycleCallbacks(zzs.zzpf);
                    application.registerActivityLifecycleCallbacks(zzs.zzpf);
                    zzs.zzad().zzdi().zzaq("Registered activity lifecycle callback");
                }
            }
        } else {
            zzad().zzdd().zzaq("Application context is not an Application");
        }
        this.zznu.zza((Runnable) new zzbz(this, zzdc));
    }

    /* access modifiers changed from: private */
    @WorkerThread
    public final void zza(zzdc zzdc) {
        String str;
        zzaw zzaw;
        zzac().zzq();
        zzt.zzbo();
        zzad zzad = new zzad(this);
        zzad.zzai();
        this.zzoe = zzad;
        zzap zzap = new zzap(this, zzdc.zzu);
        zzap.zzai();
        this.zzof = zzap;
        zzaq zzaq = new zzaq(this);
        zzaq.zzai();
        this.zzoc = zzaq;
        zzeg zzeg = new zzeg(this);
        zzeg.zzai();
        this.zzod = zzeg;
        this.zznw.zzaj();
        this.zzns.zzaj();
        this.zzog = new zzbl(this);
        this.zzof.zzaj();
        zzad().zzdg().zza("App measurement is starting up, version", Long.valueOf(this.zznr.zzav()));
        zzq zzq = this.zzfq;
        zzad().zzdg().zzaq("To enable debug logging run: adb shell setprop log.tag.FA VERBOSE");
        zzq zzq2 = this.zzfq;
        String zzan = zzap.zzan();
        if (TextUtils.isEmpty(this.zzx)) {
            if (zzab().zzbt(zzan)) {
                zzaw = zzad().zzdg();
                str = "Faster debug mode event logging enabled. To disable, run:\n  adb shell setprop debug.firebase.analytics.app .none.";
            } else {
                zzaw = zzad().zzdg();
                String str2 = "To enable faster debug mode event logging run:\n  adb shell setprop debug.firebase.analytics.app ";
                String valueOf = String.valueOf(zzan);
                str = valueOf.length() != 0 ? str2.concat(valueOf) : new String(str2);
            }
            zzaw.zzaq(str);
        }
        zzad().zzdh().zzaq("Debug-level message logging enabled");
        if (this.zzom != this.zzon.get()) {
            zzad().zzda().zza("Not all components initialized", Integer.valueOf(this.zzom), Integer.valueOf(this.zzon.get()));
        }
        this.zzce = true;
    }

    /* access modifiers changed from: protected */
    @WorkerThread
    public final void start() {
        zzac().zzq();
        if (zzae().zzlb.get() == 0) {
            zzae().zzlb.set(this.zzaa.currentTimeMillis());
        }
        if (Long.valueOf(zzae().zzlg.get()).longValue() == 0) {
            zzad().zzdi().zza("Persisting first open", Long.valueOf(this.zzdp));
            zzae().zzlg.set(this.zzdp);
        }
        if (zzet()) {
            zzq zzq = this.zzfq;
            if (!TextUtils.isEmpty(zzt().getGmpAppId()) || !TextUtils.isEmpty(zzt().zzao())) {
                zzab();
                if (zzgd.zza(zzt().getGmpAppId(), zzae().zzds(), zzt().zzao(), zzae().zzdt())) {
                    zzad().zzdg().zzaq("Rechecking which service to use due to a GMP App Id change");
                    zzae().zzdv();
                    zzw().resetAnalyticsData();
                    this.zzod.disconnect();
                    this.zzod.zzfh();
                    zzae().zzlg.set(this.zzdp);
                    zzae().zzli.zzav(null);
                }
                zzae().zzat(zzt().getGmpAppId());
                zzae().zzau(zzt().zzao());
                if (this.zznr.zzaa(zzt().zzan())) {
                    this.zznv.zzab(this.zzdp);
                }
            }
            zzs().zzbi(zzae().zzli.zzed());
            zzq zzq2 = this.zzfq;
            if (!TextUtils.isEmpty(zzt().getGmpAppId()) || !TextUtils.isEmpty(zzt().zzao())) {
                boolean isEnabled = isEnabled();
                if (!zzae().zzdz() && !this.zznr.zzbq()) {
                    zzae().zzf(!isEnabled);
                }
                if (!this.zznr.zzs(zzt().zzan()) || isEnabled) {
                    zzs().zzfb();
                }
                zzu().zza(new AtomicReference<>());
            }
        } else if (isEnabled()) {
            if (!zzab().zzbr("android.permission.INTERNET")) {
                zzad().zzda().zzaq("App is missing INTERNET permission");
            }
            if (!zzab().zzbr("android.permission.ACCESS_NETWORK_STATE")) {
                zzad().zzda().zzaq("App is missing ACCESS_NETWORK_STATE permission");
            }
            zzq zzq3 = this.zzfq;
            if (!Wrappers.packageManager(this.zzno).isCallerInstantApp() && !this.zznr.zzbw()) {
                if (!zzbo.zzl(this.zzno)) {
                    zzad().zzda().zzaq("AppMeasurementReceiver not registered/enabled");
                }
                if (!zzgd.zzb(this.zzno, false)) {
                    zzad().zzda().zzaq("AppMeasurementService not registered/enabled");
                }
            }
            zzad().zzda().zzaq("Uploading is not possible. App measurement disabled");
        }
        zzae().zzlq.set(this.zznr.zza(zzal.zziw));
        zzae().zzlr.set(this.zznr.zza(zzal.zzix));
    }

    public final zzq zzag() {
        return this.zzfq;
    }

    public final zzt zzaf() {
        return this.zznr;
    }

    public final zzbf zzae() {
        zza((zzct) this.zzns);
        return this.zzns;
    }

    public final zzau zzad() {
        zza((zzcu) this.zznt);
        return this.zznt;
    }

    public final zzau zzei() {
        zzau zzau = this.zznt;
        if (zzau == null || !zzau.isInitialized()) {
            return null;
        }
        return this.zznt;
    }

    public final zzbt zzac() {
        zza((zzcu) this.zznu);
        return this.zznu;
    }

    public final zzfj zzx() {
        zza((zzf) this.zznv);
        return this.zznv;
    }

    public final zzbl zzej() {
        return this.zzog;
    }

    /* access modifiers changed from: 0000 */
    public final zzbt zzek() {
        return this.zznu;
    }

    public final zzdd zzs() {
        zza((zzf) this.zznz);
        return this.zznz;
    }

    public final zzgd zzab() {
        zza((zzct) this.zznw);
        return this.zznw;
    }

    public final zzas zzaa() {
        zza((zzct) this.zznx);
        return this.zznx;
    }

    public final zzaq zzw() {
        zza((zzf) this.zzoc);
        return this.zzoc;
    }

    public final Context getContext() {
        return this.zzno;
    }

    public final boolean zzel() {
        return TextUtils.isEmpty(this.zzx);
    }

    public final String zzem() {
        return this.zzx;
    }

    public final String zzen() {
        return this.zznp;
    }

    public final String zzeo() {
        return this.zznq;
    }

    public final boolean zzep() {
        return this.zzv;
    }

    public final Clock zzz() {
        return this.zzaa;
    }

    public final zzed zzv() {
        zza((zzf) this.zzny);
        return this.zzny;
    }

    public final zzeg zzu() {
        zza((zzf) this.zzod);
        return this.zzod;
    }

    public final zzad zzy() {
        zza((zzcu) this.zzoe);
        return this.zzoe;
    }

    public final zzap zzt() {
        zza((zzf) this.zzof);
        return this.zzof;
    }

    public final zza zzr() {
        zza zza = this.zzoa;
        if (zza != null) {
            return zza;
        }
        throw new IllegalStateException("Component not created");
    }

    @VisibleForTesting
    public static zzby zza(Context context, String str, String str2, Bundle bundle) {
        zzy zzy = new zzy(0, 0, true, null, null, null, bundle);
        return zza(context, zzy);
    }

    public static zzby zza(Context context, zzy zzy) {
        if (zzy != null && (zzy.origin == null || zzy.zzx == null)) {
            zzy zzy2 = new zzy(zzy.zzt, zzy.zzu, zzy.zzv, zzy.zzw, null, null, zzy.zzy);
            zzy = zzy2;
        }
        Preconditions.checkNotNull(context);
        Preconditions.checkNotNull(context.getApplicationContext());
        if (zznn == null) {
            synchronized (zzby.class) {
                if (zznn == null) {
                    zznn = new zzby(new zzdc(context, zzy));
                }
            }
        } else if (!(zzy == null || zzy.zzy == null || !zzy.zzy.containsKey("dataCollectionDefaultEnabled"))) {
            zznn.zza(zzy.zzy.getBoolean("dataCollectionDefaultEnabled"));
        }
        return zznn;
    }

    private final void zzah() {
        if (!this.zzce) {
            throw new IllegalStateException("AppMeasurement is not initialized");
        }
    }

    private static void zza(zzcu zzcu) {
        if (zzcu == null) {
            throw new IllegalStateException("Component not created");
        } else if (!zzcu.isInitialized()) {
            String valueOf = String.valueOf(zzcu.getClass());
            StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 27);
            sb.append("Component not initialized: ");
            sb.append(valueOf);
            throw new IllegalStateException(sb.toString());
        }
    }

    private static void zza(zzf zzf) {
        if (zzf == null) {
            throw new IllegalStateException("Component not created");
        } else if (!zzf.isInitialized()) {
            String valueOf = String.valueOf(zzf.getClass());
            StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 27);
            sb.append("Component not initialized: ");
            sb.append(valueOf);
            throw new IllegalStateException(sb.toString());
        }
    }

    private static void zza(zzct zzct) {
        if (zzct == null) {
            throw new IllegalStateException("Component not created");
        }
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    public final void zza(boolean z) {
        this.zzoj = Boolean.valueOf(z);
    }

    @WorkerThread
    public final boolean zzeq() {
        return this.zzoj != null && this.zzoj.booleanValue();
    }

    @WorkerThread
    public final boolean isEnabled() {
        boolean z;
        zzac().zzq();
        zzah();
        if (this.zznr.zza(zzal.zzio)) {
            if (this.zznr.zzbq()) {
                return false;
            }
            Boolean bool = this.zzol;
            if (bool != null && bool.booleanValue()) {
                return false;
            }
            Boolean zzdw = zzae().zzdw();
            if (zzdw != null) {
                return zzdw.booleanValue();
            }
            Boolean zzbr = this.zznr.zzbr();
            if (zzbr != null) {
                return zzbr.booleanValue();
            }
            Boolean bool2 = this.zzok;
            if (bool2 != null) {
                return bool2.booleanValue();
            }
            if (GoogleServices.isMeasurementExplicitlyDisabled()) {
                return false;
            }
            if (!this.zznr.zza(zzal.zzik) || this.zzoj == null) {
                return true;
            }
            return this.zzoj.booleanValue();
        } else if (this.zznr.zzbq()) {
            return false;
        } else {
            Boolean zzbr2 = this.zznr.zzbr();
            if (zzbr2 != null) {
                z = zzbr2.booleanValue();
            } else {
                z = !GoogleServices.isMeasurementExplicitlyDisabled();
                if (z && this.zzoj != null && ((Boolean) zzal.zzik.get(null)).booleanValue()) {
                    z = this.zzoj.booleanValue();
                }
            }
            return zzae().zze(z);
        }
    }

    /* access modifiers changed from: 0000 */
    public final long zzer() {
        Long valueOf = Long.valueOf(zzae().zzlg.get());
        if (valueOf.longValue() == 0) {
            return this.zzdp;
        }
        return Math.min(this.zzdp, valueOf.longValue());
    }

    /* access modifiers changed from: 0000 */
    public final void zzo() {
        zzq zzq = this.zzfq;
    }

    /* access modifiers changed from: 0000 */
    public final void zzn() {
        zzq zzq = this.zzfq;
        throw new IllegalStateException("Unexpected call on client side");
    }

    /* access modifiers changed from: 0000 */
    public final void zzb(zzcu zzcu) {
        this.zzom++;
    }

    /* access modifiers changed from: 0000 */
    public final void zzb(zzf zzf) {
        this.zzom++;
    }

    /* access modifiers changed from: 0000 */
    public final void zzes() {
        this.zzon.incrementAndGet();
    }

    /* access modifiers changed from: protected */
    @WorkerThread
    public final boolean zzet() {
        zzah();
        zzac().zzq();
        Boolean bool = this.zzoh;
        if (bool == null || this.zzoi == 0 || (bool != null && !bool.booleanValue() && Math.abs(this.zzaa.elapsedRealtime() - this.zzoi) > 1000)) {
            this.zzoi = this.zzaa.elapsedRealtime();
            zzq zzq = this.zzfq;
            boolean z = true;
            this.zzoh = Boolean.valueOf(zzab().zzbr("android.permission.INTERNET") && zzab().zzbr("android.permission.ACCESS_NETWORK_STATE") && (Wrappers.packageManager(this.zzno).isCallerInstantApp() || this.zznr.zzbw() || (zzbo.zzl(this.zzno) && zzgd.zzb(this.zzno, false))));
            if (this.zzoh.booleanValue()) {
                if (!zzab().zzr(zzt().getGmpAppId(), zzt().zzao()) && TextUtils.isEmpty(zzt().zzao())) {
                    z = false;
                }
                this.zzoh = Boolean.valueOf(z);
            }
        }
        return this.zzoh.booleanValue();
    }
}
