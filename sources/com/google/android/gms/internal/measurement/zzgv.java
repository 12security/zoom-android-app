package com.google.android.gms.internal.measurement;

import java.util.ArrayList;
import java.util.List;

final class zzgv<E> extends zzdj<E> {
    private static final zzgv<Object> zzajw;
    private final List<E> zzaij;

    public static <E> zzgv<E> zzoa() {
        return zzajw;
    }

    zzgv() {
        this(new ArrayList(10));
    }

    private zzgv(List<E> list) {
        this.zzaij = list;
    }

    public final void add(int i, E e) {
        zzka();
        this.zzaij.add(i, e);
        this.modCount++;
    }

    public final E get(int i) {
        return this.zzaij.get(i);
    }

    public final E remove(int i) {
        zzka();
        E remove = this.zzaij.remove(i);
        this.modCount++;
        return remove;
    }

    public final E set(int i, E e) {
        zzka();
        E e2 = this.zzaij.set(i, e);
        this.modCount++;
        return e2;
    }

    public final int size() {
        return this.zzaij.size();
    }

    public final /* synthetic */ zzfg zzq(int i) {
        if (i >= size()) {
            ArrayList arrayList = new ArrayList(i);
            arrayList.addAll(this.zzaij);
            return new zzgv(arrayList);
        }
        throw new IllegalArgumentException();
    }

    static {
        zzgv<Object> zzgv = new zzgv<>(new ArrayList(0));
        zzajw = zzgv;
        zzgv.zzjz();
    }
}
