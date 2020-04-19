package com.google.android.gms.internal.measurement;

import android.os.Binder;

public final /* synthetic */ class zzcq {
    public static <V> V zza(zzcr<V> zzcr) {
        long clearCallingIdentity;
        try {
            return zzcr.zzjn();
        } catch (SecurityException unused) {
            clearCallingIdentity = Binder.clearCallingIdentity();
            V zzjn = zzcr.zzjn();
            Binder.restoreCallingIdentity(clearCallingIdentity);
            return zzjn;
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(clearCallingIdentity);
            throw th;
        }
    }
}
