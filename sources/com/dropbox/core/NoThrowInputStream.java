package com.dropbox.core;

import java.io.IOException;
import java.io.InputStream;

public final class NoThrowInputStream extends InputStream {
    private long bytesRead = 0;
    private final InputStream underlying;

    public static final class HiddenException extends RuntimeException {
        private static final long serialVersionUID = 0;

        public HiddenException(IOException iOException) {
            super(iOException);
        }

        public IOException getCause() {
            return (IOException) super.getCause();
        }
    }

    public NoThrowInputStream(InputStream inputStream) {
        this.underlying = inputStream;
    }

    public void close() {
        throw new UnsupportedOperationException("don't call close()");
    }

    public int read() {
        try {
            this.bytesRead++;
            return this.underlying.read();
        } catch (IOException e) {
            throw new HiddenException(e);
        }
    }

    public int read(byte[] bArr, int i, int i2) {
        try {
            int read = this.underlying.read(bArr, i, i2);
            this.bytesRead += (long) read;
            return read;
        } catch (IOException e) {
            throw new HiddenException(e);
        }
    }

    public int read(byte[] bArr) {
        try {
            int read = this.underlying.read(bArr);
            this.bytesRead += (long) read;
            return read;
        } catch (IOException e) {
            throw new HiddenException(e);
        }
    }

    public long getBytesRead() {
        return this.bytesRead;
    }
}
