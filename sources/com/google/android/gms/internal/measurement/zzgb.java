package com.google.android.gms.internal.measurement;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public final class zzgb<K, V> extends LinkedHashMap<K, V> {
    private static final zzgb zzaiv;
    private boolean zzabp = true;

    private zzgb() {
    }

    private zzgb(Map<K, V> map) {
        super(map);
    }

    public static <K, V> zzgb<K, V> zznm() {
        return zzaiv;
    }

    public final void zza(zzgb<K, V> zzgb) {
        zzno();
        if (!zzgb.isEmpty()) {
            putAll(zzgb);
        }
    }

    public final Set<Entry<K, V>> entrySet() {
        return isEmpty() ? Collections.emptySet() : super.entrySet();
    }

    public final void clear() {
        zzno();
        super.clear();
    }

    public final V put(K k, V v) {
        zzno();
        zzfb.checkNotNull(k);
        zzfb.checkNotNull(v);
        return super.put(k, v);
    }

    public final void putAll(Map<? extends K, ? extends V> map) {
        zzno();
        for (Object next : map.keySet()) {
            zzfb.checkNotNull(next);
            zzfb.checkNotNull(map.get(next));
        }
        super.putAll(map);
    }

    public final V remove(Object obj) {
        zzno();
        return super.remove(obj);
    }

    /* JADX WARNING: Removed duplicated region for block: B:23:0x005e A[RETURN] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final boolean equals(java.lang.Object r7) {
        /*
            r6 = this;
            boolean r0 = r7 instanceof java.util.Map
            r1 = 0
            if (r0 == 0) goto L_0x005f
            java.util.Map r7 = (java.util.Map) r7
            r0 = 1
            if (r6 == r7) goto L_0x005b
            int r2 = r6.size()
            int r3 = r7.size()
            if (r2 == r3) goto L_0x0016
            r7 = 0
            goto L_0x005c
        L_0x0016:
            java.util.Set r2 = r6.entrySet()
            java.util.Iterator r2 = r2.iterator()
        L_0x001e:
            boolean r3 = r2.hasNext()
            if (r3 == 0) goto L_0x005b
            java.lang.Object r3 = r2.next()
            java.util.Map$Entry r3 = (java.util.Map.Entry) r3
            java.lang.Object r4 = r3.getKey()
            boolean r4 = r7.containsKey(r4)
            if (r4 != 0) goto L_0x0036
            r7 = 0
            goto L_0x005c
        L_0x0036:
            java.lang.Object r4 = r3.getValue()
            java.lang.Object r3 = r3.getKey()
            java.lang.Object r3 = r7.get(r3)
            boolean r5 = r4 instanceof byte[]
            if (r5 == 0) goto L_0x0053
            boolean r5 = r3 instanceof byte[]
            if (r5 == 0) goto L_0x0053
            byte[] r4 = (byte[]) r4
            byte[] r3 = (byte[]) r3
            boolean r3 = java.util.Arrays.equals(r4, r3)
            goto L_0x0057
        L_0x0053:
            boolean r3 = r4.equals(r3)
        L_0x0057:
            if (r3 != 0) goto L_0x001e
            r7 = 0
            goto L_0x005c
        L_0x005b:
            r7 = 1
        L_0x005c:
            if (r7 == 0) goto L_0x005f
            return r0
        L_0x005f:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.measurement.zzgb.equals(java.lang.Object):boolean");
    }

    private static int zzl(Object obj) {
        if (obj instanceof byte[]) {
            return zzfb.hashCode((byte[]) obj);
        }
        if (!(obj instanceof zzfc)) {
            return obj.hashCode();
        }
        throw new UnsupportedOperationException();
    }

    public final int hashCode() {
        int i = 0;
        for (Entry entry : entrySet()) {
            i += zzl(entry.getValue()) ^ zzl(entry.getKey());
        }
        return i;
    }

    public final zzgb<K, V> zznn() {
        return isEmpty() ? new zzgb<>() : new zzgb<>(this);
    }

    public final void zzjz() {
        this.zzabp = false;
    }

    public final boolean isMutable() {
        return this.zzabp;
    }

    private final void zzno() {
        if (!this.zzabp) {
            throw new UnsupportedOperationException();
        }
    }

    static {
        zzgb zzgb = new zzgb();
        zzaiv = zzgb;
        zzgb.zzabp = false;
    }
}
