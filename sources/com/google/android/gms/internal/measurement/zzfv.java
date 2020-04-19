package com.google.android.gms.internal.measurement;

import java.util.Arrays;
import java.util.Collection;
import java.util.RandomAccess;

final class zzfv extends zzdj<Long> implements zzff, zzgt, RandomAccess {
    private static final zzfv zzain;
    private int size;
    private long[] zzaio;

    public static zzfv zznk() {
        return zzain;
    }

    zzfv() {
        this(new long[10], 0);
    }

    private zzfv(long[] jArr, int i) {
        this.zzaio = jArr;
        this.size = i;
    }

    /* access modifiers changed from: protected */
    public final void removeRange(int i, int i2) {
        zzka();
        if (i2 >= i) {
            long[] jArr = this.zzaio;
            System.arraycopy(jArr, i2, jArr, i, this.size - i2);
            this.size -= i2 - i;
            this.modCount++;
            return;
        }
        throw new IndexOutOfBoundsException("toIndex < fromIndex");
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof zzfv)) {
            return super.equals(obj);
        }
        zzfv zzfv = (zzfv) obj;
        if (this.size != zzfv.size) {
            return false;
        }
        long[] jArr = zzfv.zzaio;
        for (int i = 0; i < this.size; i++) {
            if (this.zzaio[i] != jArr[i]) {
                return false;
            }
        }
        return true;
    }

    public final int hashCode() {
        int i = 1;
        for (int i2 = 0; i2 < this.size; i2++) {
            i = (i * 31) + zzfb.zzba(this.zzaio[i2]);
        }
        return i;
    }

    /* renamed from: zzav */
    public final zzff zzq(int i) {
        if (i >= this.size) {
            return new zzfv(Arrays.copyOf(this.zzaio, i), this.size);
        }
        throw new IllegalArgumentException();
    }

    public final long getLong(int i) {
        zzo(i);
        return this.zzaio[i];
    }

    public final int size() {
        return this.size;
    }

    public final void zzbb(long j) {
        zzk(this.size, j);
    }

    private final void zzk(int i, long j) {
        zzka();
        if (i >= 0) {
            int i2 = this.size;
            if (i <= i2) {
                long[] jArr = this.zzaio;
                if (i2 < jArr.length) {
                    System.arraycopy(jArr, i, jArr, i + 1, i2 - i);
                } else {
                    long[] jArr2 = new long[(((i2 * 3) / 2) + 1)];
                    System.arraycopy(jArr, 0, jArr2, 0, i);
                    System.arraycopy(this.zzaio, i, jArr2, i + 1, this.size - i);
                    this.zzaio = jArr2;
                }
                this.zzaio[i] = j;
                this.size++;
                this.modCount++;
                return;
            }
        }
        throw new IndexOutOfBoundsException(zzp(i));
    }

    public final boolean addAll(Collection<? extends Long> collection) {
        zzka();
        zzfb.checkNotNull(collection);
        if (!(collection instanceof zzfv)) {
            return super.addAll(collection);
        }
        zzfv zzfv = (zzfv) collection;
        int i = zzfv.size;
        if (i == 0) {
            return false;
        }
        int i2 = this.size;
        if (Integer.MAX_VALUE - i2 >= i) {
            int i3 = i2 + i;
            long[] jArr = this.zzaio;
            if (i3 > jArr.length) {
                this.zzaio = Arrays.copyOf(jArr, i3);
            }
            System.arraycopy(zzfv.zzaio, 0, this.zzaio, this.size, zzfv.size);
            this.size = i3;
            this.modCount++;
            return true;
        }
        throw new OutOfMemoryError();
    }

    public final boolean remove(Object obj) {
        zzka();
        for (int i = 0; i < this.size; i++) {
            if (obj.equals(Long.valueOf(this.zzaio[i]))) {
                long[] jArr = this.zzaio;
                System.arraycopy(jArr, i + 1, jArr, i, (this.size - i) - 1);
                this.size--;
                this.modCount++;
                return true;
            }
        }
        return false;
    }

    private final void zzo(int i) {
        if (i < 0 || i >= this.size) {
            throw new IndexOutOfBoundsException(zzp(i));
        }
    }

    private final String zzp(int i) {
        int i2 = this.size;
        StringBuilder sb = new StringBuilder(35);
        sb.append("Index:");
        sb.append(i);
        sb.append(", Size:");
        sb.append(i2);
        return sb.toString();
    }

    public final /* synthetic */ Object set(int i, Object obj) {
        long longValue = ((Long) obj).longValue();
        zzka();
        zzo(i);
        long[] jArr = this.zzaio;
        long j = jArr[i];
        jArr[i] = longValue;
        return Long.valueOf(j);
    }

    public final /* synthetic */ Object remove(int i) {
        zzka();
        zzo(i);
        long[] jArr = this.zzaio;
        long j = jArr[i];
        int i2 = this.size;
        if (i < i2 - 1) {
            System.arraycopy(jArr, i + 1, jArr, i, (i2 - i) - 1);
        }
        this.size--;
        this.modCount++;
        return Long.valueOf(j);
    }

    public final /* synthetic */ void add(int i, Object obj) {
        zzk(i, ((Long) obj).longValue());
    }

    public final /* synthetic */ Object get(int i) {
        return Long.valueOf(getLong(i));
    }

    static {
        zzfv zzfv = new zzfv(new long[0], 0);
        zzain = zzfv;
        zzfv.zzjz();
    }
}
