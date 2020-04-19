package com.google.api.client.googleapis.compute;

import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.CredentialRefreshListener;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.OAuth2Utils;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpExecuteInterceptor;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.util.Beta;
import com.google.api.client.util.Clock;
import com.google.api.client.util.Preconditions;
import java.io.IOException;
import java.util.Collection;

@Beta
public class ComputeCredential extends Credential {
    public static final String TOKEN_SERVER_ENCODED_URL;

    @Beta
    public static class Builder extends com.google.api.client.auth.oauth2.Credential.Builder {
        public Builder(HttpTransport httpTransport, JsonFactory jsonFactory) {
            super(BearerToken.authorizationHeaderAccessMethod());
            setTransport(httpTransport);
            setJsonFactory(jsonFactory);
            setTokenServerEncodedUrl(ComputeCredential.TOKEN_SERVER_ENCODED_URL);
        }

        public ComputeCredential build() {
            return new ComputeCredential(this);
        }

        public Builder setTransport(HttpTransport httpTransport) {
            return (Builder) super.setTransport((HttpTransport) Preconditions.checkNotNull(httpTransport));
        }

        public Builder setClock(Clock clock) {
            return (Builder) super.setClock(clock);
        }

        public Builder setJsonFactory(JsonFactory jsonFactory) {
            return (Builder) super.setJsonFactory((JsonFactory) Preconditions.checkNotNull(jsonFactory));
        }

        public Builder setTokenServerUrl(GenericUrl genericUrl) {
            return (Builder) super.setTokenServerUrl((GenericUrl) Preconditions.checkNotNull(genericUrl));
        }

        public Builder setTokenServerEncodedUrl(String str) {
            return (Builder) super.setTokenServerEncodedUrl((String) Preconditions.checkNotNull(str));
        }

        public Builder setClientAuthentication(HttpExecuteInterceptor httpExecuteInterceptor) {
            Preconditions.checkArgument(httpExecuteInterceptor == null);
            return this;
        }

        public Builder setRequestInitializer(HttpRequestInitializer httpRequestInitializer) {
            return (Builder) super.setRequestInitializer(httpRequestInitializer);
        }

        public Builder addRefreshListener(CredentialRefreshListener credentialRefreshListener) {
            return (Builder) super.addRefreshListener(credentialRefreshListener);
        }

        public Builder setRefreshListeners(Collection<CredentialRefreshListener> collection) {
            return (Builder) super.setRefreshListeners(collection);
        }
    }

    static {
        StringBuilder sb = new StringBuilder();
        sb.append(OAuth2Utils.getMetadataServerUrl());
        sb.append("/computeMetadata/v1/instance/service-accounts/default/token");
        TOKEN_SERVER_ENCODED_URL = sb.toString();
    }

    public ComputeCredential(HttpTransport httpTransport, JsonFactory jsonFactory) {
        this(new Builder(httpTransport, jsonFactory));
    }

    protected ComputeCredential(Builder builder) {
        super((com.google.api.client.auth.oauth2.Credential.Builder) builder);
    }

    /* access modifiers changed from: protected */
    public TokenResponse executeRefreshToken() throws IOException {
        HttpRequest buildGetRequest = getTransport().createRequestFactory().buildGetRequest(new GenericUrl(getTokenServerEncodedUrl()));
        buildGetRequest.setParser(new JsonObjectParser(getJsonFactory()));
        buildGetRequest.getHeaders().set("Metadata-Flavor", (Object) "Google");
        return (TokenResponse) buildGetRequest.execute().parseAs(TokenResponse.class);
    }
}
