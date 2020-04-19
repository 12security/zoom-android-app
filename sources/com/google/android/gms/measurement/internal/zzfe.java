package com.google.android.gms.measurement.internal;

import android.annotation.TargetApi;
import android.app.job.JobParameters;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.MainThread;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.internal.measurement.zzy;
import com.google.android.gms.measurement.internal.zzfi;
import com.zipow.videobox.view.p014mm.MMContentFileViewerFragment;

public final class zzfe<T extends Context & zzfi> {
    private final T zzrb;

    public zzfe(T t) {
        Preconditions.checkNotNull(t);
        this.zzrb = t;
    }

    @MainThread
    public final void onCreate() {
        zzby zza = zzby.zza((Context) this.zzrb, (zzy) null);
        zzau zzad = zza.zzad();
        zza.zzag();
        zzad.zzdi().zzaq("Local AppMeasurementService is starting up");
    }

    @MainThread
    public final void onDestroy() {
        zzby zza = zzby.zza((Context) this.zzrb, (zzy) null);
        zzau zzad = zza.zzad();
        zza.zzag();
        zzad.zzdi().zzaq("Local AppMeasurementService is shutting down");
    }

    @MainThread
    public final int onStartCommand(Intent intent, int i, int i2) {
        zzby zza = zzby.zza((Context) this.zzrb, (zzy) null);
        zzau zzad = zza.zzad();
        if (intent == null) {
            zzad.zzdd().zzaq("AppMeasurementService started with null intent");
            return 2;
        }
        String action = intent.getAction();
        zza.zzag();
        zzad.zzdi().zza("Local AppMeasurementService called. startId, action", Integer.valueOf(i2), action);
        if ("com.google.android.gms.measurement.UPLOAD".equals(action)) {
            zze(new zzff(this, i2, zzad, intent));
        }
        return 2;
    }

    private final void zze(Runnable runnable) {
        zzft zzm = zzft.zzm(this.zzrb);
        zzm.zzac().zza((Runnable) new zzfh(this, zzm, runnable));
    }

    @MainThread
    public final IBinder onBind(Intent intent) {
        if (intent == null) {
            zzad().zzda().zzaq("onBind called with null intent");
            return null;
        }
        String action = intent.getAction();
        if ("com.google.android.gms.measurement.START".equals(action)) {
            return new zzca(zzft.zzm(this.zzrb));
        }
        zzad().zzdd().zza("onBind received unknown action", action);
        return null;
    }

    @MainThread
    public final boolean onUnbind(Intent intent) {
        if (intent == null) {
            zzad().zzda().zzaq("onUnbind called with null intent");
            return true;
        }
        zzad().zzdi().zza("onUnbind called for intent. action", intent.getAction());
        return true;
    }

    @TargetApi(24)
    @MainThread
    public final boolean onStartJob(JobParameters jobParameters) {
        zzby zza = zzby.zza((Context) this.zzrb, (zzy) null);
        zzau zzad = zza.zzad();
        String string = jobParameters.getExtras().getString(MMContentFileViewerFragment.RESULT_ACTION);
        zza.zzag();
        zzad.zzdi().zza("Local AppMeasurementJobService called. action", string);
        if ("com.google.android.gms.measurement.UPLOAD".equals(string)) {
            zze(new zzfg(this, zzad, jobParameters));
        }
        return true;
    }

    @MainThread
    public final void onRebind(Intent intent) {
        if (intent == null) {
            zzad().zzda().zzaq("onRebind called with null intent");
            return;
        }
        zzad().zzdi().zza("onRebind called. action", intent.getAction());
    }

    private final zzau zzad() {
        return zzby.zza((Context) this.zzrb, (zzy) null).zzad();
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void zza(zzau zzau, JobParameters jobParameters) {
        zzau.zzdi().zzaq("AppMeasurementJobService processed last upload request.");
        ((zzfi) this.zzrb).zza(jobParameters, false);
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void zza(int i, zzau zzau, Intent intent) {
        if (((zzfi) this.zzrb).zza(i)) {
            zzau.zzdi().zza("Local AppMeasurementService processed last upload request. StartId", Integer.valueOf(i));
            zzad().zzdi().zzaq("Completed wakeful intent.");
            ((zzfi) this.zzrb).zza(intent);
        }
    }
}
