package com.google.android.gms.internal.measurement;

import java.util.Iterator;
import java.util.Map.Entry;

final class zzfn<K> implements Iterator<Entry<K, Object>> {
    private Iterator<Entry<K, Object>> zzaid;

    public zzfn(Iterator<Entry<K, Object>> it) {
        this.zzaid = it;
    }

    public final boolean hasNext() {
        return this.zzaid.hasNext();
    }

    public final void remove() {
        this.zzaid.remove();
    }

    public final /* synthetic */ Object next() {
        Entry entry = (Entry) this.zzaid.next();
        return entry.getValue() instanceof zzfk ? new zzfm(entry) : entry;
    }
}
