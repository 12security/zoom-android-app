package com.google.api.client.googleapis.auth.oauth2;

import com.google.api.client.auth.openidconnect.IdToken;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.webtoken.JsonWebSignature;
import com.google.api.client.json.webtoken.JsonWebSignature.Header;
import com.google.api.client.util.Beta;
import com.google.api.client.util.Key;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@Beta
public class GoogleIdToken extends IdToken {

    @Beta
    public static class Payload extends com.google.api.client.auth.openidconnect.IdToken.Payload {
        @Key("email")
        private String email;
        @Key("email_verified")
        private Object emailVerified;
        @Key("hd")
        private String hostedDomain;

        @Deprecated
        public String getUserId() {
            return getSubject();
        }

        @Deprecated
        public Payload setUserId(String str) {
            return setSubject(str);
        }

        @Deprecated
        public String getIssuee() {
            return getAuthorizedParty();
        }

        @Deprecated
        public Payload setIssuee(String str) {
            return setAuthorizedParty(str);
        }

        public String getHostedDomain() {
            return this.hostedDomain;
        }

        public Payload setHostedDomain(String str) {
            this.hostedDomain = str;
            return this;
        }

        public String getEmail() {
            return this.email;
        }

        public Payload setEmail(String str) {
            this.email = str;
            return this;
        }

        public Boolean getEmailVerified() {
            Object obj = this.emailVerified;
            if (obj == null) {
                return null;
            }
            if (obj instanceof Boolean) {
                return (Boolean) obj;
            }
            return Boolean.valueOf((String) obj);
        }

        public Payload setEmailVerified(Boolean bool) {
            this.emailVerified = bool;
            return this;
        }

        public Payload setAuthorizationTimeSeconds(Long l) {
            return (Payload) super.setAuthorizationTimeSeconds(l);
        }

        public Payload setAuthorizedParty(String str) {
            return (Payload) super.setAuthorizedParty(str);
        }

        public Payload setNonce(String str) {
            return (Payload) super.setNonce(str);
        }

        public Payload setAccessTokenHash(String str) {
            return (Payload) super.setAccessTokenHash(str);
        }

        public Payload setClassReference(String str) {
            return (Payload) super.setClassReference(str);
        }

        public Payload setMethodsReferences(List<String> list) {
            return (Payload) super.setMethodsReferences(list);
        }

        public Payload setExpirationTimeSeconds(Long l) {
            return (Payload) super.setExpirationTimeSeconds(l);
        }

        public Payload setNotBeforeTimeSeconds(Long l) {
            return (Payload) super.setNotBeforeTimeSeconds(l);
        }

        public Payload setIssuedAtTimeSeconds(Long l) {
            return (Payload) super.setIssuedAtTimeSeconds(l);
        }

        public Payload setIssuer(String str) {
            return (Payload) super.setIssuer(str);
        }

        public Payload setAudience(Object obj) {
            return (Payload) super.setAudience(obj);
        }

        public Payload setJwtId(String str) {
            return (Payload) super.setJwtId(str);
        }

        public Payload setType(String str) {
            return (Payload) super.setType(str);
        }

        public Payload setSubject(String str) {
            return (Payload) super.setSubject(str);
        }

        public Payload set(String str, Object obj) {
            return (Payload) super.set(str, obj);
        }

        public Payload clone() {
            return (Payload) super.clone();
        }
    }

    public static GoogleIdToken parse(JsonFactory jsonFactory, String str) throws IOException {
        JsonWebSignature parse = JsonWebSignature.parser(jsonFactory).setPayloadClass(Payload.class).parse(str);
        return new GoogleIdToken(parse.getHeader(), (Payload) parse.getPayload(), parse.getSignatureBytes(), parse.getSignedContentBytes());
    }

    public GoogleIdToken(Header header, Payload payload, byte[] bArr, byte[] bArr2) {
        super(header, payload, bArr, bArr2);
    }

    public boolean verify(GoogleIdTokenVerifier googleIdTokenVerifier) throws GeneralSecurityException, IOException {
        return googleIdTokenVerifier.verify(this);
    }

    public Payload getPayload() {
        return (Payload) super.getPayload();
    }
}
