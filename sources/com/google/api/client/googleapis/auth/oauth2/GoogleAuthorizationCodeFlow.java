package com.google.api.client.googleapis.auth.oauth2;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.AuthorizationCodeFlow.CredentialCreatedListener;
import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.ClientParametersAuthentication;
import com.google.api.client.auth.oauth2.Credential.AccessMethod;
import com.google.api.client.auth.oauth2.CredentialRefreshListener;
import com.google.api.client.auth.oauth2.CredentialStore;
import com.google.api.client.auth.oauth2.StoredCredential;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpExecuteInterceptor;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.Beta;
import com.google.api.client.util.Clock;
import com.google.api.client.util.Preconditions;
import com.google.api.client.util.store.DataStore;
import com.google.api.client.util.store.DataStoreFactory;
import java.io.IOException;
import java.util.Collection;

public class GoogleAuthorizationCodeFlow extends AuthorizationCodeFlow {
    private final String accessType;
    private final String approvalPrompt;

    public static class Builder extends com.google.api.client.auth.oauth2.AuthorizationCodeFlow.Builder {
        String accessType;
        String approvalPrompt;

        public Builder(HttpTransport httpTransport, JsonFactory jsonFactory, String str, String str2, Collection<String> collection) {
            super(BearerToken.authorizationHeaderAccessMethod(), httpTransport, jsonFactory, new GenericUrl(GoogleOAuthConstants.TOKEN_SERVER_URL), new ClientParametersAuthentication(str, str2), str, GoogleOAuthConstants.AUTHORIZATION_SERVER_URL);
            setScopes((Collection) collection);
        }

        public Builder(HttpTransport httpTransport, JsonFactory jsonFactory, GoogleClientSecrets googleClientSecrets, Collection<String> collection) {
            super(BearerToken.authorizationHeaderAccessMethod(), httpTransport, jsonFactory, new GenericUrl(GoogleOAuthConstants.TOKEN_SERVER_URL), new ClientParametersAuthentication(googleClientSecrets.getDetails().getClientId(), googleClientSecrets.getDetails().getClientSecret()), googleClientSecrets.getDetails().getClientId(), GoogleOAuthConstants.AUTHORIZATION_SERVER_URL);
            setScopes((Collection) collection);
        }

        public GoogleAuthorizationCodeFlow build() {
            return new GoogleAuthorizationCodeFlow(this);
        }

        public Builder setDataStoreFactory(DataStoreFactory dataStoreFactory) throws IOException {
            return (Builder) super.setDataStoreFactory(dataStoreFactory);
        }

        public Builder setCredentialDataStore(DataStore<StoredCredential> dataStore) {
            return (Builder) super.setCredentialDataStore(dataStore);
        }

        public Builder setCredentialCreatedListener(CredentialCreatedListener credentialCreatedListener) {
            return (Builder) super.setCredentialCreatedListener(credentialCreatedListener);
        }

        @Beta
        @Deprecated
        public Builder setCredentialStore(CredentialStore credentialStore) {
            return (Builder) super.setCredentialStore(credentialStore);
        }

        public Builder setRequestInitializer(HttpRequestInitializer httpRequestInitializer) {
            return (Builder) super.setRequestInitializer(httpRequestInitializer);
        }

        public Builder setScopes(Collection<String> collection) {
            Preconditions.checkState(!collection.isEmpty());
            return (Builder) super.setScopes(collection);
        }

        public Builder setMethod(AccessMethod accessMethod) {
            return (Builder) super.setMethod(accessMethod);
        }

        public Builder setTransport(HttpTransport httpTransport) {
            return (Builder) super.setTransport(httpTransport);
        }

        public Builder setJsonFactory(JsonFactory jsonFactory) {
            return (Builder) super.setJsonFactory(jsonFactory);
        }

        public Builder setTokenServerUrl(GenericUrl genericUrl) {
            return (Builder) super.setTokenServerUrl(genericUrl);
        }

        public Builder setClientAuthentication(HttpExecuteInterceptor httpExecuteInterceptor) {
            return (Builder) super.setClientAuthentication(httpExecuteInterceptor);
        }

        public Builder setClientId(String str) {
            return (Builder) super.setClientId(str);
        }

        public Builder setAuthorizationServerEncodedUrl(String str) {
            return (Builder) super.setAuthorizationServerEncodedUrl(str);
        }

        public Builder setClock(Clock clock) {
            return (Builder) super.setClock(clock);
        }

        public Builder addRefreshListener(CredentialRefreshListener credentialRefreshListener) {
            return (Builder) super.addRefreshListener(credentialRefreshListener);
        }

        public Builder setRefreshListeners(Collection<CredentialRefreshListener> collection) {
            return (Builder) super.setRefreshListeners(collection);
        }

        public Builder setApprovalPrompt(String str) {
            this.approvalPrompt = str;
            return this;
        }

        public final String getApprovalPrompt() {
            return this.approvalPrompt;
        }

        public Builder setAccessType(String str) {
            this.accessType = str;
            return this;
        }

        public final String getAccessType() {
            return this.accessType;
        }
    }

    public GoogleAuthorizationCodeFlow(HttpTransport httpTransport, JsonFactory jsonFactory, String str, String str2, Collection<String> collection) {
        Builder builder = new Builder(httpTransport, jsonFactory, str, str2, collection);
        this(builder);
    }

    protected GoogleAuthorizationCodeFlow(Builder builder) {
        super(builder);
        this.accessType = builder.accessType;
        this.approvalPrompt = builder.approvalPrompt;
    }

    public GoogleAuthorizationCodeTokenRequest newTokenRequest(String str) {
        GoogleAuthorizationCodeTokenRequest googleAuthorizationCodeTokenRequest = new GoogleAuthorizationCodeTokenRequest(getTransport(), getJsonFactory(), getTokenServerEncodedUrl(), "", "", str, "");
        return googleAuthorizationCodeTokenRequest.setClientAuthentication(getClientAuthentication()).setRequestInitializer(getRequestInitializer()).setScopes(getScopes());
    }

    public GoogleAuthorizationCodeRequestUrl newAuthorizationUrl() {
        return new GoogleAuthorizationCodeRequestUrl(getAuthorizationServerEncodedUrl(), getClientId(), "", getScopes()).setAccessType(this.accessType).setApprovalPrompt(this.approvalPrompt);
    }

    public final String getApprovalPrompt() {
        return this.approvalPrompt;
    }

    public final String getAccessType() {
        return this.accessType;
    }
}
