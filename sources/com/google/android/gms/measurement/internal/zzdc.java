package com.google.android.gms.measurement.internal;

import android.content.Context;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.VisibleForTesting;
import com.google.android.gms.internal.measurement.zzy;

@VisibleForTesting
public final class zzdc {
    final Context zzno;
    String zznp;
    String zznq;
    Boolean zzoj;
    zzy zzpe;
    long zzu;
    boolean zzv = true;
    String zzx;

    @VisibleForTesting
    public zzdc(Context context, zzy zzy) {
        Preconditions.checkNotNull(context);
        Context applicationContext = context.getApplicationContext();
        Preconditions.checkNotNull(applicationContext);
        this.zzno = applicationContext;
        if (zzy != null) {
            this.zzpe = zzy;
            this.zzx = zzy.zzx;
            this.zznp = zzy.origin;
            this.zznq = zzy.zzw;
            this.zzv = zzy.zzv;
            this.zzu = zzy.zzu;
            if (zzy.zzy != null) {
                this.zzoj = Boolean.valueOf(zzy.zzy.getBoolean("dataCollectionDefaultEnabled", true));
            }
        }
    }
}
