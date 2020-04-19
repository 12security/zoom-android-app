package com.google.android.gms.internal.measurement;

import java.util.Arrays;
import java.util.Collection;
import java.util.RandomAccess;

final class zzej extends zzdj<Double> implements zzfg<Double>, zzgt, RandomAccess {
    private static final zzej zzadg;
    private int size;
    private double[] zzadh;

    zzej() {
        this(new double[10], 0);
    }

    private zzej(double[] dArr, int i) {
        this.zzadh = dArr;
        this.size = i;
    }

    /* access modifiers changed from: protected */
    public final void removeRange(int i, int i2) {
        zzka();
        if (i2 >= i) {
            double[] dArr = this.zzadh;
            System.arraycopy(dArr, i2, dArr, i, this.size - i2);
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
        if (!(obj instanceof zzej)) {
            return super.equals(obj);
        }
        zzej zzej = (zzej) obj;
        if (this.size != zzej.size) {
            return false;
        }
        double[] dArr = zzej.zzadh;
        for (int i = 0; i < this.size; i++) {
            if (Double.doubleToLongBits(this.zzadh[i]) != Double.doubleToLongBits(dArr[i])) {
                return false;
            }
        }
        return true;
    }

    public final int hashCode() {
        int i = 1;
        for (int i2 = 0; i2 < this.size; i2++) {
            i = (i * 31) + zzfb.zzba(Double.doubleToLongBits(this.zzadh[i2]));
        }
        return i;
    }

    public final int size() {
        return this.size;
    }

    public final void zzf(double d) {
        zzc(this.size, d);
    }

    private final void zzc(int i, double d) {
        zzka();
        if (i >= 0) {
            int i2 = this.size;
            if (i <= i2) {
                double[] dArr = this.zzadh;
                if (i2 < dArr.length) {
                    System.arraycopy(dArr, i, dArr, i + 1, i2 - i);
                } else {
                    double[] dArr2 = new double[(((i2 * 3) / 2) + 1)];
                    System.arraycopy(dArr, 0, dArr2, 0, i);
                    System.arraycopy(this.zzadh, i, dArr2, i + 1, this.size - i);
                    this.zzadh = dArr2;
                }
                this.zzadh[i] = d;
                this.size++;
                this.modCount++;
                return;
            }
        }
        throw new IndexOutOfBoundsException(zzp(i));
    }

    public final boolean addAll(Collection<? extends Double> collection) {
        zzka();
        zzfb.checkNotNull(collection);
        if (!(collection instanceof zzej)) {
            return super.addAll(collection);
        }
        zzej zzej = (zzej) collection;
        int i = zzej.size;
        if (i == 0) {
            return false;
        }
        int i2 = this.size;
        if (Integer.MAX_VALUE - i2 >= i) {
            int i3 = i2 + i;
            double[] dArr = this.zzadh;
            if (i3 > dArr.length) {
                this.zzadh = Arrays.copyOf(dArr, i3);
            }
            System.arraycopy(zzej.zzadh, 0, this.zzadh, this.size, zzej.size);
            this.size = i3;
            this.modCount++;
            return true;
        }
        throw new OutOfMemoryError();
    }

    public final boolean remove(Object obj) {
        zzka();
        for (int i = 0; i < this.size; i++) {
            if (obj.equals(Double.valueOf(this.zzadh[i]))) {
                double[] dArr = this.zzadh;
                System.arraycopy(dArr, i + 1, dArr, i, (this.size - i) - 1);
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
        double doubleValue = ((Double) obj).doubleValue();
        zzka();
        zzo(i);
        double[] dArr = this.zzadh;
        double d = dArr[i];
        dArr[i] = doubleValue;
        return Double.valueOf(d);
    }

    public final /* synthetic */ Object remove(int i) {
        zzka();
        zzo(i);
        double[] dArr = this.zzadh;
        double d = dArr[i];
        int i2 = this.size;
        if (i < i2 - 1) {
            System.arraycopy(dArr, i + 1, dArr, i, (i2 - i) - 1);
        }
        this.size--;
        this.modCount++;
        return Double.valueOf(d);
    }

    public final /* synthetic */ void add(int i, Object obj) {
        zzc(i, ((Double) obj).doubleValue());
    }

    public final /* synthetic */ zzfg zzq(int i) {
        if (i >= this.size) {
            return new zzej(Arrays.copyOf(this.zzadh, i), this.size);
        }
        throw new IllegalArgumentException();
    }

    public final /* synthetic */ Object get(int i) {
        zzo(i);
        return Double.valueOf(this.zzadh[i]);
    }

    static {
        zzej zzej = new zzej(new double[0], 0);
        zzadg = zzej;
        zzej.zzjz();
    }
}
