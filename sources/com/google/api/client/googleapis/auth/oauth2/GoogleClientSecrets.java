package com.google.api.client.googleapis.auth.oauth2;

import com.google.api.client.json.GenericJson;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.Key;
import com.google.api.client.util.Preconditions;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

public final class GoogleClientSecrets extends GenericJson {
    @Key
    private Details installed;
    @Key
    private Details web;

    public static final class Details extends GenericJson {
        @Key("auth_uri")
        private String authUri;
        @Key("client_id")
        private String clientId;
        @Key("client_secret")
        private String clientSecret;
        @Key("redirect_uris")
        private List<String> redirectUris;
        @Key("token_uri")
        private String tokenUri;

        public String getClientId() {
            return this.clientId;
        }

        public Details setClientId(String str) {
            this.clientId = str;
            return this;
        }

        public String getClientSecret() {
            return this.clientSecret;
        }

        public Details setClientSecret(String str) {
            this.clientSecret = str;
            return this;
        }

        public List<String> getRedirectUris() {
            return this.redirectUris;
        }

        public Details setRedirectUris(List<String> list) {
            this.redirectUris = list;
            return this;
        }

        public String getAuthUri() {
            return this.authUri;
        }

        public Details setAuthUri(String str) {
            this.authUri = str;
            return this;
        }

        public String getTokenUri() {
            return this.tokenUri;
        }

        public Details setTokenUri(String str) {
            this.tokenUri = str;
            return this;
        }

        public Details set(String str, Object obj) {
            return (Details) super.set(str, obj);
        }

        public Details clone() {
            return (Details) super.clone();
        }
    }

    public Details getInstalled() {
        return this.installed;
    }

    public GoogleClientSecrets setInstalled(Details details) {
        this.installed = details;
        return this;
    }

    public Details getWeb() {
        return this.web;
    }

    public GoogleClientSecrets setWeb(Details details) {
        this.web = details;
        return this;
    }

    public Details getDetails() {
        boolean z = true;
        if ((this.web == null) == (this.installed == null)) {
            z = false;
        }
        Preconditions.checkArgument(z);
        Details details = this.web;
        return details == null ? this.installed : details;
    }

    public GoogleClientSecrets set(String str, Object obj) {
        return (GoogleClientSecrets) super.set(str, obj);
    }

    public GoogleClientSecrets clone() {
        return (GoogleClientSecrets) super.clone();
    }

    public static GoogleClientSecrets load(JsonFactory jsonFactory, Reader reader) throws IOException {
        return (GoogleClientSecrets) jsonFactory.fromReader(reader, GoogleClientSecrets.class);
    }
}
