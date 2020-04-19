package com.google.android.gms.internal.measurement;

import android.os.Bundle;
import android.os.RemoteException;

final class zzay extends zza {
    private final /* synthetic */ zzaa zzar;
    private final /* synthetic */ zzm zzaw;
    private final /* synthetic */ int zzbl;

    zzay(zzaa zzaa, zzm zzm, int i) {
        this.zzar = zzaa;
        this.zzaw = zzm;
        this.zzbl = i;
        super(zzaa);
    }

    /* access modifiers changed from: 0000 */
    public final void zzl() throws RemoteException {
        this.zzar.zzan.getTestFlag(this.zzaw, this.zzbl);
    }

    /* access modifiers changed from: protected */
    public final void zzm() {
        this.zzaw.zzb((Bundle) null);
    }
}
