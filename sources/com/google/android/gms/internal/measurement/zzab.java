package com.google.android.gms.internal.measurement;

import android.content.Context;
import android.os.Bundle;

final class zzab extends zza {
    private final /* synthetic */ Context val$context;
    private final /* synthetic */ String zzao;
    private final /* synthetic */ String zzap;
    private final /* synthetic */ Bundle zzaq;
    private final /* synthetic */ zzaa zzar;

    zzab(zzaa zzaa, String str, String str2, Context context, Bundle bundle) {
        this.zzar = zzaa;
        this.zzao = str;
        this.zzap = str2;
        this.val$context = context;
        this.zzaq = bundle;
        super(zzaa);
    }

    /* JADX WARNING: Removed duplicated region for block: B:14:0x0054 A[Catch:{ RemoteException -> 0x00a1 }] */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x0060 A[Catch:{ RemoteException -> 0x00a1 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void zzl() {
        /*
            r14 = this;
            r0 = 0
            r1 = 1
            com.google.android.gms.internal.measurement.zzaa r2 = r14.zzar     // Catch:{ RemoteException -> 0x00a1 }
            java.util.HashMap r3 = new java.util.HashMap     // Catch:{ RemoteException -> 0x00a1 }
            r3.<init>()     // Catch:{ RemoteException -> 0x00a1 }
            r2.zzad = r3     // Catch:{ RemoteException -> 0x00a1 }
            com.google.android.gms.internal.measurement.zzaa r2 = r14.zzar     // Catch:{ RemoteException -> 0x00a1 }
            java.lang.String r3 = r14.zzao     // Catch:{ RemoteException -> 0x00a1 }
            java.lang.String r4 = r14.zzap     // Catch:{ RemoteException -> 0x00a1 }
            boolean r2 = com.google.android.gms.internal.measurement.zzaa.zza(r3, r4)     // Catch:{ RemoteException -> 0x00a1 }
            r3 = 0
            if (r2 == 0) goto L_0x0027
            java.lang.String r3 = r14.zzap     // Catch:{ RemoteException -> 0x00a1 }
            java.lang.String r2 = r14.zzao     // Catch:{ RemoteException -> 0x00a1 }
            com.google.android.gms.internal.measurement.zzaa r4 = r14.zzar     // Catch:{ RemoteException -> 0x00a1 }
            java.lang.String r4 = r4.zzw     // Catch:{ RemoteException -> 0x00a1 }
            r10 = r2
            r11 = r3
            r9 = r4
            goto L_0x002a
        L_0x0027:
            r9 = r3
            r10 = r9
            r11 = r10
        L_0x002a:
            android.content.Context r2 = r14.val$context     // Catch:{ RemoteException -> 0x00a1 }
            com.google.android.gms.internal.measurement.zzaa.zze(r2)     // Catch:{ RemoteException -> 0x00a1 }
            java.lang.Boolean r2 = com.google.android.gms.internal.measurement.zzaa.zzag     // Catch:{ RemoteException -> 0x00a1 }
            boolean r2 = r2.booleanValue()     // Catch:{ RemoteException -> 0x00a1 }
            if (r2 != 0) goto L_0x003e
            if (r10 == 0) goto L_0x003c
            goto L_0x003e
        L_0x003c:
            r2 = 0
            goto L_0x003f
        L_0x003e:
            r2 = 1
        L_0x003f:
            com.google.android.gms.internal.measurement.zzaa r3 = r14.zzar     // Catch:{ RemoteException -> 0x00a1 }
            com.google.android.gms.internal.measurement.zzaa r4 = r14.zzar     // Catch:{ RemoteException -> 0x00a1 }
            android.content.Context r5 = r14.val$context     // Catch:{ RemoteException -> 0x00a1 }
            com.google.android.gms.internal.measurement.zzn r4 = r4.zza(r5, r2)     // Catch:{ RemoteException -> 0x00a1 }
            r3.zzan = r4     // Catch:{ RemoteException -> 0x00a1 }
            com.google.android.gms.internal.measurement.zzaa r3 = r14.zzar     // Catch:{ RemoteException -> 0x00a1 }
            com.google.android.gms.internal.measurement.zzn r3 = r3.zzan     // Catch:{ RemoteException -> 0x00a1 }
            if (r3 != 0) goto L_0x0060
            com.google.android.gms.internal.measurement.zzaa r2 = r14.zzar     // Catch:{ RemoteException -> 0x00a1 }
            java.lang.String r2 = r2.zzw     // Catch:{ RemoteException -> 0x00a1 }
            java.lang.String r3 = "Failed to connect to measurement client."
            android.util.Log.w(r2, r3)     // Catch:{ RemoteException -> 0x00a1 }
            return
        L_0x0060:
            android.content.Context r3 = r14.val$context     // Catch:{ RemoteException -> 0x00a1 }
            int r3 = com.google.android.gms.internal.measurement.zzaa.zzd(r3)     // Catch:{ RemoteException -> 0x00a1 }
            android.content.Context r4 = r14.val$context     // Catch:{ RemoteException -> 0x00a1 }
            int r4 = com.google.android.gms.internal.measurement.zzaa.zzc(r4)     // Catch:{ RemoteException -> 0x00a1 }
            if (r2 == 0) goto L_0x0079
            int r2 = java.lang.Math.max(r3, r4)     // Catch:{ RemoteException -> 0x00a1 }
            if (r4 >= r3) goto L_0x0076
            r3 = 1
            goto L_0x0077
        L_0x0076:
            r3 = 0
        L_0x0077:
            r8 = r3
            goto L_0x0084
        L_0x0079:
            if (r3 <= 0) goto L_0x007d
            r2 = r3
            goto L_0x007e
        L_0x007d:
            r2 = r4
        L_0x007e:
            if (r3 <= 0) goto L_0x0082
            r3 = 1
            goto L_0x0083
        L_0x0082:
            r3 = 0
        L_0x0083:
            r8 = r3
        L_0x0084:
            com.google.android.gms.internal.measurement.zzy r13 = new com.google.android.gms.internal.measurement.zzy     // Catch:{ RemoteException -> 0x00a1 }
            r4 = 15300(0x3bc4, double:7.559E-320)
            long r6 = (long) r2     // Catch:{ RemoteException -> 0x00a1 }
            android.os.Bundle r12 = r14.zzaq     // Catch:{ RemoteException -> 0x00a1 }
            r3 = r13
            r3.<init>(r4, r6, r8, r9, r10, r11, r12)     // Catch:{ RemoteException -> 0x00a1 }
            com.google.android.gms.internal.measurement.zzaa r2 = r14.zzar     // Catch:{ RemoteException -> 0x00a1 }
            com.google.android.gms.internal.measurement.zzn r2 = r2.zzan     // Catch:{ RemoteException -> 0x00a1 }
            android.content.Context r3 = r14.val$context     // Catch:{ RemoteException -> 0x00a1 }
            com.google.android.gms.dynamic.IObjectWrapper r3 = com.google.android.gms.dynamic.ObjectWrapper.wrap(r3)     // Catch:{ RemoteException -> 0x00a1 }
            long r4 = r14.timestamp     // Catch:{ RemoteException -> 0x00a1 }
            r2.initialize(r3, r13, r4)     // Catch:{ RemoteException -> 0x00a1 }
            return
        L_0x00a1:
            r2 = move-exception
            com.google.android.gms.internal.measurement.zzaa r3 = r14.zzar
            r3.zza(r2, r1, r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.measurement.zzab.zzl():void");
    }
}
