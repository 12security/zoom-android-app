package com.google.android.gms.internal.measurement;

import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

final class zzhd implements Iterator<Entry<K, V>> {
    private int pos;
    private Iterator<Entry<K, V>> zzakh;
    private final /* synthetic */ zzhb zzaki;

    private zzhd(zzhb zzhb) {
        this.zzaki = zzhb;
        this.pos = this.zzaki.zzakc.size();
    }

    public final boolean hasNext() {
        int i = this.pos;
        return (i > 0 && i <= this.zzaki.zzakc.size()) || zzon().hasNext();
    }

    public final void remove() {
        throw new UnsupportedOperationException();
    }

    private final Iterator<Entry<K, V>> zzon() {
        if (this.zzakh == null) {
            this.zzakh = this.zzaki.zzakf.entrySet().iterator();
        }
        return this.zzakh;
    }

    public final /* synthetic */ Object next() {
        if (zzon().hasNext()) {
            return (Entry) zzon().next();
        }
        List zzb = this.zzaki.zzakc;
        int i = this.pos - 1;
        this.pos = i;
        return (Entry) zzb.get(i);
    }

    /* synthetic */ zzhd(zzhb zzhb, zzhc zzhc) {
        this(zzhb);
    }
}
