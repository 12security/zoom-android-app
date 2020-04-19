package com.google.firebase.analytics;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeoutException;

final class zza implements Callable<String> {
    private final /* synthetic */ FirebaseAnalytics zzaar;

    zza(FirebaseAnalytics firebaseAnalytics) {
        this.zzaar = firebaseAnalytics;
    }

    public final /* synthetic */ Object call() throws Exception {
        String str;
        String zza = this.zzaar.zzj();
        if (zza != null) {
            return zza;
        }
        if (this.zzaar.zzn) {
            str = this.zzaar.zzaan.getAppInstanceId();
        } else {
            str = this.zzaar.zzl.zzs().zzy(120000);
        }
        if (str != null) {
            this.zzaar.zzbi(str);
            return str;
        }
        throw new TimeoutException();
    }
}
