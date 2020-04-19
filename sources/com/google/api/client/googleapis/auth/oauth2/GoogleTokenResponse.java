package com.google.api.client.googleapis.auth.oauth2;

import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.util.Beta;
import com.google.api.client.util.Key;
import com.google.api.client.util.Preconditions;
import java.io.IOException;

public class GoogleTokenResponse extends TokenResponse {
    @Key("id_token")
    private String idToken;

    public GoogleTokenResponse setAccessToken(String str) {
        return (GoogleTokenResponse) super.setAccessToken(str);
    }

    public GoogleTokenResponse setTokenType(String str) {
        return (GoogleTokenResponse) super.setTokenType(str);
    }

    public GoogleTokenResponse setExpiresInSeconds(Long l) {
        return (GoogleTokenResponse) super.setExpiresInSeconds(l);
    }

    public GoogleTokenResponse setRefreshToken(String str) {
        return (GoogleTokenResponse) super.setRefreshToken(str);
    }

    public GoogleTokenResponse setScope(String str) {
        return (GoogleTokenResponse) super.setScope(str);
    }

    @Beta
    public final String getIdToken() {
        return this.idToken;
    }

    @Beta
    public GoogleTokenResponse setIdToken(String str) {
        this.idToken = (String) Preconditions.checkNotNull(str);
        return this;
    }

    @Beta
    public GoogleIdToken parseIdToken() throws IOException {
        return GoogleIdToken.parse(getFactory(), getIdToken());
    }

    public GoogleTokenResponse set(String str, Object obj) {
        return (GoogleTokenResponse) super.set(str, obj);
    }

    public GoogleTokenResponse clone() {
        return (GoogleTokenResponse) super.clone();
    }
}
