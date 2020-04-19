package org.apache.http.impl;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import org.apache.http.HttpInetConnection;
import org.apache.http.impl.p019io.SocketInputBuffer;
import org.apache.http.impl.p019io.SocketOutputBuffer;
import org.apache.http.p020io.SessionInputBuffer;
import org.apache.http.p020io.SessionOutputBuffer;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;

@Deprecated
public class SocketHttpClientConnection extends AbstractHttpClientConnection implements HttpInetConnection {
    private volatile boolean open;
    private volatile Socket socket = null;

    /* access modifiers changed from: protected */
    public void assertNotOpen() {
        Asserts.check(!this.open, "Connection is already open");
    }

    /* access modifiers changed from: protected */
    public void assertOpen() {
        Asserts.check(this.open, "Connection is not open");
    }

    /* access modifiers changed from: protected */
    public SessionInputBuffer createSessionInputBuffer(Socket socket2, int i, HttpParams httpParams) throws IOException {
        return new SocketInputBuffer(socket2, i, httpParams);
    }

    /* access modifiers changed from: protected */
    public SessionOutputBuffer createSessionOutputBuffer(Socket socket2, int i, HttpParams httpParams) throws IOException {
        return new SocketOutputBuffer(socket2, i, httpParams);
    }

    /* access modifiers changed from: protected */
    public void bind(Socket socket2, HttpParams httpParams) throws IOException {
        Args.notNull(socket2, "Socket");
        Args.notNull(httpParams, "HTTP parameters");
        this.socket = socket2;
        int intParameter = httpParams.getIntParameter(CoreConnectionPNames.SOCKET_BUFFER_SIZE, -1);
        init(createSessionInputBuffer(socket2, intParameter, httpParams), createSessionOutputBuffer(socket2, intParameter, httpParams), httpParams);
        this.open = true;
    }

    public boolean isOpen() {
        return this.open;
    }

    /* access modifiers changed from: protected */
    public Socket getSocket() {
        return this.socket;
    }

    public InetAddress getLocalAddress() {
        if (this.socket != null) {
            return this.socket.getLocalAddress();
        }
        return null;
    }

    public int getLocalPort() {
        if (this.socket != null) {
            return this.socket.getLocalPort();
        }
        return -1;
    }

    public InetAddress getRemoteAddress() {
        if (this.socket != null) {
            return this.socket.getInetAddress();
        }
        return null;
    }

    public int getRemotePort() {
        if (this.socket != null) {
            return this.socket.getPort();
        }
        return -1;
    }

    public void setSocketTimeout(int i) {
        assertOpen();
        if (this.socket != null) {
            try {
                this.socket.setSoTimeout(i);
            } catch (SocketException unused) {
            }
        }
    }

    public int getSocketTimeout() {
        if (this.socket == null) {
            return -1;
        }
        try {
            return this.socket.getSoTimeout();
        } catch (SocketException unused) {
            return -1;
        }
    }

    public void shutdown() throws IOException {
        this.open = false;
        Socket socket2 = this.socket;
        if (socket2 != null) {
            socket2.close();
        }
    }

    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:8:0x0010 */
    /* JADX WARNING: Removed duplicated region for block: B:11:? A[ExcHandler: UnsupportedOperationException (unused java.lang.UnsupportedOperationException), SYNTHETIC, Splitter:B:8:0x0010] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void close() throws java.io.IOException {
        /*
            r2 = this;
            boolean r0 = r2.open
            if (r0 != 0) goto L_0x0005
            return
        L_0x0005:
            r0 = 0
            r2.open = r0
            java.net.Socket r0 = r2.socket
            r2.doFlush()     // Catch:{ all -> 0x0017 }
            r0.shutdownOutput()     // Catch:{ IOException -> 0x0010 }
        L_0x0010:
            r0.shutdownInput()     // Catch:{ UnsupportedOperationException -> 0x0013, UnsupportedOperationException -> 0x0013 }
        L_0x0013:
            r0.close()
            return
        L_0x0017:
            r1 = move-exception
            r0.close()
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.http.impl.SocketHttpClientConnection.close():void");
    }

    private static void formatAddress(StringBuilder sb, SocketAddress socketAddress) {
        if (socketAddress instanceof InetSocketAddress) {
            InetSocketAddress inetSocketAddress = (InetSocketAddress) socketAddress;
            sb.append(inetSocketAddress.getAddress() != null ? inetSocketAddress.getAddress().getHostAddress() : inetSocketAddress.getAddress());
            sb.append(':');
            sb.append(inetSocketAddress.getPort());
            return;
        }
        sb.append(socketAddress);
    }

    public String toString() {
        if (this.socket == null) {
            return super.toString();
        }
        StringBuilder sb = new StringBuilder();
        SocketAddress remoteSocketAddress = this.socket.getRemoteSocketAddress();
        SocketAddress localSocketAddress = this.socket.getLocalSocketAddress();
        if (!(remoteSocketAddress == null || localSocketAddress == null)) {
            formatAddress(sb, localSocketAddress);
            sb.append("<->");
            formatAddress(sb, remoteSocketAddress);
        }
        return sb.toString();
    }
}
