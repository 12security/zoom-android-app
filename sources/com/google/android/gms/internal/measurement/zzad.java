package com.google.android.gms.internal.measurement;

import android.os.Bundle;
import android.os.RemoteException;

final class zzad extends zza {
    private final /* synthetic */ String zzao;
    private final /* synthetic */ zzaa zzar;
    private final /* synthetic */ String zzav;
    private final /* synthetic */ zzm zzaw;

    zzad(zzaa zzaa, String str, String str2, zzm zzm) {
        this.zzar = zzaa;
        this.zzao = str;
        this.zzav = str2;
        this.zzaw = zzm;
        super(zzaa);
    }

    /* access modifiers changed from: 0000 */
    public final void zzl() throws RemoteException {
        this.zzar.zzan.getConditionalUserProperties(this.zzao, this.zzav, this.zzaw);
    }

    /* access modifiers changed from: protected */
    public final void zzm() {
        this.zzaw.zzb((Bundle) null);
    }
}
