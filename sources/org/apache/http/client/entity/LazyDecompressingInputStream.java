package org.apache.http.client.entity;

import java.io.IOException;
import java.io.InputStream;

class LazyDecompressingInputStream extends InputStream {
    private final InputStreamFactory inputStreamFactory;
    private final InputStream wrappedStream;
    private InputStream wrapperStream;

    public boolean markSupported() {
        return false;
    }

    public LazyDecompressingInputStream(InputStream inputStream, InputStreamFactory inputStreamFactory2) {
        this.wrappedStream = inputStream;
        this.inputStreamFactory = inputStreamFactory2;
    }

    private void initWrapper() throws IOException {
        if (this.wrapperStream == null) {
            this.wrapperStream = this.inputStreamFactory.create(this.wrappedStream);
        }
    }

    public int read() throws IOException {
        initWrapper();
        return this.wrapperStream.read();
    }

    public int read(byte[] bArr) throws IOException {
        initWrapper();
        return this.wrapperStream.read(bArr);
    }

    public int read(byte[] bArr, int i, int i2) throws IOException {
        initWrapper();
        return this.wrapperStream.read(bArr, i, i2);
    }

    public long skip(long j) throws IOException {
        initWrapper();
        return this.wrapperStream.skip(j);
    }

    public int available() throws IOException {
        initWrapper();
        return this.wrapperStream.available();
    }

    public void close() throws IOException {
        try {
            if (this.wrapperStream != null) {
                this.wrapperStream.close();
            }
        } finally {
            this.wrappedStream.close();
        }
    }
}
