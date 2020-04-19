package com.google.android.gms.internal.measurement;

import android.os.RemoteException;
import android.util.Log;
import com.google.android.gms.measurement.internal.zzdb;

final class zzax extends zza {
    private final /* synthetic */ zzaa zzar;
    private final /* synthetic */ zzdb zzbk;

    zzax(zzaa zzaa, zzdb zzdb) {
        this.zzar = zzaa;
        this.zzbk = zzdb;
        super(zzaa);
    }

    /* access modifiers changed from: 0000 */
    public final void zzl() throws RemoteException {
        if (this.zzar.zzad.containsKey(this.zzbk)) {
            Log.w(this.zzar.zzw, "OnEventListener already registered.");
            return;
        }
        zzc zzc = new zzc(this.zzbk);
        this.zzar.zzad.put(this.zzbk, zzc);
        this.zzar.zzan.registerOnMeasurementEventListener(zzc);
    }
}
