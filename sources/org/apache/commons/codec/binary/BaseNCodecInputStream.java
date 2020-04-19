package org.apache.commons.codec.binary;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class BaseNCodecInputStream extends FilterInputStream {
    private final BaseNCodec baseNCodec;
    private final Context context = new Context();
    private final boolean doEncode;
    private final byte[] singleByte = new byte[1];

    public boolean markSupported() {
        return false;
    }

    protected BaseNCodecInputStream(InputStream inputStream, BaseNCodec baseNCodec2, boolean z) {
        super(inputStream);
        this.doEncode = z;
        this.baseNCodec = baseNCodec2;
    }

    public int available() throws IOException {
        return this.context.eof ^ true ? 1 : 0;
    }

    public synchronized void mark(int i) {
    }

    /* JADX WARNING: type inference failed for: r0v10 */
    /* JADX WARNING: Incorrect type for immutable var: ssa=byte, code=int, for r0v5, types: [int, byte] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int read() throws java.io.IOException {
        /*
            r3 = this;
            byte[] r0 = r3.singleByte
            r1 = 1
            r2 = 0
            int r0 = r3.read(r0, r2, r1)
        L_0x0008:
            if (r0 != 0) goto L_0x0011
            byte[] r0 = r3.singleByte
            int r0 = r3.read(r0, r2, r1)
            goto L_0x0008
        L_0x0011:
            if (r0 <= 0) goto L_0x001c
            byte[] r0 = r3.singleByte
            byte r0 = r0[r2]
            if (r0 >= 0) goto L_0x001b
            int r0 = r0 + 256
        L_0x001b:
            return r0
        L_0x001c:
            r0 = -1
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.codec.binary.BaseNCodecInputStream.read():int");
    }

    public int read(byte[] bArr, int i, int i2) throws IOException {
        if (bArr == null) {
            throw new NullPointerException();
        } else if (i < 0 || i2 < 0) {
            throw new IndexOutOfBoundsException();
        } else if (i > bArr.length || i + i2 > bArr.length) {
            throw new IndexOutOfBoundsException();
        } else if (i2 == 0) {
            return 0;
        } else {
            int i3 = 0;
            while (i3 == 0) {
                if (!this.baseNCodec.hasData(this.context)) {
                    byte[] bArr2 = new byte[(this.doEncode ? 4096 : 8192)];
                    int read = this.in.read(bArr2);
                    if (this.doEncode) {
                        this.baseNCodec.encode(bArr2, 0, read, this.context);
                    } else {
                        this.baseNCodec.decode(bArr2, 0, read, this.context);
                    }
                }
                i3 = this.baseNCodec.readResults(bArr, i, i2, this.context);
            }
            return i3;
        }
    }

    public synchronized void reset() throws IOException {
        throw new IOException("mark/reset not supported");
    }

    public long skip(long j) throws IOException {
        if (j >= 0) {
            byte[] bArr = new byte[512];
            long j2 = j;
            while (j2 > 0) {
                int read = read(bArr, 0, (int) Math.min((long) bArr.length, j2));
                if (read == -1) {
                    break;
                }
                j2 -= (long) read;
            }
            return j - j2;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Negative skip length: ");
        sb.append(j);
        throw new IllegalArgumentException(sb.toString());
    }
}
