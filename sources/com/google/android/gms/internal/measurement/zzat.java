package com.google.android.gms.internal.measurement;

import android.os.RemoteException;
import com.google.android.gms.dynamic.ObjectWrapper;

final class zzat extends zza {
    private final /* synthetic */ zzaa zzar;
    private final /* synthetic */ int zzbe = 5;
    private final /* synthetic */ String zzbf;
    private final /* synthetic */ Object zzbg;
    private final /* synthetic */ Object zzbh;
    private final /* synthetic */ Object zzbi;

    zzat(zzaa zzaa, boolean z, int i, String str, Object obj, Object obj2, Object obj3) {
        this.zzar = zzaa;
        this.zzbf = str;
        this.zzbg = obj;
        this.zzbh = null;
        this.zzbi = null;
        super(false);
    }

    /* access modifiers changed from: 0000 */
    public final void zzl() throws RemoteException {
        this.zzar.zzan.logHealthData(this.zzbe, this.zzbf, ObjectWrapper.wrap(this.zzbg), ObjectWrapper.wrap(this.zzbh), ObjectWrapper.wrap(this.zzbi));
    }
}
