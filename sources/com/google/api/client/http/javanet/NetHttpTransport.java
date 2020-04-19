package com.google.api.client.http.javanet;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.util.Beta;
import com.google.api.client.util.Preconditions;
import com.google.api.client.util.SecurityUtils;
import com.google.api.client.util.SslUtils;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.util.Arrays;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

public final class NetHttpTransport extends HttpTransport {
    private static final String SHOULD_USE_PROXY_FLAG = "com.google.api.client.should_use_proxy";
    private static final String[] SUPPORTED_METHODS = {"DELETE", "GET", "HEAD", "OPTIONS", "POST", "PUT", "TRACE"};
    private final ConnectionFactory connectionFactory;
    private final HostnameVerifier hostnameVerifier;
    private final SSLSocketFactory sslSocketFactory;

    public static final class Builder {
        private ConnectionFactory connectionFactory;
        private HostnameVerifier hostnameVerifier;
        private Proxy proxy;
        private SSLSocketFactory sslSocketFactory;

        public Builder setProxy(Proxy proxy2) {
            this.proxy = proxy2;
            return this;
        }

        public Builder setConnectionFactory(ConnectionFactory connectionFactory2) {
            this.connectionFactory = connectionFactory2;
            return this;
        }

        public Builder trustCertificatesFromJavaKeyStore(InputStream inputStream, String str) throws GeneralSecurityException, IOException {
            KeyStore javaKeyStore = SecurityUtils.getJavaKeyStore();
            SecurityUtils.loadKeyStore(javaKeyStore, inputStream, str);
            return trustCertificates(javaKeyStore);
        }

        public Builder trustCertificatesFromStream(InputStream inputStream) throws GeneralSecurityException, IOException {
            KeyStore javaKeyStore = SecurityUtils.getJavaKeyStore();
            javaKeyStore.load(null, null);
            SecurityUtils.loadKeyStoreFromCertificates(javaKeyStore, SecurityUtils.getX509CertificateFactory(), inputStream);
            return trustCertificates(javaKeyStore);
        }

        public Builder trustCertificates(KeyStore keyStore) throws GeneralSecurityException {
            SSLContext tlsSslContext = SslUtils.getTlsSslContext();
            SslUtils.initSslContext(tlsSslContext, keyStore, SslUtils.getPkixTrustManagerFactory());
            return setSslSocketFactory(tlsSslContext.getSocketFactory());
        }

        @Beta
        public Builder doNotValidateCertificate() throws GeneralSecurityException {
            this.hostnameVerifier = SslUtils.trustAllHostnameVerifier();
            this.sslSocketFactory = SslUtils.trustAllSSLContext().getSocketFactory();
            return this;
        }

        public SSLSocketFactory getSslSocketFactory() {
            return this.sslSocketFactory;
        }

        public Builder setSslSocketFactory(SSLSocketFactory sSLSocketFactory) {
            this.sslSocketFactory = sSLSocketFactory;
            return this;
        }

        public HostnameVerifier getHostnameVerifier() {
            return this.hostnameVerifier;
        }

        public Builder setHostnameVerifier(HostnameVerifier hostnameVerifier2) {
            this.hostnameVerifier = hostnameVerifier2;
            return this;
        }

        public NetHttpTransport build() {
            if (System.getProperty(NetHttpTransport.SHOULD_USE_PROXY_FLAG) != null) {
                setProxy(NetHttpTransport.defaultProxy());
            }
            Proxy proxy2 = this.proxy;
            return proxy2 == null ? new NetHttpTransport(this.connectionFactory, this.sslSocketFactory, this.hostnameVerifier) : new NetHttpTransport(proxy2, this.sslSocketFactory, this.hostnameVerifier);
        }
    }

    /* access modifiers changed from: private */
    public static Proxy defaultProxy() {
        return new Proxy(Type.HTTP, new InetSocketAddress(System.getProperty("https.proxyHost"), Integer.parseInt(System.getProperty("https.proxyPort"))));
    }

    static {
        Arrays.sort(SUPPORTED_METHODS);
    }

    public NetHttpTransport() {
        this((ConnectionFactory) null, (SSLSocketFactory) null, (HostnameVerifier) null);
    }

    NetHttpTransport(Proxy proxy, SSLSocketFactory sSLSocketFactory, HostnameVerifier hostnameVerifier2) {
        this((ConnectionFactory) new DefaultConnectionFactory(proxy), sSLSocketFactory, hostnameVerifier2);
    }

    NetHttpTransport(ConnectionFactory connectionFactory2, SSLSocketFactory sSLSocketFactory, HostnameVerifier hostnameVerifier2) {
        this.connectionFactory = getConnectionFactory(connectionFactory2);
        this.sslSocketFactory = sSLSocketFactory;
        this.hostnameVerifier = hostnameVerifier2;
    }

    private ConnectionFactory getConnectionFactory(ConnectionFactory connectionFactory2) {
        if (connectionFactory2 != null) {
            return connectionFactory2;
        }
        if (System.getProperty(SHOULD_USE_PROXY_FLAG) != null) {
            return new DefaultConnectionFactory(defaultProxy());
        }
        return new DefaultConnectionFactory();
    }

    public boolean supportsMethod(String str) {
        return Arrays.binarySearch(SUPPORTED_METHODS, str) >= 0;
    }

    /* access modifiers changed from: protected */
    public NetHttpRequest buildRequest(String str, String str2) throws IOException {
        Preconditions.checkArgument(supportsMethod(str), "HTTP method %s not supported", str);
        HttpURLConnection openConnection = this.connectionFactory.openConnection(new URL(str2));
        openConnection.setRequestMethod(str);
        if (openConnection instanceof HttpsURLConnection) {
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) openConnection;
            HostnameVerifier hostnameVerifier2 = this.hostnameVerifier;
            if (hostnameVerifier2 != null) {
                httpsURLConnection.setHostnameVerifier(hostnameVerifier2);
            }
            SSLSocketFactory sSLSocketFactory = this.sslSocketFactory;
            if (sSLSocketFactory != null) {
                httpsURLConnection.setSSLSocketFactory(sSLSocketFactory);
            }
        }
        return new NetHttpRequest(openConnection);
    }
}
