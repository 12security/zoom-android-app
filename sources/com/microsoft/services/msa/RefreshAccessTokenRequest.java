package com.microsoft.services.msa;

import android.text.TextUtils;
import com.microsoft.services.msa.OAuth.GrantType;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.message.BasicNameValuePair;

class RefreshAccessTokenRequest extends TokenRequest {
    private final GrantType grantType = GrantType.REFRESH_TOKEN;
    private final String refreshToken;
    private final String scope;

    public RefreshAccessTokenRequest(HttpClient httpClient, String str, String str2, String str3, OAuthConfig oAuthConfig) {
        super(httpClient, str, oAuthConfig);
        if (str2 == null) {
            throw new AssertionError();
        } else if (TextUtils.isEmpty(str2)) {
            throw new AssertionError();
        } else if (str3 == null) {
            throw new AssertionError();
        } else if (!TextUtils.isEmpty(str3)) {
            this.refreshToken = str2;
            this.scope = str3;
        } else {
            throw new AssertionError();
        }
    }

    /* access modifiers changed from: protected */
    public void constructBody(List<NameValuePair> list) {
        list.add(new BasicNameValuePair("refresh_token", this.refreshToken));
        list.add(new BasicNameValuePair("scope", this.scope));
        list.add(new BasicNameValuePair("grant_type", this.grantType.toString()));
    }
}
