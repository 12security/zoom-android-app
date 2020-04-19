package com.google.android.gms.internal.measurement;

import com.zipow.videobox.ptapp.DummyPolicyIDType;
import com.zipow.videobox.sip.CmmSIPCallFailReason;
import java.io.IOException;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ReadOnlyBufferException;

public final class zzin {
    private final ByteBuffer zzada;
    private zzeg zzanb;
    private int zzanc;

    private zzin(byte[] bArr, int i, int i2) {
        this(ByteBuffer.wrap(bArr, i, i2));
    }

    public static int zzar(int i) {
        if ((i & -128) == 0) {
            return 1;
        }
        if ((i & -16384) == 0) {
            return 2;
        }
        if ((-2097152 & i) == 0) {
            return 3;
        }
        return (i & -268435456) == 0 ? 4 : 5;
    }

    private zzin(ByteBuffer byteBuffer) {
        this.zzada = byteBuffer;
        this.zzada.order(ByteOrder.LITTLE_ENDIAN);
    }

    public static zzin zzl(byte[] bArr) {
        return zzk(bArr, 0, bArr.length);
    }

    public static zzin zzk(byte[] bArr, int i, int i2) {
        return new zzin(bArr, 0, i2);
    }

    public final void zzi(int i, long j) throws IOException {
        zzb(i, 0);
        zzbc(j);
    }

    public final void zzc(int i, int i2) throws IOException {
        zzb(i, 0);
        if (i2 >= 0) {
            zzbl(i2);
        } else {
            zzbc((long) i2);
        }
    }

    public final void zzb(int i, boolean z) throws IOException {
        zzb(i, 0);
        byte b = z ? (byte) 1 : 0;
        if (this.zzada.hasRemaining()) {
            this.zzada.put(b);
            return;
        }
        throw new zzio(this.zzada.position(), this.zzada.limit());
    }

    public final void zzb(int i, String str) throws IOException {
        zzb(i, 2);
        try {
            int zzar = zzar(str.length());
            if (zzar == zzar(str.length() * 3)) {
                int position = this.zzada.position();
                if (this.zzada.remaining() >= zzar) {
                    this.zzada.position(position + zzar);
                    zzd((CharSequence) str, this.zzada);
                    int position2 = this.zzada.position();
                    this.zzada.position(position);
                    zzbl((position2 - position) - zzar);
                    this.zzada.position(position2);
                    return;
                }
                throw new zzio(position + zzar, this.zzada.limit());
            }
            zzbl(zza(str));
            zzd((CharSequence) str, this.zzada);
        } catch (BufferOverflowException e) {
            zzio zzio = new zzio(this.zzada.position(), this.zzada.limit());
            zzio.initCause(e);
            throw zzio;
        }
    }

    public final void zza(int i, zziv zziv) throws IOException {
        zzb(i, 2);
        if (zziv.zzanm < 0) {
            zziv.zzly();
        }
        zzbl(zziv.zzanm);
        zziv.zza(this);
    }

    public final void zze(int i, zzgh zzgh) throws IOException {
        if (this.zzanb == null) {
            this.zzanb = zzeg.zza(this.zzada);
            this.zzanc = this.zzada.position();
        } else if (this.zzanc != this.zzada.position()) {
            this.zzanb.write(this.zzada.array(), this.zzanc, this.zzada.position() - this.zzanc);
            this.zzanc = this.zzada.position();
        }
        zzeg zzeg = this.zzanb;
        zzeg.zza(i, zzgh);
        zzeg.flush();
        this.zzanc = this.zzada.position();
    }

    private static int zza(CharSequence charSequence) {
        int length = charSequence.length();
        int i = 0;
        int i2 = 0;
        while (i2 < length && charSequence.charAt(i2) < 128) {
            i2++;
        }
        int i3 = length;
        while (true) {
            if (i2 >= length) {
                break;
            }
            char charAt = charSequence.charAt(i2);
            if (charAt < 2048) {
                i3 += (127 - charAt) >>> 31;
                i2++;
            } else {
                int length2 = charSequence.length();
                while (i2 < length2) {
                    char charAt2 = charSequence.charAt(i2);
                    if (charAt2 < 2048) {
                        i += (127 - charAt2) >>> 31;
                    } else {
                        i += 2;
                        if (55296 <= charAt2 && charAt2 <= 57343) {
                            if (Character.codePointAt(charSequence, i2) >= 65536) {
                                i2++;
                            } else {
                                StringBuilder sb = new StringBuilder(39);
                                sb.append("Unpaired surrogate at index ");
                                sb.append(i2);
                                throw new IllegalArgumentException(sb.toString());
                            }
                        }
                    }
                    i2++;
                }
                i3 += i;
            }
        }
        if (i3 >= length) {
            return i3;
        }
        long j = ((long) i3) + 4294967296L;
        StringBuilder sb2 = new StringBuilder(54);
        sb2.append("UTF-8 length does not fit in int: ");
        sb2.append(j);
        throw new IllegalArgumentException(sb2.toString());
    }

    private static void zzd(CharSequence charSequence, ByteBuffer byteBuffer) {
        int i;
        if (!byteBuffer.isReadOnly()) {
            int i2 = 0;
            if (byteBuffer.hasArray()) {
                try {
                    byte[] array = byteBuffer.array();
                    int arrayOffset = byteBuffer.arrayOffset() + byteBuffer.position();
                    int remaining = byteBuffer.remaining();
                    int length = charSequence.length();
                    int i3 = remaining + arrayOffset;
                    while (i2 < length) {
                        int i4 = i2 + arrayOffset;
                        if (i4 >= i3) {
                            break;
                        }
                        char charAt = charSequence.charAt(i2);
                        if (charAt >= 128) {
                            break;
                        }
                        array[i4] = (byte) charAt;
                        i2++;
                    }
                    if (i2 == length) {
                        i = arrayOffset + length;
                    } else {
                        i = arrayOffset + i2;
                        while (i2 < length) {
                            char charAt2 = charSequence.charAt(i2);
                            if (charAt2 < 128 && i < i3) {
                                int i5 = i + 1;
                                array[i] = (byte) charAt2;
                                i = i5;
                            } else if (charAt2 < 2048 && i <= i3 - 2) {
                                int i6 = i + 1;
                                array[i] = (byte) ((charAt2 >>> 6) | 960);
                                i = i6 + 1;
                                array[i6] = (byte) ((charAt2 & '?') | 128);
                            } else if ((charAt2 < 55296 || 57343 < charAt2) && i <= i3 - 3) {
                                int i7 = i + 1;
                                array[i] = (byte) ((charAt2 >>> 12) | CmmSIPCallFailReason.kSIPCall_FAIL_480_Temporarily_Unavailable);
                                int i8 = i7 + 1;
                                array[i7] = (byte) (((charAt2 >>> 6) & 63) | 128);
                                int i9 = i8 + 1;
                                array[i8] = (byte) ((charAt2 & '?') | 128);
                                i = i9;
                            } else if (i <= i3 - 4) {
                                int i10 = i2 + 1;
                                if (i10 != charSequence.length()) {
                                    char charAt3 = charSequence.charAt(i10);
                                    if (Character.isSurrogatePair(charAt2, charAt3)) {
                                        int codePoint = Character.toCodePoint(charAt2, charAt3);
                                        int i11 = i + 1;
                                        array[i] = (byte) ((codePoint >>> 18) | DummyPolicyIDType.zPolicy_EnableElevateForAdvDSCP);
                                        int i12 = i11 + 1;
                                        array[i11] = (byte) (((codePoint >>> 12) & 63) | 128);
                                        int i13 = i12 + 1;
                                        array[i12] = (byte) (((codePoint >>> 6) & 63) | 128);
                                        i = i13 + 1;
                                        array[i13] = (byte) ((codePoint & 63) | 128);
                                        i2 = i10;
                                    } else {
                                        i2 = i10;
                                    }
                                }
                                int i14 = i2 - 1;
                                StringBuilder sb = new StringBuilder(39);
                                sb.append("Unpaired surrogate at index ");
                                sb.append(i14);
                                throw new IllegalArgumentException(sb.toString());
                            } else {
                                StringBuilder sb2 = new StringBuilder(37);
                                sb2.append("Failed writing ");
                                sb2.append(charAt2);
                                sb2.append(" at index ");
                                sb2.append(i);
                                throw new ArrayIndexOutOfBoundsException(sb2.toString());
                            }
                            i2++;
                        }
                    }
                    byteBuffer.position(i - byteBuffer.arrayOffset());
                } catch (ArrayIndexOutOfBoundsException e) {
                    BufferOverflowException bufferOverflowException = new BufferOverflowException();
                    bufferOverflowException.initCause(e);
                    throw bufferOverflowException;
                }
            } else {
                int length2 = charSequence.length();
                while (i2 < length2) {
                    char charAt4 = charSequence.charAt(i2);
                    if (charAt4 < 128) {
                        byteBuffer.put((byte) charAt4);
                    } else if (charAt4 < 2048) {
                        byteBuffer.put((byte) ((charAt4 >>> 6) | 960));
                        byteBuffer.put((byte) ((charAt4 & '?') | 128));
                    } else if (charAt4 < 55296 || 57343 < charAt4) {
                        byteBuffer.put((byte) ((charAt4 >>> 12) | CmmSIPCallFailReason.kSIPCall_FAIL_480_Temporarily_Unavailable));
                        byteBuffer.put((byte) (((charAt4 >>> 6) & 63) | 128));
                        byteBuffer.put((byte) ((charAt4 & '?') | 128));
                    } else {
                        int i15 = i2 + 1;
                        if (i15 != charSequence.length()) {
                            char charAt5 = charSequence.charAt(i15);
                            if (Character.isSurrogatePair(charAt4, charAt5)) {
                                int codePoint2 = Character.toCodePoint(charAt4, charAt5);
                                byteBuffer.put((byte) ((codePoint2 >>> 18) | DummyPolicyIDType.zPolicy_EnableElevateForAdvDSCP));
                                byteBuffer.put((byte) (((codePoint2 >>> 12) & 63) | 128));
                                byteBuffer.put((byte) (((codePoint2 >>> 6) & 63) | 128));
                                byteBuffer.put((byte) ((codePoint2 & 63) | 128));
                                i2 = i15;
                            } else {
                                i2 = i15;
                            }
                        }
                        int i16 = i2 - 1;
                        StringBuilder sb3 = new StringBuilder(39);
                        sb3.append("Unpaired surrogate at index ");
                        sb3.append(i16);
                        throw new IllegalArgumentException(sb3.toString());
                    }
                    i2++;
                }
            }
        } else {
            throw new ReadOnlyBufferException();
        }
    }

    public static int zzd(int i, long j) {
        int zzaj = zzaj(i);
        int i2 = (-128 & j) == 0 ? 1 : (-16384 & j) == 0 ? 2 : (-2097152 & j) == 0 ? 3 : (-268435456 & j) == 0 ? 4 : (-34359738368L & j) == 0 ? 5 : (-4398046511104L & j) == 0 ? 6 : (-562949953421312L & j) == 0 ? 7 : (-72057594037927936L & j) == 0 ? 8 : (j & Long.MIN_VALUE) == 0 ? 9 : 10;
        return zzaj + i2;
    }

    public static int zzg(int i, int i2) {
        return zzaj(i) + zzak(i2);
    }

    public static int zzc(int i, String str) {
        return zzaj(i) + zzcp(str);
    }

    public static int zzb(int i, zziv zziv) {
        int zzaj = zzaj(i);
        int zzly = zziv.zzly();
        return zzaj + zzar(zzly) + zzly;
    }

    public static int zzak(int i) {
        if (i >= 0) {
            return zzar(i);
        }
        return 10;
    }

    public static int zzcp(String str) {
        int zza = zza(str);
        return zzar(zza) + zza;
    }

    public final void zzlk() {
        if (this.zzada.remaining() != 0) {
            throw new IllegalStateException(String.format("Did not write as much data as expected, %s bytes remaining.", new Object[]{Integer.valueOf(this.zzada.remaining())}));
        }
    }

    private final void zzbk(int i) throws IOException {
        byte b = (byte) i;
        if (this.zzada.hasRemaining()) {
            this.zzada.put(b);
            return;
        }
        throw new zzio(this.zzada.position(), this.zzada.limit());
    }

    public final void zzm(byte[] bArr) throws IOException {
        int length = bArr.length;
        if (this.zzada.remaining() >= length) {
            this.zzada.put(bArr, 0, length);
            return;
        }
        throw new zzio(this.zzada.position(), this.zzada.limit());
    }

    public final void zzb(int i, int i2) throws IOException {
        zzbl((i << 3) | i2);
    }

    public static int zzaj(int i) {
        return zzar(i << 3);
    }

    public final void zzbl(int i) throws IOException {
        while ((i & -128) != 0) {
            zzbk((i & 127) | 128);
            i >>>= 7;
        }
        zzbk(i);
    }

    private final void zzbc(long j) throws IOException {
        while ((-128 & j) != 0) {
            zzbk((((int) j) & 127) | 128);
            j >>>= 7;
        }
        zzbk((int) j);
    }
}
