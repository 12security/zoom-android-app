package com.google.firebase.iid;

import android.os.Looper;
import android.os.Message;
import com.google.android.gms.internal.firebase_messaging.zze;

final class zzav extends zze {
    private final /* synthetic */ zzas zzdj;

    zzav(zzas zzas, Looper looper) {
        this.zzdj = zzas;
        super(looper);
    }

    public final void handleMessage(Message message) {
        this.zzdj.zzb(message);
    }
}
