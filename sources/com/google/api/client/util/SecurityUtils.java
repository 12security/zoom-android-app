package com.google.api.client.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.List;
import javax.net.ssl.X509TrustManager;

public final class SecurityUtils {
    public static KeyStore getDefaultKeyStore() throws KeyStoreException {
        return KeyStore.getInstance(KeyStore.getDefaultType());
    }

    public static KeyStore getJavaKeyStore() throws KeyStoreException {
        return KeyStore.getInstance("JKS");
    }

    public static KeyStore getPkcs12KeyStore() throws KeyStoreException {
        return KeyStore.getInstance("PKCS12");
    }

    public static void loadKeyStore(KeyStore keyStore, InputStream inputStream, String str) throws IOException, GeneralSecurityException {
        try {
            keyStore.load(inputStream, str.toCharArray());
        } finally {
            inputStream.close();
        }
    }

    public static PrivateKey getPrivateKey(KeyStore keyStore, String str, String str2) throws GeneralSecurityException {
        return (PrivateKey) keyStore.getKey(str, str2.toCharArray());
    }

    public static PrivateKey loadPrivateKeyFromKeyStore(KeyStore keyStore, InputStream inputStream, String str, String str2, String str3) throws IOException, GeneralSecurityException {
        loadKeyStore(keyStore, inputStream, str);
        return getPrivateKey(keyStore, str2, str3);
    }

    public static KeyFactory getRsaKeyFactory() throws NoSuchAlgorithmException {
        return KeyFactory.getInstance("RSA");
    }

    public static Signature getSha1WithRsaSignatureAlgorithm() throws NoSuchAlgorithmException {
        return Signature.getInstance("SHA1withRSA");
    }

    public static Signature getSha256WithRsaSignatureAlgorithm() throws NoSuchAlgorithmException {
        return Signature.getInstance("SHA256withRSA");
    }

    public static byte[] sign(Signature signature, PrivateKey privateKey, byte[] bArr) throws InvalidKeyException, SignatureException {
        signature.initSign(privateKey);
        signature.update(bArr);
        return signature.sign();
    }

    public static boolean verify(Signature signature, PublicKey publicKey, byte[] bArr, byte[] bArr2) throws InvalidKeyException, SignatureException {
        signature.initVerify(publicKey);
        signature.update(bArr2);
        try {
            return signature.verify(bArr);
        } catch (SignatureException unused) {
            return false;
        }
    }

    public static X509Certificate verify(Signature signature, X509TrustManager x509TrustManager, List<String> list, byte[] bArr, byte[] bArr2) throws InvalidKeyException, SignatureException {
        try {
            CertificateFactory x509CertificateFactory = getX509CertificateFactory();
            X509Certificate[] x509CertificateArr = new X509Certificate[list.size()];
            int i = 0;
            for (String decodeBase64 : list) {
                try {
                    Certificate generateCertificate = x509CertificateFactory.generateCertificate(new ByteArrayInputStream(Base64.decodeBase64(decodeBase64)));
                    if (!(generateCertificate instanceof X509Certificate)) {
                        return null;
                    }
                    int i2 = i + 1;
                    x509CertificateArr[i] = (X509Certificate) generateCertificate;
                    i = i2;
                } catch (CertificateException unused) {
                    return null;
                }
            }
            try {
                x509TrustManager.checkServerTrusted(x509CertificateArr, "RSA");
                if (verify(signature, x509CertificateArr[0].getPublicKey(), bArr, bArr2)) {
                    return x509CertificateArr[0];
                }
                return null;
            } catch (CertificateException unused2) {
                return null;
            }
        } catch (CertificateException unused3) {
            return null;
        }
    }

    public static CertificateFactory getX509CertificateFactory() throws CertificateException {
        return CertificateFactory.getInstance("X.509");
    }

    public static void loadKeyStoreFromCertificates(KeyStore keyStore, CertificateFactory certificateFactory, InputStream inputStream) throws GeneralSecurityException {
        int i = 0;
        for (Certificate certificateEntry : certificateFactory.generateCertificates(inputStream)) {
            keyStore.setCertificateEntry(String.valueOf(i), certificateEntry);
            i++;
        }
    }

    private SecurityUtils() {
    }
}
