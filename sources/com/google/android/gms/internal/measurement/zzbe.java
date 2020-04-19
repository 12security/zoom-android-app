package com.google.android.gms.internal.measurement;

import android.app.Activity;
import android.os.Bundle;
import android.os.RemoteException;
import com.google.android.gms.dynamic.ObjectWrapper;

final class zzbe extends zza {
    private final /* synthetic */ Activity val$activity;
    private final /* synthetic */ Bundle zzbw;
    private final /* synthetic */ zzd zzbx;

    zzbe(zzd zzd, Activity activity, Bundle bundle) {
        this.zzbx = zzd;
        this.val$activity = activity;
        this.zzbw = bundle;
        super(zzaa.this);
    }

    /* access modifiers changed from: 0000 */
    public final void zzl() throws RemoteException {
        zzaa.this.zzan.onActivityCreated(ObjectWrapper.wrap(this.val$activity), this.zzbw, this.zzbs);
    }
}
