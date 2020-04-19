package com.google.firebase.iid;

import android.os.Bundle;
import com.google.android.gms.tasks.TaskCompletionSource;

final /* synthetic */ class zzt implements Runnable {
    private final zzr zzbr;
    private final Bundle zzbs;
    private final TaskCompletionSource zzbt;

    zzt(zzr zzr, Bundle bundle, TaskCompletionSource taskCompletionSource) {
        this.zzbr = zzr;
        this.zzbs = bundle;
        this.zzbt = taskCompletionSource;
    }

    public final void run() {
        this.zzbr.zza(this.zzbs, this.zzbt);
    }
}
