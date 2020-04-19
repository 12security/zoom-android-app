package com.google.android.gms.internal.measurement;

import java.util.ListIterator;

final class zzhu implements ListIterator<String> {
    private ListIterator<String> zzaks = this.zzaku.zzakr.listIterator(this.zzakt);
    private final /* synthetic */ int zzakt;
    private final /* synthetic */ zzht zzaku;

    zzhu(zzht zzht, int i) {
        this.zzaku = zzht;
        this.zzakt = i;
    }

    public final boolean hasNext() {
        return this.zzaks.hasNext();
    }

    public final boolean hasPrevious() {
        return this.zzaks.hasPrevious();
    }

    public final int nextIndex() {
        return this.zzaks.nextIndex();
    }

    public final int previousIndex() {
        return this.zzaks.previousIndex();
    }

    public final void remove() {
        throw new UnsupportedOperationException();
    }

    public final /* synthetic */ void add(Object obj) {
        throw new UnsupportedOperationException();
    }

    public final /* synthetic */ void set(Object obj) {
        throw new UnsupportedOperationException();
    }

    public final /* synthetic */ Object previous() {
        return (String) this.zzaks.previous();
    }

    public final /* synthetic */ Object next() {
        return (String) this.zzaks.next();
    }
}
