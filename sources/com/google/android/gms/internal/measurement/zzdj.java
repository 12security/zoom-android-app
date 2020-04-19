package com.google.android.gms.internal.measurement;

import java.util.AbstractList;
import java.util.Collection;
import java.util.List;
import java.util.RandomAccess;

abstract class zzdj<E> extends AbstractList<E> implements zzfg<E> {
    private boolean zzabp = true;

    zzdj() {
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof List)) {
            return false;
        }
        if (!(obj instanceof RandomAccess)) {
            return super.equals(obj);
        }
        List list = (List) obj;
        int size = size();
        if (size != list.size()) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            if (!get(i).equals(list.get(i))) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        int i = 1;
        for (int i2 = 0; i2 < size(); i2++) {
            i = (i * 31) + get(i2).hashCode();
        }
        return i;
    }

    public boolean add(E e) {
        zzka();
        return super.add(e);
    }

    public void add(int i, E e) {
        zzka();
        super.add(i, e);
    }

    public boolean addAll(Collection<? extends E> collection) {
        zzka();
        return super.addAll(collection);
    }

    public boolean addAll(int i, Collection<? extends E> collection) {
        zzka();
        return super.addAll(i, collection);
    }

    public void clear() {
        zzka();
        super.clear();
    }

    public boolean zzjy() {
        return this.zzabp;
    }

    public final void zzjz() {
        this.zzabp = false;
    }

    public E remove(int i) {
        zzka();
        return super.remove(i);
    }

    public boolean remove(Object obj) {
        zzka();
        return super.remove(obj);
    }

    public boolean removeAll(Collection<?> collection) {
        zzka();
        return super.removeAll(collection);
    }

    public boolean retainAll(Collection<?> collection) {
        zzka();
        return super.retainAll(collection);
    }

    public E set(int i, E e) {
        zzka();
        return super.set(i, e);
    }

    /* access modifiers changed from: protected */
    public final void zzka() {
        if (!this.zzabp) {
            throw new UnsupportedOperationException();
        }
    }
}
