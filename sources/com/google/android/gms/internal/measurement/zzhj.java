package com.google.android.gms.internal.measurement;

import java.util.Iterator;
import java.util.Map.Entry;

final class zzhj implements Iterator<Entry<K, V>> {
    private int pos;
    private Iterator<Entry<K, V>> zzakh;
    private final /* synthetic */ zzhb zzaki;
    private boolean zzakm;

    private zzhj(zzhb zzhb) {
        this.zzaki = zzhb;
        this.pos = -1;
    }

    public final boolean hasNext() {
        if (this.pos + 1 < this.zzaki.zzakc.size() || (!this.zzaki.zzakd.isEmpty() && zzon().hasNext())) {
            return true;
        }
        return false;
    }

    public final void remove() {
        if (this.zzakm) {
            this.zzakm = false;
            this.zzaki.zzol();
            if (this.pos < this.zzaki.zzakc.size()) {
                zzhb zzhb = this.zzaki;
                int i = this.pos;
                this.pos = i - 1;
                zzhb.zzbg(i);
                return;
            }
            zzon().remove();
            return;
        }
        throw new IllegalStateException("remove() was called before next()");
    }

    private final Iterator<Entry<K, V>> zzon() {
        if (this.zzakh == null) {
            this.zzakh = this.zzaki.zzakd.entrySet().iterator();
        }
        return this.zzakh;
    }

    public final /* synthetic */ Object next() {
        this.zzakm = true;
        int i = this.pos + 1;
        this.pos = i;
        if (i < this.zzaki.zzakc.size()) {
            return (Entry) this.zzaki.zzakc.get(this.pos);
        }
        return (Entry) zzon().next();
    }

    /* synthetic */ zzhj(zzhb zzhb, zzhc zzhc) {
        this(zzhb);
    }
}
