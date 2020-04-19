package org.apache.http.impl.conn;

import java.io.IOException;
import org.apache.http.Consts;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.p020io.HttpTransportMetrics;
import org.apache.http.p020io.SessionOutputBuffer;
import org.apache.http.util.CharArrayBuffer;

@Contract(threading = ThreadingBehavior.IMMUTABLE)
@Deprecated
public class LoggingSessionOutputBuffer implements SessionOutputBuffer {
    private final String charset;
    private final SessionOutputBuffer out;
    private final Wire wire;

    public LoggingSessionOutputBuffer(SessionOutputBuffer sessionOutputBuffer, Wire wire2, String str) {
        this.out = sessionOutputBuffer;
        this.wire = wire2;
        if (str == null) {
            str = Consts.ASCII.name();
        }
        this.charset = str;
    }

    public LoggingSessionOutputBuffer(SessionOutputBuffer sessionOutputBuffer, Wire wire2) {
        this(sessionOutputBuffer, wire2, null);
    }

    public void write(byte[] bArr, int i, int i2) throws IOException {
        this.out.write(bArr, i, i2);
        if (this.wire.enabled()) {
            this.wire.output(bArr, i, i2);
        }
    }

    public void write(int i) throws IOException {
        this.out.write(i);
        if (this.wire.enabled()) {
            this.wire.output(i);
        }
    }

    public void write(byte[] bArr) throws IOException {
        this.out.write(bArr);
        if (this.wire.enabled()) {
            this.wire.output(bArr);
        }
    }

    public void flush() throws IOException {
        this.out.flush();
    }

    public void writeLine(CharArrayBuffer charArrayBuffer) throws IOException {
        this.out.writeLine(charArrayBuffer);
        if (this.wire.enabled()) {
            String str = new String(charArrayBuffer.buffer(), 0, charArrayBuffer.length());
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append("\r\n");
            this.wire.output(sb.toString().getBytes(this.charset));
        }
    }

    public void writeLine(String str) throws IOException {
        this.out.writeLine(str);
        if (this.wire.enabled()) {
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append("\r\n");
            this.wire.output(sb.toString().getBytes(this.charset));
        }
    }

    public HttpTransportMetrics getMetrics() {
        return this.out.getMetrics();
    }
}
