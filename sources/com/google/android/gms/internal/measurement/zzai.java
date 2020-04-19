package com.google.android.gms.internal.measurement;

import android.os.RemoteException;

final class zzai extends zza {
    private final /* synthetic */ zzaa zzar;
    private final /* synthetic */ long zzba;

    zzai(zzaa zzaa, long j) {
        this.zzar = zzaa;
        this.zzba = j;
        super(zzaa);
    }

    /* access modifiers changed from: 0000 */
    public final void zzl() throws RemoteException {
        this.zzar.zzan.setMinimumSessionDuration(this.zzba);
    }
}
