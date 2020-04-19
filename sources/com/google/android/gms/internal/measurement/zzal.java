package com.google.android.gms.internal.measurement;

import android.os.RemoteException;

final class zzal extends zza {
    private final /* synthetic */ zzaa zzar;
    private final /* synthetic */ String zzbb;

    zzal(zzaa zzaa, String str) {
        this.zzar = zzaa;
        this.zzbb = str;
        super(zzaa);
    }

    /* access modifiers changed from: 0000 */
    public final void zzl() throws RemoteException {
        this.zzar.zzan.endAdUnitExposure(this.zzbb, this.zzbs);
    }
}
