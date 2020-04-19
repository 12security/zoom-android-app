package com.google.api.client.auth.oauth2;

import com.box.androidsdk.content.models.BoxSharedLink;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpExecuteInterceptor;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.Key;
import com.google.api.client.util.Preconditions;
import java.util.Collection;

public class PasswordTokenRequest extends TokenRequest {
    @Key
    private String password;
    @Key
    private String username;

    public PasswordTokenRequest(HttpTransport httpTransport, JsonFactory jsonFactory, GenericUrl genericUrl, String str, String str2) {
        super(httpTransport, jsonFactory, genericUrl, BoxSharedLink.FIELD_PASSWORD);
        setUsername(str);
        setPassword(str2);
    }

    public PasswordTokenRequest setRequestInitializer(HttpRequestInitializer httpRequestInitializer) {
        return (PasswordTokenRequest) super.setRequestInitializer(httpRequestInitializer);
    }

    public PasswordTokenRequest setTokenServerUrl(GenericUrl genericUrl) {
        return (PasswordTokenRequest) super.setTokenServerUrl(genericUrl);
    }

    public PasswordTokenRequest setScopes(Collection<String> collection) {
        return (PasswordTokenRequest) super.setScopes(collection);
    }

    public PasswordTokenRequest setGrantType(String str) {
        return (PasswordTokenRequest) super.setGrantType(str);
    }

    public PasswordTokenRequest setClientAuthentication(HttpExecuteInterceptor httpExecuteInterceptor) {
        return (PasswordTokenRequest) super.setClientAuthentication(httpExecuteInterceptor);
    }

    public PasswordTokenRequest setResponseClass(Class<? extends TokenResponse> cls) {
        return (PasswordTokenRequest) super.setResponseClass(cls);
    }

    public final String getUsername() {
        return this.username;
    }

    public PasswordTokenRequest setUsername(String str) {
        this.username = (String) Preconditions.checkNotNull(str);
        return this;
    }

    public final String getPassword() {
        return this.password;
    }

    public PasswordTokenRequest setPassword(String str) {
        this.password = (String) Preconditions.checkNotNull(str);
        return this;
    }

    public PasswordTokenRequest set(String str, Object obj) {
        return (PasswordTokenRequest) super.set(str, obj);
    }
}
