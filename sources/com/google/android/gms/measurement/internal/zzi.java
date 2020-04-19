package com.google.android.gms.measurement.internal;

import com.google.android.gms.internal.measurement.zzq;

final class zzi implements Runnable {
    private final /* synthetic */ zzq zzdh;
    private final /* synthetic */ AppMeasurementDynamiteService zzdi;
    private final /* synthetic */ zzaj zzdj;
    private final /* synthetic */ String zzdk;

    zzi(AppMeasurementDynamiteService appMeasurementDynamiteService, zzq zzq, zzaj zzaj, String str) {
        this.zzdi = appMeasurementDynamiteService;
        this.zzdh = zzq;
        this.zzdj = zzaj;
        this.zzdk = str;
    }

    public final void run() {
        this.zzdi.zzl.zzu().zza(this.zzdh, this.zzdj, this.zzdk);
    }
}
