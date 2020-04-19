package com.google.android.gms.internal.measurement;

import android.os.Bundle;
import android.os.RemoteException;

final class zzap extends zza {
    private final /* synthetic */ zzaa zzar;
    private final /* synthetic */ zzm zzaw;

    zzap(zzaa zzaa, zzm zzm) {
        this.zzar = zzaa;
        this.zzaw = zzm;
        super(zzaa);
    }

    /* access modifiers changed from: 0000 */
    public final void zzl() throws RemoteException {
        this.zzar.zzan.generateEventId(this.zzaw);
    }

    /* access modifiers changed from: protected */
    public final void zzm() {
        this.zzaw.zzb((Bundle) null);
    }
}
