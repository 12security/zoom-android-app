package com.google.api.client.auth.oauth2;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import com.google.api.client.util.Preconditions;

public class TokenResponse extends GenericJson {
    @Key("access_token")
    private String accessToken;
    @Key("expires_in")
    private Long expiresInSeconds;
    @Key("refresh_token")
    private String refreshToken;
    @Key
    private String scope;
    @Key("token_type")
    private String tokenType;

    public String getAccessToken() {
        return this.accessToken;
    }

    public TokenResponse setAccessToken(String str) {
        this.accessToken = (String) Preconditions.checkNotNull(str);
        return this;
    }

    public String getTokenType() {
        return this.tokenType;
    }

    public TokenResponse setTokenType(String str) {
        this.tokenType = (String) Preconditions.checkNotNull(str);
        return this;
    }

    public Long getExpiresInSeconds() {
        return this.expiresInSeconds;
    }

    public TokenResponse setExpiresInSeconds(Long l) {
        this.expiresInSeconds = l;
        return this;
    }

    public String getRefreshToken() {
        return this.refreshToken;
    }

    public TokenResponse setRefreshToken(String str) {
        this.refreshToken = str;
        return this;
    }

    public String getScope() {
        return this.scope;
    }

    public TokenResponse setScope(String str) {
        this.scope = str;
        return this;
    }

    public TokenResponse set(String str, Object obj) {
        return (TokenResponse) super.set(str, obj);
    }

    public TokenResponse clone() {
        return (TokenResponse) super.clone();
    }
}
