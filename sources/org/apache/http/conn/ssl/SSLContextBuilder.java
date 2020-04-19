package org.apache.http.conn.ssl;

import java.net.Socket;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509KeyManager;
import javax.net.ssl.X509TrustManager;

@Deprecated
public class SSLContextBuilder {
    static final String SSL = "SSL";
    static final String TLS = "TLS";
    private final Set<KeyManager> keymanagers = new LinkedHashSet();
    private String protocol;
    private SecureRandom secureRandom;
    private final Set<TrustManager> trustmanagers = new LinkedHashSet();

    static class KeyManagerDelegate implements X509KeyManager {
        private final PrivateKeyStrategy aliasStrategy;
        private final X509KeyManager keyManager;

        KeyManagerDelegate(X509KeyManager x509KeyManager, PrivateKeyStrategy privateKeyStrategy) {
            this.keyManager = x509KeyManager;
            this.aliasStrategy = privateKeyStrategy;
        }

        public String[] getClientAliases(String str, Principal[] principalArr) {
            return this.keyManager.getClientAliases(str, principalArr);
        }

        public String chooseClientAlias(String[] strArr, Principal[] principalArr, Socket socket) {
            HashMap hashMap = new HashMap();
            for (String str : strArr) {
                String[] clientAliases = this.keyManager.getClientAliases(str, principalArr);
                if (clientAliases != null) {
                    for (String str2 : clientAliases) {
                        hashMap.put(str2, new PrivateKeyDetails(str, this.keyManager.getCertificateChain(str2)));
                    }
                }
            }
            return this.aliasStrategy.chooseAlias(hashMap, socket);
        }

        public String[] getServerAliases(String str, Principal[] principalArr) {
            return this.keyManager.getServerAliases(str, principalArr);
        }

        public String chooseServerAlias(String str, Principal[] principalArr, Socket socket) {
            HashMap hashMap = new HashMap();
            String[] serverAliases = this.keyManager.getServerAliases(str, principalArr);
            if (serverAliases != null) {
                for (String str2 : serverAliases) {
                    hashMap.put(str2, new PrivateKeyDetails(str, this.keyManager.getCertificateChain(str2)));
                }
            }
            return this.aliasStrategy.chooseAlias(hashMap, socket);
        }

        public X509Certificate[] getCertificateChain(String str) {
            return this.keyManager.getCertificateChain(str);
        }

        public PrivateKey getPrivateKey(String str) {
            return this.keyManager.getPrivateKey(str);
        }
    }

    static class TrustManagerDelegate implements X509TrustManager {
        private final X509TrustManager trustManager;
        private final TrustStrategy trustStrategy;

        TrustManagerDelegate(X509TrustManager x509TrustManager, TrustStrategy trustStrategy2) {
            this.trustManager = x509TrustManager;
            this.trustStrategy = trustStrategy2;
        }

        public void checkClientTrusted(X509Certificate[] x509CertificateArr, String str) throws CertificateException {
            this.trustManager.checkClientTrusted(x509CertificateArr, str);
        }

        public void checkServerTrusted(X509Certificate[] x509CertificateArr, String str) throws CertificateException {
            if (!this.trustStrategy.isTrusted(x509CertificateArr, str)) {
                this.trustManager.checkServerTrusted(x509CertificateArr, str);
            }
        }

        public X509Certificate[] getAcceptedIssuers() {
            return this.trustManager.getAcceptedIssuers();
        }
    }

    public SSLContextBuilder useTLS() {
        this.protocol = "TLS";
        return this;
    }

    public SSLContextBuilder useSSL() {
        this.protocol = "SSL";
        return this;
    }

    public SSLContextBuilder useProtocol(String str) {
        this.protocol = str;
        return this;
    }

    public SSLContextBuilder setSecureRandom(SecureRandom secureRandom2) {
        this.secureRandom = secureRandom2;
        return this;
    }

    public SSLContextBuilder loadTrustMaterial(KeyStore keyStore, TrustStrategy trustStrategy) throws NoSuchAlgorithmException, KeyStoreException {
        TrustManagerFactory instance = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        instance.init(keyStore);
        TrustManager[] trustManagers = instance.getTrustManagers();
        if (trustManagers != null) {
            if (trustStrategy != null) {
                for (int i = 0; i < trustManagers.length; i++) {
                    TrustManager trustManager = trustManagers[i];
                    if (trustManager instanceof X509TrustManager) {
                        trustManagers[i] = new TrustManagerDelegate((X509TrustManager) trustManager, trustStrategy);
                    }
                }
            }
            for (TrustManager add : trustManagers) {
                this.trustmanagers.add(add);
            }
        }
        return this;
    }

    public SSLContextBuilder loadTrustMaterial(KeyStore keyStore) throws NoSuchAlgorithmException, KeyStoreException {
        return loadTrustMaterial(keyStore, null);
    }

    public SSLContextBuilder loadKeyMaterial(KeyStore keyStore, char[] cArr) throws NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException {
        loadKeyMaterial(keyStore, cArr, null);
        return this;
    }

    public SSLContextBuilder loadKeyMaterial(KeyStore keyStore, char[] cArr, PrivateKeyStrategy privateKeyStrategy) throws NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException {
        KeyManagerFactory instance = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        instance.init(keyStore, cArr);
        KeyManager[] keyManagers = instance.getKeyManagers();
        if (keyManagers != null) {
            if (privateKeyStrategy != null) {
                for (int i = 0; i < keyManagers.length; i++) {
                    KeyManager keyManager = keyManagers[i];
                    if (keyManager instanceof X509KeyManager) {
                        keyManagers[i] = new KeyManagerDelegate((X509KeyManager) keyManager, privateKeyStrategy);
                    }
                }
            }
            for (KeyManager add : keyManagers) {
                this.keymanagers.add(add);
            }
        }
        return this;
    }

    public SSLContext build() throws NoSuchAlgorithmException, KeyManagementException {
        KeyManager[] keyManagerArr;
        String str = this.protocol;
        if (str == null) {
            str = "TLS";
        }
        SSLContext instance = SSLContext.getInstance(str);
        TrustManager[] trustManagerArr = null;
        if (!this.keymanagers.isEmpty()) {
            Set<KeyManager> set = this.keymanagers;
            keyManagerArr = (KeyManager[]) set.toArray(new KeyManager[set.size()]);
        } else {
            keyManagerArr = null;
        }
        if (!this.trustmanagers.isEmpty()) {
            Set<TrustManager> set2 = this.trustmanagers;
            trustManagerArr = (TrustManager[]) set2.toArray(new TrustManager[set2.size()]);
        }
        instance.init(keyManagerArr, trustManagerArr, this.secureRandom);
        return instance;
    }
}
