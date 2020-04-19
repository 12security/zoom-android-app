package com.google.android.gms.internal.measurement;

import android.os.RemoteException;

final class zzah extends zza {
    private final /* synthetic */ zzaa zzar;

    zzah(zzaa zzaa) {
        this.zzar = zzaa;
        super(zzaa);
    }

    /* access modifiers changed from: 0000 */
    public final void zzl() throws RemoteException {
        this.zzar.zzan.resetAnalyticsData(this.timestamp);
    }
}
