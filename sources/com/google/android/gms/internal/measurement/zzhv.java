package com.google.android.gms.internal.measurement;

import java.util.Iterator;

final class zzhv implements Iterator<String> {
    private final /* synthetic */ zzht zzaku;
    private Iterator<String> zzakv = this.zzaku.zzakr.iterator();

    zzhv(zzht zzht) {
        this.zzaku = zzht;
    }

    public final boolean hasNext() {
        return this.zzakv.hasNext();
    }

    public final void remove() {
        throw new UnsupportedOperationException();
    }

    public final /* synthetic */ Object next() {
        return (String) this.zzakv.next();
    }
}
