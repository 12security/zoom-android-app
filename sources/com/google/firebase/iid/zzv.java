package com.google.firebase.iid;

import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import java.io.IOException;

final class zzv implements Continuation<Bundle, String> {
    private final /* synthetic */ zzr zzbu;

    zzv(zzr zzr) {
        this.zzbu = zzr;
    }

    public final /* synthetic */ Object then(@NonNull Task task) throws Exception {
        return zzr.zza((Bundle) task.getResult(IOException.class));
    }
}
