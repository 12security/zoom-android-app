package com.google.firebase.iid;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.VisibleForTesting;
import com.google.android.gms.common.util.concurrent.NamedThreadFactory;
import com.google.android.gms.internal.firebase_messaging.zza;
import com.google.android.gms.internal.firebase_messaging.zzf;
import com.google.android.gms.tasks.Task;
import java.util.concurrent.ScheduledExecutorService;
import javax.annotation.concurrent.GuardedBy;

public final class zzab {
    @GuardedBy("MessengerIpcClient.class")
    private static zzab zzca;
    /* access modifiers changed from: private */
    public final Context zzag;
    /* access modifiers changed from: private */
    public final ScheduledExecutorService zzcb;
    @GuardedBy("this")
    private zzac zzcc = new zzac(this);
    @GuardedBy("this")
    private int zzcd = 1;

    public static synchronized zzab zzc(Context context) {
        zzab zzab;
        synchronized (zzab.class) {
            if (zzca == null) {
                zzca = new zzab(context, zza.zza().zza(1, new NamedThreadFactory("MessengerIpcClient"), zzf.zze));
            }
            zzab = zzca;
        }
        return zzab;
    }

    @VisibleForTesting
    private zzab(Context context, ScheduledExecutorService scheduledExecutorService) {
        this.zzcb = scheduledExecutorService;
        this.zzag = context.getApplicationContext();
    }

    public final Task<Void> zza(int i, Bundle bundle) {
        return zza((zzal<T>) new zzai<T>(zzx(), 2, bundle));
    }

    public final Task<Bundle> zzb(int i, Bundle bundle) {
        return zza((zzal<T>) new zzan<T>(zzx(), 1, bundle));
    }

    private final synchronized <T> Task<T> zza(zzal<T> zzal) {
        if (Log.isLoggable("MessengerIpcClient", 3)) {
            String valueOf = String.valueOf(zzal);
            StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 9);
            sb.append("Queueing ");
            sb.append(valueOf);
            Log.d("MessengerIpcClient", sb.toString());
        }
        if (!this.zzcc.zzb(zzal)) {
            this.zzcc = new zzac(this);
            this.zzcc.zzb(zzal);
        }
        return zzal.zzcn.getTask();
    }

    private final synchronized int zzx() {
        int i;
        i = this.zzcd;
        this.zzcd = i + 1;
        return i;
    }
}
