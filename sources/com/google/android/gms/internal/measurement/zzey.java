package com.google.android.gms.internal.measurement;

import com.google.android.gms.internal.measurement.zzez.zze;

final class zzey implements zzgg {
    private static final zzey zzagm = new zzey();

    private zzey() {
    }

    public static zzey zzmf() {
        return zzagm;
    }

    public final boolean zzb(Class<?> cls) {
        return zzez.class.isAssignableFrom(cls);
    }

    public final zzgf zzc(Class<?> cls) {
        if (!zzez.class.isAssignableFrom(cls)) {
            String str = "Unsupported message type: ";
            String valueOf = String.valueOf(cls.getName());
            throw new IllegalArgumentException(valueOf.length() != 0 ? str.concat(valueOf) : new String(str));
        }
        try {
            return (zzgf) zzez.zzd(cls.asSubclass(zzez.class)).zza(zze.zzagw, (Object) null, (Object) null);
        } catch (Exception e) {
            String str2 = "Unable to get message info for ";
            String valueOf2 = String.valueOf(cls.getName());
            throw new RuntimeException(valueOf2.length() != 0 ? str2.concat(valueOf2) : new String(str2), e);
        }
    }
}
