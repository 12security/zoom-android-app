package org.apache.http.impl.p019io;

import java.io.IOException;
import java.net.Socket;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;

@Deprecated
/* renamed from: org.apache.http.impl.io.SocketOutputBuffer */
public class SocketOutputBuffer extends AbstractSessionOutputBuffer {
    public SocketOutputBuffer(Socket socket, int i, HttpParams httpParams) throws IOException {
        Args.notNull(socket, "Socket");
        if (i < 0) {
            i = socket.getSendBufferSize();
        }
        if (i < 1024) {
            i = 1024;
        }
        init(socket.getOutputStream(), i, httpParams);
    }
}
