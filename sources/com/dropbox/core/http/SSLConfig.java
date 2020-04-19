package com.dropbox.core.http;

import com.dropbox.core.util.IOUtil;
import com.dropbox.core.util.LangUtil;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class SSLConfig {
    private static final HashSet<String> ALLOWED_CIPHER_SUITES = new HashSet<>(Arrays.asList(new String[]{"SSL_ECDHE_RSA_WITH_AES_256_GCM_SHA384", "SSL_ECDHE_RSA_WITH_AES_256_CBC_SHA384", "SSL_ECDHE_RSA_WITH_AES_256_CBC_SHA", "SSL_ECDHE_RSA_WITH_AES_128_GCM_SHA256", "SSL_ECDHE_RSA_WITH_AES_128_CBC_SHA256", "SSL_ECDHE_RSA_WITH_AES_128_CBC_SHA", "SSL_ECDHE_RSA_WITH_RC4_128_SHA", "SSL_DHE_RSA_WITH_AES_256_GCM_SHA384", "SSL_DHE_RSA_WITH_AES_256_CBC_SHA256", "SSL_DHE_RSA_WITH_AES_256_CBC_SHA", "SSL_DHE_RSA_WITH_AES_128_GCM_SHA256", "SSL_DHE_RSA_WITH_AES_128_CBC_SHA256", "SSL_DHE_RSA_WITH_AES_128_CBC_SHA", "SSL_RSA_WITH_AES_256_GCM_SHA384", "SSL_RSA_WITH_AES_256_CBC_SHA256", "SSL_RSA_WITH_AES_256_CBC_SHA", "SSL_RSA_WITH_AES_128_GCM_SHA256", "SSL_RSA_WITH_AES_128_CBC_SHA256", "SSL_RSA_WITH_AES_128_CBC_SHA", "TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384", "TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384", "TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA", "TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256", "TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256", "TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA", "TLS_ECDHE_RSA_WITH_RC4_128_SHA", "TLS_DHE_RSA_WITH_AES_256_GCM_SHA384", "TLS_DHE_RSA_WITH_AES_256_CBC_SHA256", "TLS_DHE_RSA_WITH_AES_256_CBC_SHA", "TLS_DHE_RSA_WITH_AES_128_GCM_SHA256", "TLS_DHE_RSA_WITH_AES_128_CBC_SHA256", "TLS_DHE_RSA_WITH_AES_128_CBC_SHA", "TLS_RSA_WITH_AES_256_GCM_SHA384", "TLS_RSA_WITH_AES_256_CBC_SHA256", "TLS_RSA_WITH_AES_256_CBC_SHA", "TLS_RSA_WITH_AES_128_GCM_SHA256", "TLS_RSA_WITH_AES_128_CBC_SHA256", "TLS_RSA_WITH_AES_128_CBC_SHA", "ECDHE-RSA-AES256-GCM-SHA384", "ECDHE-RSA-AES256-SHA384", "ECDHE-RSA-AES256-SHA", "ECDHE-RSA-AES128-GCM-SHA256", "ECDHE-RSA-AES128-SHA256", "ECDHE-RSA-AES128-SHA", "ECDHE-RSA-RC4-SHA", "DHE-RSA-AES256-GCM-SHA384", "DHE-RSA-AES256-SHA256", "DHE-RSA-AES256-SHA", "DHE-RSA-AES128-GCM-SHA256", "DHE-RSA-AES128-SHA256", "DHE-RSA-AES128-SHA", "AES256-GCM-SHA384", "AES256-SHA256", "AES256-SHA", "AES128-GCM-SHA256", "AES128-SHA256", "AES128-SHA"}));
    private static CipherSuiteFilterationResults CACHED_CIPHER_SUITE_FILTERATION_RESULTS = null;
    private static final int MAX_CERT_LENGTH = 10240;
    private static final String[] PROTOCOL_LIST_TLS_V1 = {"TLSv1"};
    private static final String[] PROTOCOL_LIST_TLS_V1_0 = {"TLSv1.0"};
    private static final String[] PROTOCOL_LIST_TLS_V1_2 = {"TLSv1.2"};
    private static final String ROOT_CERTS_RESOURCE = "/trusted-certs.raw";
    private static final SSLSocketFactory SSL_SOCKET_FACTORY = createSSLSocketFactory();
    private static final X509TrustManager TRUST_MANAGER = createTrustManager();

    private static final class CipherSuiteFilterationResults {
        /* access modifiers changed from: private */
        public final String[] enabled;
        /* access modifiers changed from: private */
        public final String[] supported;

        public CipherSuiteFilterationResults(String[] strArr, String[] strArr2) {
            this.supported = strArr;
            this.enabled = strArr2;
        }

        public String[] getSupported() {
            return this.supported;
        }

        public String[] getEnabled() {
            return this.enabled;
        }
    }

    public static final class LoadException extends Exception {
        private static final long serialVersionUID = 0;

        public LoadException(String str, Throwable th) {
            super(str, th);
        }
    }

    private static final class SSLSocketFactoryWrapper extends SSLSocketFactory {
        private final SSLSocketFactory mBase;

        public SSLSocketFactoryWrapper(SSLSocketFactory sSLSocketFactory) {
            this.mBase = sSLSocketFactory;
        }

        public String[] getDefaultCipherSuites() {
            return this.mBase.getDefaultCipherSuites();
        }

        public String[] getSupportedCipherSuites() {
            return this.mBase.getSupportedCipherSuites();
        }

        public Socket createSocket(String str, int i) throws IOException {
            Socket createSocket = this.mBase.createSocket(str, i);
            SSLConfig.limitProtocolsAndCiphers((SSLSocket) createSocket);
            return createSocket;
        }

        public Socket createSocket(InetAddress inetAddress, int i) throws IOException {
            Socket createSocket = this.mBase.createSocket(inetAddress, i);
            SSLConfig.limitProtocolsAndCiphers((SSLSocket) createSocket);
            return createSocket;
        }

        public Socket createSocket(String str, int i, InetAddress inetAddress, int i2) throws IOException {
            Socket createSocket = this.mBase.createSocket(str, i, inetAddress, i2);
            SSLConfig.limitProtocolsAndCiphers((SSLSocket) createSocket);
            return createSocket;
        }

        public Socket createSocket(InetAddress inetAddress, int i, InetAddress inetAddress2, int i2) throws IOException {
            Socket createSocket = this.mBase.createSocket(inetAddress, i, inetAddress2, i2);
            SSLConfig.limitProtocolsAndCiphers((SSLSocket) createSocket);
            return createSocket;
        }

        public Socket createSocket(Socket socket, String str, int i, boolean z) throws IOException {
            Socket createSocket = this.mBase.createSocket(socket, str, i, z);
            SSLConfig.limitProtocolsAndCiphers((SSLSocket) createSocket);
            return createSocket;
        }
    }

    public static void apply(HttpsURLConnection httpsURLConnection) throws SSLException {
        httpsURLConnection.setSSLSocketFactory(SSL_SOCKET_FACTORY);
    }

    public static X509TrustManager getTrustManager() {
        return TRUST_MANAGER;
    }

    public static SSLSocketFactory getSSLSocketFactory() {
        return SSL_SOCKET_FACTORY;
    }

    /* access modifiers changed from: private */
    public static void limitProtocolsAndCiphers(SSLSocket sSLSocket) throws SSLException {
        String[] supportedProtocols = sSLSocket.getSupportedProtocols();
        int length = supportedProtocols.length;
        int i = 0;
        while (i < length) {
            String str = supportedProtocols[i];
            if (str.equals("TLSv1.2")) {
                sSLSocket.setEnabledProtocols(PROTOCOL_LIST_TLS_V1_2);
            } else if (str.equals("TLSv1.0")) {
                sSLSocket.setEnabledProtocols(PROTOCOL_LIST_TLS_V1_0);
            } else if (str.equals("TLSv1")) {
                sSLSocket.setEnabledProtocols(PROTOCOL_LIST_TLS_V1);
            } else {
                i++;
            }
            sSLSocket.setEnabledCipherSuites(getFilteredCipherSuites(sSLSocket.getSupportedCipherSuites()));
            return;
        }
        throw new SSLException("Socket doesn't support protocols \"TLSv1.2\", \"TLSv1.0\" or \"TLSv1\".");
    }

    private static String[] getFilteredCipherSuites(String[] strArr) {
        CipherSuiteFilterationResults cipherSuiteFilterationResults = CACHED_CIPHER_SUITE_FILTERATION_RESULTS;
        if (cipherSuiteFilterationResults != null && Arrays.equals(cipherSuiteFilterationResults.supported, strArr)) {
            return cipherSuiteFilterationResults.enabled;
        }
        ArrayList arrayList = new ArrayList(ALLOWED_CIPHER_SUITES.size());
        for (String str : strArr) {
            if (ALLOWED_CIPHER_SUITES.contains(str)) {
                arrayList.add(str);
            }
        }
        String[] strArr2 = (String[]) arrayList.toArray(new String[arrayList.size()]);
        CACHED_CIPHER_SUITE_FILTERATION_RESULTS = new CipherSuiteFilterationResults(strArr, strArr2);
        return strArr2;
    }

    private static X509TrustManager createTrustManager() {
        return createTrustManager(loadKeyStore(ROOT_CERTS_RESOURCE));
    }

    private static SSLSocketFactory createSSLSocketFactory() {
        return new SSLSocketFactoryWrapper(createSSLContext(new TrustManager[]{TRUST_MANAGER}).getSocketFactory());
    }

    private static SSLContext createSSLContext(TrustManager[] trustManagerArr) {
        try {
            SSLContext instance = SSLContext.getInstance("TLS");
            try {
                instance.init(null, trustManagerArr, null);
                return instance;
            } catch (KeyManagementException e) {
                throw LangUtil.mkAssert("Couldn't initialize SSLContext", e);
            }
        } catch (NoSuchAlgorithmException e2) {
            throw LangUtil.mkAssert("Couldn't create SSLContext", e2);
        }
    }

    private static X509TrustManager createTrustManager(KeyStore keyStore) {
        try {
            TrustManagerFactory instance = TrustManagerFactory.getInstance("X509");
            try {
                instance.init(keyStore);
                TrustManager[] trustManagers = instance.getTrustManagers();
                if (trustManagers.length != 1) {
                    throw new AssertionError("More than 1 TrustManager created.");
                } else if (trustManagers[0] instanceof X509TrustManager) {
                    return (X509TrustManager) trustManagers[0];
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb.append("TrustManager not of type X509: ");
                    sb.append(trustManagers[0].getClass());
                    throw new AssertionError(sb.toString());
                }
            } catch (KeyStoreException e) {
                throw LangUtil.mkAssert("Unable to initialize TrustManagerFactory with key store", e);
            }
        } catch (NoSuchAlgorithmException e2) {
            throw LangUtil.mkAssert("Unable to create TrustManagerFactory", e2);
        }
    }

    private static KeyStore loadKeyStore(String str) {
        try {
            KeyStore instance = KeyStore.getInstance(KeyStore.getDefaultType());
            instance.load(null, new char[0]);
            InputStream resourceAsStream = SSLConfig.class.getResourceAsStream(str);
            if (resourceAsStream != null) {
                try {
                    loadKeyStore(instance, resourceAsStream);
                    IOUtil.closeInput(resourceAsStream);
                    return instance;
                } catch (KeyStoreException e) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Error loading from \"");
                    sb.append(str);
                    sb.append("\"");
                    throw LangUtil.mkAssert(sb.toString(), e);
                } catch (LoadException e2) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("Error loading from \"");
                    sb2.append(str);
                    sb2.append("\"");
                    throw LangUtil.mkAssert(sb2.toString(), e2);
                } catch (IOException e3) {
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("Error loading from \"");
                    sb3.append(str);
                    sb3.append("\"");
                    throw LangUtil.mkAssert(sb3.toString(), e3);
                } catch (Throwable th) {
                    IOUtil.closeInput(resourceAsStream);
                    throw th;
                }
            } else {
                StringBuilder sb4 = new StringBuilder();
                sb4.append("Couldn't find resource \"");
                sb4.append(str);
                sb4.append("\"");
                throw new AssertionError(sb4.toString());
            }
        } catch (KeyStoreException e4) {
            throw LangUtil.mkAssert("Couldn't initialize KeyStore", e4);
        } catch (CertificateException e5) {
            throw LangUtil.mkAssert("Couldn't initialize KeyStore", e5);
        } catch (NoSuchAlgorithmException e6) {
            throw LangUtil.mkAssert("Couldn't initialize KeyStore", e6);
        } catch (IOException e7) {
            throw LangUtil.mkAssert("Couldn't initialize KeyStore", e7);
        }
    }

    private static void loadKeyStore(KeyStore keyStore, InputStream inputStream) throws IOException, LoadException, KeyStoreException {
        try {
            try {
                for (X509Certificate x509Certificate : deserializeCertificates(CertificateFactory.getInstance("X.509"), inputStream)) {
                    try {
                        keyStore.setCertificateEntry(x509Certificate.getSubjectX500Principal().getName(), x509Certificate);
                    } catch (KeyStoreException e) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("Error loading certificate: ");
                        sb.append(e.getMessage());
                        throw new LoadException(sb.toString(), e);
                    }
                }
            } catch (CertificateException e2) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Error loading certificate: ");
                sb2.append(e2.getMessage());
                throw new LoadException(sb2.toString(), e2);
            }
        } catch (CertificateException e3) {
            throw LangUtil.mkAssert("Couldn't initialize X.509 CertificateFactory", e3);
        }
    }

    private static List<X509Certificate> deserializeCertificates(CertificateFactory certificateFactory, InputStream inputStream) throws IOException, LoadException, CertificateException {
        ArrayList arrayList = new ArrayList();
        DataInputStream dataInputStream = new DataInputStream(inputStream);
        byte[] bArr = new byte[MAX_CERT_LENGTH];
        while (true) {
            int readUnsignedShort = dataInputStream.readUnsignedShort();
            if (readUnsignedShort == 0) {
                if (dataInputStream.read() < 0) {
                    return arrayList;
                }
                throw new LoadException("Found data after after zero-length header.", null);
            } else if (readUnsignedShort <= MAX_CERT_LENGTH) {
                dataInputStream.readFully(bArr, 0, readUnsignedShort);
                arrayList.add((X509Certificate) certificateFactory.generateCertificate(new ByteArrayInputStream(bArr, 0, readUnsignedShort)));
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("Invalid length for certificate entry: ");
                sb.append(readUnsignedShort);
                throw new LoadException(sb.toString(), null);
            }
        }
    }
}
