package com.google.android.gms.internal.measurement;

import android.os.Bundle;
import android.os.RemoteException;

final class zzac extends zza {
    private final /* synthetic */ zzaa zzar;
    private final /* synthetic */ String zzas;
    private final /* synthetic */ String zzat;
    private final /* synthetic */ Bundle zzau;

    zzac(zzaa zzaa, String str, String str2, Bundle bundle) {
        this.zzar = zzaa;
        this.zzas = str;
        this.zzat = str2;
        this.zzau = bundle;
        super(zzaa);
    }

    /* access modifiers changed from: 0000 */
    public final void zzl() throws RemoteException {
        this.zzar.zzan.clearConditionalUserProperty(this.zzas, this.zzat, this.zzau);
    }
}
