package com.microsoft.services.msa;

import android.text.TextUtils;
import com.microsoft.services.msa.OAuth.GrantType;
import java.util.List;
import java.util.Locale;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.message.BasicNameValuePair;

class AccessTokenRequest extends TokenRequest {
    private final String code;
    private final GrantType grantType;

    public AccessTokenRequest(HttpClient httpClient, String str, String str2, OAuthConfig oAuthConfig) {
        super(httpClient, str, oAuthConfig);
        if (!TextUtils.isEmpty(str2)) {
            this.code = str2;
            this.grantType = GrantType.AUTHORIZATION_CODE;
            return;
        }
        throw new AssertionError();
    }

    /* access modifiers changed from: protected */
    public void constructBody(List<NameValuePair> list) {
        list.add(new BasicNameValuePair("code", this.code));
        list.add(new BasicNameValuePair("redirect_uri", this.mOAuthConfig.getDesktopUri().toString()));
        list.add(new BasicNameValuePair("grant_type", this.grantType.toString().toLowerCase(Locale.US)));
    }
}
