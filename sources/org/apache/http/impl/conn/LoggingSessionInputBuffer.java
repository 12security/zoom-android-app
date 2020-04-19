package org.apache.http.impl.conn;

import java.io.IOException;
import org.apache.http.Consts;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.p020io.EofSensor;
import org.apache.http.p020io.HttpTransportMetrics;
import org.apache.http.p020io.SessionInputBuffer;
import org.apache.http.util.CharArrayBuffer;

@Contract(threading = ThreadingBehavior.IMMUTABLE)
@Deprecated
public class LoggingSessionInputBuffer implements SessionInputBuffer, EofSensor {
    private final String charset;
    private final EofSensor eofSensor;

    /* renamed from: in */
    private final SessionInputBuffer f489in;
    private final Wire wire;

    public LoggingSessionInputBuffer(SessionInputBuffer sessionInputBuffer, Wire wire2, String str) {
        this.f489in = sessionInputBuffer;
        this.eofSensor = sessionInputBuffer instanceof EofSensor ? (EofSensor) sessionInputBuffer : null;
        this.wire = wire2;
        if (str == null) {
            str = Consts.ASCII.name();
        }
        this.charset = str;
    }

    public LoggingSessionInputBuffer(SessionInputBuffer sessionInputBuffer, Wire wire2) {
        this(sessionInputBuffer, wire2, null);
    }

    public boolean isDataAvailable(int i) throws IOException {
        return this.f489in.isDataAvailable(i);
    }

    public int read(byte[] bArr, int i, int i2) throws IOException {
        int read = this.f489in.read(bArr, i, i2);
        if (this.wire.enabled() && read > 0) {
            this.wire.input(bArr, i, read);
        }
        return read;
    }

    public int read() throws IOException {
        int read = this.f489in.read();
        if (this.wire.enabled() && read != -1) {
            this.wire.input(read);
        }
        return read;
    }

    public int read(byte[] bArr) throws IOException {
        int read = this.f489in.read(bArr);
        if (this.wire.enabled() && read > 0) {
            this.wire.input(bArr, 0, read);
        }
        return read;
    }

    public String readLine() throws IOException {
        String readLine = this.f489in.readLine();
        if (this.wire.enabled() && readLine != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(readLine);
            sb.append("\r\n");
            this.wire.input(sb.toString().getBytes(this.charset));
        }
        return readLine;
    }

    public int readLine(CharArrayBuffer charArrayBuffer) throws IOException {
        int readLine = this.f489in.readLine(charArrayBuffer);
        if (this.wire.enabled() && readLine >= 0) {
            String str = new String(charArrayBuffer.buffer(), charArrayBuffer.length() - readLine, readLine);
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append("\r\n");
            this.wire.input(sb.toString().getBytes(this.charset));
        }
        return readLine;
    }

    public HttpTransportMetrics getMetrics() {
        return this.f489in.getMetrics();
    }

    public boolean isEof() {
        EofSensor eofSensor2 = this.eofSensor;
        if (eofSensor2 != null) {
            return eofSensor2.isEof();
        }
        return false;
    }
}
