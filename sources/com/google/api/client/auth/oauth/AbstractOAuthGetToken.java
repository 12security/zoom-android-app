package com.google.api.client.auth.oauth;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.UrlEncodedParser;
import com.google.api.client.util.Beta;
import java.io.IOException;

@Beta
public abstract class AbstractOAuthGetToken extends GenericUrl {
    public String consumerKey;
    public OAuthSigner signer;
    public HttpTransport transport;
    protected boolean usePost;

    protected AbstractOAuthGetToken(String str) {
        super(str);
    }

    public final OAuthCredentialsResponse execute() throws IOException {
        HttpRequest buildRequest = this.transport.createRequestFactory().buildRequest(this.usePost ? "POST" : "GET", this, null);
        createParameters().intercept(buildRequest);
        HttpResponse execute = buildRequest.execute();
        execute.setContentLoggingLimit(0);
        OAuthCredentialsResponse oAuthCredentialsResponse = new OAuthCredentialsResponse();
        UrlEncodedParser.parse(execute.parseAsString(), (Object) oAuthCredentialsResponse);
        return oAuthCredentialsResponse;
    }

    public OAuthParameters createParameters() {
        OAuthParameters oAuthParameters = new OAuthParameters();
        oAuthParameters.consumerKey = this.consumerKey;
        oAuthParameters.signer = this.signer;
        return oAuthParameters;
    }
}
