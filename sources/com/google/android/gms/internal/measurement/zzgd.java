package com.google.android.gms.internal.measurement;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

final class zzgd implements zzgc {
    zzgd() {
    }

    public final Map<?, ?> zzm(Object obj) {
        return (zzgb) obj;
    }

    public final zzga<?, ?> zzr(Object obj) {
        throw new NoSuchMethodError();
    }

    public final Map<?, ?> zzn(Object obj) {
        return (zzgb) obj;
    }

    public final boolean zzo(Object obj) {
        return !((zzgb) obj).isMutable();
    }

    public final Object zzp(Object obj) {
        ((zzgb) obj).zzjz();
        return obj;
    }

    public final Object zzq(Object obj) {
        return zzgb.zznm().zznn();
    }

    public final Object zzb(Object obj, Object obj2) {
        zzgb zzgb = (zzgb) obj;
        zzgb zzgb2 = (zzgb) obj2;
        if (!zzgb2.isEmpty()) {
            if (!zzgb.isMutable()) {
                zzgb = zzgb.zznn();
            }
            zzgb.zza(zzgb2);
        }
        return zzgb;
    }

    public final int zzb(int i, Object obj, Object obj2) {
        zzgb zzgb = (zzgb) obj;
        if (zzgb.isEmpty()) {
            return 0;
        }
        Iterator it = zzgb.entrySet().iterator();
        if (!it.hasNext()) {
            return 0;
        }
        Entry entry = (Entry) it.next();
        entry.getKey();
        entry.getValue();
        throw new NoSuchMethodError();
    }
}
