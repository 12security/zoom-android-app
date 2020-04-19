package com.google.android.gms.measurement.internal;

import java.util.List;
import java.util.concurrent.Callable;

final class zzcq implements Callable<List<zzgc>> {
    private final /* synthetic */ zzm zzos;
    private final /* synthetic */ zzca zzot;

    zzcq(zzca zzca, zzm zzm) {
        this.zzot = zzca;
        this.zzos = zzm;
    }

    public final /* synthetic */ Object call() throws Exception {
        this.zzot.zzkt.zzgf();
        return this.zzot.zzkt.zzdo().zzad(this.zzos.packageName);
    }
}
