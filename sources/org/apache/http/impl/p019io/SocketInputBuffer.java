package org.apache.http.impl.p019io;

import java.io.IOException;
import java.net.Socket;
import org.apache.http.p020io.EofSensor;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;

@Deprecated
/* renamed from: org.apache.http.impl.io.SocketInputBuffer */
public class SocketInputBuffer extends AbstractSessionInputBuffer implements EofSensor {
    private boolean eof = false;
    private final Socket socket;

    public SocketInputBuffer(Socket socket2, int i, HttpParams httpParams) throws IOException {
        Args.notNull(socket2, "Socket");
        this.socket = socket2;
        if (i < 0) {
            i = socket2.getReceiveBufferSize();
        }
        if (i < 1024) {
            i = 1024;
        }
        init(socket2.getInputStream(), i, httpParams);
    }

    /* access modifiers changed from: protected */
    public int fillBuffer() throws IOException {
        int fillBuffer = super.fillBuffer();
        this.eof = fillBuffer == -1;
        return fillBuffer;
    }

    public boolean isDataAvailable(int i) throws IOException {
        boolean hasBufferedData = hasBufferedData();
        if (hasBufferedData) {
            return hasBufferedData;
        }
        int soTimeout = this.socket.getSoTimeout();
        try {
            this.socket.setSoTimeout(i);
            fillBuffer();
            return hasBufferedData();
        } finally {
            this.socket.setSoTimeout(soTimeout);
        }
    }

    public boolean isEof() {
        return this.eof;
    }
}
