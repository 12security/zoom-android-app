package com.google.android.gms.internal.measurement;

import java.util.Map.Entry;

final class zzfm<K> implements Entry<K, Object> {
    private Entry<K, zzfk> zzaic;

    private zzfm(Entry<K, zzfk> entry) {
        this.zzaic = entry;
    }

    public final K getKey() {
        return this.zzaic.getKey();
    }

    public final Object getValue() {
        if (((zzfk) this.zzaic.getValue()) == null) {
            return null;
        }
        return zzfk.zzne();
    }

    public final zzfk zznf() {
        return (zzfk) this.zzaic.getValue();
    }

    public final Object setValue(Object obj) {
        if (obj instanceof zzgh) {
            return ((zzfk) this.zzaic.getValue()).zzi((zzgh) obj);
        }
        throw new IllegalArgumentException("LazyField now only used for MessageSet, and the value of MessageSet must be an instance of MessageLite");
    }
}
