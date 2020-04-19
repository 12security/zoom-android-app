package com.dropbox.core.util;

import java.io.IOException;
import java.io.OutputStream;

public class CountingOutputStream extends OutputStream {
    private long bytesWritten = 0;
    private final OutputStream out;

    public CountingOutputStream(OutputStream outputStream) {
        this.out = outputStream;
    }

    public long getBytesWritten() {
        return this.bytesWritten;
    }

    public void write(int i) throws IOException {
        this.bytesWritten++;
        this.out.write(i);
    }

    public void write(byte[] bArr) throws IOException {
        this.bytesWritten += (long) bArr.length;
        this.out.write(bArr);
    }

    public void write(byte[] bArr, int i, int i2) throws IOException {
        this.bytesWritten += (long) i2;
        this.out.write(bArr, i, i2);
    }

    public void flush() throws IOException {
        this.out.flush();
    }

    public void close() throws IOException {
        throw new UnsupportedOperationException("You aren't allowed to call close() on this object.");
    }
}
