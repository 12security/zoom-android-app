package com.google.android.gms.internal.measurement;

public final class zzir implements Cloneable {
    private static final zzis zzanf = new zzis();
    private int mSize;
    private boolean zzang;
    private int[] zzanh;
    private zzis[] zzani;

    zzir() {
        this(10);
    }

    private zzir(int i) {
        this.zzang = false;
        int idealIntArraySize = idealIntArraySize(i);
        this.zzanh = new int[idealIntArraySize];
        this.zzani = new zzis[idealIntArraySize];
        this.mSize = 0;
    }

    /* access modifiers changed from: 0000 */
    public final zzis zzbm(int i) {
        int zzbo = zzbo(i);
        if (zzbo >= 0) {
            zzis[] zzisArr = this.zzani;
            if (zzisArr[zzbo] != zzanf) {
                return zzisArr[zzbo];
            }
        }
        return null;
    }

    /* access modifiers changed from: 0000 */
    public final void zza(int i, zzis zzis) {
        int zzbo = zzbo(i);
        if (zzbo >= 0) {
            this.zzani[zzbo] = zzis;
            return;
        }
        int i2 = ~zzbo;
        if (i2 < this.mSize) {
            zzis[] zzisArr = this.zzani;
            if (zzisArr[i2] == zzanf) {
                this.zzanh[i2] = i;
                zzisArr[i2] = zzis;
                return;
            }
        }
        int i3 = this.mSize;
        if (i3 >= this.zzanh.length) {
            int idealIntArraySize = idealIntArraySize(i3 + 1);
            int[] iArr = new int[idealIntArraySize];
            zzis[] zzisArr2 = new zzis[idealIntArraySize];
            int[] iArr2 = this.zzanh;
            System.arraycopy(iArr2, 0, iArr, 0, iArr2.length);
            zzis[] zzisArr3 = this.zzani;
            System.arraycopy(zzisArr3, 0, zzisArr2, 0, zzisArr3.length);
            this.zzanh = iArr;
            this.zzani = zzisArr2;
        }
        int i4 = this.mSize;
        if (i4 - i2 != 0) {
            int[] iArr3 = this.zzanh;
            int i5 = i2 + 1;
            System.arraycopy(iArr3, i2, iArr3, i5, i4 - i2);
            zzis[] zzisArr4 = this.zzani;
            System.arraycopy(zzisArr4, i2, zzisArr4, i5, this.mSize - i2);
        }
        this.zzanh[i2] = i;
        this.zzani[i2] = zzis;
        this.mSize++;
    }

    /* access modifiers changed from: 0000 */
    public final int size() {
        return this.mSize;
    }

    public final boolean isEmpty() {
        return this.mSize == 0;
    }

    /* access modifiers changed from: 0000 */
    public final zzis zzbn(int i) {
        return this.zzani[i];
    }

    public final boolean equals(Object obj) {
        boolean z;
        boolean z2;
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzir)) {
            return false;
        }
        zzir zzir = (zzir) obj;
        int i = this.mSize;
        if (i != zzir.mSize) {
            return false;
        }
        int[] iArr = this.zzanh;
        int[] iArr2 = zzir.zzanh;
        int i2 = 0;
        while (true) {
            if (i2 >= i) {
                z = true;
                break;
            } else if (iArr[i2] != iArr2[i2]) {
                z = false;
                break;
            } else {
                i2++;
            }
        }
        if (z) {
            zzis[] zzisArr = this.zzani;
            zzis[] zzisArr2 = zzir.zzani;
            int i3 = this.mSize;
            int i4 = 0;
            while (true) {
                if (i4 >= i3) {
                    z2 = true;
                    break;
                } else if (!zzisArr[i4].equals(zzisArr2[i4])) {
                    z2 = false;
                    break;
                } else {
                    i4++;
                }
            }
            if (z2) {
                return true;
            }
        }
        return false;
    }

    public final int hashCode() {
        int i = 17;
        for (int i2 = 0; i2 < this.mSize; i2++) {
            i = (((i * 31) + this.zzanh[i2]) * 31) + this.zzani[i2].hashCode();
        }
        return i;
    }

    private static int idealIntArraySize(int i) {
        int i2 = i << 2;
        int i3 = 4;
        while (true) {
            if (i3 >= 32) {
                break;
            }
            int i4 = (1 << i3) - 12;
            if (i2 <= i4) {
                i2 = i4;
                break;
            }
            i3++;
        }
        return i2 / 4;
    }

    private final int zzbo(int i) {
        int i2 = this.mSize - 1;
        int i3 = 0;
        while (i3 <= i2) {
            int i4 = (i3 + i2) >>> 1;
            int i5 = this.zzanh[i4];
            if (i5 < i) {
                i3 = i4 + 1;
            } else if (i5 <= i) {
                return i4;
            } else {
                i2 = i4 - 1;
            }
        }
        return ~i3;
    }

    public final /* synthetic */ Object clone() throws CloneNotSupportedException {
        int i = this.mSize;
        zzir zzir = new zzir(i);
        System.arraycopy(this.zzanh, 0, zzir.zzanh, 0, i);
        for (int i2 = 0; i2 < i; i2++) {
            zzis[] zzisArr = this.zzani;
            if (zzisArr[i2] != null) {
                zzir.zzani[i2] = (zzis) zzisArr[i2].clone();
            }
        }
        zzir.mSize = i;
        return zzir;
    }
}
