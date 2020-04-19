package com.google.android.gms.measurement.internal;

import java.util.List;
import java.util.concurrent.Callable;

final class zzci implements Callable<List<zzr>> {
    private final /* synthetic */ String zzao;
    private final /* synthetic */ String zzav;
    private final /* synthetic */ zzm zzos;
    private final /* synthetic */ zzca zzot;

    zzci(zzca zzca, zzm zzm, String str, String str2) {
        this.zzot = zzca;
        this.zzos = zzm;
        this.zzao = str;
        this.zzav = str2;
    }

    public final /* synthetic */ Object call() throws Exception {
        this.zzot.zzkt.zzgf();
        return this.zzot.zzkt.zzdo().zzb(this.zzos.packageName, this.zzao, this.zzav);
    }
}
