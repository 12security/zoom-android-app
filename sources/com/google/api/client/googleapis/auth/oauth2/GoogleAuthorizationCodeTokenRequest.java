package com.google.api.client.googleapis.auth.oauth2;

import com.google.api.client.auth.oauth2.AuthorizationCodeTokenRequest;
import com.google.api.client.auth.oauth2.ClientParametersAuthentication;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpExecuteInterceptor;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.Preconditions;
import java.io.IOException;
import java.util.Collection;

public class GoogleAuthorizationCodeTokenRequest extends AuthorizationCodeTokenRequest {
    public GoogleAuthorizationCodeTokenRequest(HttpTransport httpTransport, JsonFactory jsonFactory, String str, String str2, String str3, String str4) {
        this(httpTransport, jsonFactory, GoogleOAuthConstants.TOKEN_SERVER_URL, str, str2, str3, str4);
    }

    public GoogleAuthorizationCodeTokenRequest(HttpTransport httpTransport, JsonFactory jsonFactory, String str, String str2, String str3, String str4, String str5) {
        super(httpTransport, jsonFactory, new GenericUrl(str), str4);
        setClientAuthentication((HttpExecuteInterceptor) new ClientParametersAuthentication(str2, str3));
        setRedirectUri(str5);
    }

    public GoogleAuthorizationCodeTokenRequest setRequestInitializer(HttpRequestInitializer httpRequestInitializer) {
        return (GoogleAuthorizationCodeTokenRequest) super.setRequestInitializer(httpRequestInitializer);
    }

    public GoogleAuthorizationCodeTokenRequest setTokenServerUrl(GenericUrl genericUrl) {
        return (GoogleAuthorizationCodeTokenRequest) super.setTokenServerUrl(genericUrl);
    }

    public GoogleAuthorizationCodeTokenRequest setScopes(Collection<String> collection) {
        return (GoogleAuthorizationCodeTokenRequest) super.setScopes(collection);
    }

    public GoogleAuthorizationCodeTokenRequest setGrantType(String str) {
        return (GoogleAuthorizationCodeTokenRequest) super.setGrantType(str);
    }

    public GoogleAuthorizationCodeTokenRequest setClientAuthentication(HttpExecuteInterceptor httpExecuteInterceptor) {
        Preconditions.checkNotNull(httpExecuteInterceptor);
        return (GoogleAuthorizationCodeTokenRequest) super.setClientAuthentication(httpExecuteInterceptor);
    }

    public GoogleAuthorizationCodeTokenRequest setCode(String str) {
        return (GoogleAuthorizationCodeTokenRequest) super.setCode(str);
    }

    public GoogleAuthorizationCodeTokenRequest setRedirectUri(String str) {
        Preconditions.checkNotNull(str);
        return (GoogleAuthorizationCodeTokenRequest) super.setRedirectUri(str);
    }

    public GoogleTokenResponse execute() throws IOException {
        return (GoogleTokenResponse) executeUnparsed().parseAs(GoogleTokenResponse.class);
    }

    public GoogleAuthorizationCodeTokenRequest set(String str, Object obj) {
        return (GoogleAuthorizationCodeTokenRequest) super.set(str, obj);
    }
}
