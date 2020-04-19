package com.google.android.gms.internal.measurement;

import java.io.IOException;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class zzeg extends zzdo {
    private static final Logger logger = Logger.getLogger(zzeg.class.getName());
    /* access modifiers changed from: private */
    public static final boolean zzacv = zzhw.zzou();
    zzei zzacw = this;

    static class zza extends zzeg {
        private final byte[] buffer;
        private final int limit;
        private final int offset;
        private int position;

        zza(byte[] bArr, int i, int i2) {
            super();
            if (bArr != null) {
                int i3 = i + i2;
                if ((i | i2 | (bArr.length - i3)) >= 0) {
                    this.buffer = bArr;
                    this.offset = i;
                    this.position = i;
                    this.limit = i3;
                    return;
                }
                throw new IllegalArgumentException(String.format("Array range is invalid. Buffer.length=%d, offset=%d, length=%d", new Object[]{Integer.valueOf(bArr.length), Integer.valueOf(i), Integer.valueOf(i2)}));
            }
            throw new NullPointerException("buffer");
        }

        public void flush() {
        }

        public final void zzb(int i, int i2) throws IOException {
            zzag((i << 3) | i2);
        }

        public final void zzc(int i, int i2) throws IOException {
            zzb(i, 0);
            zzaf(i2);
        }

        public final void zzd(int i, int i2) throws IOException {
            zzb(i, 0);
            zzag(i2);
        }

        public final void zzf(int i, int i2) throws IOException {
            zzb(i, 5);
            zzai(i2);
        }

        public final void zza(int i, long j) throws IOException {
            zzb(i, 0);
            zzaq(j);
        }

        public final void zzc(int i, long j) throws IOException {
            zzb(i, 1);
            zzas(j);
        }

        public final void zzb(int i, boolean z) throws IOException {
            zzb(i, 0);
            zzc(z ? (byte) 1 : 0);
        }

        public final void zzb(int i, String str) throws IOException {
            zzb(i, 2);
            zzco(str);
        }

        public final void zza(int i, zzdp zzdp) throws IOException {
            zzb(i, 2);
            zza(zzdp);
        }

        public final void zza(zzdp zzdp) throws IOException {
            zzag(zzdp.size());
            zzdp.zza((zzdo) this);
        }

        public final void zze(byte[] bArr, int i, int i2) throws IOException {
            zzag(i2);
            write(bArr, 0, i2);
        }

        public final void zza(int i, zzgh zzgh) throws IOException {
            zzb(i, 2);
            zzb(zzgh);
        }

        /* access modifiers changed from: 0000 */
        public final void zza(int i, zzgh zzgh, zzgy zzgy) throws IOException {
            zzb(i, 2);
            zzdg zzdg = (zzdg) zzgh;
            int zzjw = zzdg.zzjw();
            if (zzjw == -1) {
                zzjw = zzgy.zzs(zzdg);
                zzdg.zzn(zzjw);
            }
            zzag(zzjw);
            zzgy.zza(zzgh, this.zzacw);
        }

        public final void zzb(int i, zzgh zzgh) throws IOException {
            zzb(1, 3);
            zzd(2, i);
            zza(3, zzgh);
            zzb(1, 4);
        }

        public final void zzb(int i, zzdp zzdp) throws IOException {
            zzb(1, 3);
            zzd(2, i);
            zza(3, zzdp);
            zzb(1, 4);
        }

        public final void zzb(zzgh zzgh) throws IOException {
            zzag(zzgh.zzly());
            zzgh.zzb(this);
        }

        /* access modifiers changed from: 0000 */
        public final void zza(zzgh zzgh, zzgy zzgy) throws IOException {
            zzdg zzdg = (zzdg) zzgh;
            int zzjw = zzdg.zzjw();
            if (zzjw == -1) {
                zzjw = zzgy.zzs(zzdg);
                zzdg.zzn(zzjw);
            }
            zzag(zzjw);
            zzgy.zza(zzgh, this.zzacw);
        }

        public final void zzc(byte b) throws IOException {
            try {
                byte[] bArr = this.buffer;
                int i = this.position;
                this.position = i + 1;
                bArr[i] = b;
            } catch (IndexOutOfBoundsException e) {
                throw new zzc(String.format("Pos: %d, limit: %d, len: %d", new Object[]{Integer.valueOf(this.position), Integer.valueOf(this.limit), Integer.valueOf(1)}), e);
            }
        }

        public final void zzaf(int i) throws IOException {
            if (i >= 0) {
                zzag(i);
            } else {
                zzaq((long) i);
            }
        }

        public final void zzag(int i) throws IOException {
            if (!zzeg.zzacv || zzlj() < 10) {
                while ((i & -128) != 0) {
                    byte[] bArr = this.buffer;
                    int i2 = this.position;
                    this.position = i2 + 1;
                    bArr[i2] = (byte) ((i & 127) | 128);
                    i >>>= 7;
                }
                try {
                    byte[] bArr2 = this.buffer;
                    int i3 = this.position;
                    this.position = i3 + 1;
                    bArr2[i3] = (byte) i;
                } catch (IndexOutOfBoundsException e) {
                    throw new zzc(String.format("Pos: %d, limit: %d, len: %d", new Object[]{Integer.valueOf(this.position), Integer.valueOf(this.limit), Integer.valueOf(1)}), e);
                }
            } else {
                while ((i & -128) != 0) {
                    byte[] bArr3 = this.buffer;
                    int i4 = this.position;
                    this.position = i4 + 1;
                    zzhw.zza(bArr3, (long) i4, (byte) ((i & 127) | 128));
                    i >>>= 7;
                }
                byte[] bArr4 = this.buffer;
                int i5 = this.position;
                this.position = i5 + 1;
                zzhw.zza(bArr4, (long) i5, (byte) i);
            }
        }

        public final void zzai(int i) throws IOException {
            try {
                byte[] bArr = this.buffer;
                int i2 = this.position;
                this.position = i2 + 1;
                bArr[i2] = (byte) i;
                byte[] bArr2 = this.buffer;
                int i3 = this.position;
                this.position = i3 + 1;
                bArr2[i3] = (byte) (i >> 8);
                byte[] bArr3 = this.buffer;
                int i4 = this.position;
                this.position = i4 + 1;
                bArr3[i4] = (byte) (i >> 16);
                byte[] bArr4 = this.buffer;
                int i5 = this.position;
                this.position = i5 + 1;
                bArr4[i5] = (byte) (i >>> 24);
            } catch (IndexOutOfBoundsException e) {
                throw new zzc(String.format("Pos: %d, limit: %d, len: %d", new Object[]{Integer.valueOf(this.position), Integer.valueOf(this.limit), Integer.valueOf(1)}), e);
            }
        }

        public final void zzaq(long j) throws IOException {
            if (!zzeg.zzacv || zzlj() < 10) {
                while ((j & -128) != 0) {
                    byte[] bArr = this.buffer;
                    int i = this.position;
                    this.position = i + 1;
                    bArr[i] = (byte) ((((int) j) & 127) | 128);
                    j >>>= 7;
                }
                try {
                    byte[] bArr2 = this.buffer;
                    int i2 = this.position;
                    this.position = i2 + 1;
                    bArr2[i2] = (byte) ((int) j);
                } catch (IndexOutOfBoundsException e) {
                    throw new zzc(String.format("Pos: %d, limit: %d, len: %d", new Object[]{Integer.valueOf(this.position), Integer.valueOf(this.limit), Integer.valueOf(1)}), e);
                }
            } else {
                while ((j & -128) != 0) {
                    byte[] bArr3 = this.buffer;
                    int i3 = this.position;
                    this.position = i3 + 1;
                    zzhw.zza(bArr3, (long) i3, (byte) ((((int) j) & 127) | 128));
                    j >>>= 7;
                }
                byte[] bArr4 = this.buffer;
                int i4 = this.position;
                this.position = i4 + 1;
                zzhw.zza(bArr4, (long) i4, (byte) ((int) j));
            }
        }

        public final void zzas(long j) throws IOException {
            try {
                byte[] bArr = this.buffer;
                int i = this.position;
                this.position = i + 1;
                bArr[i] = (byte) ((int) j);
                byte[] bArr2 = this.buffer;
                int i2 = this.position;
                this.position = i2 + 1;
                bArr2[i2] = (byte) ((int) (j >> 8));
                byte[] bArr3 = this.buffer;
                int i3 = this.position;
                this.position = i3 + 1;
                bArr3[i3] = (byte) ((int) (j >> 16));
                byte[] bArr4 = this.buffer;
                int i4 = this.position;
                this.position = i4 + 1;
                bArr4[i4] = (byte) ((int) (j >> 24));
                byte[] bArr5 = this.buffer;
                int i5 = this.position;
                this.position = i5 + 1;
                bArr5[i5] = (byte) ((int) (j >> 32));
                byte[] bArr6 = this.buffer;
                int i6 = this.position;
                this.position = i6 + 1;
                bArr6[i6] = (byte) ((int) (j >> 40));
                byte[] bArr7 = this.buffer;
                int i7 = this.position;
                this.position = i7 + 1;
                bArr7[i7] = (byte) ((int) (j >> 48));
                byte[] bArr8 = this.buffer;
                int i8 = this.position;
                this.position = i8 + 1;
                bArr8[i8] = (byte) ((int) (j >> 56));
            } catch (IndexOutOfBoundsException e) {
                throw new zzc(String.format("Pos: %d, limit: %d, len: %d", new Object[]{Integer.valueOf(this.position), Integer.valueOf(this.limit), Integer.valueOf(1)}), e);
            }
        }

        public final void write(byte[] bArr, int i, int i2) throws IOException {
            try {
                System.arraycopy(bArr, i, this.buffer, this.position, i2);
                this.position += i2;
            } catch (IndexOutOfBoundsException e) {
                throw new zzc(String.format("Pos: %d, limit: %d, len: %d", new Object[]{Integer.valueOf(this.position), Integer.valueOf(this.limit), Integer.valueOf(i2)}), e);
            }
        }

        public final void zza(byte[] bArr, int i, int i2) throws IOException {
            write(bArr, i, i2);
        }

        public final void zzco(String str) throws IOException {
            int i = this.position;
            try {
                int zzal = zzal(str.length() * 3);
                int zzal2 = zzal(str.length());
                if (zzal2 == zzal) {
                    this.position = i + zzal2;
                    int zza = zzhy.zza(str, this.buffer, this.position, zzlj());
                    this.position = i;
                    zzag((zza - i) - zzal2);
                    this.position = zza;
                    return;
                }
                zzag(zzhy.zza(str));
                this.position = zzhy.zza(str, this.buffer, this.position, zzlj());
            } catch (zzic e) {
                this.position = i;
                zza(str, e);
            } catch (IndexOutOfBoundsException e2) {
                throw new zzc((Throwable) e2);
            }
        }

        public final int zzlj() {
            return this.limit - this.position;
        }

        public final int zzlm() {
            return this.position - this.offset;
        }
    }

    static final class zzb extends zza {
        private final ByteBuffer zzacx;
        private int zzacy;

        zzb(ByteBuffer byteBuffer) {
            super(byteBuffer.array(), byteBuffer.arrayOffset() + byteBuffer.position(), byteBuffer.remaining());
            this.zzacx = byteBuffer;
            this.zzacy = byteBuffer.position();
        }

        public final void flush() {
            this.zzacx.position(this.zzacy + zzlm());
        }
    }

    public static class zzc extends IOException {
        zzc() {
            super("CodedOutputStream was writing to a flat byte array and ran out of space.");
        }

        zzc(String str) {
            String valueOf = String.valueOf("CodedOutputStream was writing to a flat byte array and ran out of space.: ");
            String valueOf2 = String.valueOf(str);
            super(valueOf2.length() != 0 ? valueOf.concat(valueOf2) : new String(valueOf));
        }

        zzc(Throwable th) {
            super("CodedOutputStream was writing to a flat byte array and ran out of space.", th);
        }

        zzc(String str, Throwable th) {
            String valueOf = String.valueOf("CodedOutputStream was writing to a flat byte array and ran out of space.: ");
            String valueOf2 = String.valueOf(str);
            super(valueOf2.length() != 0 ? valueOf.concat(valueOf2) : new String(valueOf), th);
        }
    }

    static final class zzd extends zzeg {
        private final int zzacy;
        private final ByteBuffer zzacz;
        private final ByteBuffer zzada;

        zzd(ByteBuffer byteBuffer) {
            super();
            this.zzacz = byteBuffer;
            this.zzada = byteBuffer.duplicate().order(ByteOrder.LITTLE_ENDIAN);
            this.zzacy = byteBuffer.position();
        }

        public final void zzb(int i, int i2) throws IOException {
            zzag((i << 3) | i2);
        }

        public final void zzc(int i, int i2) throws IOException {
            zzb(i, 0);
            zzaf(i2);
        }

        public final void zzd(int i, int i2) throws IOException {
            zzb(i, 0);
            zzag(i2);
        }

        public final void zzf(int i, int i2) throws IOException {
            zzb(i, 5);
            zzai(i2);
        }

        public final void zza(int i, long j) throws IOException {
            zzb(i, 0);
            zzaq(j);
        }

        public final void zzc(int i, long j) throws IOException {
            zzb(i, 1);
            zzas(j);
        }

        public final void zzb(int i, boolean z) throws IOException {
            zzb(i, 0);
            zzc(z ? (byte) 1 : 0);
        }

        public final void zzb(int i, String str) throws IOException {
            zzb(i, 2);
            zzco(str);
        }

        public final void zza(int i, zzdp zzdp) throws IOException {
            zzb(i, 2);
            zza(zzdp);
        }

        public final void zza(int i, zzgh zzgh) throws IOException {
            zzb(i, 2);
            zzb(zzgh);
        }

        /* access modifiers changed from: 0000 */
        public final void zza(int i, zzgh zzgh, zzgy zzgy) throws IOException {
            zzb(i, 2);
            zza(zzgh, zzgy);
        }

        public final void zzb(int i, zzgh zzgh) throws IOException {
            zzb(1, 3);
            zzd(2, i);
            zza(3, zzgh);
            zzb(1, 4);
        }

        public final void zzb(int i, zzdp zzdp) throws IOException {
            zzb(1, 3);
            zzd(2, i);
            zza(3, zzdp);
            zzb(1, 4);
        }

        public final void zzb(zzgh zzgh) throws IOException {
            zzag(zzgh.zzly());
            zzgh.zzb(this);
        }

        /* access modifiers changed from: 0000 */
        public final void zza(zzgh zzgh, zzgy zzgy) throws IOException {
            zzdg zzdg = (zzdg) zzgh;
            int zzjw = zzdg.zzjw();
            if (zzjw == -1) {
                zzjw = zzgy.zzs(zzdg);
                zzdg.zzn(zzjw);
            }
            zzag(zzjw);
            zzgy.zza(zzgh, this.zzacw);
        }

        public final void zzc(byte b) throws IOException {
            try {
                this.zzada.put(b);
            } catch (BufferOverflowException e) {
                throw new zzc((Throwable) e);
            }
        }

        public final void zza(zzdp zzdp) throws IOException {
            zzag(zzdp.size());
            zzdp.zza((zzdo) this);
        }

        public final void zze(byte[] bArr, int i, int i2) throws IOException {
            zzag(i2);
            write(bArr, 0, i2);
        }

        public final void zzaf(int i) throws IOException {
            if (i >= 0) {
                zzag(i);
            } else {
                zzaq((long) i);
            }
        }

        public final void zzag(int i) throws IOException {
            while ((i & -128) != 0) {
                this.zzada.put((byte) ((i & 127) | 128));
                i >>>= 7;
            }
            try {
                this.zzada.put((byte) i);
            } catch (BufferOverflowException e) {
                throw new zzc((Throwable) e);
            }
        }

        public final void zzai(int i) throws IOException {
            try {
                this.zzada.putInt(i);
            } catch (BufferOverflowException e) {
                throw new zzc((Throwable) e);
            }
        }

        public final void zzaq(long j) throws IOException {
            while ((-128 & j) != 0) {
                this.zzada.put((byte) ((((int) j) & 127) | 128));
                j >>>= 7;
            }
            try {
                this.zzada.put((byte) ((int) j));
            } catch (BufferOverflowException e) {
                throw new zzc((Throwable) e);
            }
        }

        public final void zzas(long j) throws IOException {
            try {
                this.zzada.putLong(j);
            } catch (BufferOverflowException e) {
                throw new zzc((Throwable) e);
            }
        }

        public final void write(byte[] bArr, int i, int i2) throws IOException {
            try {
                this.zzada.put(bArr, i, i2);
            } catch (IndexOutOfBoundsException e) {
                throw new zzc((Throwable) e);
            } catch (BufferOverflowException e2) {
                throw new zzc((Throwable) e2);
            }
        }

        public final void zza(byte[] bArr, int i, int i2) throws IOException {
            write(bArr, i, i2);
        }

        public final void zzco(String str) throws IOException {
            int position = this.zzada.position();
            try {
                int zzal = zzal(str.length() * 3);
                int zzal2 = zzal(str.length());
                if (zzal2 == zzal) {
                    int position2 = this.zzada.position() + zzal2;
                    this.zzada.position(position2);
                    zzcq(str);
                    int position3 = this.zzada.position();
                    this.zzada.position(position);
                    zzag(position3 - position2);
                    this.zzada.position(position3);
                    return;
                }
                zzag(zzhy.zza(str));
                zzcq(str);
            } catch (zzic e) {
                this.zzada.position(position);
                zza(str, e);
            } catch (IllegalArgumentException e2) {
                throw new zzc((Throwable) e2);
            }
        }

        public final void flush() {
            this.zzacz.position(this.zzada.position());
        }

        public final int zzlj() {
            return this.zzada.remaining();
        }

        private final void zzcq(String str) throws IOException {
            try {
                zzhy.zza(str, this.zzada);
            } catch (IndexOutOfBoundsException e) {
                throw new zzc((Throwable) e);
            }
        }
    }

    static final class zze extends zzeg {
        private final ByteBuffer zzacz;
        private final ByteBuffer zzada;
        private final long zzadb;
        private final long zzadc;
        private final long zzadd;
        private final long zzade = (this.zzadd - 10);
        private long zzadf = this.zzadc;

        zze(ByteBuffer byteBuffer) {
            super();
            this.zzacz = byteBuffer;
            this.zzada = byteBuffer.duplicate().order(ByteOrder.LITTLE_ENDIAN);
            this.zzadb = zzhw.zzb(byteBuffer);
            this.zzadc = this.zzadb + ((long) byteBuffer.position());
            this.zzadd = this.zzadb + ((long) byteBuffer.limit());
        }

        public final void zzb(int i, int i2) throws IOException {
            zzag((i << 3) | i2);
        }

        public final void zzc(int i, int i2) throws IOException {
            zzb(i, 0);
            zzaf(i2);
        }

        public final void zzd(int i, int i2) throws IOException {
            zzb(i, 0);
            zzag(i2);
        }

        public final void zzf(int i, int i2) throws IOException {
            zzb(i, 5);
            zzai(i2);
        }

        public final void zza(int i, long j) throws IOException {
            zzb(i, 0);
            zzaq(j);
        }

        public final void zzc(int i, long j) throws IOException {
            zzb(i, 1);
            zzas(j);
        }

        public final void zzb(int i, boolean z) throws IOException {
            zzb(i, 0);
            zzc(z ? (byte) 1 : 0);
        }

        public final void zzb(int i, String str) throws IOException {
            zzb(i, 2);
            zzco(str);
        }

        public final void zza(int i, zzdp zzdp) throws IOException {
            zzb(i, 2);
            zza(zzdp);
        }

        public final void zza(int i, zzgh zzgh) throws IOException {
            zzb(i, 2);
            zzb(zzgh);
        }

        /* access modifiers changed from: 0000 */
        public final void zza(int i, zzgh zzgh, zzgy zzgy) throws IOException {
            zzb(i, 2);
            zza(zzgh, zzgy);
        }

        public final void zzb(int i, zzgh zzgh) throws IOException {
            zzb(1, 3);
            zzd(2, i);
            zza(3, zzgh);
            zzb(1, 4);
        }

        public final void zzb(int i, zzdp zzdp) throws IOException {
            zzb(1, 3);
            zzd(2, i);
            zza(3, zzdp);
            zzb(1, 4);
        }

        public final void zzb(zzgh zzgh) throws IOException {
            zzag(zzgh.zzly());
            zzgh.zzb(this);
        }

        /* access modifiers changed from: 0000 */
        public final void zza(zzgh zzgh, zzgy zzgy) throws IOException {
            zzdg zzdg = (zzdg) zzgh;
            int zzjw = zzdg.zzjw();
            if (zzjw == -1) {
                zzjw = zzgy.zzs(zzdg);
                zzdg.zzn(zzjw);
            }
            zzag(zzjw);
            zzgy.zza(zzgh, this.zzacw);
        }

        public final void zzc(byte b) throws IOException {
            long j = this.zzadf;
            if (j < this.zzadd) {
                this.zzadf = 1 + j;
                zzhw.zza(j, b);
                return;
            }
            throw new zzc(String.format("Pos: %d, limit: %d, len: %d", new Object[]{Long.valueOf(j), Long.valueOf(this.zzadd), Integer.valueOf(1)}));
        }

        public final void zza(zzdp zzdp) throws IOException {
            zzag(zzdp.size());
            zzdp.zza((zzdo) this);
        }

        public final void zze(byte[] bArr, int i, int i2) throws IOException {
            zzag(i2);
            write(bArr, 0, i2);
        }

        public final void zzaf(int i) throws IOException {
            if (i >= 0) {
                zzag(i);
            } else {
                zzaq((long) i);
            }
        }

        public final void zzag(int i) throws IOException {
            if (this.zzadf <= this.zzade) {
                while ((i & -128) != 0) {
                    long j = this.zzadf;
                    this.zzadf = j + 1;
                    zzhw.zza(j, (byte) ((i & 127) | 128));
                    i >>>= 7;
                }
                long j2 = this.zzadf;
                this.zzadf = 1 + j2;
                zzhw.zza(j2, (byte) i);
                return;
            }
            while (true) {
                long j3 = this.zzadf;
                if (j3 >= this.zzadd) {
                    throw new zzc(String.format("Pos: %d, limit: %d, len: %d", new Object[]{Long.valueOf(j3), Long.valueOf(this.zzadd), Integer.valueOf(1)}));
                } else if ((i & -128) == 0) {
                    this.zzadf = 1 + j3;
                    zzhw.zza(j3, (byte) i);
                    return;
                } else {
                    this.zzadf = j3 + 1;
                    zzhw.zza(j3, (byte) ((i & 127) | 128));
                    i >>>= 7;
                }
            }
        }

        public final void zzai(int i) throws IOException {
            this.zzada.putInt((int) (this.zzadf - this.zzadb), i);
            this.zzadf += 4;
        }

        public final void zzaq(long j) throws IOException {
            if (this.zzadf <= this.zzade) {
                while ((j & -128) != 0) {
                    long j2 = this.zzadf;
                    this.zzadf = j2 + 1;
                    zzhw.zza(j2, (byte) ((((int) j) & 127) | 128));
                    j >>>= 7;
                }
                long j3 = this.zzadf;
                this.zzadf = 1 + j3;
                zzhw.zza(j3, (byte) ((int) j));
                return;
            }
            while (true) {
                long j4 = this.zzadf;
                if (j4 >= this.zzadd) {
                    throw new zzc(String.format("Pos: %d, limit: %d, len: %d", new Object[]{Long.valueOf(j4), Long.valueOf(this.zzadd), Integer.valueOf(1)}));
                } else if ((j & -128) == 0) {
                    this.zzadf = 1 + j4;
                    zzhw.zza(j4, (byte) ((int) j));
                    return;
                } else {
                    this.zzadf = j4 + 1;
                    zzhw.zza(j4, (byte) ((((int) j) & 127) | 128));
                    j >>>= 7;
                }
            }
        }

        public final void zzas(long j) throws IOException {
            this.zzada.putLong((int) (this.zzadf - this.zzadb), j);
            this.zzadf += 8;
        }

        public final void write(byte[] bArr, int i, int i2) throws IOException {
            if (bArr != null && i >= 0 && i2 >= 0 && bArr.length - i2 >= i) {
                long j = (long) i2;
                long j2 = this.zzadd - j;
                long j3 = this.zzadf;
                if (j2 >= j3) {
                    zzhw.zza(bArr, (long) i, j3, j);
                    this.zzadf += j;
                    return;
                }
            }
            if (bArr == null) {
                throw new NullPointerException("value");
            }
            throw new zzc(String.format("Pos: %d, limit: %d, len: %d", new Object[]{Long.valueOf(this.zzadf), Long.valueOf(this.zzadd), Integer.valueOf(i2)}));
        }

        public final void zza(byte[] bArr, int i, int i2) throws IOException {
            write(bArr, i, i2);
        }

        public final void zzco(String str) throws IOException {
            long j = this.zzadf;
            try {
                int zzal = zzal(str.length() * 3);
                int zzal2 = zzal(str.length());
                if (zzal2 == zzal) {
                    int i = ((int) (this.zzadf - this.zzadb)) + zzal2;
                    this.zzada.position(i);
                    zzhy.zza(str, this.zzada);
                    int position = this.zzada.position() - i;
                    zzag(position);
                    this.zzadf += (long) position;
                    return;
                }
                int zza = zzhy.zza(str);
                zzag(zza);
                zzaz(this.zzadf);
                zzhy.zza(str, this.zzada);
                this.zzadf += (long) zza;
            } catch (zzic e) {
                this.zzadf = j;
                zzaz(this.zzadf);
                zza(str, e);
            } catch (IllegalArgumentException e2) {
                throw new zzc((Throwable) e2);
            } catch (IndexOutOfBoundsException e3) {
                throw new zzc((Throwable) e3);
            }
        }

        public final void flush() {
            this.zzacz.position((int) (this.zzadf - this.zzadb));
        }

        public final int zzlj() {
            return (int) (this.zzadd - this.zzadf);
        }

        private final void zzaz(long j) {
            this.zzada.position((int) (j - this.zzadb));
        }
    }

    public static int zzal(int i) {
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

    public static int zzan(int i) {
        return 4;
    }

    public static int zzao(int i) {
        return 4;
    }

    private static int zzaq(int i) {
        return (i >> 31) ^ (i << 1);
    }

    public static int zzau(long j) {
        int i;
        if ((-128 & j) == 0) {
            return 1;
        }
        if (j < 0) {
            return 10;
        }
        if ((-34359738368L & j) != 0) {
            i = 6;
            j >>>= 28;
        } else {
            i = 2;
        }
        if ((-2097152 & j) != 0) {
            i += 2;
            j >>>= 14;
        }
        if ((j & -16384) != 0) {
            i++;
        }
        return i;
    }

    public static int zzaw(long j) {
        return 8;
    }

    public static int zzax(long j) {
        return 8;
    }

    private static long zzay(long j) {
        return (j >> 63) ^ (j << 1);
    }

    public static int zzb(float f) {
        return 4;
    }

    public static int zze(double d) {
        return 8;
    }

    public static zzeg zzh(byte[] bArr) {
        return new zza(bArr, 0, bArr.length);
    }

    public static int zzn(boolean z) {
        return 1;
    }

    public abstract void flush() throws IOException;

    public abstract void write(byte[] bArr, int i, int i2) throws IOException;

    public abstract void zza(int i, long j) throws IOException;

    public abstract void zza(int i, zzdp zzdp) throws IOException;

    public abstract void zza(int i, zzgh zzgh) throws IOException;

    /* access modifiers changed from: 0000 */
    public abstract void zza(int i, zzgh zzgh, zzgy zzgy) throws IOException;

    public abstract void zza(zzdp zzdp) throws IOException;

    /* access modifiers changed from: 0000 */
    public abstract void zza(zzgh zzgh, zzgy zzgy) throws IOException;

    public abstract void zzaf(int i) throws IOException;

    public abstract void zzag(int i) throws IOException;

    public abstract void zzai(int i) throws IOException;

    public abstract void zzaq(long j) throws IOException;

    public abstract void zzas(long j) throws IOException;

    public abstract void zzb(int i, int i2) throws IOException;

    public abstract void zzb(int i, zzdp zzdp) throws IOException;

    public abstract void zzb(int i, zzgh zzgh) throws IOException;

    public abstract void zzb(int i, String str) throws IOException;

    public abstract void zzb(int i, boolean z) throws IOException;

    public abstract void zzb(zzgh zzgh) throws IOException;

    public abstract void zzc(byte b) throws IOException;

    public abstract void zzc(int i, int i2) throws IOException;

    public abstract void zzc(int i, long j) throws IOException;

    public abstract void zzco(String str) throws IOException;

    public abstract void zzd(int i, int i2) throws IOException;

    /* access modifiers changed from: 0000 */
    public abstract void zze(byte[] bArr, int i, int i2) throws IOException;

    public abstract void zzf(int i, int i2) throws IOException;

    public abstract int zzlj();

    public static zzeg zza(ByteBuffer byteBuffer) {
        if (byteBuffer.hasArray()) {
            return new zzb(byteBuffer);
        }
        if (!byteBuffer.isDirect() || byteBuffer.isReadOnly()) {
            throw new IllegalArgumentException("ByteBuffer is read-only");
        } else if (zzhw.zzov()) {
            return new zze(byteBuffer);
        } else {
            return new zzd(byteBuffer);
        }
    }

    private zzeg() {
    }

    public final void zze(int i, int i2) throws IOException {
        zzd(i, zzaq(i2));
    }

    public final void zzb(int i, long j) throws IOException {
        zza(i, zzay(j));
    }

    public final void zza(int i, float f) throws IOException {
        zzf(i, Float.floatToRawIntBits(f));
    }

    public final void zza(int i, double d) throws IOException {
        zzc(i, Double.doubleToRawLongBits(d));
    }

    public final void zzah(int i) throws IOException {
        zzag(zzaq(i));
    }

    public final void zzar(long j) throws IOException {
        zzaq(zzay(j));
    }

    public final void zza(float f) throws IOException {
        zzai(Float.floatToRawIntBits(f));
    }

    public final void zzd(double d) throws IOException {
        zzas(Double.doubleToRawLongBits(d));
    }

    public final void zzm(boolean z) throws IOException {
        zzc(z ? (byte) 1 : 0);
    }

    public static int zzg(int i, int i2) {
        return zzaj(i) + zzak(i2);
    }

    public static int zzh(int i, int i2) {
        return zzaj(i) + zzal(i2);
    }

    public static int zzi(int i, int i2) {
        return zzaj(i) + zzal(zzaq(i2));
    }

    public static int zzj(int i, int i2) {
        return zzaj(i) + 4;
    }

    public static int zzk(int i, int i2) {
        return zzaj(i) + 4;
    }

    public static int zzd(int i, long j) {
        return zzaj(i) + zzau(j);
    }

    public static int zze(int i, long j) {
        return zzaj(i) + zzau(j);
    }

    public static int zzf(int i, long j) {
        return zzaj(i) + zzau(zzay(j));
    }

    public static int zzg(int i, long j) {
        return zzaj(i) + 8;
    }

    public static int zzh(int i, long j) {
        return zzaj(i) + 8;
    }

    public static int zzb(int i, float f) {
        return zzaj(i) + 4;
    }

    public static int zzb(int i, double d) {
        return zzaj(i) + 8;
    }

    public static int zzc(int i, boolean z) {
        return zzaj(i) + 1;
    }

    public static int zzl(int i, int i2) {
        return zzaj(i) + zzak(i2);
    }

    public static int zzc(int i, String str) {
        return zzaj(i) + zzcp(str);
    }

    public static int zzc(int i, zzdp zzdp) {
        int zzaj = zzaj(i);
        int size = zzdp.size();
        return zzaj + zzal(size) + size;
    }

    public static int zza(int i, zzfo zzfo) {
        int zzaj = zzaj(i);
        int zzly = zzfo.zzly();
        return zzaj + zzal(zzly) + zzly;
    }

    public static int zzc(int i, zzgh zzgh) {
        return zzaj(i) + zzc(zzgh);
    }

    static int zzb(int i, zzgh zzgh, zzgy zzgy) {
        return zzaj(i) + zzb(zzgh, zzgy);
    }

    public static int zzd(int i, zzgh zzgh) {
        return (zzaj(1) << 1) + zzh(2, i) + zzc(3, zzgh);
    }

    public static int zzd(int i, zzdp zzdp) {
        return (zzaj(1) << 1) + zzh(2, i) + zzc(3, zzdp);
    }

    public static int zzb(int i, zzfo zzfo) {
        return (zzaj(1) << 1) + zzh(2, i) + zza(3, zzfo);
    }

    public static int zzaj(int i) {
        return zzal(i << 3);
    }

    public static int zzak(int i) {
        if (i >= 0) {
            return zzal(i);
        }
        return 10;
    }

    public static int zzam(int i) {
        return zzal(zzaq(i));
    }

    public static int zzat(long j) {
        return zzau(j);
    }

    public static int zzav(long j) {
        return zzau(zzay(j));
    }

    public static int zzap(int i) {
        return zzak(i);
    }

    public static int zzcp(String str) {
        int i;
        try {
            i = zzhy.zza(str);
        } catch (zzic unused) {
            i = str.getBytes(zzfb.UTF_8).length;
        }
        return zzal(i) + i;
    }

    public static int zza(zzfo zzfo) {
        int zzly = zzfo.zzly();
        return zzal(zzly) + zzly;
    }

    public static int zzb(zzdp zzdp) {
        int size = zzdp.size();
        return zzal(size) + size;
    }

    public static int zzi(byte[] bArr) {
        int length = bArr.length;
        return zzal(length) + length;
    }

    public static int zzc(zzgh zzgh) {
        int zzly = zzgh.zzly();
        return zzal(zzly) + zzly;
    }

    static int zzb(zzgh zzgh, zzgy zzgy) {
        zzdg zzdg = (zzdg) zzgh;
        int zzjw = zzdg.zzjw();
        if (zzjw == -1) {
            zzjw = zzgy.zzs(zzdg);
            zzdg.zzn(zzjw);
        }
        return zzal(zzjw) + zzjw;
    }

    public final void zzlk() {
        if (zzlj() != 0) {
            throw new IllegalStateException("Did not write as much data as expected.");
        }
    }

    /* access modifiers changed from: 0000 */
    public final void zza(String str, zzic zzic) throws IOException {
        logger.logp(Level.WARNING, "com.google.protobuf.CodedOutputStream", "inefficientWriteStringNoTag", "Converting ill-formed UTF-16. Your Protocol Buffer will not round trip correctly!", zzic);
        byte[] bytes = str.getBytes(zzfb.UTF_8);
        try {
            zzag(bytes.length);
            zza(bytes, 0, bytes.length);
        } catch (IndexOutOfBoundsException e) {
            throw new zzc((Throwable) e);
        } catch (zzc e2) {
            throw e2;
        }
    }

    @Deprecated
    static int zzc(int i, zzgh zzgh, zzgy zzgy) {
        int zzaj = zzaj(i) << 1;
        zzdg zzdg = (zzdg) zzgh;
        int zzjw = zzdg.zzjw();
        if (zzjw == -1) {
            zzjw = zzgy.zzs(zzdg);
            zzdg.zzn(zzjw);
        }
        return zzaj + zzjw;
    }

    @Deprecated
    public static int zzd(zzgh zzgh) {
        return zzgh.zzly();
    }

    @Deprecated
    public static int zzar(int i) {
        return zzal(i);
    }
}
