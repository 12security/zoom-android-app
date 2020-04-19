package com.google.android.gms.measurement;

import android.app.Service;
import android.app.job.JobParameters;
import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.MainThread;
import com.google.android.gms.measurement.internal.zzfe;
import com.google.android.gms.measurement.internal.zzfi;

public final class AppMeasurementService extends Service implements zzfi {
    private zzfe<AppMeasurementService> zzp;

    private final zzfe<AppMeasurementService> zzg() {
        if (this.zzp == null) {
            this.zzp = new zzfe<>(this);
        }
        return this.zzp;
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

    @MainThread
    public final int onStartCommand(Intent intent, int i, int i2) {
        return zzg().onStartCommand(intent, i, i2);
    }

    @MainThread
    public final IBinder onBind(Intent intent) {
        return zzg().onBind(intent);
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
        return stopSelfResult(i);
    }

    public final void zza(JobParameters jobParameters, boolean z) {
        throw new UnsupportedOperationException();
    }

    public final void zza(Intent intent) {
        AppMeasurementReceiver.completeWakefulIntent(intent);
    }
}
