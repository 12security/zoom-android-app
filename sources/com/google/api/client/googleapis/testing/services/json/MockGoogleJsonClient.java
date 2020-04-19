package com.google.api.client.googleapis.testing.services.json;

import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.google.api.client.googleapis.services.json.AbstractGoogleJsonClient;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.Beta;

@Beta
public class MockGoogleJsonClient extends AbstractGoogleJsonClient {

    @Beta
    public static class Builder extends com.google.api.client.googleapis.services.json.AbstractGoogleJsonClient.Builder {
        public Builder(HttpTransport httpTransport, JsonFactory jsonFactory, String str, String str2, HttpRequestInitializer httpRequestInitializer, boolean z) {
            super(httpTransport, jsonFactory, str, str2, httpRequestInitializer, z);
        }

        public MockGoogleJsonClient build() {
            return new MockGoogleJsonClient(this);
        }

        public Builder setRootUrl(String str) {
            return (Builder) super.setRootUrl(str);
        }

        public Builder setServicePath(String str) {
            return (Builder) super.setServicePath(str);
        }

        public Builder setGoogleClientRequestInitializer(GoogleClientRequestInitializer googleClientRequestInitializer) {
            return (Builder) super.setGoogleClientRequestInitializer(googleClientRequestInitializer);
        }

        public Builder setHttpRequestInitializer(HttpRequestInitializer httpRequestInitializer) {
            return (Builder) super.setHttpRequestInitializer(httpRequestInitializer);
        }

        public Builder setApplicationName(String str) {
            return (Builder) super.setApplicationName(str);
        }

        public Builder setSuppressPatternChecks(boolean z) {
            return (Builder) super.setSuppressPatternChecks(z);
        }

        public Builder setSuppressRequiredParameterChecks(boolean z) {
            return (Builder) super.setSuppressRequiredParameterChecks(z);
        }

        public Builder setSuppressAllChecks(boolean z) {
            return (Builder) super.setSuppressAllChecks(z);
        }
    }

    protected MockGoogleJsonClient(Builder builder) {
        super(builder);
    }

    public MockGoogleJsonClient(HttpTransport httpTransport, JsonFactory jsonFactory, String str, String str2, HttpRequestInitializer httpRequestInitializer, boolean z) {
        Builder builder = new Builder(httpTransport, jsonFactory, str, str2, httpRequestInitializer, z);
        this(builder);
    }
}
