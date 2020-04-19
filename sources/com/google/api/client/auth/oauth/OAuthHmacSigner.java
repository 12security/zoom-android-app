package com.google.api.client.auth.oauth;

import com.google.api.client.util.Base64;
import com.google.api.client.util.Beta;
import com.google.api.client.util.StringUtils;
import java.security.GeneralSecurityException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

@Beta
public final class OAuthHmacSigner implements OAuthSigner {
    public String clientSharedSecret;
    public String tokenSharedSecret;

    public String getSignatureMethod() {
        return "HMAC-SHA1";
    }

    public String computeSignature(String str) throws GeneralSecurityException {
        StringBuilder sb = new StringBuilder();
        String str2 = this.clientSharedSecret;
        if (str2 != null) {
            sb.append(OAuthParameters.escape(str2));
        }
        sb.append('&');
        String str3 = this.tokenSharedSecret;
        if (str3 != null) {
            sb.append(OAuthParameters.escape(str3));
        }
        SecretKeySpec secretKeySpec = new SecretKeySpec(StringUtils.getBytesUtf8(sb.toString()), "HmacSHA1");
        Mac instance = Mac.getInstance("HmacSHA1");
        instance.init(secretKeySpec);
        return Base64.encodeBase64String(instance.doFinal(StringUtils.getBytesUtf8(str)));
    }
}
