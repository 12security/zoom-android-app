package com.google.common.hash;

import com.google.common.primitives.UnsignedBytes;
import java.io.Serializable;
import java.nio.ByteBuffer;
import javax.annotation.Nullable;

final class Murmur3_32HashFunction extends AbstractStreamingHashFunction implements Serializable {

    /* renamed from: C1 */
    private static final int f267C1 = -862048943;

    /* renamed from: C2 */
    private static final int f268C2 = 461845907;
    private static final long serialVersionUID = 0;
    private final int seed;

    private static final class Murmur3_32Hasher extends AbstractStreamingHasher {
        private static final int CHUNK_SIZE = 4;

        /* renamed from: h1 */
        private int f269h1;
        private int length = 0;

        Murmur3_32Hasher(int i) {
            super(4);
            this.f269h1 = i;
        }

        /* access modifiers changed from: protected */
        public void process(ByteBuffer byteBuffer) {
            this.f269h1 = Murmur3_32HashFunction.mixH1(this.f269h1, Murmur3_32HashFunction.mixK1(byteBuffer.getInt()));
            this.length += 4;
        }

        /* access modifiers changed from: protected */
        public void processRemaining(ByteBuffer byteBuffer) {
            this.length += byteBuffer.remaining();
            int i = 0;
            int i2 = 0;
            while (byteBuffer.hasRemaining()) {
                i ^= UnsignedBytes.toInt(byteBuffer.get()) << i2;
                i2 += 8;
            }
            this.f269h1 ^= Murmur3_32HashFunction.mixK1(i);
        }

        public HashCode makeHash() {
            return Murmur3_32HashFunction.fmix(this.f269h1, this.length);
        }
    }

    public int bits() {
        return 32;
    }

    Murmur3_32HashFunction(int i) {
        this.seed = i;
    }

    public Hasher newHasher() {
        return new Murmur3_32Hasher(this.seed);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Hashing.murmur3_32(");
        sb.append(this.seed);
        sb.append(")");
        return sb.toString();
    }

    public boolean equals(@Nullable Object obj) {
        boolean z = false;
        if (!(obj instanceof Murmur3_32HashFunction)) {
            return false;
        }
        if (this.seed == ((Murmur3_32HashFunction) obj).seed) {
            z = true;
        }
        return z;
    }

    public int hashCode() {
        return getClass().hashCode() ^ this.seed;
    }

    public HashCode hashInt(int i) {
        return fmix(mixH1(this.seed, mixK1(i)), 4);
    }

    public HashCode hashLong(long j) {
        int i = (int) j;
        int i2 = (int) (j >>> 32);
        return fmix(mixH1(mixH1(this.seed, mixK1(i)), mixK1(i2)), 8);
    }

    public HashCode hashUnencodedChars(CharSequence charSequence) {
        int i = this.seed;
        for (int i2 = 1; i2 < charSequence.length(); i2 += 2) {
            i = mixH1(i, mixK1(charSequence.charAt(i2 - 1) | (charSequence.charAt(i2) << 16)));
        }
        if ((charSequence.length() & 1) == 1) {
            i ^= mixK1(charSequence.charAt(charSequence.length() - 1));
        }
        return fmix(i, charSequence.length() * 2);
    }

    /* access modifiers changed from: private */
    public static int mixK1(int i) {
        return Integer.rotateLeft(i * f267C1, 15) * f268C2;
    }

    /* access modifiers changed from: private */
    public static int mixH1(int i, int i2) {
        return (Integer.rotateLeft(i ^ i2, 13) * 5) - 430675100;
    }

    /* access modifiers changed from: private */
    public static HashCode fmix(int i, int i2) {
        int i3 = i ^ i2;
        int i4 = (i3 ^ (i3 >>> 16)) * -2048144789;
        int i5 = (i4 ^ (i4 >>> 13)) * -1028477387;
        return HashCode.fromInt(i5 ^ (i5 >>> 16));
    }
}
