package com.google.android.gms.internal.measurement;

import android.os.Bundle;
import android.os.RemoteException;

final class zzau extends zza {
    private final /* synthetic */ zzaa zzar;
    private final /* synthetic */ zzm zzaw;
    private final /* synthetic */ Bundle zzbj;

    zzau(zzaa zzaa, Bundle bundle, zzm zzm) {
        this.zzar = zzaa;
        this.zzbj = bundle;
        this.zzaw = zzm;
        super(zzaa);
    }

    /* access modifiers changed from: 0000 */
    public final void zzl() throws RemoteException {
        this.zzar.zzan.performAction(this.zzbj, this.zzaw, this.timestamp);
    }

    /* access modifiers changed from: protected */
    public final void zzm() {
        this.zzaw.zzb((Bundle) null);
    }
}
