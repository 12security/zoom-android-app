package com.google.android.gms.internal.measurement;

import android.util.Log;

final class zzcx extends zzcw<Long> {
    zzcx(zzdc zzdc, String str, Long l) {
        super(zzdc, str, l, null);
    }

    /* access modifiers changed from: private */
    /* renamed from: zzd */
    public final Long zzc(Object obj) {
        if (obj instanceof Long) {
            return (Long) obj;
        }
        if (obj instanceof String) {
            try {
                return Long.valueOf(Long.parseLong((String) obj));
            } catch (NumberFormatException unused) {
            }
        }
        String zzjq = super.zzjq();
        String valueOf = String.valueOf(obj);
        StringBuilder sb = new StringBuilder(String.valueOf(zzjq).length() + 25 + String.valueOf(valueOf).length());
        sb.append("Invalid long value for ");
        sb.append(zzjq);
        sb.append(": ");
        sb.append(valueOf);
        Log.e("PhenotypeFlag", sb.toString());
        return null;
    }
}
