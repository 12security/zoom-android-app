package com.google.android.gms.internal.measurement;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map.Entry;

class zzhk extends AbstractSet<Entry<K, V>> {
    private final /* synthetic */ zzhb zzaki;

    private zzhk(zzhb zzhb) {
        this.zzaki = zzhb;
    }

    public Iterator<Entry<K, V>> iterator() {
        return new zzhj(this.zzaki, null);
    }

    public int size() {
        return this.zzaki.size();
    }

    public boolean contains(Object obj) {
        Entry entry = (Entry) obj;
        Object obj2 = this.zzaki.get(entry.getKey());
        Object value = entry.getValue();
        return obj2 == value || (obj2 != null && obj2.equals(value));
    }

    public boolean remove(Object obj) {
        Entry entry = (Entry) obj;
        if (!contains(entry)) {
            return false;
        }
        this.zzaki.remove(entry.getKey());
        return true;
    }

    public void clear() {
        this.zzaki.clear();
    }

    public /* synthetic */ boolean add(Object obj) {
        Entry entry = (Entry) obj;
        if (contains(entry)) {
            return false;
        }
        this.zzaki.put((Comparable) entry.getKey(), entry.getValue());
        return true;
    }

    /* synthetic */ zzhk(zzhb zzhb, zzhc zzhc) {
        this(zzhb);
    }
}
