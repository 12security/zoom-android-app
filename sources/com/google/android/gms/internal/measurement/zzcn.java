package com.google.android.gms.internal.measurement;

import android.database.ContentObserver;
import android.os.Handler;

final class zzcn extends ContentObserver {
    private final /* synthetic */ zzcl zzzp;

    zzcn(zzcl zzcl, Handler handler) {
        this.zzzp = zzcl;
        super(null);
    }

    public final void onChange(boolean z) {
        this.zzzp.zzjk();
    }
}
