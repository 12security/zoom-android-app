package com.google.api.client.auth.oauth2;

import com.box.androidsdk.content.BoxConstants;
import com.google.api.client.http.HttpExecuteInterceptor;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.UrlEncodedContent;
import com.google.api.client.util.Data;
import com.google.api.client.util.Preconditions;
import java.io.IOException;
import java.util.Map;

public class ClientParametersAuthentication implements HttpRequestInitializer, HttpExecuteInterceptor {
    private final String clientId;
    private final String clientSecret;

    public ClientParametersAuthentication(String str, String str2) {
        this.clientId = (String) Preconditions.checkNotNull(str);
        this.clientSecret = str2;
    }

    public void initialize(HttpRequest httpRequest) throws IOException {
        httpRequest.setInterceptor(this);
    }

    public void intercept(HttpRequest httpRequest) throws IOException {
        Map mapOf = Data.mapOf(UrlEncodedContent.getContent(httpRequest).getData());
        mapOf.put("client_id", this.clientId);
        String str = this.clientSecret;
        if (str != null) {
            mapOf.put(BoxConstants.KEY_CLIENT_SECRET, str);
        }
    }

    public final String getClientId() {
        return this.clientId;
    }

    public final String getClientSecret() {
        return this.clientSecret;
    }
}
