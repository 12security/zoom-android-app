package com.google.android.gms.measurement;

import android.annotation.TargetApi;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import androidx.annotation.MainThread;
import com.google.android.gms.measurement.internal.zzfe;
import com.google.android.gms.measurement.internal.zzfi;

@TargetApi(24)
public final class AppMeasurementJobService extends JobService implements zzfi {
    private zzfe<AppMeasurementJobService> zzp;

    private final zzfe<AppMeasurementJobService> zzg() {
        if (this.zzp == null) {
            this.zzp = new zzfe<>(this);
        }
        return this.zzp;
    }

    public final boolean onStopJob(JobParameters jobParameters) {
        return false;
    }

    public final void zza(Intent intent) {
    }

    @MainThread
    public final void onCreate() {
        super.onCreate();
        zzg().onCreate();
    }

    @MainThread
    public final void onDestroy() {
        zzg().onDestroy();
        super.onDestroy();
    }

    public final boolean onStartJob(JobParameters jobParameters) {
        return zzg().onStartJob(jobParameters);
    }

    @MainThread
    public final boolean onUnbind(Intent intent) {
        return zzg().onUnbind(intent);
    }

    @MainThread
    public final void onRebind(Intent intent) {
        zzg().onRebind(intent);
    }

    public final boolean zza(int i) {
        throw new UnsupportedOperationException();
    }

    @TargetApi(24)
    public final void zza(JobParameters jobParameters, boolean z) {
        jobFinished(jobParameters, false);
    }
}
