package com.google.android.gms.internal.measurement;

import android.app.Activity;
import android.os.RemoteException;
import com.google.android.gms.dynamic.ObjectWrapper;

final class zzbk extends zza {
    private final /* synthetic */ Activity val$activity;
    private final /* synthetic */ zzd zzbx;

    zzbk(zzd zzd, Activity activity) {
        this.zzbx = zzd;
        this.val$activity = activity;
        super(zzaa.this);
    }

    /* access modifiers changed from: 0000 */
    public final void zzl() throws RemoteException {
        zzaa.this.zzan.onActivityDestroyed(ObjectWrapper.wrap(this.val$activity), this.zzbs);
    }
}
