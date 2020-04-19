package com.google.android.gms.internal.measurement;

import android.os.RemoteException;

final class zzae extends zza {
    private final /* synthetic */ String val$id;
    private final /* synthetic */ zzaa zzar;

    zzae(zzaa zzaa, String str) {
        this.zzar = zzaa;
        this.val$id = str;
        super(zzaa);
    }

    /* access modifiers changed from: 0000 */
    public final void zzl() throws RemoteException {
        this.zzar.zzan.setUserId(this.val$id, this.timestamp);
    }
}
