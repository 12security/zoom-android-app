package com.google.android.gms.internal.measurement;

import java.util.Arrays;
import java.util.Collection;
import java.util.RandomAccess;

final class zzfa extends zzdj<Integer> implements zzfg<Integer>, zzgt, RandomAccess {
    private static final zzfa zzahi;
    private int size;
    private int[] zzahj;

    zzfa() {
        this(new int[10], 0);
    }

    private zzfa(int[] iArr, int i) {
        this.zzahj = iArr;
        this.size = i;
    }

    /* access modifiers changed from: protected */
    public final void removeRange(int i, int i2) {
        zzka();
        if (i2 >= i) {
            int[] iArr = this.zzahj;
            System.arraycopy(iArr, i2, iArr, i, this.size - i2);
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
        if (!(obj instanceof zzfa)) {
            return super.equals(obj);
        }
        zzfa zzfa = (zzfa) obj;
        if (this.size != zzfa.size) {
            return false;
        }
        int[] iArr = zzfa.zzahj;
        for (int i = 0; i < this.size; i++) {
            if (this.zzahj[i] != iArr[i]) {
                return false;
            }
        }
        return true;
    }

    public final int hashCode() {
        int i = 1;
        for (int i2 = 0; i2 < this.size; i2++) {
            i = (i * 31) + this.zzahj[i2];
        }
        return i;
    }

    public final int getInt(int i) {
        zzo(i);
        return this.zzahj[i];
    }

    public final int size() {
        return this.size;
    }

    public final void zzau(int i) {
        zzo(this.size, i);
    }

    private final void zzo(int i, int i2) {
        zzka();
        if (i >= 0) {
            int i3 = this.size;
            if (i <= i3) {
                int[] iArr = this.zzahj;
                if (i3 < iArr.length) {
                    System.arraycopy(iArr, i, iArr, i + 1, i3 - i);
                } else {
                    int[] iArr2 = new int[(((i3 * 3) / 2) + 1)];
                    System.arraycopy(iArr, 0, iArr2, 0, i);
                    System.arraycopy(this.zzahj, i, iArr2, i + 1, this.size - i);
                    this.zzahj = iArr2;
                }
                this.zzahj[i] = i2;
                this.size++;
                this.modCount++;
                return;
            }
        }
        throw new IndexOutOfBoundsException(zzp(i));
    }

    public final boolean addAll(Collection<? extends Integer> collection) {
        zzka();
        zzfb.checkNotNull(collection);
        if (!(collection instanceof zzfa)) {
            return super.addAll(collection);
        }
        zzfa zzfa = (zzfa) collection;
        int i = zzfa.size;
        if (i == 0) {
            return false;
        }
        int i2 = this.size;
        if (Integer.MAX_VALUE - i2 >= i) {
            int i3 = i2 + i;
            int[] iArr = this.zzahj;
            if (i3 > iArr.length) {
                this.zzahj = Arrays.copyOf(iArr, i3);
            }
            System.arraycopy(zzfa.zzahj, 0, this.zzahj, this.size, zzfa.size);
            this.size = i3;
            this.modCount++;
            return true;
        }
        throw new OutOfMemoryError();
    }

    public final boolean remove(Object obj) {
        zzka();
        for (int i = 0; i < this.size; i++) {
            if (obj.equals(Integer.valueOf(this.zzahj[i]))) {
                int[] iArr = this.zzahj;
                System.arraycopy(iArr, i + 1, iArr, i, (this.size - i) - 1);
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
        int intValue = ((Integer) obj).intValue();
        zzka();
        zzo(i);
        int[] iArr = this.zzahj;
        int i2 = iArr[i];
        iArr[i] = intValue;
        return Integer.valueOf(i2);
    }

    public final /* synthetic */ Object remove(int i) {
        zzka();
        zzo(i);
        int[] iArr = this.zzahj;
        int i2 = iArr[i];
        int i3 = this.size;
        if (i < i3 - 1) {
            System.arraycopy(iArr, i + 1, iArr, i, (i3 - i) - 1);
        }
        this.size--;
        this.modCount++;
        return Integer.valueOf(i2);
    }

    public final /* synthetic */ void add(int i, Object obj) {
        zzo(i, ((Integer) obj).intValue());
    }

    public final /* synthetic */ zzfg zzq(int i) {
        if (i >= this.size) {
            return new zzfa(Arrays.copyOf(this.zzahj, i), this.size);
        }
        throw new IllegalArgumentException();
    }

    public final /* synthetic */ Object get(int i) {
        return Integer.valueOf(getInt(i));
    }

    static {
        zzfa zzfa = new zzfa(new int[0], 0);
        zzahi = zzfa;
        zzfa.zzjz();
    }
}
