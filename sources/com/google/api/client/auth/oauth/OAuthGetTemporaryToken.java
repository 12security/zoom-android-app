package com.google.api.client.auth.oauth;

import com.google.api.client.util.Beta;

@Beta
public class OAuthGetTemporaryToken extends AbstractOAuthGetToken {
    public String callback;

    public OAuthGetTemporaryToken(String str) {
        super(str);
    }

    public OAuthParameters createParameters() {
        OAuthParameters createParameters = super.createParameters();
        createParameters.callback = this.callback;
        return createParameters;
    }
}
