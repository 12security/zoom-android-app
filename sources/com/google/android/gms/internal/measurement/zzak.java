package com.google.android.gms.internal.measurement;

import android.os.RemoteException;

final class zzak extends zza {
    private final /* synthetic */ zzaa zzar;
    private final /* synthetic */ String zzbb;

    zzak(zzaa zzaa, String str) {
        this.zzar = zzaa;
        this.zzbb = str;
        super(zzaa);
    }

    /* access modifiers changed from: 0000 */
    public final void zzl() throws RemoteException {
        this.zzar.zzan.beginAdUnitExposure(this.zzbb, this.zzbs);
    }
}
