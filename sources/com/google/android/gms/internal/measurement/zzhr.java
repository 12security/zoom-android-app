package com.google.android.gms.internal.measurement;

import com.google.android.gms.internal.measurement.zzez.zze;
import java.io.IOException;
import java.util.Arrays;

public final class zzhr {
    private static final zzhr zzakp = new zzhr(0, new int[0], new Object[0], false);
    private int count;
    private boolean zzabp;
    private int zzago;
    private Object[] zzajb;
    private int[] zzakq;

    public static zzhr zzor() {
        return zzakp;
    }

    static zzhr zzos() {
        return new zzhr();
    }

    static zzhr zza(zzhr zzhr, zzhr zzhr2) {
        int i = zzhr.count + zzhr2.count;
        int[] copyOf = Arrays.copyOf(zzhr.zzakq, i);
        System.arraycopy(zzhr2.zzakq, 0, copyOf, zzhr.count, zzhr2.count);
        Object[] copyOf2 = Arrays.copyOf(zzhr.zzajb, i);
        System.arraycopy(zzhr2.zzajb, 0, copyOf2, zzhr.count, zzhr2.count);
        return new zzhr(i, copyOf, copyOf2, true);
    }

    private zzhr() {
        this(0, new int[8], new Object[8], true);
    }

    private zzhr(int i, int[] iArr, Object[] objArr, boolean z) {
        this.zzago = -1;
        this.count = i;
        this.zzakq = iArr;
        this.zzajb = objArr;
        this.zzabp = z;
    }

    public final void zzjz() {
        this.zzabp = false;
    }

    /* access modifiers changed from: 0000 */
    public final void zza(zzil zzil) throws IOException {
        if (zzil.zzln() == zze.zzahg) {
            for (int i = this.count - 1; i >= 0; i--) {
                zzil.zza(this.zzakq[i] >>> 3, this.zzajb[i]);
            }
            return;
        }
        for (int i2 = 0; i2 < this.count; i2++) {
            zzil.zza(this.zzakq[i2] >>> 3, this.zzajb[i2]);
        }
    }

    public final void zzb(zzil zzil) throws IOException {
        if (this.count != 0) {
            if (zzil.zzln() == zze.zzahf) {
                for (int i = 0; i < this.count; i++) {
                    zzb(this.zzakq[i], this.zzajb[i], zzil);
                }
                return;
            }
            for (int i2 = this.count - 1; i2 >= 0; i2--) {
                zzb(this.zzakq[i2], this.zzajb[i2], zzil);
            }
        }
    }

    private static void zzb(int i, Object obj, zzil zzil) throws IOException {
        int i2 = i >>> 3;
        int i3 = i & 7;
        if (i3 != 5) {
            switch (i3) {
                case 0:
                    zzil.zzi(i2, ((Long) obj).longValue());
                    return;
                case 1:
                    zzil.zzc(i2, ((Long) obj).longValue());
                    return;
                case 2:
                    zzil.zza(i2, (zzdp) obj);
                    return;
                case 3:
                    if (zzil.zzln() == zze.zzahf) {
                        zzil.zzas(i2);
                        ((zzhr) obj).zzb(zzil);
                        zzil.zzat(i2);
                        return;
                    }
                    zzil.zzat(i2);
                    ((zzhr) obj).zzb(zzil);
                    zzil.zzas(i2);
                    return;
                default:
                    throw new RuntimeException(zzfh.zzmz());
            }
        } else {
            zzil.zzf(i2, ((Integer) obj).intValue());
        }
    }

    public final int zzot() {
        int i = this.zzago;
        if (i != -1) {
            return i;
        }
        int i2 = 0;
        for (int i3 = 0; i3 < this.count; i3++) {
            i2 += zzeg.zzd(this.zzakq[i3] >>> 3, (zzdp) this.zzajb[i3]);
        }
        this.zzago = i2;
        return i2;
    }

    public final int zzly() {
        int i;
        int i2 = this.zzago;
        if (i2 != -1) {
            return i2;
        }
        int i3 = 0;
        for (int i4 = 0; i4 < this.count; i4++) {
            int i5 = this.zzakq[i4];
            int i6 = i5 >>> 3;
            int i7 = i5 & 7;
            if (i7 != 5) {
                switch (i7) {
                    case 0:
                        i = zzeg.zze(i6, ((Long) this.zzajb[i4]).longValue());
                        break;
                    case 1:
                        i = zzeg.zzg(i6, ((Long) this.zzajb[i4]).longValue());
                        break;
                    case 2:
                        i = zzeg.zzc(i6, (zzdp) this.zzajb[i4]);
                        break;
                    case 3:
                        i = (zzeg.zzaj(i6) << 1) + ((zzhr) this.zzajb[i4]).zzly();
                        break;
                    default:
                        throw new IllegalStateException(zzfh.zzmz());
                }
            } else {
                i = zzeg.zzj(i6, ((Integer) this.zzajb[i4]).intValue());
            }
            i3 += i;
        }
        this.zzago = i3;
        return i3;
    }

    public final boolean equals(Object obj) {
        boolean z;
        boolean z2;
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof zzhr)) {
            return false;
        }
        zzhr zzhr = (zzhr) obj;
        int i = this.count;
        if (i == zzhr.count) {
            int[] iArr = this.zzakq;
            int[] iArr2 = zzhr.zzakq;
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
                Object[] objArr = this.zzajb;
                Object[] objArr2 = zzhr.zzajb;
                int i3 = this.count;
                int i4 = 0;
                while (true) {
                    if (i4 >= i3) {
                        z2 = true;
                        break;
                    } else if (!objArr[i4].equals(objArr2[i4])) {
                        z2 = false;
                        break;
                    } else {
                        i4++;
                    }
                }
                return z2;
            }
        }
    }

    public final int hashCode() {
        int i = this.count;
        int i2 = (i + 527) * 31;
        int[] iArr = this.zzakq;
        int i3 = 17;
        int i4 = 17;
        for (int i5 = 0; i5 < i; i5++) {
            i4 = (i4 * 31) + iArr[i5];
        }
        int i6 = (i2 + i4) * 31;
        Object[] objArr = this.zzajb;
        for (int i7 = 0; i7 < this.count; i7++) {
            i3 = (i3 * 31) + objArr[i7].hashCode();
        }
        return i6 + i3;
    }

    /* access modifiers changed from: 0000 */
    public final void zzb(StringBuilder sb, int i) {
        for (int i2 = 0; i2 < this.count; i2++) {
            zzgk.zzb(sb, i, String.valueOf(this.zzakq[i2] >>> 3), this.zzajb[i2]);
        }
    }

    /* access modifiers changed from: 0000 */
    public final void zzb(int i, Object obj) {
        if (this.zzabp) {
            int i2 = this.count;
            if (i2 == this.zzakq.length) {
                int i3 = this.count + (i2 < 4 ? 8 : i2 >> 1);
                this.zzakq = Arrays.copyOf(this.zzakq, i3);
                this.zzajb = Arrays.copyOf(this.zzajb, i3);
            }
            int[] iArr = this.zzakq;
            int i4 = this.count;
            iArr[i4] = i;
            this.zzajb[i4] = obj;
            this.count = i4 + 1;
            return;
        }
        throw new UnsupportedOperationException();
    }
}
