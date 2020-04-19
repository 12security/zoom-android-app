package com.google.android.gms.measurement.internal;

import android.content.Context;
import android.os.Looper;

public final class zzq {
    private final boolean zzdu = false;

    zzq(Context context) {
    }

    public static boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }
}
