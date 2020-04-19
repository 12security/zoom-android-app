package com.google.api.client.auth.oauth2;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpExecuteInterceptor;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.Key;
import com.google.api.client.util.Preconditions;
import java.util.Collection;

public class RefreshTokenRequest extends TokenRequest {
    @Key("refresh_token")
    private String refreshToken;

    public RefreshTokenRequest(HttpTransport httpTransport, JsonFactory jsonFactory, GenericUrl genericUrl, String str) {
        super(httpTransport, jsonFactory, genericUrl, "refresh_token");
        setRefreshToken(str);
    }

    public RefreshTokenRequest setRequestInitializer(HttpRequestInitializer httpRequestInitializer) {
        return (RefreshTokenRequest) super.setRequestInitializer(httpRequestInitializer);
    }

    public RefreshTokenRequest setTokenServerUrl(GenericUrl genericUrl) {
        return (RefreshTokenRequest) super.setTokenServerUrl(genericUrl);
    }

    public RefreshTokenRequest setScopes(Collection<String> collection) {
        return (RefreshTokenRequest) super.setScopes(collection);
    }

    public RefreshTokenRequest setGrantType(String str) {
        return (RefreshTokenRequest) super.setGrantType(str);
    }

    public RefreshTokenRequest setClientAuthentication(HttpExecuteInterceptor httpExecuteInterceptor) {
        return (RefreshTokenRequest) super.setClientAuthentication(httpExecuteInterceptor);
    }

    public RefreshTokenRequest setResponseClass(Class<? extends TokenResponse> cls) {
        return (RefreshTokenRequest) super.setResponseClass(cls);
    }

    public final String getRefreshToken() {
        return this.refreshToken;
    }

    public RefreshTokenRequest setRefreshToken(String str) {
        this.refreshToken = (String) Preconditions.checkNotNull(str);
        return this;
    }

    public RefreshTokenRequest set(String str, Object obj) {
        return (RefreshTokenRequest) super.set(str, obj);
    }
}
