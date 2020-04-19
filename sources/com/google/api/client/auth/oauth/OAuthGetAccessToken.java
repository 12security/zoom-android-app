package com.google.api.client.auth.oauth;

import com.google.api.client.util.Beta;

@Beta
public class OAuthGetAccessToken extends AbstractOAuthGetToken {
    public String temporaryToken;
    public String verifier;

    public OAuthGetAccessToken(String str) {
        super(str);
    }

    public OAuthParameters createParameters() {
        OAuthParameters createParameters = super.createParameters();
        createParameters.token = this.temporaryToken;
        createParameters.verifier = this.verifier;
        return createParameters;
    }
}
