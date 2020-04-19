package com.google.android.gms.internal.measurement;

final class zzdk {
    private static final Class<?> zzabq = zzcm("libcore.io.Memory");
    private static final boolean zzabr = (zzcm("org.robolectric.Robolectric") != null);

    static boolean zzkb() {
        return zzabq != null && !zzabr;
    }

    static Class<?> zzkc() {
        return zzabq;
    }

    private static <T> Class<T> zzcm(String str) {
        try {
            return Class.forName(str);
        } catch (Throwable unused) {
            return null;
        }
    }
}
