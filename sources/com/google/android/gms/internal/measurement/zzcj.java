package com.google.android.gms.internal.measurement;

import android.database.ContentObserver;
import android.os.Handler;

final class zzcj extends ContentObserver {
    zzcj(Handler handler) {
        super(null);
    }

    public final void onChange(boolean z) {
        zzci.zzyx.set(true);
    }
}
