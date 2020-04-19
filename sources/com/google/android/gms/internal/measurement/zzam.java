package com.google.android.gms.internal.measurement;

import android.os.RemoteException;
import com.google.android.gms.measurement.internal.zzda;

final class zzam extends zza {
    private final /* synthetic */ zzaa zzar;
    private final /* synthetic */ zzda zzbc;

    zzam(zzaa zzaa, zzda zzda) {
        this.zzar = zzaa;
        this.zzbc = zzda;
        super(zzaa);
    }

    /* access modifiers changed from: 0000 */
    public final void zzl() throws RemoteException {
        this.zzar.zzan.setEventInterceptor(new zzb(this.zzbc));
    }
}
