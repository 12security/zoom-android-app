package com.google.common.hash;

import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.nio.ByteBuffer;
import javax.annotation.Nullable;

final class SipHashFunction extends AbstractStreamingHashFunction implements Serializable {
    private static final long serialVersionUID = 0;

    /* renamed from: c */
    private final int f270c;

    /* renamed from: d */
    private final int f271d;

    /* renamed from: k0 */
    private final long f272k0;

    /* renamed from: k1 */
    private final long f273k1;

    private static final class SipHasher extends AbstractStreamingHasher {
        private static final int CHUNK_SIZE = 8;

        /* renamed from: b */
        private long f274b = 0;

        /* renamed from: c */
        private final int f275c;

        /* renamed from: d */
        private final int f276d;
        private long finalM = 0;

        /* renamed from: v0 */
        private long f277v0 = 8317987319222330741L;

        /* renamed from: v1 */
        private long f278v1 = 7237128888997146477L;

        /* renamed from: v2 */
        private long f279v2 = 7816392313619706465L;

        /* renamed from: v3 */
        private long f280v3 = 8387220255154660723L;

        SipHasher(int i, int i2, long j, long j2) {
            super(8);
            this.f275c = i;
            this.f276d = i2;
            this.f277v0 ^= j;
            this.f278v1 ^= j2;
            this.f279v2 ^= j;
            this.f280v3 ^= j2;
        }

        /* access modifiers changed from: protected */
        public void process(ByteBuffer byteBuffer) {
            this.f274b += 8;
            processM(byteBuffer.getLong());
        }

        /* access modifiers changed from: protected */
        public void processRemaining(ByteBuffer byteBuffer) {
            this.f274b += (long) byteBuffer.remaining();
            int i = 0;
            while (byteBuffer.hasRemaining()) {
                this.finalM ^= (((long) byteBuffer.get()) & 255) << i;
                i += 8;
            }
        }

        public HashCode makeHash() {
            this.finalM ^= this.f274b << 56;
            processM(this.finalM);
            this.f279v2 ^= 255;
            sipRound(this.f276d);
            return HashCode.fromLong(((this.f277v0 ^ this.f278v1) ^ this.f279v2) ^ this.f280v3);
        }

        private void processM(long j) {
            this.f280v3 ^= j;
            sipRound(this.f275c);
            this.f277v0 = j ^ this.f277v0;
        }

        private void sipRound(int i) {
            for (int i2 = 0; i2 < i; i2++) {
                long j = this.f277v0;
                long j2 = this.f278v1;
                this.f277v0 = j + j2;
                this.f279v2 += this.f280v3;
                this.f278v1 = Long.rotateLeft(j2, 13);
                this.f280v3 = Long.rotateLeft(this.f280v3, 16);
                long j3 = this.f278v1;
                long j4 = this.f277v0;
                this.f278v1 = j3 ^ j4;
                this.f280v3 ^= this.f279v2;
                this.f277v0 = Long.rotateLeft(j4, 32);
                long j5 = this.f279v2;
                long j6 = this.f278v1;
                this.f279v2 = j5 + j6;
                this.f277v0 += this.f280v3;
                this.f278v1 = Long.rotateLeft(j6, 17);
                this.f280v3 = Long.rotateLeft(this.f280v3, 21);
                long j7 = this.f278v1;
                long j8 = this.f279v2;
                this.f278v1 = j7 ^ j8;
                this.f280v3 ^= this.f277v0;
                this.f279v2 = Long.rotateLeft(j8, 32);
            }
        }
    }

    public int bits() {
        return 64;
    }

    SipHashFunction(int i, int i2, long j, long j2) {
        boolean z = true;
        Preconditions.checkArgument(i > 0, "The number of SipRound iterations (c=%s) during Compression must be positive.", i);
        if (i2 <= 0) {
            z = false;
        }
        Preconditions.checkArgument(z, "The number of SipRound iterations (d=%s) during Finalization must be positive.", i2);
        this.f270c = i;
        this.f271d = i2;
        this.f272k0 = j;
        this.f273k1 = j2;
    }

    public Hasher newHasher() {
        SipHasher sipHasher = new SipHasher(this.f270c, this.f271d, this.f272k0, this.f273k1);
        return sipHasher;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Hashing.sipHash");
        sb.append(this.f270c);
        sb.append("");
        sb.append(this.f271d);
        sb.append("(");
        sb.append(this.f272k0);
        sb.append(", ");
        sb.append(this.f273k1);
        sb.append(")");
        return sb.toString();
    }

    public boolean equals(@Nullable Object obj) {
        boolean z = false;
        if (!(obj instanceof SipHashFunction)) {
            return false;
        }
        SipHashFunction sipHashFunction = (SipHashFunction) obj;
        if (this.f270c == sipHashFunction.f270c && this.f271d == sipHashFunction.f271d && this.f272k0 == sipHashFunction.f272k0 && this.f273k1 == sipHashFunction.f273k1) {
            z = true;
        }
        return z;
    }

    public int hashCode() {
        return (int) ((((long) ((getClass().hashCode() ^ this.f270c) ^ this.f271d)) ^ this.f272k0) ^ this.f273k1);
    }
}
