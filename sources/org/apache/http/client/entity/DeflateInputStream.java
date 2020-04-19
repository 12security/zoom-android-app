package org.apache.http.client.entity;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;
import java.util.zip.ZipException;

public class DeflateInputStream extends InputStream {
    private final InputStream sourceStream;

    static class DeflateStream extends InflaterInputStream {
        private boolean closed = false;

        public DeflateStream(InputStream inputStream, Inflater inflater) {
            super(inputStream, inflater);
        }

        public void close() throws IOException {
            if (!this.closed) {
                this.closed = true;
                this.inf.end();
                super.close();
            }
        }
    }

    public DeflateInputStream(InputStream inputStream) throws IOException {
        PushbackInputStream pushbackInputStream = new PushbackInputStream(inputStream, 2);
        int read = pushbackInputStream.read();
        int read2 = pushbackInputStream.read();
        if (read == -1 || read2 == -1) {
            throw new ZipException("Unexpected end of stream");
        }
        pushbackInputStream.unread(read2);
        pushbackInputStream.unread(read);
        boolean z = true;
        int i = read & 255;
        int i2 = (i >> 4) & 15;
        int i3 = read2 & 255;
        if ((i & 15) == 8 && i2 <= 7 && ((i << 8) | i3) % 31 == 0) {
            z = false;
        }
        this.sourceStream = new DeflateStream(pushbackInputStream, new Inflater(z));
    }

    public int read() throws IOException {
        return this.sourceStream.read();
    }

    public int read(byte[] bArr) throws IOException {
        return this.sourceStream.read(bArr);
    }

    public int read(byte[] bArr, int i, int i2) throws IOException {
        return this.sourceStream.read(bArr, i, i2);
    }

    public long skip(long j) throws IOException {
        return this.sourceStream.skip(j);
    }

    public int available() throws IOException {
        return this.sourceStream.available();
    }

    public void mark(int i) {
        this.sourceStream.mark(i);
    }

    public void reset() throws IOException {
        this.sourceStream.reset();
    }

    public boolean markSupported() {
        return this.sourceStream.markSupported();
    }

    public void close() throws IOException {
        this.sourceStream.close();
    }
}
