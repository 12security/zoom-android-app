package com.google.android.gms.measurement.internal;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application.ActivityLifecycleCallbacks;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.annotation.MainThread;

@TargetApi(14)
@MainThread
final class zzdx implements ActivityLifecycleCallbacks {
    private final /* synthetic */ zzdd zzpm;

    private zzdx(zzdd zzdd) {
        this.zzpm = zzdd;
    }

    public final void onActivityStarted(Activity activity) {
    }

    public final void onActivityStopped(Activity activity) {
    }

    public final void onActivityCreated(Activity activity, Bundle bundle) {
        try {
            this.zzpm.zzad().zzdi().zzaq("onActivityCreated");
            Intent intent = activity.getIntent();
            if (intent != null) {
                Uri data = intent.getData();
                if (data != null && data.isHierarchical()) {
                    if (bundle == null) {
                        Bundle zza = this.zzpm.zzab().zza(data);
                        this.zzpm.zzab();
                        String str = zzgd.zzc(intent) ? "gs" : "auto";
                        if (zza != null) {
                            this.zzpm.logEvent(str, "_cmp", zza);
                        }
                    }
                    String queryParameter = data.getQueryParameter("referrer");
                    if (!TextUtils.isEmpty(queryParameter)) {
                        if (!(queryParameter.contains("gclid") && (queryParameter.contains("utm_campaign") || queryParameter.contains("utm_source") || queryParameter.contains("utm_medium") || queryParameter.contains("utm_term") || queryParameter.contains("utm_content")))) {
                            this.zzpm.zzad().zzdh().zzaq("Activity created with data 'referrer' param without gclid and at least one utm field");
                            return;
                        }
                        this.zzpm.zzad().zzdh().zza("Activity created with referrer", queryParameter);
                        if (!TextUtils.isEmpty(queryParameter)) {
                            this.zzpm.zzb("auto", "_ldl", (Object) queryParameter, true);
                        }
                    } else {
                        return;
                    }
                }
            }
        } catch (Exception e) {
            this.zzpm.zzad().zzda().zza("Throwable caught in onActivityCreated", e);
        }
        this.zzpm.zzv().onActivityCreated(activity, bundle);
    }

    public final void onActivityDestroyed(Activity activity) {
        this.zzpm.zzv().onActivityDestroyed(activity);
    }

    @MainThread
    public final void onActivityPaused(Activity activity) {
        this.zzpm.zzv().onActivityPaused(activity);
        zzfj zzx = this.zzpm.zzx();
        zzx.zzac().zza((Runnable) new zzfn(zzx, zzx.zzz().elapsedRealtime()));
    }

    @MainThread
    public final void onActivityResumed(Activity activity) {
        this.zzpm.zzv().onActivityResumed(activity);
        zzfj zzx = this.zzpm.zzx();
        zzx.zzac().zza((Runnable) new zzfm(zzx, zzx.zzz().elapsedRealtime()));
    }

    public final void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        this.zzpm.zzv().onActivitySaveInstanceState(activity, bundle);
    }

    /* synthetic */ zzdx(zzdd zzdd, zzde zzde) {
        this(zzdd);
    }
}
