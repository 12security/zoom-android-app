package com.google.android.gms.internal.measurement;

import android.content.Context;
import android.util.Log;
import androidx.annotation.GuardedBy;
import androidx.core.content.PermissionChecker;

final class zzcs implements zzcp {
    @GuardedBy("GservicesLoader.class")
    static zzcs zzzq;
    private final Context zzno;

    static zzcs zzp(Context context) {
        zzcs zzcs;
        synchronized (zzcs.class) {
            if (zzzq == null) {
                zzzq = PermissionChecker.checkSelfPermission(context, "com.google.android.providers.gsf.permission.READ_GSERVICES") == 0 ? new zzcs(context) : new zzcs();
            }
            zzcs = zzzq;
        }
        return zzcs;
    }

    private zzcs(Context context) {
        this.zzno = context;
        this.zzno.getContentResolver().registerContentObserver(zzci.CONTENT_URI, true, new zzcu(this, null));
    }

    private zzcs() {
        this.zzno = null;
    }

    /* access modifiers changed from: private */
    /* renamed from: zzcb */
    public final String zzca(String str) {
        if (this.zzno == null) {
            return null;
        }
        try {
            return (String) zzcq.zza(new zzct(this, str));
        } catch (SecurityException e) {
            String str2 = "GservicesLoader";
            String str3 = "Unable to read GServices for: ";
            String valueOf = String.valueOf(str);
            Log.e(str2, valueOf.length() != 0 ? str3.concat(valueOf) : new String(str3), e);
            return null;
        }
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ String zzcc(String str) {
        return zzci.zza(this.zzno.getContentResolver(), str, (String) null);
    }
}
