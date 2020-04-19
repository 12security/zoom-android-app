package org.apache.http.impl.p019io;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.http.p020io.SessionOutputBuffer;
import org.apache.http.util.Args;

/* renamed from: org.apache.http.impl.io.ContentLengthOutputStream */
public class ContentLengthOutputStream extends OutputStream {
    private boolean closed = false;
    private final long contentLength;
    private final SessionOutputBuffer out;
    private long total = 0;

    public ContentLengthOutputStream(SessionOutputBuffer sessionOutputBuffer, long j) {
        this.out = (SessionOutputBuffer) Args.notNull(sessionOutputBuffer, "Session output buffer");
        this.contentLength = Args.notNegative(j, "Content length");
    }

    public void close() throws IOException {
        if (!this.closed) {
            this.closed = true;
            this.out.flush();
        }
    }

    public void flush() throws IOException {
        this.out.flush();
    }

    public void write(byte[] bArr, int i, int i2) throws IOException {
        if (!this.closed) {
            long j = this.total;
            long j2 = this.contentLength;
            if (j < j2) {
                long j3 = j2 - j;
                if (((long) i2) > j3) {
                    i2 = (int) j3;
                }
                this.out.write(bArr, i, i2);
                this.total += (long) i2;
                return;
            }
            return;
        }
        throw new IOException("Attempted write to closed stream.");
    }

    public void write(byte[] bArr) throws IOException {
        write(bArr, 0, bArr.length);
    }

    public void write(int i) throws IOException {
        if (this.closed) {
            throw new IOException("Attempted write to closed stream.");
        } else if (this.total < this.contentLength) {
            this.out.write(i);
            this.total++;
        }
    }
}
