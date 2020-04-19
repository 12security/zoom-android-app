package com.google.android.gms.internal.measurement;

import com.google.common.base.Ascii;
import com.google.common.primitives.UnsignedBytes;
import java.io.IOException;

public final class zzim {
    private final byte[] buffer;
    private int zzach;
    private int zzaci = 64;
    private int zzacj = 67108864;
    private int zzacn;
    private int zzacp;
    private int zzacq = Integer.MAX_VALUE;
    private final int zzamw;
    private final int zzamx;
    private int zzamy;
    private int zzamz;
    private zzeb zzana;

    public static zzim zzj(byte[] bArr, int i, int i2) {
        return new zzim(bArr, 0, i2);
    }

    public final int zzkj() throws IOException {
        if (this.zzamz == this.zzamy) {
            this.zzacp = 0;
            return 0;
        }
        this.zzacp = zzlb();
        int i = this.zzacp;
        if (i != 0) {
            return i;
        }
        throw new zziu("Protocol message contained an invalid tag (zero).");
    }

    public final void zzu(int i) throws zziu {
        if (this.zzacp != i) {
            throw new zziu("Protocol message end-group tag did not match expected tag.");
        }
    }

    public final boolean zzv(int i) throws IOException {
        int zzkj;
        switch (i & 7) {
            case 0:
                zzlb();
                return true;
            case 1:
                zzlg();
                zzlg();
                zzlg();
                zzlg();
                zzlg();
                zzlg();
                zzlg();
                zzlg();
                return true;
            case 2:
                zzz(zzlb());
                return true;
            case 3:
                break;
            case 4:
                return false;
            case 5:
                zzlg();
                zzlg();
                zzlg();
                zzlg();
                return true;
            default:
                throw new zziu("Protocol message tag had invalid wire type.");
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

    public final boolean zzkp() throws IOException {
        return zzlb() != 0;
    }

    public final String readString() throws IOException {
        int zzlb = zzlb();
        if (zzlb >= 0) {
            int i = this.zzamy;
            int i2 = this.zzamz;
            if (zzlb <= i - i2) {
                String str = new String(this.buffer, i2, zzlb, zzit.UTF_8);
                this.zzamz += zzlb;
                return str;
            }
            throw zziu.zzpg();
        }
        throw zziu.zzph();
    }

    public final void zza(zziv zziv) throws IOException {
        int zzlb = zzlb();
        if (this.zzach < this.zzaci) {
            int zzx = zzx(zzlb);
            this.zzach++;
            zziv.zza(this);
            zzu(0);
            this.zzach--;
            zzy(zzx);
            return;
        }
        throw new zziu("Protocol message had too many levels of nesting.  May be malicious.  Use CodedInputStream.setRecursionLimit() to increase the depth limit.");
    }

    public final int zzlb() throws IOException {
        byte b;
        byte zzlg = zzlg();
        if (zzlg >= 0) {
            return zzlg;
        }
        byte b2 = zzlg & Ascii.DEL;
        byte zzlg2 = zzlg();
        if (zzlg2 >= 0) {
            b = b2 | (zzlg2 << 7);
        } else {
            byte b3 = b2 | ((zzlg2 & Ascii.DEL) << 7);
            byte zzlg3 = zzlg();
            if (zzlg3 >= 0) {
                b = b3 | (zzlg3 << Ascii.f229SO);
            } else {
                byte b4 = b3 | ((zzlg3 & Ascii.DEL) << Ascii.f229SO);
                byte zzlg4 = zzlg();
                if (zzlg4 >= 0) {
                    b = b4 | (zzlg4 << Ascii.NAK);
                } else {
                    byte b5 = b4 | ((zzlg4 & Ascii.DEL) << Ascii.NAK);
                    byte zzlg5 = zzlg();
                    b = b5 | (zzlg5 << Ascii.f222FS);
                    if (zzlg5 < 0) {
                        for (int i = 0; i < 5; i++) {
                            if (zzlg() >= 0) {
                                return b;
                            }
                        }
                        throw zziu.zzpi();
                    }
                }
            }
        }
        return b;
    }

    public final long zzlc() throws IOException {
        long j = 0;
        for (int i = 0; i < 64; i += 7) {
            byte zzlg = zzlg();
            j |= ((long) (zzlg & Ascii.DEL)) << i;
            if ((zzlg & UnsignedBytes.MAX_POWER_OF_TWO) == 0) {
                return j;
            }
        }
        throw zziu.zzpi();
    }

    private zzim(byte[] bArr, int i, int i2) {
        this.buffer = bArr;
        this.zzamw = i;
        int i3 = i2 + i;
        this.zzamy = i3;
        this.zzamx = i3;
        this.zzamz = i;
    }

    public final <T extends zzez<T, ?>> T zza(zzgs<T> zzgs) throws IOException {
        try {
            if (this.zzana == null) {
                this.zzana = zzeb.zzd(this.buffer, this.zzamw, this.zzamx);
            }
            int zzla = this.zzana.zzla();
            int i = this.zzamz - this.zzamw;
            if (zzla <= i) {
                this.zzana.zzz(i - zzla);
                this.zzana.zzw(this.zzaci - this.zzach);
                T t = (zzez) this.zzana.zza(zzgs, zzem.zzlt());
                zzv(this.zzacp);
                return t;
            }
            throw new IOException(String.format("CodedInputStream read ahead of CodedInputByteBufferNano: %s > %s", new Object[]{Integer.valueOf(zzla), Integer.valueOf(i)}));
        } catch (zzfh e) {
            throw new zziu("", e);
        }
    }

    public final int zzx(int i) throws zziu {
        if (i >= 0) {
            int i2 = i + this.zzamz;
            int i3 = this.zzacq;
            if (i2 <= i3) {
                this.zzacq = i2;
                zzlf();
                return i3;
            }
            throw zziu.zzpg();
        }
        throw zziu.zzph();
    }

    private final void zzlf() {
        this.zzamy += this.zzacn;
        int i = this.zzamy;
        int i2 = this.zzacq;
        if (i > i2) {
            this.zzacn = i - i2;
            this.zzamy = i - this.zzacn;
            return;
        }
        this.zzacn = 0;
    }

    public final void zzy(int i) {
        this.zzacq = i;
        zzlf();
    }

    public final int zzpd() {
        int i = this.zzacq;
        if (i == Integer.MAX_VALUE) {
            return -1;
        }
        return i - this.zzamz;
    }

    public final int getPosition() {
        return this.zzamz - this.zzamw;
    }

    public final byte[] zzt(int i, int i2) {
        if (i2 == 0) {
            return zziy.zzanx;
        }
        byte[] bArr = new byte[i2];
        System.arraycopy(this.buffer, this.zzamw + i, bArr, 0, i2);
        return bArr;
    }

    public final void zzbj(int i) {
        zzu(i, this.zzacp);
    }

    /* access modifiers changed from: 0000 */
    public final void zzu(int i, int i2) {
        int i3 = this.zzamz;
        int i4 = this.zzamw;
        if (i > i3 - i4) {
            int i5 = i3 - i4;
            StringBuilder sb = new StringBuilder(50);
            sb.append("Position ");
            sb.append(i);
            sb.append(" is beyond current ");
            sb.append(i5);
            throw new IllegalArgumentException(sb.toString());
        } else if (i >= 0) {
            this.zzamz = i4 + i;
            this.zzacp = i2;
        } else {
            StringBuilder sb2 = new StringBuilder(24);
            sb2.append("Bad position ");
            sb2.append(i);
            throw new IllegalArgumentException(sb2.toString());
        }
    }

    private final byte zzlg() throws IOException {
        int i = this.zzamz;
        if (i != this.zzamy) {
            byte[] bArr = this.buffer;
            this.zzamz = i + 1;
            return bArr[i];
        }
        throw zziu.zzpg();
    }

    private final void zzz(int i) throws IOException {
        if (i >= 0) {
            int i2 = this.zzamz;
            int i3 = i2 + i;
            int i4 = this.zzacq;
            if (i3 > i4) {
                zzz(i4 - i2);
                throw zziu.zzpg();
            } else if (i <= this.zzamy - i2) {
                this.zzamz = i2 + i;
            } else {
                throw zziu.zzpg();
            }
        } else {
            throw zziu.zzph();
        }
    }
}
