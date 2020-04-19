package com.google.android.gms.measurement.internal;

import com.google.android.gms.internal.measurement.zzq;

final class zzh implements Runnable {
    private final /* synthetic */ zzq zzdh;
    private final /* synthetic */ AppMeasurementDynamiteService zzdi;

    zzh(AppMeasurementDynamiteService appMeasurementDynamiteService, zzq zzq) {
        this.zzdi = appMeasurementDynamiteService;
        this.zzdh = zzq;
    }

    public final void run() {
        this.zzdi.zzl.zzu().getAppInstanceId(this.zzdh);
    }
}
