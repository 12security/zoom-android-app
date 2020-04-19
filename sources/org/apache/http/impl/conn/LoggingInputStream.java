package org.apache.http.impl.conn;

import java.io.IOException;
import java.io.InputStream;

class LoggingInputStream extends InputStream {

    /* renamed from: in */
    private final InputStream f488in;
    private final Wire wire;

    public boolean markSupported() {
        return false;
    }

    public LoggingInputStream(InputStream inputStream, Wire wire2) {
        this.f488in = inputStream;
        this.wire = wire2;
    }

    public int read() throws IOException {
        try {
            int read = this.f488in.read();
            if (read == -1) {
                this.wire.input("end of stream");
            } else {
                this.wire.input(read);
            }
            return read;
        } catch (IOException e) {
            Wire wire2 = this.wire;
            StringBuilder sb = new StringBuilder();
            sb.append("[read] I/O error: ");
            sb.append(e.getMessage());
            wire2.input(sb.toString());
            throw e;
        }
    }

    public int read(byte[] bArr) throws IOException {
        try {
            int read = this.f488in.read(bArr);
            if (read == -1) {
                this.wire.input("end of stream");
            } else if (read > 0) {
                this.wire.input(bArr, 0, read);
            }
            return read;
        } catch (IOException e) {
            Wire wire2 = this.wire;
            StringBuilder sb = new StringBuilder();
            sb.append("[read] I/O error: ");
            sb.append(e.getMessage());
            wire2.input(sb.toString());
            throw e;
        }
    }

    public int read(byte[] bArr, int i, int i2) throws IOException {
        try {
            int read = this.f488in.read(bArr, i, i2);
            if (read == -1) {
                this.wire.input("end of stream");
            } else if (read > 0) {
                this.wire.input(bArr, i, read);
            }
            return read;
        } catch (IOException e) {
            Wire wire2 = this.wire;
            StringBuilder sb = new StringBuilder();
            sb.append("[read] I/O error: ");
            sb.append(e.getMessage());
            wire2.input(sb.toString());
            throw e;
        }
    }

    public long skip(long j) throws IOException {
        try {
            return super.skip(j);
        } catch (IOException e) {
            Wire wire2 = this.wire;
            StringBuilder sb = new StringBuilder();
            sb.append("[skip] I/O error: ");
            sb.append(e.getMessage());
            wire2.input(sb.toString());
            throw e;
        }
    }

    public int available() throws IOException {
        try {
            return this.f488in.available();
        } catch (IOException e) {
            Wire wire2 = this.wire;
            StringBuilder sb = new StringBuilder();
            sb.append("[available] I/O error : ");
            sb.append(e.getMessage());
            wire2.input(sb.toString());
            throw e;
        }
    }

    public void mark(int i) {
        super.mark(i);
    }

    public void reset() throws IOException {
        super.reset();
    }

    public void close() throws IOException {
        try {
            this.f488in.close();
        } catch (IOException e) {
            Wire wire2 = this.wire;
            StringBuilder sb = new StringBuilder();
            sb.append("[close] I/O error: ");
            sb.append(e.getMessage());
            wire2.input(sb.toString());
            throw e;
        }
    }
}
