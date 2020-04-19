package com.google.android.gms.internal.measurement;

import android.os.Bundle;
import android.os.RemoteException;

final class zzbb extends zza {
    private final /* synthetic */ String zzao;
    private final /* synthetic */ zzaa zzar;
    private final /* synthetic */ Long zzbm;
    private final /* synthetic */ String zzbn;
    private final /* synthetic */ Bundle zzbo;
    private final /* synthetic */ boolean zzbp;
    private final /* synthetic */ boolean zzbq;

    zzbb(zzaa zzaa, Long l, String str, String str2, Bundle bundle, boolean z, boolean z2) {
        this.zzar = zzaa;
        this.zzbm = l;
        this.zzao = str;
        this.zzbn = str2;
        this.zzbo = bundle;
        this.zzbp = z;
        this.zzbq = z2;
        super(zzaa);
    }

    /* access modifiers changed from: 0000 */
    public final void zzl() throws RemoteException {
        Long l = this.zzbm;
        this.zzar.zzan.logEvent(this.zzao, this.zzbn, this.zzbo, this.zzbp, this.zzbq, l == null ? this.timestamp : l.longValue());
    }
}
