package com.google.android.gms.internal.measurement;

import android.util.Log;

final class zzcz extends zzcw<Boolean> {
    zzcz(zzdc zzdc, String str, Boolean bool) {
        super(zzdc, str, bool, null);
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ Object zzc(Object obj) {
        if (obj instanceof Boolean) {
            return (Boolean) obj;
        }
        if (obj instanceof String) {
            String str = (String) obj;
            if (zzci.zzyv.matcher(str).matches()) {
                return Boolean.valueOf(true);
            }
            if (zzci.zzyw.matcher(str).matches()) {
                return Boolean.valueOf(false);
            }
        }
        String zzjq = super.zzjq();
        String valueOf = String.valueOf(obj);
        StringBuilder sb = new StringBuilder(String.valueOf(zzjq).length() + 28 + String.valueOf(valueOf).length());
        sb.append("Invalid boolean value for ");
        sb.append(zzjq);
        sb.append(": ");
        sb.append(valueOf);
        Log.e("PhenotypeFlag", sb.toString());
        return null;
    }
}
