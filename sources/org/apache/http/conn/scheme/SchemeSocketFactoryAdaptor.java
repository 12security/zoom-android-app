package org.apache.http.conn.scheme;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.params.HttpParams;

@Deprecated
class SchemeSocketFactoryAdaptor implements SchemeSocketFactory {
    private final SocketFactory factory;

    SchemeSocketFactoryAdaptor(SocketFactory socketFactory) {
        this.factory = socketFactory;
    }

    public Socket connectSocket(Socket socket, InetSocketAddress inetSocketAddress, InetSocketAddress inetSocketAddress2, HttpParams httpParams) throws IOException, UnknownHostException, ConnectTimeoutException {
        int i;
        InetAddress inetAddress;
        String hostName = inetSocketAddress.getHostName();
        int port = inetSocketAddress.getPort();
        if (inetSocketAddress2 != null) {
            inetAddress = inetSocketAddress2.getAddress();
            i = inetSocketAddress2.getPort();
        } else {
            inetAddress = null;
            i = 0;
        }
        return this.factory.connectSocket(socket, hostName, port, inetAddress, i, httpParams);
    }

    public Socket createSocket(HttpParams httpParams) throws IOException {
        return this.factory.createSocket();
    }

    public boolean isSecure(Socket socket) throws IllegalArgumentException {
        return this.factory.isSecure(socket);
    }

    public SocketFactory getFactory() {
        return this.factory;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (obj instanceof SchemeSocketFactoryAdaptor) {
            return this.factory.equals(((SchemeSocketFactoryAdaptor) obj).factory);
        }
        return this.factory.equals(obj);
    }

    public int hashCode() {
        return this.factory.hashCode();
    }
}
