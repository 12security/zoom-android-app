package com.google.api.client.auth.oauth;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpExecuteInterceptor;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.util.Beta;
import com.google.api.client.util.escape.PercentEscaper;
import com.google.common.collect.Multiset;
import com.google.common.collect.TreeMultiset;
import com.microsoft.aad.adal.AuthenticationConstants.AAD;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Collection;
import java.util.Map.Entry;
import org.apache.http.HttpHost;
import org.apache.http.message.TokenParser;

@Beta
public final class OAuthParameters implements HttpExecuteInterceptor, HttpRequestInitializer {
    private static final PercentEscaper ESCAPER = new PercentEscaper("-_.~", false);
    private static final SecureRandom RANDOM = new SecureRandom();
    public String callback;
    public String consumerKey;
    public String nonce;
    public String realm;
    public String signature;
    public String signatureMethod;
    public OAuthSigner signer;
    public String timestamp;
    public String token;
    public String verifier;
    public String version;

    private static class Parameter implements Comparable<Parameter> {
        private final String key;
        private final String value;

        public Parameter(String str, String str2) {
            this.key = str;
            this.value = str2;
        }

        public String getKey() {
            return this.key;
        }

        public String getValue() {
            return this.value;
        }

        public int compareTo(Parameter parameter) {
            int compareTo = this.key.compareTo(parameter.key);
            return compareTo == 0 ? this.value.compareTo(parameter.value) : compareTo;
        }
    }

    public void computeNonce() {
        this.nonce = Long.toHexString(Math.abs(RANDOM.nextLong()));
    }

    public void computeTimestamp() {
        this.timestamp = Long.toString(System.currentTimeMillis() / 1000);
    }

    public void computeSignature(String str, GenericUrl genericUrl) throws GeneralSecurityException {
        OAuthSigner oAuthSigner = this.signer;
        String signatureMethod2 = oAuthSigner.getSignatureMethod();
        this.signatureMethod = signatureMethod2;
        TreeMultiset create = TreeMultiset.create();
        putParameterIfValueNotNull(create, "oauth_callback", this.callback);
        putParameterIfValueNotNull(create, "oauth_consumer_key", this.consumerKey);
        putParameterIfValueNotNull(create, "oauth_nonce", this.nonce);
        putParameterIfValueNotNull(create, "oauth_signature_method", signatureMethod2);
        putParameterIfValueNotNull(create, "oauth_timestamp", this.timestamp);
        putParameterIfValueNotNull(create, "oauth_token", this.token);
        putParameterIfValueNotNull(create, "oauth_verifier", this.verifier);
        putParameterIfValueNotNull(create, "oauth_version", this.version);
        for (Entry entry : genericUrl.entrySet()) {
            Object value = entry.getValue();
            if (value != null) {
                String str2 = (String) entry.getKey();
                if (value instanceof Collection) {
                    for (Object putParameter : (Collection) value) {
                        putParameter(create, str2, putParameter);
                    }
                } else {
                    putParameter(create, str2, value);
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        boolean z = true;
        for (Parameter parameter : create.elementSet()) {
            if (z) {
                z = false;
            } else {
                sb.append('&');
            }
            sb.append(parameter.getKey());
            String value2 = parameter.getValue();
            if (value2 != null) {
                sb.append('=');
                sb.append(value2);
            }
        }
        String sb2 = sb.toString();
        GenericUrl genericUrl2 = new GenericUrl();
        String scheme = genericUrl.getScheme();
        genericUrl2.setScheme(scheme);
        genericUrl2.setHost(genericUrl.getHost());
        genericUrl2.setPathParts(genericUrl.getPathParts());
        int port = genericUrl.getPort();
        if ((HttpHost.DEFAULT_SCHEME_NAME.equals(scheme) && port == 80) || ("https".equals(scheme) && port == 443)) {
            port = -1;
        }
        genericUrl2.setPort(port);
        String build = genericUrl2.build();
        StringBuilder sb3 = new StringBuilder();
        sb3.append(escape(str));
        sb3.append('&');
        sb3.append(escape(build));
        sb3.append('&');
        sb3.append(escape(sb2));
        this.signature = oAuthSigner.computeSignature(sb3.toString());
    }

    public String getAuthorizationHeader() {
        StringBuilder sb = new StringBuilder("OAuth");
        appendParameter(sb, AAD.REALM, this.realm);
        appendParameter(sb, "oauth_callback", this.callback);
        appendParameter(sb, "oauth_consumer_key", this.consumerKey);
        appendParameter(sb, "oauth_nonce", this.nonce);
        appendParameter(sb, "oauth_signature", this.signature);
        appendParameter(sb, "oauth_signature_method", this.signatureMethod);
        appendParameter(sb, "oauth_timestamp", this.timestamp);
        appendParameter(sb, "oauth_token", this.token);
        appendParameter(sb, "oauth_verifier", this.verifier);
        appendParameter(sb, "oauth_version", this.version);
        return sb.substring(0, sb.length() - 1);
    }

    private void appendParameter(StringBuilder sb, String str, String str2) {
        if (str2 != null) {
            sb.append(TokenParser.f498SP);
            sb.append(escape(str));
            sb.append("=\"");
            sb.append(escape(str2));
            sb.append("\",");
        }
    }

    private void putParameterIfValueNotNull(Multiset<Parameter> multiset, String str, String str2) {
        if (str2 != null) {
            putParameter(multiset, str, str2);
        }
    }

    private void putParameter(Multiset<Parameter> multiset, String str, Object obj) {
        multiset.add(new Parameter(escape(str), obj == null ? null : escape(obj.toString())));
    }

    public static String escape(String str) {
        return ESCAPER.escape(str);
    }

    public void initialize(HttpRequest httpRequest) throws IOException {
        httpRequest.setInterceptor(this);
    }

    public void intercept(HttpRequest httpRequest) throws IOException {
        computeNonce();
        computeTimestamp();
        try {
            computeSignature(httpRequest.getRequestMethod(), httpRequest.getUrl());
            httpRequest.getHeaders().setAuthorization(getAuthorizationHeader());
        } catch (GeneralSecurityException e) {
            IOException iOException = new IOException();
            iOException.initCause(e);
            throw iOException;
        }
    }
}
