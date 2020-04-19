package com.google.android.gms.internal.measurement;

import android.os.Bundle;
import android.os.RemoteException;

final class zzav extends zza {
    private final /* synthetic */ String zzao;
    private final /* synthetic */ zzaa zzar;
    private final /* synthetic */ zzm zzaw;

    zzav(zzaa zzaa, String str, zzm zzm) {
        this.zzar = zzaa;
        this.zzao = str;
        this.zzaw = zzm;
        super(zzaa);
    }

    /* access modifiers changed from: 0000 */
    public final void zzl() throws RemoteException {
        this.zzar.zzan.getMaxUserProperties(this.zzao, this.zzaw);
    }

    /* access modifiers changed from: protected */
    public final void zzm() {
        this.zzaw.zzb((Bundle) null);
    }
}
