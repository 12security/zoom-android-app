package com.google.android.gms.measurement.internal;

import java.util.concurrent.Callable;

final class zzcn implements Callable<byte[]> {
    private final /* synthetic */ zzaj zzdj;
    private final /* synthetic */ String zzdk;
    private final /* synthetic */ zzca zzot;

    zzcn(zzca zzca, zzaj zzaj, String str) {
        this.zzot = zzca;
        this.zzdj = zzaj;
        this.zzdk = str;
    }

    public final /* synthetic */ Object call() throws Exception {
        this.zzot.zzkt.zzgf();
        return this.zzot.zzkt.zzfx().zzb(this.zzdj, this.zzdk);
    }
}
