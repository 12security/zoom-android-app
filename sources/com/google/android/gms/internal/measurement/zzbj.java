package com.google.android.gms.internal.measurement;

import android.app.Activity;
import android.os.RemoteException;
import com.google.android.gms.dynamic.ObjectWrapper;

final class zzbj extends zza {
    private final /* synthetic */ Activity val$activity;
    private final /* synthetic */ zzm zzaw;
    private final /* synthetic */ zzd zzbx;

    zzbj(zzd zzd, Activity activity, zzm zzm) {
        this.zzbx = zzd;
        this.val$activity = activity;
        this.zzaw = zzm;
        super(zzaa.this);
    }

    /* access modifiers changed from: 0000 */
    public final void zzl() throws RemoteException {
        zzaa.this.zzan.onActivitySaveInstanceState(ObjectWrapper.wrap(this.val$activity), this.zzaw, this.zzbs);
    }
}
