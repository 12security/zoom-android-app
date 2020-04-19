package com.google.android.gms.internal.measurement;

import com.google.common.base.Ascii;
import com.google.common.primitives.UnsignedBytes;
import java.io.IOException;

final class zzdl {
    static int zza(byte[] bArr, int i, zzdm zzdm) {
        int i2 = i + 1;
        byte b = bArr[i];
        if (b < 0) {
            return zza((int) b, bArr, i2, zzdm);
        }
        zzdm.zzabs = b;
        return i2;
    }

    static int zza(int i, byte[] bArr, int i2, zzdm zzdm) {
        int i3 = i & 127;
        int i4 = i2 + 1;
        byte b = bArr[i2];
        if (b >= 0) {
            zzdm.zzabs = i3 | (b << 7);
            return i4;
        }
        int i5 = i3 | ((b & Ascii.DEL) << 7);
        int i6 = i4 + 1;
        byte b2 = bArr[i4];
        if (b2 >= 0) {
            zzdm.zzabs = i5 | (b2 << Ascii.f229SO);
            return i6;
        }
        int i7 = i5 | ((b2 & Ascii.DEL) << Ascii.f229SO);
        int i8 = i6 + 1;
        byte b3 = bArr[i6];
        if (b3 >= 0) {
            zzdm.zzabs = i7 | (b3 << Ascii.NAK);
            return i8;
        }
        int i9 = i7 | ((b3 & Ascii.DEL) << Ascii.NAK);
        int i10 = i8 + 1;
        byte b4 = bArr[i8];
        if (b4 >= 0) {
            zzdm.zzabs = i9 | (b4 << Ascii.f222FS);
            return i10;
        }
        int i11 = i9 | ((b4 & Ascii.DEL) << Ascii.f222FS);
        while (true) {
            int i12 = i10 + 1;
            if (bArr[i10] >= 0) {
                zzdm.zzabs = i11;
                return i12;
            }
            i10 = i12;
        }
    }

    static int zzb(byte[] bArr, int i, zzdm zzdm) {
        int i2 = i + 1;
        long j = (long) bArr[i];
        if (j >= 0) {
            zzdm.zzabt = j;
            return i2;
        }
        long j2 = j & 127;
        int i3 = i2 + 1;
        byte b = bArr[i2];
        long j3 = j2 | (((long) (b & Ascii.DEL)) << 7);
        int i4 = 7;
        while (b < 0) {
            int i5 = i3 + 1;
            byte b2 = bArr[i3];
            i4 += 7;
            j3 |= ((long) (b2 & Ascii.DEL)) << i4;
            int i6 = i5;
            b = b2;
            i3 = i6;
        }
        zzdm.zzabt = j3;
        return i3;
    }

    static int zza(byte[] bArr, int i) {
        return ((bArr[i + 3] & UnsignedBytes.MAX_VALUE) << Ascii.CAN) | (bArr[i] & UnsignedBytes.MAX_VALUE) | ((bArr[i + 1] & UnsignedBytes.MAX_VALUE) << 8) | ((bArr[i + 2] & UnsignedBytes.MAX_VALUE) << Ascii.DLE);
    }

    static long zzb(byte[] bArr, int i) {
        return ((((long) bArr[i + 7]) & 255) << 56) | (((long) bArr[i]) & 255) | ((((long) bArr[i + 1]) & 255) << 8) | ((((long) bArr[i + 2]) & 255) << 16) | ((((long) bArr[i + 3]) & 255) << 24) | ((((long) bArr[i + 4]) & 255) << 32) | ((((long) bArr[i + 5]) & 255) << 40) | ((((long) bArr[i + 6]) & 255) << 48);
    }

    static double zzc(byte[] bArr, int i) {
        return Double.longBitsToDouble(zzb(bArr, i));
    }

    static float zzd(byte[] bArr, int i) {
        return Float.intBitsToFloat(zza(bArr, i));
    }

    static int zzc(byte[] bArr, int i, zzdm zzdm) throws zzfh {
        int zza = zza(bArr, i, zzdm);
        int i2 = zzdm.zzabs;
        if (i2 < 0) {
            throw zzfh.zzmv();
        } else if (i2 == 0) {
            zzdm.zzabu = "";
            return zza;
        } else {
            zzdm.zzabu = new String(bArr, zza, i2, zzfb.UTF_8);
            return zza + i2;
        }
    }

    static int zzd(byte[] bArr, int i, zzdm zzdm) throws zzfh {
        int zza = zza(bArr, i, zzdm);
        int i2 = zzdm.zzabs;
        if (i2 < 0) {
            throw zzfh.zzmv();
        } else if (i2 == 0) {
            zzdm.zzabu = "";
            return zza;
        } else {
            zzdm.zzabu = zzhy.zzh(bArr, zza, i2);
            return zza + i2;
        }
    }

    static int zze(byte[] bArr, int i, zzdm zzdm) throws zzfh {
        int zza = zza(bArr, i, zzdm);
        int i2 = zzdm.zzabs;
        if (i2 < 0) {
            throw zzfh.zzmv();
        } else if (i2 > bArr.length - zza) {
            throw zzfh.zzmu();
        } else if (i2 == 0) {
            zzdm.zzabu = zzdp.zzaby;
            return zza;
        } else {
            zzdm.zzabu = zzdp.zzb(bArr, zza, i2);
            return zza + i2;
        }
    }

    static int zza(zzgy zzgy, byte[] bArr, int i, int i2, zzdm zzdm) throws IOException {
        int i3;
        int i4;
        int i5 = i + 1;
        byte b = bArr[i];
        if (b < 0) {
            i4 = zza((int) b, bArr, i5, zzdm);
            i3 = zzdm.zzabs;
        } else {
            i4 = i5;
            i3 = b;
        }
        if (i3 < 0 || i3 > i2 - i4) {
            throw zzfh.zzmu();
        }
        Object newInstance = zzgy.newInstance();
        int i6 = i3 + i4;
        zzgy.zza(newInstance, bArr, i4, i6, zzdm);
        zzgy.zzi(newInstance);
        zzdm.zzabu = newInstance;
        return i6;
    }

    static int zza(zzgy zzgy, byte[] bArr, int i, int i2, int i3, zzdm zzdm) throws IOException {
        zzgl zzgl = (zzgl) zzgy;
        Object newInstance = zzgl.newInstance();
        int zza = zzgl.zza(newInstance, bArr, i, i2, i3, zzdm);
        zzgl.zzi(newInstance);
        zzdm.zzabu = newInstance;
        return zza;
    }

    static int zza(int i, byte[] bArr, int i2, int i3, zzfg<?> zzfg, zzdm zzdm) {
        zzfa zzfa = (zzfa) zzfg;
        int zza = zza(bArr, i2, zzdm);
        zzfa.zzau(zzdm.zzabs);
        while (zza < i3) {
            int zza2 = zza(bArr, zza, zzdm);
            if (i != zzdm.zzabs) {
                break;
            }
            zza = zza(bArr, zza2, zzdm);
            zzfa.zzau(zzdm.zzabs);
        }
        return zza;
    }

    static int zza(byte[] bArr, int i, zzfg<?> zzfg, zzdm zzdm) throws IOException {
        zzfa zzfa = (zzfa) zzfg;
        int zza = zza(bArr, i, zzdm);
        int i2 = zzdm.zzabs + zza;
        while (zza < i2) {
            zza = zza(bArr, zza, zzdm);
            zzfa.zzau(zzdm.zzabs);
        }
        if (zza == i2) {
            return zza;
        }
        throw zzfh.zzmu();
    }

    static int zza(zzgy<?> zzgy, int i, byte[] bArr, int i2, int i3, zzfg<?> zzfg, zzdm zzdm) throws IOException {
        int zza = zza((zzgy) zzgy, bArr, i2, i3, zzdm);
        zzfg.add(zzdm.zzabu);
        while (zza < i3) {
            int zza2 = zza(bArr, zza, zzdm);
            if (i != zzdm.zzabs) {
                break;
            }
            zza = zza((zzgy) zzgy, bArr, zza2, i3, zzdm);
            zzfg.add(zzdm.zzabu);
        }
        return zza;
    }

    static int zza(int i, byte[] bArr, int i2, int i3, zzhr zzhr, zzdm zzdm) throws zzfh {
        if ((i >>> 3) != 0) {
            int i4 = i & 7;
            if (i4 != 5) {
                switch (i4) {
                    case 0:
                        int zzb = zzb(bArr, i2, zzdm);
                        zzhr.zzb(i, (Object) Long.valueOf(zzdm.zzabt));
                        return zzb;
                    case 1:
                        zzhr.zzb(i, (Object) Long.valueOf(zzb(bArr, i2)));
                        return i2 + 8;
                    case 2:
                        int zza = zza(bArr, i2, zzdm);
                        int i5 = zzdm.zzabs;
                        if (i5 < 0) {
                            throw zzfh.zzmv();
                        } else if (i5 <= bArr.length - zza) {
                            if (i5 == 0) {
                                zzhr.zzb(i, (Object) zzdp.zzaby);
                            } else {
                                zzhr.zzb(i, (Object) zzdp.zzb(bArr, zza, i5));
                            }
                            return zza + i5;
                        } else {
                            throw zzfh.zzmu();
                        }
                    case 3:
                        zzhr zzos = zzhr.zzos();
                        int i6 = (i & -8) | 4;
                        int i7 = 0;
                        while (true) {
                            if (i2 < i3) {
                                int zza2 = zza(bArr, i2, zzdm);
                                int i8 = zzdm.zzabs;
                                if (i8 != i6) {
                                    i7 = i8;
                                    i2 = zza(i8, bArr, zza2, i3, zzos, zzdm);
                                } else {
                                    i7 = i8;
                                    i2 = zza2;
                                }
                            }
                        }
                        if (i2 > i3 || i7 != i6) {
                            throw zzfh.zznb();
                        }
                        zzhr.zzb(i, (Object) zzos);
                        return i2;
                    default:
                        throw zzfh.zzmx();
                }
            } else {
                zzhr.zzb(i, (Object) Integer.valueOf(zza(bArr, i2)));
                return i2 + 4;
            }
        } else {
            throw zzfh.zzmx();
        }
    }

    static int zza(int i, byte[] bArr, int i2, int i3, zzdm zzdm) throws zzfh {
        if ((i >>> 3) != 0) {
            int i4 = i & 7;
            if (i4 == 5) {
                return i2 + 4;
            }
            switch (i4) {
                case 0:
                    return zzb(bArr, i2, zzdm);
                case 1:
                    return i2 + 8;
                case 2:
                    return zza(bArr, i2, zzdm) + zzdm.zzabs;
                case 3:
                    int i5 = (i & -8) | 4;
                    int i6 = 0;
                    while (i2 < i3) {
                        i2 = zza(bArr, i2, zzdm);
                        i6 = zzdm.zzabs;
                        if (i6 != i5) {
                            i2 = zza(i6, bArr, i2, i3, zzdm);
                        } else if (i2 > i3 && i6 == i5) {
                            return i2;
                        } else {
                            throw zzfh.zznb();
                        }
                    }
                    if (i2 > i3) {
                    }
                    throw zzfh.zznb();
                default:
                    throw zzfh.zzmx();
            }
        } else {
            throw zzfh.zzmx();
        }
    }
}
