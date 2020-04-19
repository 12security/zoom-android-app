package com.google.android.gms.internal.measurement;

import android.os.RemoteException;

final class zzaz extends zza {
    private final /* synthetic */ zzaa zzar;
    private final /* synthetic */ boolean zzaz;

    zzaz(zzaa zzaa, boolean z) {
        this.zzar = zzaa;
        this.zzaz = z;
        super(zzaa);
    }

    /* access modifiers changed from: 0000 */
    public final void zzl() throws RemoteException {
        this.zzar.zzan.setDataCollectionEnabled(this.zzaz);
    }
}
