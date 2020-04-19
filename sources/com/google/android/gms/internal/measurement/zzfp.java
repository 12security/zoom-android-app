package com.google.android.gms.internal.measurement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.RandomAccess;

public final class zzfp extends zzdj<String> implements zzfq, RandomAccess {
    private static final zzfp zzaih;
    private static final zzfq zzaii = zzaih;
    private final List<Object> zzaij;

    public zzfp() {
        this(10);
    }

    public zzfp(int i) {
        this(new ArrayList<>(i));
    }

    private zzfp(ArrayList<Object> arrayList) {
        this.zzaij = arrayList;
    }

    public final int size() {
        return this.zzaij.size();
    }

    public final boolean addAll(Collection<? extends String> collection) {
        return addAll(size(), collection);
    }

    public final boolean addAll(int i, Collection<? extends String> collection) {
        zzka();
        if (collection instanceof zzfq) {
            collection = ((zzfq) collection).zzng();
        }
        boolean addAll = this.zzaij.addAll(i, collection);
        this.modCount++;
        return addAll;
    }

    public final void clear() {
        zzka();
        this.zzaij.clear();
        this.modCount++;
    }

    public final void zzc(zzdp zzdp) {
        zzka();
        this.zzaij.add(zzdp);
        this.modCount++;
    }

    public final Object zzaw(int i) {
        return this.zzaij.get(i);
    }

    private static String zzk(Object obj) {
        if (obj instanceof String) {
            return (String) obj;
        }
        if (obj instanceof zzdp) {
            return ((zzdp) obj).zzkd();
        }
        return zzfb.zzk((byte[]) obj);
    }

    public final List<?> zzng() {
        return Collections.unmodifiableList(this.zzaij);
    }

    public final zzfq zznh() {
        return zzjy() ? new zzht(this) : this;
    }

    public final /* synthetic */ Object set(int i, Object obj) {
        String str = (String) obj;
        zzka();
        return zzk(this.zzaij.set(i, str));
    }

    public final /* bridge */ /* synthetic */ boolean retainAll(Collection collection) {
        return super.retainAll(collection);
    }

    public final /* bridge */ /* synthetic */ boolean removeAll(Collection collection) {
        return super.removeAll(collection);
    }

    public final /* bridge */ /* synthetic */ boolean remove(Object obj) {
        return super.remove(obj);
    }

    public final /* synthetic */ Object remove(int i) {
        zzka();
        Object remove = this.zzaij.remove(i);
        this.modCount++;
        return zzk(remove);
    }

    public final /* bridge */ /* synthetic */ boolean zzjy() {
        return super.zzjy();
    }

    public final /* synthetic */ void add(int i, Object obj) {
        String str = (String) obj;
        zzka();
        this.zzaij.add(i, str);
        this.modCount++;
    }

    public final /* bridge */ /* synthetic */ int hashCode() {
        return super.hashCode();
    }

    public final /* bridge */ /* synthetic */ boolean equals(Object obj) {
        return super.equals(obj);
    }

    public final /* synthetic */ zzfg zzq(int i) {
        if (i >= size()) {
            ArrayList arrayList = new ArrayList(i);
            arrayList.addAll(this.zzaij);
            return new zzfp(arrayList);
        }
        throw new IllegalArgumentException();
    }

    public final /* synthetic */ Object get(int i) {
        Object obj = this.zzaij.get(i);
        if (obj instanceof String) {
            return (String) obj;
        }
        if (obj instanceof zzdp) {
            zzdp zzdp = (zzdp) obj;
            String zzkd = zzdp.zzkd();
            if (zzdp.zzke()) {
                this.zzaij.set(i, zzkd);
            }
            return zzkd;
        }
        byte[] bArr = (byte[]) obj;
        String zzk = zzfb.zzk(bArr);
        if (zzfb.zzj(bArr)) {
            this.zzaij.set(i, zzk);
        }
        return zzk;
    }

    static {
        zzfp zzfp = new zzfp();
        zzaih = zzfp;
        zzfp.zzjz();
    }
}
