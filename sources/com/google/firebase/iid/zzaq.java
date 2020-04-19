package com.google.firebase.iid;

import android.util.Pair;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;

final /* synthetic */ class zzaq implements Continuation {
    private final zzar zzcv;
    private final Pair zzcw;

    zzaq(zzar zzar, Pair pair) {
        this.zzcv = zzar;
        this.zzcw = pair;
    }

    public final Object then(Task task) {
        return this.zzcv.zza(this.zzcw, task);
    }
}
