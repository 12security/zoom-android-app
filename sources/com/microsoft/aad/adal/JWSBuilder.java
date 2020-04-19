package com.microsoft.aad.adal;

import android.util.Base64;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

class JWSBuilder implements IJWSBuilder {
    private static final String JWS_ALGORITHM = "SHA256withRSA";
    private static final String JWS_HEADER_ALG = "RS256";
    private static final long SECONDS_MS = 1000;
    private static final String TAG = "JWSBuilder";

    final class Claims {
        /* access modifiers changed from: private */
        @SerializedName("aud")
        public String mAudience;
        /* access modifiers changed from: private */
        @SerializedName("iat")
        public long mIssueAt;
        /* access modifiers changed from: private */
        @SerializedName("nonce")
        public String mNonce;

        private Claims() {
        }
    }

    final class JwsHeader {
        /* access modifiers changed from: private */
        @SerializedName("alg")
        public String mAlgorithm;
        /* access modifiers changed from: private */
        @SerializedName("x5c")
        public String[] mCert;
        /* access modifiers changed from: private */
        @SerializedName("typ")
        public String mType;

        private JwsHeader() {
        }
    }

    JWSBuilder() {
    }

    public String generateSignedJWT(String str, String str2, RSAPrivateKey rSAPrivateKey, RSAPublicKey rSAPublicKey, X509Certificate x509Certificate) throws AuthenticationException {
        if (StringExtensions.isNullOrBlank(str)) {
            throw new IllegalArgumentException("nonce");
        } else if (StringExtensions.isNullOrBlank(str2)) {
            throw new IllegalArgumentException("audience");
        } else if (rSAPrivateKey == null) {
            throw new IllegalArgumentException("privateKey");
        } else if (rSAPublicKey != null) {
            Gson gson = new Gson();
            Claims claims = new Claims();
            claims.mNonce = str;
            claims.mAudience = str2;
            claims.mIssueAt = System.currentTimeMillis() / 1000;
            JwsHeader jwsHeader = new JwsHeader();
            jwsHeader.mAlgorithm = JWS_HEADER_ALG;
            jwsHeader.mType = "JWT";
            try {
                jwsHeader.mCert = new String[1];
                jwsHeader.mCert[0] = new String(Base64.encode(x509Certificate.getEncoded(), 2), "UTF_8");
                String json = gson.toJson((Object) jwsHeader);
                String json2 = gson.toJson((Object) claims);
                StringBuilder sb = new StringBuilder();
                sb.append("Header: ");
                sb.append(json);
                Logger.m237v("JWSBuilder:generateSignedJWT", "Generate client certificate challenge response JWS Header. ", sb.toString(), null);
                StringBuilder sb2 = new StringBuilder();
                sb2.append(StringExtensions.encodeBase64URLSafeString(json.getBytes("UTF_8")));
                sb2.append(".");
                sb2.append(StringExtensions.encodeBase64URLSafeString(json2.getBytes("UTF_8")));
                String sb3 = sb2.toString();
                String sign = sign(rSAPrivateKey, sb3.getBytes("UTF_8"));
                StringBuilder sb4 = new StringBuilder();
                sb4.append(sb3);
                sb4.append(".");
                sb4.append(sign);
                return sb4.toString();
            } catch (UnsupportedEncodingException e) {
                throw new AuthenticationException(ADALError.ENCODING_IS_NOT_SUPPORTED, "Unsupported encoding", (Throwable) e);
            } catch (CertificateEncodingException e2) {
                throw new AuthenticationException(ADALError.CERTIFICATE_ENCODING_ERROR, "Certificate encoding error", (Throwable) e2);
            }
        } else {
            throw new IllegalArgumentException("pubKey");
        }
    }

    private static String sign(RSAPrivateKey rSAPrivateKey, byte[] bArr) throws AuthenticationException {
        try {
            Signature instance = Signature.getInstance(JWS_ALGORITHM);
            instance.initSign(rSAPrivateKey);
            instance.update(bArr);
            return StringExtensions.encodeBase64URLSafeString(instance.sign());
        } catch (InvalidKeyException e) {
            ADALError aDALError = ADALError.KEY_CHAIN_PRIVATE_KEY_EXCEPTION;
            StringBuilder sb = new StringBuilder();
            sb.append("Invalid private RSA key: ");
            sb.append(e.getMessage());
            throw new AuthenticationException(aDALError, sb.toString(), (Throwable) e);
        } catch (SignatureException e2) {
            ADALError aDALError2 = ADALError.SIGNATURE_EXCEPTION;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("RSA signature exception: ");
            sb2.append(e2.getMessage());
            throw new AuthenticationException(aDALError2, sb2.toString(), (Throwable) e2);
        } catch (UnsupportedEncodingException unused) {
            throw new AuthenticationException(ADALError.ENCODING_IS_NOT_SUPPORTED);
        } catch (NoSuchAlgorithmException e3) {
            ADALError aDALError3 = ADALError.DEVICE_NO_SUCH_ALGORITHM;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("Unsupported RSA algorithm: ");
            sb3.append(e3.getMessage());
            throw new AuthenticationException(aDALError3, sb3.toString(), (Throwable) e3);
        }
    }
}
