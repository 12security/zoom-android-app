package com.google.api.client.auth.oauth2;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpExecuteInterceptor;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.Key;
import com.google.api.client.util.Preconditions;
import com.microsoft.aad.adal.AuthenticationConstants.OAuth2;
import java.util.Collection;

public class AuthorizationCodeTokenRequest extends TokenRequest {
    @Key
    private String code;
    @Key("redirect_uri")
    private String redirectUri;

    public AuthorizationCodeTokenRequest(HttpTransport httpTransport, JsonFactory jsonFactory, GenericUrl genericUrl, String str) {
        super(httpTransport, jsonFactory, genericUrl, OAuth2.AUTHORIZATION_CODE);
        setCode(str);
    }

    public AuthorizationCodeTokenRequest setRequestInitializer(HttpRequestInitializer httpRequestInitializer) {
        return (AuthorizationCodeTokenRequest) super.setRequestInitializer(httpRequestInitializer);
    }

    public AuthorizationCodeTokenRequest setTokenServerUrl(GenericUrl genericUrl) {
        return (AuthorizationCodeTokenRequest) super.setTokenServerUrl(genericUrl);
    }

    public AuthorizationCodeTokenRequest setScopes(Collection<String> collection) {
        return (AuthorizationCodeTokenRequest) super.setScopes(collection);
    }

    public AuthorizationCodeTokenRequest setGrantType(String str) {
        return (AuthorizationCodeTokenRequest) super.setGrantType(str);
    }

    public AuthorizationCodeTokenRequest setClientAuthentication(HttpExecuteInterceptor httpExecuteInterceptor) {
        return (AuthorizationCodeTokenRequest) super.setClientAuthentication(httpExecuteInterceptor);
    }

    public AuthorizationCodeTokenRequest setResponseClass(Class<? extends TokenResponse> cls) {
        return (AuthorizationCodeTokenRequest) super.setResponseClass(cls);
    }

    public final String getCode() {
        return this.code;
    }

    public AuthorizationCodeTokenRequest setCode(String str) {
        this.code = (String) Preconditions.checkNotNull(str);
        return this;
    }

    public final String getRedirectUri() {
        return this.redirectUri;
    }

    public AuthorizationCodeTokenRequest setRedirectUri(String str) {
        this.redirectUri = str;
        return this;
    }

    public AuthorizationCodeTokenRequest set(String str, Object obj) {
        return (AuthorizationCodeTokenRequest) super.set(str, obj);
    }
}
