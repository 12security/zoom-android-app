package com.google.android.gms.measurement.internal;

import java.util.Iterator;

final class zzah implements Iterator<String> {
    private Iterator<String> zzfn = this.zzfo.zzfm.keySet().iterator();
    private final /* synthetic */ zzag zzfo;

    zzah(zzag zzag) {
        this.zzfo = zzag;
    }

    public final boolean hasNext() {
        return this.zzfn.hasNext();
    }

    public final void remove() {
        throw new UnsupportedOperationException("Remove not supported");
    }

    public final /* synthetic */ Object next() {
        return (String) this.zzfn.next();
    }
}
