package com.google.api.client.json.webtoken;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.webtoken.JsonWebToken.Payload;
import com.google.api.client.util.Base64;
import com.google.api.client.util.Beta;
import com.google.api.client.util.Key;
import com.google.api.client.util.Preconditions;
import com.google.api.client.util.SecurityUtils;
import com.google.api.client.util.StringUtils;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class JsonWebSignature extends JsonWebToken {
    private final byte[] signatureBytes;
    private final byte[] signedContentBytes;

    public static class Header extends com.google.api.client.json.webtoken.JsonWebToken.Header {
        @Key("alg")
        private String algorithm;
        @Key("crit")
        private List<String> critical;
        @Key("jwk")
        private String jwk;
        @Key("jku")
        private String jwkUrl;
        @Key("kid")
        private String keyId;
        @Key("x5c")
        private List<String> x509Certificates;
        @Key("x5t")
        private String x509Thumbprint;
        @Key("x5u")
        private String x509Url;

        public Header setType(String str) {
            super.setType(str);
            return this;
        }

        public final String getAlgorithm() {
            return this.algorithm;
        }

        public Header setAlgorithm(String str) {
            this.algorithm = str;
            return this;
        }

        public final String getJwkUrl() {
            return this.jwkUrl;
        }

        public Header setJwkUrl(String str) {
            this.jwkUrl = str;
            return this;
        }

        public final String getJwk() {
            return this.jwk;
        }

        public Header setJwk(String str) {
            this.jwk = str;
            return this;
        }

        public final String getKeyId() {
            return this.keyId;
        }

        public Header setKeyId(String str) {
            this.keyId = str;
            return this;
        }

        public final String getX509Url() {
            return this.x509Url;
        }

        public Header setX509Url(String str) {
            this.x509Url = str;
            return this;
        }

        public final String getX509Thumbprint() {
            return this.x509Thumbprint;
        }

        public Header setX509Thumbprint(String str) {
            this.x509Thumbprint = str;
            return this;
        }

        @Deprecated
        public final String getX509Certificate() {
            List<String> list = this.x509Certificates;
            if (list == null || list.isEmpty()) {
                return null;
            }
            return (String) this.x509Certificates.get(0);
        }

        public final List<String> getX509Certificates() {
            return this.x509Certificates;
        }

        @Deprecated
        public Header setX509Certificate(String str) {
            ArrayList arrayList = new ArrayList();
            arrayList.add(str);
            this.x509Certificates = arrayList;
            return this;
        }

        public Header setX509Certificates(List<String> list) {
            this.x509Certificates = list;
            return this;
        }

        public final List<String> getCritical() {
            return this.critical;
        }

        public Header setCritical(List<String> list) {
            this.critical = list;
            return this;
        }

        public Header set(String str, Object obj) {
            return (Header) super.set(str, obj);
        }

        public Header clone() {
            return (Header) super.clone();
        }
    }

    public static final class Parser {
        private Class<? extends Header> headerClass = Header.class;
        private final JsonFactory jsonFactory;
        private Class<? extends Payload> payloadClass = Payload.class;

        public Parser(JsonFactory jsonFactory2) {
            this.jsonFactory = (JsonFactory) Preconditions.checkNotNull(jsonFactory2);
        }

        public Class<? extends Header> getHeaderClass() {
            return this.headerClass;
        }

        public Parser setHeaderClass(Class<? extends Header> cls) {
            this.headerClass = cls;
            return this;
        }

        public Class<? extends Payload> getPayloadClass() {
            return this.payloadClass;
        }

        public Parser setPayloadClass(Class<? extends Payload> cls) {
            this.payloadClass = cls;
            return this;
        }

        public JsonFactory getJsonFactory() {
            return this.jsonFactory;
        }

        public JsonWebSignature parse(String str) throws IOException {
            int indexOf = str.indexOf(46);
            boolean z = true;
            Preconditions.checkArgument(indexOf != -1);
            byte[] decodeBase64 = Base64.decodeBase64(str.substring(0, indexOf));
            int i = indexOf + 1;
            int indexOf2 = str.indexOf(46, i);
            Preconditions.checkArgument(indexOf2 != -1);
            int i2 = indexOf2 + 1;
            Preconditions.checkArgument(str.indexOf(46, i2) == -1);
            byte[] decodeBase642 = Base64.decodeBase64(str.substring(i, indexOf2));
            byte[] decodeBase643 = Base64.decodeBase64(str.substring(i2));
            byte[] bytesUtf8 = StringUtils.getBytesUtf8(str.substring(0, indexOf2));
            Header header = (Header) this.jsonFactory.fromInputStream(new ByteArrayInputStream(decodeBase64), this.headerClass);
            if (header.getAlgorithm() == null) {
                z = false;
            }
            Preconditions.checkArgument(z);
            return new JsonWebSignature(header, (Payload) this.jsonFactory.fromInputStream(new ByteArrayInputStream(decodeBase642), this.payloadClass), decodeBase643, bytesUtf8);
        }
    }

    public JsonWebSignature(Header header, Payload payload, byte[] bArr, byte[] bArr2) {
        super(header, payload);
        this.signatureBytes = (byte[]) Preconditions.checkNotNull(bArr);
        this.signedContentBytes = (byte[]) Preconditions.checkNotNull(bArr2);
    }

    public Header getHeader() {
        return (Header) super.getHeader();
    }

    public final boolean verifySignature(PublicKey publicKey) throws GeneralSecurityException {
        if ("RS256".equals(getHeader().getAlgorithm())) {
            return SecurityUtils.verify(SecurityUtils.getSha256WithRsaSignatureAlgorithm(), publicKey, this.signatureBytes, this.signedContentBytes);
        }
        return false;
    }

    @Beta
    public final X509Certificate verifySignature(X509TrustManager x509TrustManager) throws GeneralSecurityException {
        List x509Certificates = getHeader().getX509Certificates();
        if (x509Certificates == null || x509Certificates.isEmpty()) {
            return null;
        }
        if ("RS256".equals(getHeader().getAlgorithm())) {
            return SecurityUtils.verify(SecurityUtils.getSha256WithRsaSignatureAlgorithm(), x509TrustManager, x509Certificates, this.signatureBytes, this.signedContentBytes);
        }
        return null;
    }

    @Beta
    public final X509Certificate verifySignature() throws GeneralSecurityException {
        X509TrustManager defaultX509TrustManager = getDefaultX509TrustManager();
        if (defaultX509TrustManager == null) {
            return null;
        }
        return verifySignature(defaultX509TrustManager);
    }

    private static X509TrustManager getDefaultX509TrustManager() {
        TrustManager[] trustManagers;
        try {
            TrustManagerFactory instance = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            instance.init(null);
            for (TrustManager trustManager : instance.getTrustManagers()) {
                if (trustManager instanceof X509TrustManager) {
                    return (X509TrustManager) trustManager;
                }
            }
            return null;
        } catch (NoSuchAlgorithmException unused) {
            return null;
        } catch (KeyStoreException unused2) {
            return null;
        }
    }

    public final byte[] getSignatureBytes() {
        return this.signatureBytes;
    }

    public final byte[] getSignedContentBytes() {
        return this.signedContentBytes;
    }

    public static JsonWebSignature parse(JsonFactory jsonFactory, String str) throws IOException {
        return parser(jsonFactory).parse(str);
    }

    public static Parser parser(JsonFactory jsonFactory) {
        return new Parser(jsonFactory);
    }

    public static String signUsingRsaSha256(PrivateKey privateKey, JsonFactory jsonFactory, Header header, Payload payload) throws GeneralSecurityException, IOException {
        StringBuilder sb = new StringBuilder();
        sb.append(Base64.encodeBase64URLSafeString(jsonFactory.toByteArray(header)));
        sb.append(".");
        sb.append(Base64.encodeBase64URLSafeString(jsonFactory.toByteArray(payload)));
        String sb2 = sb.toString();
        byte[] sign = SecurityUtils.sign(SecurityUtils.getSha256WithRsaSignatureAlgorithm(), privateKey, StringUtils.getBytesUtf8(sb2));
        StringBuilder sb3 = new StringBuilder();
        sb3.append(sb2);
        sb3.append(".");
        sb3.append(Base64.encodeBase64URLSafeString(sign));
        return sb3.toString();
    }
}
