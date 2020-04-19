package com.google.android.gms.internal.measurement;

import android.os.RemoteException;

final class zzaj extends zza {
    private final /* synthetic */ zzaa zzar;
    private final /* synthetic */ long zzba;

    zzaj(zzaa zzaa, long j) {
        this.zzar = zzaa;
        this.zzba = j;
        super(zzaa);
    }

    /* access modifiers changed from: 0000 */
    public final void zzl() throws RemoteException {
        this.zzar.zzan.setSessionTimeoutDuration(this.zzba);
    }
}
