package com.google.api.client.http.apache;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import org.apache.http.conn.ssl.SSLSocketFactory;

final class SSLSocketFactoryExtension extends SSLSocketFactory {
    private final javax.net.ssl.SSLSocketFactory socketFactory;

    SSLSocketFactoryExtension(SSLContext sSLContext) throws KeyManagementException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException {
        super((KeyStore) null);
        this.socketFactory = sSLContext.getSocketFactory();
    }

    public Socket createSocket() throws IOException {
        return this.socketFactory.createSocket();
    }

    public Socket createSocket(Socket socket, String str, int i, boolean z) throws IOException, UnknownHostException {
        SSLSocket sSLSocket = (SSLSocket) this.socketFactory.createSocket(socket, str, i, z);
        getHostnameVerifier().verify(str, sSLSocket);
        return sSLSocket;
    }
}
