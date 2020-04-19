package com.google.android.gms.internal.measurement;

import com.zipow.videobox.ptapp.DummyPolicyIDType;
import com.zipow.videobox.sip.CmmSIPCallFailReason;
import java.nio.ByteBuffer;

final class zzid extends zzia {
    zzid() {
    }

    /* access modifiers changed from: 0000 */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0065, code lost:
        return -1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:0x00ba, code lost:
        return -1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final int zzb(int r18, byte[] r19, int r20, int r21) {
        /*
            r17 = this;
            r0 = r19
            r1 = r20
            r2 = r21
            r3 = r1 | r2
            int r4 = r0.length
            int r4 = r4 - r2
            r3 = r3 | r4
            r4 = 2
            r5 = 3
            r6 = 0
            if (r3 < 0) goto L_0x00bb
            long r7 = (long) r1
            long r1 = (long) r2
            long r1 = r1 - r7
            int r1 = (int) r1
            r2 = 16
            r9 = 1
            if (r1 >= r2) goto L_0x001c
            r2 = 0
            goto L_0x002e
        L_0x001c:
            r11 = r7
            r2 = 0
        L_0x001e:
            if (r2 >= r1) goto L_0x002d
            long r13 = r11 + r9
            byte r3 = com.google.android.gms.internal.measurement.zzhw.zza(r0, r11)
            if (r3 >= 0) goto L_0x0029
            goto L_0x002e
        L_0x0029:
            int r2 = r2 + 1
            r11 = r13
            goto L_0x001e
        L_0x002d:
            r2 = r1
        L_0x002e:
            int r1 = r1 - r2
            long r2 = (long) r2
            long r7 = r7 + r2
        L_0x0031:
            r2 = 0
        L_0x0032:
            if (r1 <= 0) goto L_0x0045
            long r2 = r7 + r9
            byte r7 = com.google.android.gms.internal.measurement.zzhw.zza(r0, r7)
            if (r7 < 0) goto L_0x0042
            int r1 = r1 + -1
            r15 = r2
            r2 = r7
            r7 = r15
            goto L_0x0032
        L_0x0042:
            r15 = r2
            r2 = r7
            r7 = r15
        L_0x0045:
            if (r1 != 0) goto L_0x0048
            return r6
        L_0x0048:
            int r1 = r1 + -1
            r3 = -32
            r11 = -65
            r12 = -1
            if (r2 >= r3) goto L_0x0066
            if (r1 != 0) goto L_0x0054
            return r2
        L_0x0054:
            int r1 = r1 + -1
            r3 = -62
            if (r2 < r3) goto L_0x0065
            long r2 = r7 + r9
            byte r7 = com.google.android.gms.internal.measurement.zzhw.zza(r0, r7)
            if (r7 <= r11) goto L_0x0063
            goto L_0x0065
        L_0x0063:
            r7 = r2
            goto L_0x0031
        L_0x0065:
            return r12
        L_0x0066:
            r13 = -16
            if (r2 >= r13) goto L_0x0090
            if (r1 >= r4) goto L_0x0071
            int r0 = zza(r0, r2, r7, r1)
            return r0
        L_0x0071:
            int r1 = r1 + -2
            long r13 = r7 + r9
            byte r7 = com.google.android.gms.internal.measurement.zzhw.zza(r0, r7)
            if (r7 > r11) goto L_0x008f
            r8 = -96
            if (r2 != r3) goto L_0x0081
            if (r7 < r8) goto L_0x008f
        L_0x0081:
            r3 = -19
            if (r2 != r3) goto L_0x0087
            if (r7 >= r8) goto L_0x008f
        L_0x0087:
            long r7 = r13 + r9
            byte r2 = com.google.android.gms.internal.measurement.zzhw.zza(r0, r13)
            if (r2 <= r11) goto L_0x0031
        L_0x008f:
            return r12
        L_0x0090:
            if (r1 >= r5) goto L_0x0097
            int r0 = zza(r0, r2, r7, r1)
            return r0
        L_0x0097:
            int r1 = r1 + -3
            long r13 = r7 + r9
            byte r3 = com.google.android.gms.internal.measurement.zzhw.zza(r0, r7)
            if (r3 > r11) goto L_0x00ba
            int r2 = r2 << 28
            int r3 = r3 + 112
            int r2 = r2 + r3
            int r2 = r2 >> 30
            if (r2 != 0) goto L_0x00ba
            long r2 = r13 + r9
            byte r7 = com.google.android.gms.internal.measurement.zzhw.zza(r0, r13)
            if (r7 > r11) goto L_0x00ba
            long r7 = r2 + r9
            byte r2 = com.google.android.gms.internal.measurement.zzhw.zza(r0, r2)
            if (r2 <= r11) goto L_0x0031
        L_0x00ba:
            return r12
        L_0x00bb:
            java.lang.ArrayIndexOutOfBoundsException r3 = new java.lang.ArrayIndexOutOfBoundsException
            java.lang.Object[] r5 = new java.lang.Object[r5]
            int r0 = r0.length
            java.lang.Integer r0 = java.lang.Integer.valueOf(r0)
            r5[r6] = r0
            java.lang.Integer r0 = java.lang.Integer.valueOf(r20)
            r1 = 1
            r5[r1] = r0
            java.lang.Integer r0 = java.lang.Integer.valueOf(r21)
            r5[r4] = r0
            java.lang.String r0 = "Array length=%d, index=%d, limit=%d"
            java.lang.String r0 = java.lang.String.format(r0, r5)
            r3.<init>(r0)
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.measurement.zzid.zzb(int, byte[], int, int):int");
    }

    /* access modifiers changed from: 0000 */
    public final String zzh(byte[] bArr, int i, int i2) throws zzfh {
        if ((i | i2 | ((bArr.length - i) - i2)) >= 0) {
            int i3 = i + i2;
            char[] cArr = new char[i2];
            int i4 = 0;
            while (r13 < i3) {
                byte zza = zzhw.zza(bArr, (long) r13);
                if (!zzhz.zzd(zza)) {
                    break;
                }
                i = r13 + 1;
                int i5 = i4 + 1;
                zzhz.zza(zza, cArr, i4);
                i4 = i5;
            }
            int i6 = i4;
            while (r13 < i3) {
                int i7 = r13 + 1;
                byte zza2 = zzhw.zza(bArr, (long) r13);
                if (zzhz.zzd(zza2)) {
                    int i8 = i6 + 1;
                    zzhz.zza(zza2, cArr, i6);
                    while (i7 < i3) {
                        byte zza3 = zzhw.zza(bArr, (long) i7);
                        if (!zzhz.zzd(zza3)) {
                            break;
                        }
                        i7++;
                        int i9 = i8 + 1;
                        zzhz.zza(zza3, cArr, i8);
                        i8 = i9;
                    }
                    r13 = i7;
                    i6 = i8;
                } else if (zzhz.zze(zza2)) {
                    if (i7 < i3) {
                        int i10 = i7 + 1;
                        int i11 = i6 + 1;
                        zzhz.zza(zza2, zzhw.zza(bArr, (long) i7), cArr, i6);
                        r13 = i10;
                        i6 = i11;
                    } else {
                        throw zzfh.zznc();
                    }
                } else if (zzhz.zzf(zza2)) {
                    if (i7 < i3 - 1) {
                        int i12 = i7 + 1;
                        int i13 = i12 + 1;
                        int i14 = i6 + 1;
                        zzhz.zza(zza2, zzhw.zza(bArr, (long) i7), zzhw.zza(bArr, (long) i12), cArr, i6);
                        r13 = i13;
                        i6 = i14;
                    } else {
                        throw zzfh.zznc();
                    }
                } else if (i7 < i3 - 2) {
                    int i15 = i7 + 1;
                    byte zza4 = zzhw.zza(bArr, (long) i7);
                    int i16 = i15 + 1;
                    int i17 = i16 + 1;
                    int i18 = i6 + 1;
                    zzhz.zza(zza2, zza4, zzhw.zza(bArr, (long) i15), zzhw.zza(bArr, (long) i16), cArr, i6);
                    r13 = i17;
                    i6 = i18 + 1;
                } else {
                    throw zzfh.zznc();
                }
            }
            return new String(cArr, 0, i6);
        }
        throw new ArrayIndexOutOfBoundsException(String.format("buffer length=%d, index=%d, size=%d", new Object[]{Integer.valueOf(bArr.length), Integer.valueOf(i), Integer.valueOf(i2)}));
    }

    /* access modifiers changed from: 0000 */
    public final int zzb(CharSequence charSequence, byte[] bArr, int i, int i2) {
        long j;
        CharSequence charSequence2 = charSequence;
        byte[] bArr2 = bArr;
        int i3 = i;
        int i4 = i2;
        long j2 = (long) i3;
        long j3 = ((long) i4) + j2;
        int length = charSequence.length();
        if (length > i4 || bArr2.length - i4 < i3) {
            char charAt = charSequence2.charAt(length - 1);
            int i5 = i3 + i4;
            StringBuilder sb = new StringBuilder(37);
            sb.append("Failed writing ");
            sb.append(charAt);
            sb.append(" at index ");
            sb.append(i5);
            throw new ArrayIndexOutOfBoundsException(sb.toString());
        }
        int i6 = 0;
        while (i6 < length) {
            char charAt2 = charSequence2.charAt(i6);
            if (charAt2 >= 128) {
                break;
            }
            long j4 = 1 + j;
            zzhw.zza(bArr2, j, (byte) charAt2);
            i6++;
            j2 = j4;
        }
        if (i6 == length) {
            return (int) j;
        }
        while (i6 < length) {
            char charAt3 = charSequence2.charAt(i6);
            if (charAt3 < 128 && j < j3) {
                long j5 = j + 1;
                zzhw.zza(bArr2, j, (byte) charAt3);
                j = j5;
            } else if (charAt3 < 2048 && j <= j3 - 2) {
                long j6 = j + 1;
                zzhw.zza(bArr2, j, (byte) ((charAt3 >>> 6) | 960));
                j = j6 + 1;
                zzhw.zza(bArr2, j6, (byte) ((charAt3 & '?') | 128));
            } else if ((charAt3 < 55296 || 57343 < charAt3) && j <= j3 - 3) {
                long j7 = j + 1;
                zzhw.zza(bArr2, j, (byte) ((charAt3 >>> 12) | CmmSIPCallFailReason.kSIPCall_FAIL_480_Temporarily_Unavailable));
                long j8 = j7 + 1;
                zzhw.zza(bArr2, j7, (byte) (((charAt3 >>> 6) & 63) | 128));
                long j9 = j8 + 1;
                zzhw.zza(bArr2, j8, (byte) ((charAt3 & '?') | 128));
                j = j9;
            } else if (j <= j3 - 4) {
                int i7 = i6 + 1;
                if (i7 != length) {
                    char charAt4 = charSequence2.charAt(i7);
                    if (Character.isSurrogatePair(charAt3, charAt4)) {
                        int codePoint = Character.toCodePoint(charAt3, charAt4);
                        long j10 = j + 1;
                        zzhw.zza(bArr2, j, (byte) ((codePoint >>> 18) | DummyPolicyIDType.zPolicy_EnableElevateForAdvDSCP));
                        long j11 = j10 + 1;
                        zzhw.zza(bArr2, j10, (byte) (((codePoint >>> 12) & 63) | 128));
                        long j12 = j11 + 1;
                        zzhw.zza(bArr2, j11, (byte) (((codePoint >>> 6) & 63) | 128));
                        j = j12 + 1;
                        zzhw.zza(bArr2, j12, (byte) ((codePoint & 63) | 128));
                        i6 = i7;
                    }
                } else {
                    i7 = i6;
                }
                throw new zzic(i7 - 1, length);
            } else {
                if (55296 <= charAt3 && charAt3 <= 57343) {
                    int i8 = i6 + 1;
                    if (i8 == length || !Character.isSurrogatePair(charAt3, charSequence2.charAt(i8))) {
                        throw new zzic(i6, length);
                    }
                }
                StringBuilder sb2 = new StringBuilder(46);
                sb2.append("Failed writing ");
                sb2.append(charAt3);
                sb2.append(" at index ");
                sb2.append(j);
                throw new ArrayIndexOutOfBoundsException(sb2.toString());
            }
            i6++;
        }
        return (int) j;
    }

    /* access modifiers changed from: 0000 */
    public final void zzb(CharSequence charSequence, ByteBuffer byteBuffer) {
        char c;
        long j;
        long j2;
        long j3;
        CharSequence charSequence2 = charSequence;
        ByteBuffer byteBuffer2 = byteBuffer;
        long zzb = zzhw.zzb(byteBuffer);
        long position = ((long) byteBuffer.position()) + zzb;
        long limit = ((long) byteBuffer.limit()) + zzb;
        int length = charSequence.length();
        if (((long) length) <= limit - position) {
            int i = 0;
            while (true) {
                c = 128;
                j = 1;
                if (i >= length) {
                    break;
                }
                char charAt = charSequence2.charAt(i);
                if (charAt >= 128) {
                    break;
                }
                long j4 = position + 1;
                zzhw.zza(position, (byte) charAt);
                i++;
                position = j4;
            }
            if (i == length) {
                byteBuffer2.position((int) (position - zzb));
                return;
            }
            while (i < length) {
                char charAt2 = charSequence2.charAt(i);
                if (charAt2 < c && position < limit) {
                    j2 = position + j;
                    zzhw.zza(position, (byte) charAt2);
                    j3 = j;
                } else if (charAt2 < 2048 && position <= limit - 2) {
                    long j5 = position + j;
                    zzhw.zza(position, (byte) ((charAt2 >>> 6) | 960));
                    long j6 = j5 + j;
                    zzhw.zza(j5, (byte) ((charAt2 & '?') | 128));
                    j2 = j6;
                    j3 = j;
                } else if ((charAt2 < 55296 || 57343 < charAt2) && position <= limit - 3) {
                    long j7 = position + j;
                    zzhw.zza(position, (byte) ((charAt2 >>> 12) | CmmSIPCallFailReason.kSIPCall_FAIL_480_Temporarily_Unavailable));
                    long j8 = j7 + j;
                    zzhw.zza(j7, (byte) (((charAt2 >>> 6) & 63) | 128));
                    long j9 = j8 + 1;
                    zzhw.zza(j8, (byte) ((charAt2 & '?') | 128));
                    j2 = j9;
                    j3 = 1;
                } else if (position <= limit - 4) {
                    int i2 = i + 1;
                    if (i2 != length) {
                        char charAt3 = charSequence2.charAt(i2);
                        if (Character.isSurrogatePair(charAt2, charAt3)) {
                            int codePoint = Character.toCodePoint(charAt2, charAt3);
                            long j10 = position + 1;
                            zzhw.zza(position, (byte) ((codePoint >>> 18) | DummyPolicyIDType.zPolicy_EnableElevateForAdvDSCP));
                            long j11 = j10 + 1;
                            zzhw.zza(j10, (byte) (((codePoint >>> 12) & 63) | 128));
                            long j12 = j11 + 1;
                            zzhw.zza(j11, (byte) (((codePoint >>> 6) & 63) | 128));
                            j3 = 1;
                            long j13 = j12 + 1;
                            zzhw.zza(j12, (byte) ((codePoint & 63) | 128));
                            i = i2;
                            j2 = j13;
                        } else {
                            i = i2;
                        }
                    }
                    throw new zzic(i - 1, length);
                } else {
                    if (55296 <= charAt2 && charAt2 <= 57343) {
                        int i3 = i + 1;
                        if (i3 == length || !Character.isSurrogatePair(charAt2, charSequence2.charAt(i3))) {
                            throw new zzic(i, length);
                        }
                    }
                    StringBuilder sb = new StringBuilder(46);
                    sb.append("Failed writing ");
                    sb.append(charAt2);
                    sb.append(" at index ");
                    sb.append(position);
                    throw new ArrayIndexOutOfBoundsException(sb.toString());
                }
                i++;
                j = j3;
                position = j2;
                c = 128;
            }
            byteBuffer2.position((int) (position - zzb));
            return;
        }
        char charAt4 = charSequence2.charAt(length - 1);
        int limit2 = byteBuffer.limit();
        StringBuilder sb2 = new StringBuilder(37);
        sb2.append("Failed writing ");
        sb2.append(charAt4);
        sb2.append(" at index ");
        sb2.append(limit2);
        throw new ArrayIndexOutOfBoundsException(sb2.toString());
    }

    private static int zza(byte[] bArr, int i, long j, int i2) {
        switch (i2) {
            case 0:
                return zzhy.zzbh(i);
            case 1:
                return zzhy.zzr(i, zzhw.zza(bArr, j));
            case 2:
                return zzhy.zzc(i, zzhw.zza(bArr, j), zzhw.zza(bArr, j + 1));
            default:
                throw new AssertionError();
        }
    }
}
