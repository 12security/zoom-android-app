package com.google.android.gms.internal.measurement;

import android.os.RemoteException;
import com.google.android.gms.dynamic.ObjectWrapper;

final class zzbc extends zza {
    private final /* synthetic */ String zzao;
    private final /* synthetic */ zzaa zzar;
    private final /* synthetic */ String zzbn;
    private final /* synthetic */ boolean zzbp;
    private final /* synthetic */ Object zzbr;

    zzbc(zzaa zzaa, String str, String str2, Object obj, boolean z) {
        this.zzar = zzaa;
        this.zzao = str;
        this.zzbn = str2;
        this.zzbr = obj;
        this.zzbp = z;
        super(zzaa);
    }

    /* access modifiers changed from: 0000 */
    public final void zzl() throws RemoteException {
        this.zzar.zzan.setUserProperty(this.zzao, this.zzbn, ObjectWrapper.wrap(this.zzbr), this.zzbp, this.timestamp);
    }
}
