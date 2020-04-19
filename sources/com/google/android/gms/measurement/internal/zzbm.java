package com.google.android.gms.measurement.internal;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import androidx.annotation.MainThread;
import com.google.android.gms.internal.measurement.zze;
import com.google.android.gms.internal.measurement.zzf;

public final class zzbm implements ServiceConnection {
    /* access modifiers changed from: private */
    public final String packageName;
    final /* synthetic */ zzbl zzmf;

    zzbm(zzbl zzbl, String str) {
        this.zzmf = zzbl;
        this.packageName = str;
    }

    @MainThread
    public final void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        if (iBinder == null) {
            this.zzmf.zzl.zzad().zzdd().zzaq("Install Referrer connection returned with null binder");
            return;
        }
        try {
            zze zza = zzf.zza(iBinder);
            if (zza == null) {
                this.zzmf.zzl.zzad().zzdd().zzaq("Install Referrer Service implementation was not found");
                return;
            }
            this.zzmf.zzl.zzad().zzdg().zzaq("Install Referrer Service connected");
            this.zzmf.zzl.zzac().zza((Runnable) new zzbn(this, zza, this));
        } catch (Exception e) {
            this.zzmf.zzl.zzad().zzdd().zza("Exception occurred while calling Install Referrer API", e);
        }
    }

    @MainThread
    public final void onServiceDisconnected(ComponentName componentName) {
        this.zzmf.zzl.zzad().zzdg().zzaq("Install Referrer Service disconnected");
    }
}
