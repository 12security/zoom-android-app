package org.apache.http.impl.conn;

import java.io.IOException;
import java.io.OutputStream;

class LoggingOutputStream extends OutputStream {
    private final OutputStream out;
    private final Wire wire;

    public LoggingOutputStream(OutputStream outputStream, Wire wire2) {
        this.out = outputStream;
        this.wire = wire2;
    }

    public void write(int i) throws IOException {
        try {
            this.wire.output(i);
        } catch (IOException e) {
            Wire wire2 = this.wire;
            StringBuilder sb = new StringBuilder();
            sb.append("[write] I/O error: ");
            sb.append(e.getMessage());
            wire2.output(sb.toString());
            throw e;
        }
    }

    public void write(byte[] bArr) throws IOException {
        try {
            this.wire.output(bArr);
            this.out.write(bArr);
        } catch (IOException e) {
            Wire wire2 = this.wire;
            StringBuilder sb = new StringBuilder();
            sb.append("[write] I/O error: ");
            sb.append(e.getMessage());
            wire2.output(sb.toString());
            throw e;
        }
    }

    public void write(byte[] bArr, int i, int i2) throws IOException {
        try {
            this.wire.output(bArr, i, i2);
            this.out.write(bArr, i, i2);
        } catch (IOException e) {
            Wire wire2 = this.wire;
            StringBuilder sb = new StringBuilder();
            sb.append("[write] I/O error: ");
            sb.append(e.getMessage());
            wire2.output(sb.toString());
            throw e;
        }
    }

    public void flush() throws IOException {
        try {
            this.out.flush();
        } catch (IOException e) {
            Wire wire2 = this.wire;
            StringBuilder sb = new StringBuilder();
            sb.append("[flush] I/O error: ");
            sb.append(e.getMessage());
            wire2.output(sb.toString());
            throw e;
        }
    }

    public void close() throws IOException {
        try {
            this.out.close();
        } catch (IOException e) {
            Wire wire2 = this.wire;
            StringBuilder sb = new StringBuilder();
            sb.append("[close] I/O error: ");
            sb.append(e.getMessage());
            wire2.output(sb.toString());
            throw e;
        }
    }
}
