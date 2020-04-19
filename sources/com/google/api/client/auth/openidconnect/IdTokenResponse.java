package com.google.api.client.auth.openidconnect;

import com.google.api.client.auth.oauth2.TokenRequest;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.util.Beta;
import com.google.api.client.util.Key;
import com.google.api.client.util.Preconditions;
import java.io.IOException;

@Beta
public class IdTokenResponse extends TokenResponse {
    @Key("id_token")
    private String idToken;

    public final String getIdToken() {
        return this.idToken;
    }

    public IdTokenResponse setIdToken(String str) {
        this.idToken = (String) Preconditions.checkNotNull(str);
        return this;
    }

    public IdTokenResponse setAccessToken(String str) {
        super.setAccessToken(str);
        return this;
    }

    public IdTokenResponse setTokenType(String str) {
        super.setTokenType(str);
        return this;
    }

    public IdTokenResponse setExpiresInSeconds(Long l) {
        super.setExpiresInSeconds(l);
        return this;
    }

    public IdTokenResponse setRefreshToken(String str) {
        super.setRefreshToken(str);
        return this;
    }

    public IdTokenResponse setScope(String str) {
        super.setScope(str);
        return this;
    }

    public IdToken parseIdToken() throws IOException {
        return IdToken.parse(getFactory(), this.idToken);
    }

    public static IdTokenResponse execute(TokenRequest tokenRequest) throws IOException {
        return (IdTokenResponse) tokenRequest.executeUnparsed().parseAs(IdTokenResponse.class);
    }

    public IdTokenResponse set(String str, Object obj) {
        return (IdTokenResponse) super.set(str, obj);
    }

    public IdTokenResponse clone() {
        return (IdTokenResponse) super.clone();
    }
}
