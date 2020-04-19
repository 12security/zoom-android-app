package com.google.android.gms.measurement.internal;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.util.Pair;
import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.ads.identifier.AdvertisingIdClient.Info;
import com.google.android.gms.common.util.VisibleForTesting;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Locale;

final class zzbf extends zzcu {
    @VisibleForTesting
    static final Pair<String, Long> zzky = new Pair<>("", Long.valueOf(0));
    private SharedPreferences zzkz;
    public zzbj zzla;
    public final zzbi zzlb = new zzbi(this, "last_upload", 0);
    public final zzbi zzlc = new zzbi(this, "last_upload_attempt", 0);
    public final zzbi zzld = new zzbi(this, "backoff", 0);
    public final zzbi zzle = new zzbi(this, "last_delete_stale", 0);
    public final zzbi zzlf = new zzbi(this, "midnight_offset", 0);
    public final zzbi zzlg = new zzbi(this, "first_open_time", 0);
    public final zzbi zzlh = new zzbi(this, "app_install_time", 0);
    public final zzbk zzli = new zzbk(this, "app_instance_id", null);
    private String zzlj;
    private boolean zzlk;
    private long zzll;
    public final zzbi zzlm = new zzbi(this, "time_before_start", 10000);
    public final zzbi zzln = new zzbi(this, "session_timeout", 1800000);
    public final zzbh zzlo = new zzbh(this, "start_new_session", true);
    public final zzbk zzlp = new zzbk(this, "non_personalized_ads", null);
    public final zzbh zzlq = new zzbh(this, "use_dynamite_api", false);
    public final zzbh zzlr = new zzbh(this, "allow_remote_dynamite", false);
    public final zzbi zzls = new zzbi(this, "last_pause_time", 0);
    public final zzbi zzlt = new zzbi(this, "time_active", 0);
    public boolean zzlu;
    public zzbh zzlv = new zzbh(this, "app_backgrounded", false);

    /* access modifiers changed from: protected */
    public final boolean zzak() {
        return true;
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    @NonNull
    public final Pair<String, Boolean> zzar(String str) {
        zzq();
        long elapsedRealtime = zzz().elapsedRealtime();
        String str2 = this.zzlj;
        if (str2 != null && elapsedRealtime < this.zzll) {
            return new Pair<>(str2, Boolean.valueOf(this.zzlk));
        }
        this.zzll = elapsedRealtime + zzaf().zza(str, zzal.zzge);
        AdvertisingIdClient.setShouldSkipGmsCoreVersionCheck(true);
        try {
            Info advertisingIdInfo = AdvertisingIdClient.getAdvertisingIdInfo(getContext());
            if (advertisingIdInfo != null) {
                this.zzlj = advertisingIdInfo.getId();
                this.zzlk = advertisingIdInfo.isLimitAdTrackingEnabled();
            }
            if (this.zzlj == null) {
                this.zzlj = "";
            }
        } catch (Exception e) {
            zzad().zzdh().zza("Unable to get advertising id", e);
            this.zzlj = "";
        }
        AdvertisingIdClient.setShouldSkipGmsCoreVersionCheck(false);
        return new Pair<>(this.zzlj, Boolean.valueOf(this.zzlk));
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    public final String zzas(String str) {
        zzq();
        String str2 = (String) zzar(str).first;
        MessageDigest messageDigest = zzgd.getMessageDigest();
        if (messageDigest == null) {
            return null;
        }
        return String.format(Locale.US, "%032X", new Object[]{new BigInteger(1, messageDigest.digest(str2.getBytes()))});
    }

    zzbf(zzby zzby) {
        super(zzby);
    }

    /* access modifiers changed from: protected */
    @WorkerThread
    public final void zzal() {
        this.zzkz = getContext().getSharedPreferences("com.google.android.gms.measurement.prefs", 0);
        this.zzlu = this.zzkz.getBoolean("has_been_opened", false);
        if (!this.zzlu) {
            Editor edit = this.zzkz.edit();
            edit.putBoolean("has_been_opened", true);
            edit.apply();
        }
        zzbj zzbj = new zzbj(this, "health_monitor", Math.max(0, ((Long) zzal.zzgf.get(null)).longValue()));
        this.zzla = zzbj;
    }

    /* access modifiers changed from: private */
    @WorkerThread
    public final SharedPreferences zzdr() {
        zzq();
        zzah();
        return this.zzkz;
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    public final void zzat(String str) {
        zzq();
        Editor edit = zzdr().edit();
        edit.putString("gmp_app_id", str);
        edit.apply();
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    public final String zzds() {
        zzq();
        return zzdr().getString("gmp_app_id", null);
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    public final void zzau(String str) {
        zzq();
        Editor edit = zzdr().edit();
        edit.putString("admob_app_id", str);
        edit.apply();
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    public final String zzdt() {
        zzq();
        return zzdr().getString("admob_app_id", null);
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    public final Boolean zzdu() {
        zzq();
        if (!zzdr().contains("use_service")) {
            return null;
        }
        return Boolean.valueOf(zzdr().getBoolean("use_service", false));
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    public final void zzd(boolean z) {
        zzq();
        zzad().zzdi().zza("Setting useService", Boolean.valueOf(z));
        Editor edit = zzdr().edit();
        edit.putBoolean("use_service", z);
        edit.apply();
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    public final void zzdv() {
        zzq();
        zzad().zzdi().zzaq("Clearing collection preferences.");
        if (zzaf().zza(zzal.zzio)) {
            Boolean zzdw = zzdw();
            Editor edit = zzdr().edit();
            edit.clear();
            edit.apply();
            if (zzdw != null) {
                setMeasurementEnabled(zzdw.booleanValue());
            }
            return;
        }
        boolean contains = zzdr().contains("measurement_enabled");
        boolean z = true;
        if (contains) {
            z = zze(true);
        }
        Editor edit2 = zzdr().edit();
        edit2.clear();
        edit2.apply();
        if (contains) {
            setMeasurementEnabled(z);
        }
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    public final void setMeasurementEnabled(boolean z) {
        zzq();
        zzad().zzdi().zza("Setting measurementEnabled", Boolean.valueOf(z));
        Editor edit = zzdr().edit();
        edit.putBoolean("measurement_enabled", z);
        edit.apply();
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    public final boolean zze(boolean z) {
        zzq();
        return zzdr().getBoolean("measurement_enabled", z);
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    public final Boolean zzdw() {
        zzq();
        if (zzdr().contains("measurement_enabled")) {
            return Boolean.valueOf(zzdr().getBoolean("measurement_enabled", true));
        }
        return null;
    }

    /* access modifiers changed from: protected */
    @WorkerThread
    public final String zzdx() {
        zzq();
        String string = zzdr().getString("previous_os_version", null);
        zzy().zzah();
        String str = VERSION.RELEASE;
        if (!TextUtils.isEmpty(str) && !str.equals(string)) {
            Editor edit = zzdr().edit();
            edit.putString("previous_os_version", str);
            edit.apply();
        }
        return string;
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    public final void zzf(boolean z) {
        zzq();
        zzad().zzdi().zza("Updating deferred analytics collection", Boolean.valueOf(z));
        Editor edit = zzdr().edit();
        edit.putBoolean("deferred_analytics_collection", z);
        edit.apply();
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    public final boolean zzdy() {
        zzq();
        return zzdr().getBoolean("deferred_analytics_collection", false);
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    public final boolean zzdz() {
        return this.zzkz.contains("deferred_analytics_collection");
    }

    /* access modifiers changed from: 0000 */
    public final boolean zzx(long j) {
        return j - this.zzln.get() > this.zzls.get();
    }
}
