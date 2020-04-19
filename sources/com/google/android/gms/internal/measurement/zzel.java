package com.google.android.gms.internal.measurement;

final class zzel {
    private static final Class<?> zzadi = zzlo();

    private static Class<?> zzlo() {
        try {
            return Class.forName("com.google.protobuf.ExtensionRegistry");
        } catch (ClassNotFoundException unused) {
            return null;
        }
    }

    public static zzem zzlp() {
        if (zzadi != null) {
            try {
                return zzcr("getEmptyRegistry");
            } catch (Exception unused) {
            }
        }
        return zzem.zzadm;
    }

    /* JADX WARNING: Removed duplicated region for block: B:10:0x0014  */
    /* JADX WARNING: Removed duplicated region for block: B:12:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x000e  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static com.google.android.gms.internal.measurement.zzem zzlq() {
        /*
            java.lang.Class<?> r0 = zzadi
            if (r0 == 0) goto L_0x000b
            java.lang.String r0 = "loadGeneratedRegistry"
            com.google.android.gms.internal.measurement.zzem r0 = zzcr(r0)     // Catch:{ Exception -> 0x000b }
            goto L_0x000c
        L_0x000b:
            r0 = 0
        L_0x000c:
            if (r0 != 0) goto L_0x0012
            com.google.android.gms.internal.measurement.zzem r0 = com.google.android.gms.internal.measurement.zzem.zzlq()
        L_0x0012:
            if (r0 != 0) goto L_0x0018
            com.google.android.gms.internal.measurement.zzem r0 = zzlp()
        L_0x0018:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.measurement.zzel.zzlq():com.google.android.gms.internal.measurement.zzem");
    }

    private static final zzem zzcr(String str) throws Exception {
        return (zzem) zzadi.getDeclaredMethod(str, new Class[0]).invoke(null, new Object[0]);
    }
}
