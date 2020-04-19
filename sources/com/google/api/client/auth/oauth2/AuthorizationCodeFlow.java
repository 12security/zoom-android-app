package com.google.api.client.auth.oauth2;

import com.google.api.client.auth.oauth2.Credential.AccessMethod;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpExecuteInterceptor;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.Beta;
import com.google.api.client.util.Clock;
import com.google.api.client.util.Joiner;
import com.google.api.client.util.Lists;
import com.google.api.client.util.Preconditions;
import com.google.api.client.util.Strings;
import com.google.api.client.util.store.DataStore;
import com.google.api.client.util.store.DataStoreFactory;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import org.apache.http.message.TokenParser;

public class AuthorizationCodeFlow {
    private final String authorizationServerEncodedUrl;
    private final HttpExecuteInterceptor clientAuthentication;
    private final String clientId;
    private final Clock clock;
    private final CredentialCreatedListener credentialCreatedListener;
    @Beta
    private final DataStore<StoredCredential> credentialDataStore;
    @Beta
    @Deprecated
    private final CredentialStore credentialStore;
    private final JsonFactory jsonFactory;
    private final AccessMethod method;
    private final Collection<CredentialRefreshListener> refreshListeners;
    private final HttpRequestInitializer requestInitializer;
    private final Collection<String> scopes;
    private final String tokenServerEncodedUrl;
    private final HttpTransport transport;

    public static class Builder {
        String authorizationServerEncodedUrl;
        HttpExecuteInterceptor clientAuthentication;
        String clientId;
        Clock clock = Clock.SYSTEM;
        CredentialCreatedListener credentialCreatedListener;
        @Beta
        DataStore<StoredCredential> credentialDataStore;
        @Beta
        @Deprecated
        CredentialStore credentialStore;
        JsonFactory jsonFactory;
        AccessMethod method;
        Collection<CredentialRefreshListener> refreshListeners = Lists.newArrayList();
        HttpRequestInitializer requestInitializer;
        Collection<String> scopes = Lists.newArrayList();
        GenericUrl tokenServerUrl;
        HttpTransport transport;

        public Builder(AccessMethod accessMethod, HttpTransport httpTransport, JsonFactory jsonFactory2, GenericUrl genericUrl, HttpExecuteInterceptor httpExecuteInterceptor, String str, String str2) {
            setMethod(accessMethod);
            setTransport(httpTransport);
            setJsonFactory(jsonFactory2);
            setTokenServerUrl(genericUrl);
            setClientAuthentication(httpExecuteInterceptor);
            setClientId(str);
            setAuthorizationServerEncodedUrl(str2);
        }

        public AuthorizationCodeFlow build() {
            return new AuthorizationCodeFlow(this);
        }

        public final AccessMethod getMethod() {
            return this.method;
        }

        public Builder setMethod(AccessMethod accessMethod) {
            this.method = (AccessMethod) Preconditions.checkNotNull(accessMethod);
            return this;
        }

        public final HttpTransport getTransport() {
            return this.transport;
        }

        public Builder setTransport(HttpTransport httpTransport) {
            this.transport = (HttpTransport) Preconditions.checkNotNull(httpTransport);
            return this;
        }

        public final JsonFactory getJsonFactory() {
            return this.jsonFactory;
        }

        public Builder setJsonFactory(JsonFactory jsonFactory2) {
            this.jsonFactory = (JsonFactory) Preconditions.checkNotNull(jsonFactory2);
            return this;
        }

        public final GenericUrl getTokenServerUrl() {
            return this.tokenServerUrl;
        }

        public Builder setTokenServerUrl(GenericUrl genericUrl) {
            this.tokenServerUrl = (GenericUrl) Preconditions.checkNotNull(genericUrl);
            return this;
        }

        public final HttpExecuteInterceptor getClientAuthentication() {
            return this.clientAuthentication;
        }

        public Builder setClientAuthentication(HttpExecuteInterceptor httpExecuteInterceptor) {
            this.clientAuthentication = httpExecuteInterceptor;
            return this;
        }

        public final String getClientId() {
            return this.clientId;
        }

        public Builder setClientId(String str) {
            this.clientId = (String) Preconditions.checkNotNull(str);
            return this;
        }

        public final String getAuthorizationServerEncodedUrl() {
            return this.authorizationServerEncodedUrl;
        }

        public Builder setAuthorizationServerEncodedUrl(String str) {
            this.authorizationServerEncodedUrl = (String) Preconditions.checkNotNull(str);
            return this;
        }

        @Beta
        @Deprecated
        public final CredentialStore getCredentialStore() {
            return this.credentialStore;
        }

        @Beta
        public final DataStore<StoredCredential> getCredentialDataStore() {
            return this.credentialDataStore;
        }

        public final Clock getClock() {
            return this.clock;
        }

        public Builder setClock(Clock clock2) {
            this.clock = (Clock) Preconditions.checkNotNull(clock2);
            return this;
        }

        @Beta
        @Deprecated
        public Builder setCredentialStore(CredentialStore credentialStore2) {
            Preconditions.checkArgument(this.credentialDataStore == null);
            this.credentialStore = credentialStore2;
            return this;
        }

        @Beta
        public Builder setDataStoreFactory(DataStoreFactory dataStoreFactory) throws IOException {
            return setCredentialDataStore(StoredCredential.getDefaultDataStore(dataStoreFactory));
        }

        @Beta
        public Builder setCredentialDataStore(DataStore<StoredCredential> dataStore) {
            Preconditions.checkArgument(this.credentialStore == null);
            this.credentialDataStore = dataStore;
            return this;
        }

        public final HttpRequestInitializer getRequestInitializer() {
            return this.requestInitializer;
        }

        public Builder setRequestInitializer(HttpRequestInitializer httpRequestInitializer) {
            this.requestInitializer = httpRequestInitializer;
            return this;
        }

        public Builder setScopes(Collection<String> collection) {
            this.scopes = (Collection) Preconditions.checkNotNull(collection);
            return this;
        }

        public final Collection<String> getScopes() {
            return this.scopes;
        }

        public Builder setCredentialCreatedListener(CredentialCreatedListener credentialCreatedListener2) {
            this.credentialCreatedListener = credentialCreatedListener2;
            return this;
        }

        public Builder addRefreshListener(CredentialRefreshListener credentialRefreshListener) {
            this.refreshListeners.add(Preconditions.checkNotNull(credentialRefreshListener));
            return this;
        }

        public final Collection<CredentialRefreshListener> getRefreshListeners() {
            return this.refreshListeners;
        }

        public Builder setRefreshListeners(Collection<CredentialRefreshListener> collection) {
            this.refreshListeners = (Collection) Preconditions.checkNotNull(collection);
            return this;
        }

        public final CredentialCreatedListener getCredentialCreatedListener() {
            return this.credentialCreatedListener;
        }
    }

    public interface CredentialCreatedListener {
        void onCredentialCreated(Credential credential, TokenResponse tokenResponse) throws IOException;
    }

    public AuthorizationCodeFlow(AccessMethod accessMethod, HttpTransport httpTransport, JsonFactory jsonFactory2, GenericUrl genericUrl, HttpExecuteInterceptor httpExecuteInterceptor, String str, String str2) {
        Builder builder = new Builder(accessMethod, httpTransport, jsonFactory2, genericUrl, httpExecuteInterceptor, str, str2);
        this(builder);
    }

    protected AuthorizationCodeFlow(Builder builder) {
        this.method = (AccessMethod) Preconditions.checkNotNull(builder.method);
        this.transport = (HttpTransport) Preconditions.checkNotNull(builder.transport);
        this.jsonFactory = (JsonFactory) Preconditions.checkNotNull(builder.jsonFactory);
        this.tokenServerEncodedUrl = ((GenericUrl) Preconditions.checkNotNull(builder.tokenServerUrl)).build();
        this.clientAuthentication = builder.clientAuthentication;
        this.clientId = (String) Preconditions.checkNotNull(builder.clientId);
        this.authorizationServerEncodedUrl = (String) Preconditions.checkNotNull(builder.authorizationServerEncodedUrl);
        this.requestInitializer = builder.requestInitializer;
        this.credentialStore = builder.credentialStore;
        this.credentialDataStore = builder.credentialDataStore;
        this.scopes = Collections.unmodifiableCollection(builder.scopes);
        this.clock = (Clock) Preconditions.checkNotNull(builder.clock);
        this.credentialCreatedListener = builder.credentialCreatedListener;
        this.refreshListeners = Collections.unmodifiableCollection(builder.refreshListeners);
    }

    public AuthorizationCodeRequestUrl newAuthorizationUrl() {
        return new AuthorizationCodeRequestUrl(this.authorizationServerEncodedUrl, this.clientId).setScopes(this.scopes);
    }

    public AuthorizationCodeTokenRequest newTokenRequest(String str) {
        return new AuthorizationCodeTokenRequest(this.transport, this.jsonFactory, new GenericUrl(this.tokenServerEncodedUrl), str).setClientAuthentication(this.clientAuthentication).setRequestInitializer(this.requestInitializer).setScopes(this.scopes);
    }

    public Credential createAndStoreCredential(TokenResponse tokenResponse, String str) throws IOException {
        Credential fromTokenResponse = newCredential(str).setFromTokenResponse(tokenResponse);
        CredentialStore credentialStore2 = this.credentialStore;
        if (credentialStore2 != null) {
            credentialStore2.store(str, fromTokenResponse);
        }
        DataStore<StoredCredential> dataStore = this.credentialDataStore;
        if (dataStore != null) {
            dataStore.set(str, new StoredCredential(fromTokenResponse));
        }
        CredentialCreatedListener credentialCreatedListener2 = this.credentialCreatedListener;
        if (credentialCreatedListener2 != null) {
            credentialCreatedListener2.onCredentialCreated(fromTokenResponse, tokenResponse);
        }
        return fromTokenResponse;
    }

    public Credential loadCredential(String str) throws IOException {
        if (Strings.isNullOrEmpty(str)) {
            return null;
        }
        if (this.credentialDataStore == null && this.credentialStore == null) {
            return null;
        }
        Credential newCredential = newCredential(str);
        DataStore<StoredCredential> dataStore = this.credentialDataStore;
        if (dataStore != null) {
            StoredCredential storedCredential = (StoredCredential) dataStore.get(str);
            if (storedCredential == null) {
                return null;
            }
            newCredential.setAccessToken(storedCredential.getAccessToken());
            newCredential.setRefreshToken(storedCredential.getRefreshToken());
            newCredential.setExpirationTimeMilliseconds(storedCredential.getExpirationTimeMilliseconds());
        } else if (!this.credentialStore.load(str, newCredential)) {
            return null;
        }
        return newCredential;
    }

    private Credential newCredential(String str) {
        com.google.api.client.auth.oauth2.Credential.Builder clock2 = new com.google.api.client.auth.oauth2.Credential.Builder(this.method).setTransport(this.transport).setJsonFactory(this.jsonFactory).setTokenServerEncodedUrl(this.tokenServerEncodedUrl).setClientAuthentication(this.clientAuthentication).setRequestInitializer(this.requestInitializer).setClock(this.clock);
        DataStore<StoredCredential> dataStore = this.credentialDataStore;
        if (dataStore != null) {
            clock2.addRefreshListener(new DataStoreCredentialRefreshListener(str, dataStore));
        } else {
            CredentialStore credentialStore2 = this.credentialStore;
            if (credentialStore2 != null) {
                clock2.addRefreshListener(new CredentialStoreRefreshListener(str, credentialStore2));
            }
        }
        clock2.getRefreshListeners().addAll(this.refreshListeners);
        return clock2.build();
    }

    public final AccessMethod getMethod() {
        return this.method;
    }

    public final HttpTransport getTransport() {
        return this.transport;
    }

    public final JsonFactory getJsonFactory() {
        return this.jsonFactory;
    }

    public final String getTokenServerEncodedUrl() {
        return this.tokenServerEncodedUrl;
    }

    public final HttpExecuteInterceptor getClientAuthentication() {
        return this.clientAuthentication;
    }

    public final String getClientId() {
        return this.clientId;
    }

    public final String getAuthorizationServerEncodedUrl() {
        return this.authorizationServerEncodedUrl;
    }

    @Beta
    @Deprecated
    public final CredentialStore getCredentialStore() {
        return this.credentialStore;
    }

    @Beta
    public final DataStore<StoredCredential> getCredentialDataStore() {
        return this.credentialDataStore;
    }

    public final HttpRequestInitializer getRequestInitializer() {
        return this.requestInitializer;
    }

    public final String getScopesAsString() {
        return Joiner.m70on(TokenParser.f498SP).join(this.scopes);
    }

    public final Collection<String> getScopes() {
        return this.scopes;
    }

    public final Clock getClock() {
        return this.clock;
    }

    public final Collection<CredentialRefreshListener> getRefreshListeners() {
        return this.refreshListeners;
    }
}
