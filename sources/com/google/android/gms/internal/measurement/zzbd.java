package com.google.android.gms.internal.measurement;

import android.os.Bundle;
import android.os.RemoteException;

final class zzbd extends zza {
    private final /* synthetic */ zzaa zzar;
    private final /* synthetic */ Bundle zzbj;

    zzbd(zzaa zzaa, Bundle bundle) {
        this.zzar = zzaa;
        this.zzbj = bundle;
        super(zzaa);
    }

    /* access modifiers changed from: 0000 */
    public final void zzl() throws RemoteException {
        this.zzar.zzan.setConditionalUserProperty(this.zzbj, this.timestamp);
    }
}
