package com.google.api.client.util;

import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public final class SslUtils {
    public static SSLContext getSslContext() throws NoSuchAlgorithmException {
        return SSLContext.getInstance("SSL");
    }

    public static SSLContext getTlsSslContext() throws NoSuchAlgorithmException {
        return SSLContext.getInstance("TLS");
    }

    public static TrustManagerFactory getDefaultTrustManagerFactory() throws NoSuchAlgorithmException {
        return TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
    }

    public static TrustManagerFactory getPkixTrustManagerFactory() throws NoSuchAlgorithmException {
        return TrustManagerFactory.getInstance("PKIX");
    }

    public static KeyManagerFactory getDefaultKeyManagerFactory() throws NoSuchAlgorithmException {
        return KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
    }

    public static KeyManagerFactory getPkixKeyManagerFactory() throws NoSuchAlgorithmException {
        return KeyManagerFactory.getInstance("PKIX");
    }

    public static SSLContext initSslContext(SSLContext sSLContext, KeyStore keyStore, TrustManagerFactory trustManagerFactory) throws GeneralSecurityException {
        trustManagerFactory.init(keyStore);
        sSLContext.init(null, trustManagerFactory.getTrustManagers(), null);
        return sSLContext;
    }

    @Beta
    public static SSLContext trustAllSSLContext() throws GeneralSecurityException {
        TrustManager[] trustManagerArr = {new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] x509CertificateArr, String str) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] x509CertificateArr, String str) throws CertificateException {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        }};
        SSLContext tlsSslContext = getTlsSslContext();
        tlsSslContext.init(null, trustManagerArr, null);
        return tlsSslContext;
    }

    @Beta
    public static HostnameVerifier trustAllHostnameVerifier() {
        return new HostnameVerifier() {
            public boolean verify(String str, SSLSession sSLSession) {
                return true;
            }
        };
    }

    private SslUtils() {
    }
}
