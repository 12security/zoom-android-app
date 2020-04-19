package com.google.api.client.auth.oauth2;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpExecuteInterceptor;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.UrlEncodedContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.util.GenericData;
import com.google.api.client.util.Joiner;
import com.google.api.client.util.Key;
import com.google.api.client.util.Preconditions;
import java.io.IOException;
import java.util.Collection;
import org.apache.http.message.TokenParser;

public class TokenRequest extends GenericData {
    HttpExecuteInterceptor clientAuthentication;
    @Key("grant_type")
    private String grantType;
    private final JsonFactory jsonFactory;
    HttpRequestInitializer requestInitializer;
    protected Class<? extends TokenResponse> responseClass;
    @Key("scope")
    private String scopes;
    private GenericUrl tokenServerUrl;
    private final HttpTransport transport;

    public TokenRequest(HttpTransport httpTransport, JsonFactory jsonFactory2, GenericUrl genericUrl, String str) {
        this(httpTransport, jsonFactory2, genericUrl, str, TokenResponse.class);
    }

    public TokenRequest(HttpTransport httpTransport, JsonFactory jsonFactory2, GenericUrl genericUrl, String str, Class<? extends TokenResponse> cls) {
        this.transport = (HttpTransport) Preconditions.checkNotNull(httpTransport);
        this.jsonFactory = (JsonFactory) Preconditions.checkNotNull(jsonFactory2);
        setTokenServerUrl(genericUrl);
        setGrantType(str);
        setResponseClass(cls);
    }

    public final HttpTransport getTransport() {
        return this.transport;
    }

    public final JsonFactory getJsonFactory() {
        return this.jsonFactory;
    }

    public final HttpRequestInitializer getRequestInitializer() {
        return this.requestInitializer;
    }

    public TokenRequest setRequestInitializer(HttpRequestInitializer httpRequestInitializer) {
        this.requestInitializer = httpRequestInitializer;
        return this;
    }

    public final HttpExecuteInterceptor getClientAuthentication() {
        return this.clientAuthentication;
    }

    public TokenRequest setClientAuthentication(HttpExecuteInterceptor httpExecuteInterceptor) {
        this.clientAuthentication = httpExecuteInterceptor;
        return this;
    }

    public final GenericUrl getTokenServerUrl() {
        return this.tokenServerUrl;
    }

    public TokenRequest setTokenServerUrl(GenericUrl genericUrl) {
        this.tokenServerUrl = genericUrl;
        Preconditions.checkArgument(genericUrl.getFragment() == null);
        return this;
    }

    public final String getScopes() {
        return this.scopes;
    }

    public TokenRequest setScopes(Collection<String> collection) {
        this.scopes = collection == null ? null : Joiner.m70on(TokenParser.f498SP).join(collection);
        return this;
    }

    public final String getGrantType() {
        return this.grantType;
    }

    public TokenRequest setGrantType(String str) {
        this.grantType = (String) Preconditions.checkNotNull(str);
        return this;
    }

    public final Class<? extends TokenResponse> getResponseClass() {
        return this.responseClass;
    }

    public TokenRequest setResponseClass(Class<? extends TokenResponse> cls) {
        this.responseClass = cls;
        return this;
    }

    public final HttpResponse executeUnparsed() throws IOException {
        HttpRequest buildPostRequest = this.transport.createRequestFactory(new HttpRequestInitializer() {
            public void initialize(HttpRequest httpRequest) throws IOException {
                if (TokenRequest.this.requestInitializer != null) {
                    TokenRequest.this.requestInitializer.initialize(httpRequest);
                }
                final HttpExecuteInterceptor interceptor = httpRequest.getInterceptor();
                httpRequest.setInterceptor(new HttpExecuteInterceptor() {
                    public void intercept(HttpRequest httpRequest) throws IOException {
                        HttpExecuteInterceptor httpExecuteInterceptor = interceptor;
                        if (httpExecuteInterceptor != null) {
                            httpExecuteInterceptor.intercept(httpRequest);
                        }
                        if (TokenRequest.this.clientAuthentication != null) {
                            TokenRequest.this.clientAuthentication.intercept(httpRequest);
                        }
                    }
                });
            }
        }).buildPostRequest(this.tokenServerUrl, new UrlEncodedContent(this));
        buildPostRequest.setParser(new JsonObjectParser(this.jsonFactory));
        buildPostRequest.setThrowExceptionOnExecuteError(false);
        HttpResponse execute = buildPostRequest.execute();
        if (execute.isSuccessStatusCode()) {
            return execute;
        }
        throw TokenResponseException.from(this.jsonFactory, execute);
    }

    public TokenResponse execute() throws IOException {
        return (TokenResponse) executeUnparsed().parseAs(this.responseClass);
    }

    public TokenRequest set(String str, Object obj) {
        return (TokenRequest) super.set(str, obj);
    }
}
