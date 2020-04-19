package com.google.android.gms.internal.measurement;

import java.util.AbstractList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;

public final class zzht extends AbstractList<String> implements zzfq, RandomAccess {
    /* access modifiers changed from: private */
    public final zzfq zzakr;

    public zzht(zzfq zzfq) {
        this.zzakr = zzfq;
    }

    public final zzfq zznh() {
        return this;
    }

    public final Object zzaw(int i) {
        return this.zzakr.zzaw(i);
    }

    public final int size() {
        return this.zzakr.size();
    }

    public final void zzc(zzdp zzdp) {
        throw new UnsupportedOperationException();
    }

    public final ListIterator<String> listIterator(int i) {
        return new zzhu(this, i);
    }

    public final Iterator<String> iterator() {
        return new zzhv(this);
    }

    public final List<?> zzng() {
        return this.zzakr.zzng();
    }

    public final /* synthetic */ Object get(int i) {
        return (String) this.zzakr.get(i);
    }
}
