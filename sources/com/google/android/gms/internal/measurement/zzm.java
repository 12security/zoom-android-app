package com.google.android.gms.internal.measurement;

import android.os.Bundle;
import android.util.Log;
import java.util.concurrent.atomic.AtomicReference;

public final class zzm extends zzr {
    private final AtomicReference<Bundle> zzr = new AtomicReference<>();
    private boolean zzs;

    public final void zzb(Bundle bundle) {
        synchronized (this.zzr) {
            try {
                this.zzr.set(bundle);
                this.zzs = true;
                this.zzr.notify();
            } catch (Throwable th) {
                this.zzr.notify();
                throw th;
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public final String zza(long j) {
        return (String) zza(zzb(j), String.class);
    }

    /* access modifiers changed from: 0000 */
    public final Bundle zzb(long j) {
        Bundle bundle;
        synchronized (this.zzr) {
            if (!this.zzs) {
                try {
                    this.zzr.wait(j);
                } catch (InterruptedException unused) {
                    return null;
                }
            }
            bundle = (Bundle) this.zzr.get();
        }
        return bundle;
    }

    static <T> T zza(Bundle bundle, Class<T> cls) {
        if (bundle == null) {
            return null;
        }
        Object obj = bundle.get("r");
        if (obj == null) {
            return null;
        }
        try {
            return cls.cast(obj);
        } catch (ClassCastException e) {
            String canonicalName = cls.getCanonicalName();
            String canonicalName2 = obj.getClass().getCanonicalName();
            Object[] objArr = {canonicalName, canonicalName2};
            String str = "AM";
            Log.w(str, String.format(String.valueOf("Unexpected object type. Expected, Received").concat(": %s, %s"), objArr), e);
            throw e;
        }
    }
}
