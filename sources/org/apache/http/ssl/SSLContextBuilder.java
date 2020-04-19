package org.apache.http.ssl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509ExtendedKeyManager;
import javax.net.ssl.X509TrustManager;
import org.apache.http.util.Args;

public class SSLContextBuilder {
    static final String TLS = "TLS";
    private String keyManagerFactoryAlgorithm = KeyManagerFactory.getDefaultAlgorithm();
    private final Set<KeyManager> keyManagers = new LinkedHashSet();
    private String keyStoreType = KeyStore.getDefaultType();
    private String protocol;
    private Provider provider;
    private SecureRandom secureRandom;
    private String trustManagerFactoryAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
    private final Set<TrustManager> trustManagers = new LinkedHashSet();

    static class KeyManagerDelegate extends X509ExtendedKeyManager {
        private final PrivateKeyStrategy aliasStrategy;
        private final X509ExtendedKeyManager keyManager;

        KeyManagerDelegate(X509ExtendedKeyManager x509ExtendedKeyManager, PrivateKeyStrategy privateKeyStrategy) {
            this.keyManager = x509ExtendedKeyManager;
            this.aliasStrategy = privateKeyStrategy;
        }

        public String[] getClientAliases(String str, Principal[] principalArr) {
            return this.keyManager.getClientAliases(str, principalArr);
        }

        public Map<String, PrivateKeyDetails> getClientAliasMap(String[] strArr, Principal[] principalArr) {
            HashMap hashMap = new HashMap();
            for (String str : strArr) {
                String[] clientAliases = this.keyManager.getClientAliases(str, principalArr);
                if (clientAliases != null) {
                    for (String str2 : clientAliases) {
                        hashMap.put(str2, new PrivateKeyDetails(str, this.keyManager.getCertificateChain(str2)));
                    }
                }
            }
            return hashMap;
        }

        public Map<String, PrivateKeyDetails> getServerAliasMap(String str, Principal[] principalArr) {
            HashMap hashMap = new HashMap();
            String[] serverAliases = this.keyManager.getServerAliases(str, principalArr);
            if (serverAliases != null) {
                for (String str2 : serverAliases) {
                    hashMap.put(str2, new PrivateKeyDetails(str, this.keyManager.getCertificateChain(str2)));
                }
            }
            return hashMap;
        }

        public String chooseClientAlias(String[] strArr, Principal[] principalArr, Socket socket) {
            return this.aliasStrategy.chooseAlias(getClientAliasMap(strArr, principalArr), socket);
        }

        public String[] getServerAliases(String str, Principal[] principalArr) {
            return this.keyManager.getServerAliases(str, principalArr);
        }

        public String chooseServerAlias(String str, Principal[] principalArr, Socket socket) {
            return this.aliasStrategy.chooseAlias(getServerAliasMap(str, principalArr), socket);
        }

        public X509Certificate[] getCertificateChain(String str) {
            return this.keyManager.getCertificateChain(str);
        }

        public PrivateKey getPrivateKey(String str) {
            return this.keyManager.getPrivateKey(str);
        }

        public String chooseEngineClientAlias(String[] strArr, Principal[] principalArr, SSLEngine sSLEngine) {
            return this.aliasStrategy.chooseAlias(getClientAliasMap(strArr, principalArr), null);
        }

        public String chooseEngineServerAlias(String str, Principal[] principalArr, SSLEngine sSLEngine) {
            return this.aliasStrategy.chooseAlias(getServerAliasMap(str, principalArr), null);
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

    public static SSLContextBuilder create() {
        return new SSLContextBuilder();
    }

    @Deprecated
    public SSLContextBuilder useProtocol(String str) {
        this.protocol = str;
        return this;
    }

    public SSLContextBuilder setProtocol(String str) {
        this.protocol = str;
        return this;
    }

    public SSLContextBuilder setSecureRandom(SecureRandom secureRandom2) {
        this.secureRandom = secureRandom2;
        return this;
    }

    public SSLContextBuilder setProvider(Provider provider2) {
        this.provider = provider2;
        return this;
    }

    public SSLContextBuilder setProvider(String str) {
        this.provider = Security.getProvider(str);
        return this;
    }

    public SSLContextBuilder setKeyStoreType(String str) {
        this.keyStoreType = str;
        return this;
    }

    public SSLContextBuilder setKeyManagerFactoryAlgorithm(String str) {
        this.keyManagerFactoryAlgorithm = str;
        return this;
    }

    public SSLContextBuilder setTrustManagerFactoryAlgorithm(String str) {
        this.trustManagerFactoryAlgorithm = str;
        return this;
    }

    public SSLContextBuilder loadTrustMaterial(KeyStore keyStore, TrustStrategy trustStrategy) throws NoSuchAlgorithmException, KeyStoreException {
        String str = this.trustManagerFactoryAlgorithm;
        if (str == null) {
            str = TrustManagerFactory.getDefaultAlgorithm();
        }
        TrustManagerFactory instance = TrustManagerFactory.getInstance(str);
        instance.init(keyStore);
        TrustManager[] trustManagers2 = instance.getTrustManagers();
        if (trustManagers2 != null) {
            if (trustStrategy != null) {
                for (int i = 0; i < trustManagers2.length; i++) {
                    TrustManager trustManager = trustManagers2[i];
                    if (trustManager instanceof X509TrustManager) {
                        trustManagers2[i] = new TrustManagerDelegate((X509TrustManager) trustManager, trustStrategy);
                    }
                }
            }
            for (TrustManager add : trustManagers2) {
                this.trustManagers.add(add);
            }
        }
        return this;
    }

    public SSLContextBuilder loadTrustMaterial(TrustStrategy trustStrategy) throws NoSuchAlgorithmException, KeyStoreException {
        return loadTrustMaterial((KeyStore) null, trustStrategy);
    }

    /* JADX INFO: finally extract failed */
    public SSLContextBuilder loadTrustMaterial(File file, char[] cArr, TrustStrategy trustStrategy) throws NoSuchAlgorithmException, KeyStoreException, CertificateException, IOException {
        Args.notNull(file, "Truststore file");
        KeyStore instance = KeyStore.getInstance(this.keyStoreType);
        FileInputStream fileInputStream = new FileInputStream(file);
        try {
            instance.load(fileInputStream, cArr);
            fileInputStream.close();
            return loadTrustMaterial(instance, trustStrategy);
        } catch (Throwable th) {
            fileInputStream.close();
            throw th;
        }
    }

    public SSLContextBuilder loadTrustMaterial(File file, char[] cArr) throws NoSuchAlgorithmException, KeyStoreException, CertificateException, IOException {
        return loadTrustMaterial(file, cArr, (TrustStrategy) null);
    }

    public SSLContextBuilder loadTrustMaterial(File file) throws NoSuchAlgorithmException, KeyStoreException, CertificateException, IOException {
        return loadTrustMaterial(file, (char[]) null);
    }

    /* JADX INFO: finally extract failed */
    public SSLContextBuilder loadTrustMaterial(URL url, char[] cArr, TrustStrategy trustStrategy) throws NoSuchAlgorithmException, KeyStoreException, CertificateException, IOException {
        Args.notNull(url, "Truststore URL");
        KeyStore instance = KeyStore.getInstance(this.keyStoreType);
        InputStream openStream = url.openStream();
        try {
            instance.load(openStream, cArr);
            openStream.close();
            return loadTrustMaterial(instance, trustStrategy);
        } catch (Throwable th) {
            openStream.close();
            throw th;
        }
    }

    public SSLContextBuilder loadTrustMaterial(URL url, char[] cArr) throws NoSuchAlgorithmException, KeyStoreException, CertificateException, IOException {
        return loadTrustMaterial(url, cArr, (TrustStrategy) null);
    }

    public SSLContextBuilder loadKeyMaterial(KeyStore keyStore, char[] cArr, PrivateKeyStrategy privateKeyStrategy) throws NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException {
        String str = this.keyManagerFactoryAlgorithm;
        if (str == null) {
            str = KeyManagerFactory.getDefaultAlgorithm();
        }
        KeyManagerFactory instance = KeyManagerFactory.getInstance(str);
        instance.init(keyStore, cArr);
        KeyManager[] keyManagers2 = instance.getKeyManagers();
        if (keyManagers2 != null) {
            if (privateKeyStrategy != null) {
                for (int i = 0; i < keyManagers2.length; i++) {
                    KeyManager keyManager = keyManagers2[i];
                    if (keyManager instanceof X509ExtendedKeyManager) {
                        keyManagers2[i] = new KeyManagerDelegate((X509ExtendedKeyManager) keyManager, privateKeyStrategy);
                    }
                }
            }
            for (KeyManager add : keyManagers2) {
                this.keyManagers.add(add);
            }
        }
        return this;
    }

    public SSLContextBuilder loadKeyMaterial(KeyStore keyStore, char[] cArr) throws NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException {
        return loadKeyMaterial(keyStore, cArr, (PrivateKeyStrategy) null);
    }

    /* JADX INFO: finally extract failed */
    public SSLContextBuilder loadKeyMaterial(File file, char[] cArr, char[] cArr2, PrivateKeyStrategy privateKeyStrategy) throws NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException, CertificateException, IOException {
        Args.notNull(file, "Keystore file");
        KeyStore instance = KeyStore.getInstance(this.keyStoreType);
        FileInputStream fileInputStream = new FileInputStream(file);
        try {
            instance.load(fileInputStream, cArr);
            fileInputStream.close();
            return loadKeyMaterial(instance, cArr2, privateKeyStrategy);
        } catch (Throwable th) {
            fileInputStream.close();
            throw th;
        }
    }

    public SSLContextBuilder loadKeyMaterial(File file, char[] cArr, char[] cArr2) throws NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException, CertificateException, IOException {
        return loadKeyMaterial(file, cArr, cArr2, (PrivateKeyStrategy) null);
    }

    /* JADX INFO: finally extract failed */
    public SSLContextBuilder loadKeyMaterial(URL url, char[] cArr, char[] cArr2, PrivateKeyStrategy privateKeyStrategy) throws NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException, CertificateException, IOException {
        Args.notNull(url, "Keystore URL");
        KeyStore instance = KeyStore.getInstance(this.keyStoreType);
        InputStream openStream = url.openStream();
        try {
            instance.load(openStream, cArr);
            openStream.close();
            return loadKeyMaterial(instance, cArr2, privateKeyStrategy);
        } catch (Throwable th) {
            openStream.close();
            throw th;
        }
    }

    public SSLContextBuilder loadKeyMaterial(URL url, char[] cArr, char[] cArr2) throws NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException, CertificateException, IOException {
        return loadKeyMaterial(url, cArr, cArr2, (PrivateKeyStrategy) null);
    }

    /* access modifiers changed from: protected */
    public void initSSLContext(SSLContext sSLContext, Collection<KeyManager> collection, Collection<TrustManager> collection2, SecureRandom secureRandom2) throws KeyManagementException {
        TrustManager[] trustManagerArr = null;
        KeyManager[] keyManagerArr = !collection.isEmpty() ? (KeyManager[]) collection.toArray(new KeyManager[collection.size()]) : null;
        if (!collection2.isEmpty()) {
            trustManagerArr = (TrustManager[]) collection2.toArray(new TrustManager[collection2.size()]);
        }
        sSLContext.init(keyManagerArr, trustManagerArr, secureRandom2);
    }

    public SSLContext build() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sSLContext;
        String str = this.protocol;
        if (str == null) {
            str = "TLS";
        }
        Provider provider2 = this.provider;
        if (provider2 != null) {
            sSLContext = SSLContext.getInstance(str, provider2);
        } else {
            sSLContext = SSLContext.getInstance(str);
        }
        initSSLContext(sSLContext, this.keyManagers, this.trustManagers, this.secureRandom);
        return sSLContext;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[provider=");
        sb.append(this.provider);
        sb.append(", protocol=");
        sb.append(this.protocol);
        sb.append(", keyStoreType=");
        sb.append(this.keyStoreType);
        sb.append(", keyManagerFactoryAlgorithm=");
        sb.append(this.keyManagerFactoryAlgorithm);
        sb.append(", keyManagers=");
        sb.append(this.keyManagers);
        sb.append(", trustManagerFactoryAlgorithm=");
        sb.append(this.trustManagerFactoryAlgorithm);
        sb.append(", trustManagers=");
        sb.append(this.trustManagers);
        sb.append(", secureRandom=");
        sb.append(this.secureRandom);
        sb.append("]");
        return sb.toString();
    }
}
