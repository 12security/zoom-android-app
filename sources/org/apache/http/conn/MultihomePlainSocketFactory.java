package org.apache.http.conn;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;

@Contract(threading = ThreadingBehavior.IMMUTABLE)
@Deprecated
public final class MultihomePlainSocketFactory implements SocketFactory {
    private static final MultihomePlainSocketFactory DEFAULT_FACTORY = new MultihomePlainSocketFactory();

    public static MultihomePlainSocketFactory getSocketFactory() {
        return DEFAULT_FACTORY;
    }

    private MultihomePlainSocketFactory() {
    }

    public Socket createSocket() {
        return new Socket();
    }

    public Socket connectSocket(Socket socket, String str, int i, InetAddress inetAddress, int i2, HttpParams httpParams) throws IOException {
        Args.notNull(str, "Target host");
        Args.notNull(httpParams, "HTTP parameters");
        if (socket == null) {
            socket = createSocket();
        }
        if (inetAddress != null || i2 > 0) {
            if (i2 <= 0) {
                i2 = 0;
            }
            socket.bind(new InetSocketAddress(inetAddress, i2));
        }
        int connectionTimeout = HttpConnectionParams.getConnectionTimeout(httpParams);
        InetAddress[] allByName = InetAddress.getAllByName(str);
        ArrayList<InetAddress> arrayList = new ArrayList<>(allByName.length);
        arrayList.addAll(Arrays.asList(allByName));
        Collections.shuffle(arrayList);
        Throwable th = null;
        for (InetAddress inetAddress2 : arrayList) {
            try {
                socket.connect(new InetSocketAddress(inetAddress2, i), connectionTimeout);
                break;
            } catch (SocketTimeoutException unused) {
                StringBuilder sb = new StringBuilder();
                sb.append("Connect to ");
                sb.append(inetAddress2);
                sb.append(" timed out");
                throw new ConnectTimeoutException(sb.toString());
            } catch (IOException e) {
                th = e;
                socket = new Socket();
            }
        }
        if (th == null) {
            return socket;
        }
        throw th;
    }

    public final boolean isSecure(Socket socket) throws IllegalArgumentException {
        Args.notNull(socket, "Socket");
        Asserts.check(!socket.isClosed(), "Socket is closed");
        return false;
    }
}
