package com.google.android.gms.internal.measurement;

import android.os.RemoteException;
import android.util.Log;
import com.google.android.gms.measurement.internal.zzdb;

final class zzba extends zza {
    private final /* synthetic */ zzaa zzar;
    private final /* synthetic */ zzdb zzbk;

    zzba(zzaa zzaa, zzdb zzdb) {
        this.zzar = zzaa;
        this.zzbk = zzdb;
        super(zzaa);
    }

    /* access modifiers changed from: 0000 */
    public final void zzl() throws RemoteException {
        zzc zzc = (zzc) this.zzar.zzad.get(this.zzbk);
        if (zzc == null) {
            Log.w(this.zzar.zzw, "OnEventListener had not been registered.");
            return;
        }
        this.zzar.zzan.unregisterOnMeasurementEventListener(zzc);
        this.zzar.zzad.remove(this.zzbk);
    }
}
