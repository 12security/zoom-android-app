package com.google.android.gms.measurement;

import android.content.BroadcastReceiver.PendingResult;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.MainThread;
import androidx.legacy.content.WakefulBroadcastReceiver;
import com.google.android.gms.measurement.internal.zzbo;
import com.google.android.gms.measurement.internal.zzbr;

public final class AppMeasurementReceiver extends WakefulBroadcastReceiver implements zzbr {
    private zzbo zzo;

    @MainThread
    public final void onReceive(Context context, Intent intent) {
        if (this.zzo == null) {
            this.zzo = new zzbo(this);
        }
        this.zzo.onReceive(context, intent);
    }

    @MainThread
    public final void doStartService(Context context, Intent intent) {
        startWakefulService(context, intent);
    }

    public final PendingResult doGoAsync() {
        return goAsync();
    }
}
