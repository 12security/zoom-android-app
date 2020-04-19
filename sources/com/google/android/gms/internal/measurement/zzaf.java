package com.google.android.gms.internal.measurement;

import android.app.Activity;
import android.os.RemoteException;
import com.google.android.gms.dynamic.ObjectWrapper;

final class zzaf extends zza {
    private final /* synthetic */ Activity val$activity;
    private final /* synthetic */ zzaa zzar;
    private final /* synthetic */ String zzax;
    private final /* synthetic */ String zzay;

    zzaf(zzaa zzaa, Activity activity, String str, String str2) {
        this.zzar = zzaa;
        this.val$activity = activity;
        this.zzax = str;
        this.zzay = str2;
        super(zzaa);
    }

    /* access modifiers changed from: 0000 */
    public final void zzl() throws RemoteException {
        this.zzar.zzan.setCurrentScreen(ObjectWrapper.wrap(this.val$activity), this.zzax, this.zzay, this.timestamp);
    }
}
