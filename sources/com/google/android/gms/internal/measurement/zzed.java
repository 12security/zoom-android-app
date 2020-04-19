package com.google.android.gms.internal.measurement;

import com.google.common.base.Ascii;
import com.google.common.primitives.UnsignedBytes;
import java.io.IOException;
import java.util.Arrays;

final class zzed extends zzeb {
    private final byte[] buffer;
    private int limit;
    private int pos;
    private final boolean zzacm;
    private int zzacn;
    private int zzaco;
    private int zzacp;
    private int zzacq;

    private zzed(byte[] bArr, int i, int i2, boolean z) {
        super();
        this.zzacq = Integer.MAX_VALUE;
        this.buffer = bArr;
        this.limit = i2 + i;
        this.pos = i;
        this.zzaco = this.pos;
        this.zzacm = z;
    }

    public final int zzkj() throws IOException {
        if (zzkz()) {
            this.zzacp = 0;
            return 0;
        }
        this.zzacp = zzlb();
        int i = this.zzacp;
        if ((i >>> 3) != 0) {
            return i;
        }
        throw zzfh.zzmx();
    }

    public final void zzu(int i) throws zzfh {
        if (this.zzacp != i) {
            throw zzfh.zzmy();
        }
    }

    public final boolean zzv(int i) throws IOException {
        int zzkj;
        int i2 = 0;
        switch (i & 7) {
            case 0:
                if (this.limit - this.pos >= 10) {
                    while (i2 < 10) {
                        byte[] bArr = this.buffer;
                        int i3 = this.pos;
                        this.pos = i3 + 1;
                        if (bArr[i3] < 0) {
                            i2++;
                        }
                    }
                    throw zzfh.zzmw();
                }
                while (i2 < 10) {
                    if (zzlg() < 0) {
                        i2++;
                    }
                }
                throw zzfh.zzmw();
                return true;
            case 1:
                zzz(8);
                return true;
            case 2:
                zzz(zzlb());
                return true;
            case 3:
                break;
            case 4:
                return false;
            case 5:
                zzz(4);
                return true;
            default:
                throw zzfh.zzmz();
        }
        do {
            zzkj = zzkj();
            if (zzkj != 0) {
            }
            zzu(((i >>> 3) << 3) | 4);
            return true;
        } while (zzv(zzkj));
        zzu(((i >>> 3) << 3) | 4);
        return true;
    }

    public final double readDouble() throws IOException {
        return Double.longBitsToDouble(zzle());
    }

    public final float readFloat() throws IOException {
        return Float.intBitsToFloat(zzld());
    }

    public final long zzkk() throws IOException {
        return zzlc();
    }

    public final long zzkl() throws IOException {
        return zzlc();
    }

    public final int zzkm() throws IOException {
        return zzlb();
    }

    public final long zzkn() throws IOException {
        return zzle();
    }

    public final int zzko() throws IOException {
        return zzld();
    }

    public final boolean zzkp() throws IOException {
        return zzlc() != 0;
    }

    public final String readString() throws IOException {
        int zzlb = zzlb();
        if (zzlb > 0) {
            int i = this.limit;
            int i2 = this.pos;
            if (zzlb <= i - i2) {
                String str = new String(this.buffer, i2, zzlb, zzfb.UTF_8);
                this.pos += zzlb;
                return str;
            }
        }
        if (zzlb == 0) {
            return "";
        }
        if (zzlb < 0) {
            throw zzfh.zzmv();
        }
        throw zzfh.zzmu();
    }

    public final String zzkq() throws IOException {
        int zzlb = zzlb();
        if (zzlb > 0) {
            int i = this.limit;
            int i2 = this.pos;
            if (zzlb <= i - i2) {
                String zzh = zzhy.zzh(this.buffer, i2, zzlb);
                this.pos += zzlb;
                return zzh;
            }
        }
        if (zzlb == 0) {
            return "";
        }
        if (zzlb <= 0) {
            throw zzfh.zzmv();
        }
        throw zzfh.zzmu();
    }

    public final <T extends zzgh> T zza(zzgs<T> zzgs, zzem zzem) throws IOException {
        int zzlb = zzlb();
        if (this.zzach < this.zzaci) {
            int zzx = zzx(zzlb);
            this.zzach++;
            T t = (zzgh) zzgs.zza(this, zzem);
            zzu(0);
            this.zzach--;
            zzy(zzx);
            return t;
        }
        throw zzfh.zzna();
    }

    public final zzdp zzkr() throws IOException {
        byte[] bArr;
        int zzlb = zzlb();
        if (zzlb > 0) {
            int i = this.limit;
            int i2 = this.pos;
            if (zzlb <= i - i2) {
                zzdp zzb = zzdp.zzb(this.buffer, i2, zzlb);
                this.pos += zzlb;
                return zzb;
            }
        }
        if (zzlb == 0) {
            return zzdp.zzaby;
        }
        if (zzlb > 0) {
            int i3 = this.limit;
            int i4 = this.pos;
            if (zzlb <= i3 - i4) {
                this.pos = zzlb + i4;
                bArr = Arrays.copyOfRange(this.buffer, i4, this.pos);
                return zzdp.zzg(bArr);
            }
        }
        if (zzlb > 0) {
            throw zzfh.zzmu();
        } else if (zzlb == 0) {
            bArr = zzfb.zzahk;
            return zzdp.zzg(bArr);
        } else {
            throw zzfh.zzmv();
        }
    }

    public final int zzks() throws IOException {
        return zzlb();
    }

    public final int zzkt() throws IOException {
        return zzlb();
    }

    public final int zzku() throws IOException {
        return zzld();
    }

    public final long zzkv() throws IOException {
        return zzle();
    }

    public final int zzkw() throws IOException {
        return zzaa(zzlb());
    }

    public final long zzkx() throws IOException {
        return zzap(zzlc());
    }

    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0066, code lost:
        if (r2[r3] >= 0) goto L_0x006a;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final int zzlb() throws java.io.IOException {
        /*
            r5 = this;
            int r0 = r5.pos
            int r1 = r5.limit
            if (r1 == r0) goto L_0x006d
            byte[] r2 = r5.buffer
            int r3 = r0 + 1
            byte r0 = r2[r0]
            if (r0 < 0) goto L_0x0011
            r5.pos = r3
            return r0
        L_0x0011:
            int r1 = r1 - r3
            r4 = 9
            if (r1 < r4) goto L_0x006d
            int r1 = r3 + 1
            byte r3 = r2[r3]
            int r3 = r3 << 7
            r0 = r0 ^ r3
            if (r0 >= 0) goto L_0x0022
            r0 = r0 ^ -128(0xffffffffffffff80, float:NaN)
            goto L_0x006a
        L_0x0022:
            int r3 = r1 + 1
            byte r1 = r2[r1]
            int r1 = r1 << 14
            r0 = r0 ^ r1
            if (r0 < 0) goto L_0x002f
            r0 = r0 ^ 16256(0x3f80, float:2.278E-41)
            r1 = r3
            goto L_0x006a
        L_0x002f:
            int r1 = r3 + 1
            byte r3 = r2[r3]
            int r3 = r3 << 21
            r0 = r0 ^ r3
            if (r0 >= 0) goto L_0x003d
            r2 = -2080896(0xffffffffffe03f80, float:NaN)
            r0 = r0 ^ r2
            goto L_0x006a
        L_0x003d:
            int r3 = r1 + 1
            byte r1 = r2[r1]
            int r4 = r1 << 28
            r0 = r0 ^ r4
            r4 = 266354560(0xfe03f80, float:2.2112565E-29)
            r0 = r0 ^ r4
            if (r1 >= 0) goto L_0x0069
            int r1 = r3 + 1
            byte r3 = r2[r3]
            if (r3 >= 0) goto L_0x006a
            int r3 = r1 + 1
            byte r1 = r2[r1]
            if (r1 >= 0) goto L_0x0069
            int r1 = r3 + 1
            byte r3 = r2[r3]
            if (r3 >= 0) goto L_0x006a
            int r3 = r1 + 1
            byte r1 = r2[r1]
            if (r1 >= 0) goto L_0x0069
            int r1 = r3 + 1
            byte r2 = r2[r3]
            if (r2 < 0) goto L_0x006d
            goto L_0x006a
        L_0x0069:
            r1 = r3
        L_0x006a:
            r5.pos = r1
            return r0
        L_0x006d:
            long r0 = r5.zzky()
            int r0 = (int) r0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.measurement.zzed.zzlb():int");
    }

    private final long zzlc() throws IOException {
        long j;
        int i = this.pos;
        int i2 = this.limit;
        if (i2 != i) {
            byte[] bArr = this.buffer;
            int i3 = i + 1;
            byte b = bArr[i];
            if (b >= 0) {
                this.pos = i3;
                return (long) b;
            } else if (i2 - i3 >= 9) {
                int i4 = i3 + 1;
                byte b2 = b ^ (bArr[i3] << 7);
                if (b2 < 0) {
                    j = (long) (b2 ^ UnsignedBytes.MAX_POWER_OF_TWO);
                } else {
                    int i5 = i4 + 1;
                    byte b3 = b2 ^ (bArr[i4] << Ascii.f229SO);
                    if (b3 >= 0) {
                        i4 = i5;
                        j = (long) (b3 ^ UnsignedBytes.MAX_POWER_OF_TWO);
                    } else {
                        i4 = i5 + 1;
                        byte b4 = b3 ^ (bArr[i5] << Ascii.NAK);
                        if (b4 < 0) {
                            j = (long) (b4 ^ UnsignedBytes.MAX_POWER_OF_TWO);
                        } else {
                            long j2 = (long) b4;
                            int i6 = i4 + 1;
                            long j3 = j2 ^ (((long) bArr[i4]) << 28);
                            if (j3 >= 0) {
                                j = j3 ^ 266354560;
                                i4 = i6;
                            } else {
                                i4 = i6 + 1;
                                long j4 = j3 ^ (((long) bArr[i6]) << 35);
                                if (j4 < 0) {
                                    j = j4 ^ -34093383808L;
                                } else {
                                    int i7 = i4 + 1;
                                    long j5 = j4 ^ (((long) bArr[i4]) << 42);
                                    if (j5 >= 0) {
                                        j = j5 ^ 4363953127296L;
                                        i4 = i7;
                                    } else {
                                        i4 = i7 + 1;
                                        long j6 = j5 ^ (((long) bArr[i7]) << 49);
                                        if (j6 < 0) {
                                            j = j6 ^ -558586000294016L;
                                        } else {
                                            int i8 = i4 + 1;
                                            long j7 = (j6 ^ (((long) bArr[i4]) << 56)) ^ 71499008037633920L;
                                            if (j7 < 0) {
                                                i4 = i8 + 1;
                                                if (((long) bArr[i8]) >= 0) {
                                                    j = j7;
                                                }
                                            } else {
                                                i4 = i8;
                                                j = j7;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                this.pos = i4;
                return j;
            }
        }
        return zzky();
    }

    /* access modifiers changed from: 0000 */
    public final long zzky() throws IOException {
        long j = 0;
        for (int i = 0; i < 64; i += 7) {
            byte zzlg = zzlg();
            j |= ((long) (zzlg & Ascii.DEL)) << i;
            if ((zzlg & UnsignedBytes.MAX_POWER_OF_TWO) == 0) {
                return j;
            }
        }
        throw zzfh.zzmw();
    }

    private final int zzld() throws IOException {
        int i = this.pos;
        if (this.limit - i >= 4) {
            byte[] bArr = this.buffer;
            this.pos = i + 4;
            return ((bArr[i + 3] & UnsignedBytes.MAX_VALUE) << Ascii.CAN) | (bArr[i] & UnsignedBytes.MAX_VALUE) | ((bArr[i + 1] & UnsignedBytes.MAX_VALUE) << 8) | ((bArr[i + 2] & UnsignedBytes.MAX_VALUE) << Ascii.DLE);
        }
        throw zzfh.zzmu();
    }

    private final long zzle() throws IOException {
        int i = this.pos;
        if (this.limit - i >= 8) {
            byte[] bArr = this.buffer;
            this.pos = i + 8;
            return ((((long) bArr[i + 7]) & 255) << 56) | (((long) bArr[i]) & 255) | ((((long) bArr[i + 1]) & 255) << 8) | ((((long) bArr[i + 2]) & 255) << 16) | ((((long) bArr[i + 3]) & 255) << 24) | ((((long) bArr[i + 4]) & 255) << 32) | ((((long) bArr[i + 5]) & 255) << 40) | ((((long) bArr[i + 6]) & 255) << 48);
        }
        throw zzfh.zzmu();
    }

    public final int zzx(int i) throws zzfh {
        if (i >= 0) {
            int zzla = i + zzla();
            int i2 = this.zzacq;
            if (zzla <= i2) {
                this.zzacq = zzla;
                zzlf();
                return i2;
            }
            throw zzfh.zzmu();
        }
        throw zzfh.zzmv();
    }

    private final void zzlf() {
        this.limit += this.zzacn;
        int i = this.limit;
        int i2 = i - this.zzaco;
        int i3 = this.zzacq;
        if (i2 > i3) {
            this.zzacn = i2 - i3;
            this.limit = i - this.zzacn;
            return;
        }
        this.zzacn = 0;
    }

    public final void zzy(int i) {
        this.zzacq = i;
        zzlf();
    }

    public final boolean zzkz() throws IOException {
        return this.pos == this.limit;
    }

    public final int zzla() {
        return this.pos - this.zzaco;
    }

    private final byte zzlg() throws IOException {
        int i = this.pos;
        if (i != this.limit) {
            byte[] bArr = this.buffer;
            this.pos = i + 1;
            return bArr[i];
        }
        throw zzfh.zzmu();
    }

    public final void zzz(int i) throws IOException {
        if (i >= 0) {
            int i2 = this.limit;
            int i3 = this.pos;
            if (i <= i2 - i3) {
                this.pos = i3 + i;
                return;
            }
        }
        if (i < 0) {
            throw zzfh.zzmv();
        }
        throw zzfh.zzmu();
    }
}
