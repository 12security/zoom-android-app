package com.google.android.gms.internal.measurement;

import java.util.Arrays;
import java.util.Collection;
import java.util.RandomAccess;

final class zzew extends zzdj<Float> implements zzfg<Float>, zzgt, RandomAccess {
    private static final zzew zzagj;
    private int size;
    private float[] zzagk;

    zzew() {
        this(new float[10], 0);
    }

    private zzew(float[] fArr, int i) {
        this.zzagk = fArr;
        this.size = i;
    }

    /* access modifiers changed from: protected */
    public final void removeRange(int i, int i2) {
        zzka();
        if (i2 >= i) {
            float[] fArr = this.zzagk;
            System.arraycopy(fArr, i2, fArr, i, this.size - i2);
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
        if (!(obj instanceof zzew)) {
            return super.equals(obj);
        }
        zzew zzew = (zzew) obj;
        if (this.size != zzew.size) {
            return false;
        }
        float[] fArr = zzew.zzagk;
        for (int i = 0; i < this.size; i++) {
            if (Float.floatToIntBits(this.zzagk[i]) != Float.floatToIntBits(fArr[i])) {
                return false;
            }
        }
        return true;
    }

    public final int hashCode() {
        int i = 1;
        for (int i2 = 0; i2 < this.size; i2++) {
            i = (i * 31) + Float.floatToIntBits(this.zzagk[i2]);
        }
        return i;
    }

    public final int size() {
        return this.size;
    }

    public final void zzc(float f) {
        zzc(this.size, f);
    }

    private final void zzc(int i, float f) {
        zzka();
        if (i >= 0) {
            int i2 = this.size;
            if (i <= i2) {
                float[] fArr = this.zzagk;
                if (i2 < fArr.length) {
                    System.arraycopy(fArr, i, fArr, i + 1, i2 - i);
                } else {
                    float[] fArr2 = new float[(((i2 * 3) / 2) + 1)];
                    System.arraycopy(fArr, 0, fArr2, 0, i);
                    System.arraycopy(this.zzagk, i, fArr2, i + 1, this.size - i);
                    this.zzagk = fArr2;
                }
                this.zzagk[i] = f;
                this.size++;
                this.modCount++;
                return;
            }
        }
        throw new IndexOutOfBoundsException(zzp(i));
    }

    public final boolean addAll(Collection<? extends Float> collection) {
        zzka();
        zzfb.checkNotNull(collection);
        if (!(collection instanceof zzew)) {
            return super.addAll(collection);
        }
        zzew zzew = (zzew) collection;
        int i = zzew.size;
        if (i == 0) {
            return false;
        }
        int i2 = this.size;
        if (Integer.MAX_VALUE - i2 >= i) {
            int i3 = i2 + i;
            float[] fArr = this.zzagk;
            if (i3 > fArr.length) {
                this.zzagk = Arrays.copyOf(fArr, i3);
            }
            System.arraycopy(zzew.zzagk, 0, this.zzagk, this.size, zzew.size);
            this.size = i3;
            this.modCount++;
            return true;
        }
        throw new OutOfMemoryError();
    }

    public final boolean remove(Object obj) {
        zzka();
        for (int i = 0; i < this.size; i++) {
            if (obj.equals(Float.valueOf(this.zzagk[i]))) {
                float[] fArr = this.zzagk;
                System.arraycopy(fArr, i + 1, fArr, i, (this.size - i) - 1);
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
        float floatValue = ((Float) obj).floatValue();
        zzka();
        zzo(i);
        float[] fArr = this.zzagk;
        float f = fArr[i];
        fArr[i] = floatValue;
        return Float.valueOf(f);
    }

    public final /* synthetic */ Object remove(int i) {
        zzka();
        zzo(i);
        float[] fArr = this.zzagk;
        float f = fArr[i];
        int i2 = this.size;
        if (i < i2 - 1) {
            System.arraycopy(fArr, i + 1, fArr, i, (i2 - i) - 1);
        }
        this.size--;
        this.modCount++;
        return Float.valueOf(f);
    }

    public final /* synthetic */ void add(int i, Object obj) {
        zzc(i, ((Float) obj).floatValue());
    }

    public final /* synthetic */ zzfg zzq(int i) {
        if (i >= this.size) {
            return new zzew(Arrays.copyOf(this.zzagk, i), this.size);
        }
        throw new IllegalArgumentException();
    }

    public final /* synthetic */ Object get(int i) {
        zzo(i);
        return Float.valueOf(this.zzagk[i]);
    }

    static {
        zzew zzew = new zzew(new float[0], 0);
        zzagj = zzew;
        zzew.zzjz();
    }
}
