package com.google.android.gms.measurement.internal;

import java.util.concurrent.Callable;

final class zzfx implements Callable<String> {
    private final /* synthetic */ zzm zzos;
    private final /* synthetic */ zzft zzsr;

    zzfx(zzft zzft, zzm zzm) {
        this.zzsr = zzft;
        this.zzos = zzm;
    }

    public final /* synthetic */ Object call() throws Exception {
        zzg zzg;
        if (this.zzsr.zzaf().zzs(this.zzos.packageName)) {
            zzg = this.zzsr.zzg(this.zzos);
        } else {
            zzg = this.zzsr.zzdo().zzae(this.zzos.packageName);
        }
        if (zzg != null) {
            return zzg.getAppInstanceId();
        }
        this.zzsr.zzad().zzdd().zzaq("App info was null when attempting to get app instance id");
        return null;
    }
}
