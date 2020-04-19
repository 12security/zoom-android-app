package org.apache.http.impl.p019io;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.http.p020io.SessionOutputBuffer;

/* renamed from: org.apache.http.impl.io.ChunkedOutputStream */
public class ChunkedOutputStream extends OutputStream {
    private final byte[] cache;
    private int cachePosition;
    private boolean closed;
    private final SessionOutputBuffer out;
    private boolean wroteLastChunk;

    @Deprecated
    public ChunkedOutputStream(SessionOutputBuffer sessionOutputBuffer, int i) throws IOException {
        this(i, sessionOutputBuffer);
    }

    @Deprecated
    public ChunkedOutputStream(SessionOutputBuffer sessionOutputBuffer) throws IOException {
        this(2048, sessionOutputBuffer);
    }

    public ChunkedOutputStream(int i, SessionOutputBuffer sessionOutputBuffer) {
        this.cachePosition = 0;
        this.wroteLastChunk = false;
        this.closed = false;
        this.cache = new byte[i];
        this.out = sessionOutputBuffer;
    }

    /* access modifiers changed from: protected */
    public void flushCache() throws IOException {
        int i = this.cachePosition;
        if (i > 0) {
            this.out.writeLine(Integer.toHexString(i));
            this.out.write(this.cache, 0, this.cachePosition);
            this.out.writeLine("");
            this.cachePosition = 0;
        }
    }

    /* access modifiers changed from: protected */
    public void flushCacheWithAppend(byte[] bArr, int i, int i2) throws IOException {
        this.out.writeLine(Integer.toHexString(this.cachePosition + i2));
        this.out.write(this.cache, 0, this.cachePosition);
        this.out.write(bArr, i, i2);
        this.out.writeLine("");
        this.cachePosition = 0;
    }

    /* access modifiers changed from: protected */
    public void writeClosingChunk() throws IOException {
        this.out.writeLine("0");
        this.out.writeLine("");
    }

    public void finish() throws IOException {
        if (!this.wroteLastChunk) {
            flushCache();
            writeClosingChunk();
            this.wroteLastChunk = true;
        }
    }

    public void write(int i) throws IOException {
        if (!this.closed) {
            byte[] bArr = this.cache;
            int i2 = this.cachePosition;
            bArr[i2] = (byte) i;
            this.cachePosition = i2 + 1;
            if (this.cachePosition == bArr.length) {
                flushCache();
                return;
            }
            return;
        }
        throw new IOException("Attempted write to closed stream.");
    }

    public void write(byte[] bArr) throws IOException {
        write(bArr, 0, bArr.length);
    }

    public void write(byte[] bArr, int i, int i2) throws IOException {
        if (!this.closed) {
            byte[] bArr2 = this.cache;
            int length = bArr2.length;
            int i3 = this.cachePosition;
            if (i2 >= length - i3) {
                flushCacheWithAppend(bArr, i, i2);
                return;
            }
            System.arraycopy(bArr, i, bArr2, i3, i2);
            this.cachePosition += i2;
            return;
        }
        throw new IOException("Attempted write to closed stream.");
    }

    public void flush() throws IOException {
        flushCache();
        this.out.flush();
    }

    public void close() throws IOException {
        if (!this.closed) {
            this.closed = true;
            finish();
            this.out.flush();
        }
    }
}
