package com.google.api.client.googleapis.services;

import com.google.api.client.googleapis.batch.BatchRequest;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.util.ObjectParser;
import com.google.api.client.util.Preconditions;
import com.google.api.client.util.Strings;
import java.io.IOException;
import java.util.logging.Logger;

public abstract class AbstractGoogleClient {
    private static final Logger logger = Logger.getLogger(AbstractGoogleClient.class.getName());
    private final String applicationName;
    private final String batchPath;
    private final GoogleClientRequestInitializer googleClientRequestInitializer;
    private final ObjectParser objectParser;
    private final HttpRequestFactory requestFactory;
    private final String rootUrl;
    private final String servicePath;
    private final boolean suppressPatternChecks;
    private final boolean suppressRequiredParameterChecks;

    public static abstract class Builder {
        String applicationName;
        String batchPath;
        GoogleClientRequestInitializer googleClientRequestInitializer;
        HttpRequestInitializer httpRequestInitializer;
        final ObjectParser objectParser;
        String rootUrl;
        String servicePath;
        boolean suppressPatternChecks;
        boolean suppressRequiredParameterChecks;
        final HttpTransport transport;

        public abstract AbstractGoogleClient build();

        protected Builder(HttpTransport httpTransport, String str, String str2, ObjectParser objectParser2, HttpRequestInitializer httpRequestInitializer2) {
            this.transport = (HttpTransport) Preconditions.checkNotNull(httpTransport);
            this.objectParser = objectParser2;
            setRootUrl(str);
            setServicePath(str2);
            this.httpRequestInitializer = httpRequestInitializer2;
        }

        public final HttpTransport getTransport() {
            return this.transport;
        }

        public ObjectParser getObjectParser() {
            return this.objectParser;
        }

        public final String getRootUrl() {
            return this.rootUrl;
        }

        public Builder setRootUrl(String str) {
            this.rootUrl = AbstractGoogleClient.normalizeRootUrl(str);
            return this;
        }

        public final String getServicePath() {
            return this.servicePath;
        }

        public Builder setServicePath(String str) {
            this.servicePath = AbstractGoogleClient.normalizeServicePath(str);
            return this;
        }

        public Builder setBatchPath(String str) {
            this.batchPath = str;
            return this;
        }

        public final GoogleClientRequestInitializer getGoogleClientRequestInitializer() {
            return this.googleClientRequestInitializer;
        }

        public Builder setGoogleClientRequestInitializer(GoogleClientRequestInitializer googleClientRequestInitializer2) {
            this.googleClientRequestInitializer = googleClientRequestInitializer2;
            return this;
        }

        public final HttpRequestInitializer getHttpRequestInitializer() {
            return this.httpRequestInitializer;
        }

        public Builder setHttpRequestInitializer(HttpRequestInitializer httpRequestInitializer2) {
            this.httpRequestInitializer = httpRequestInitializer2;
            return this;
        }

        public final String getApplicationName() {
            return this.applicationName;
        }

        public Builder setApplicationName(String str) {
            this.applicationName = str;
            return this;
        }

        public final boolean getSuppressPatternChecks() {
            return this.suppressPatternChecks;
        }

        public Builder setSuppressPatternChecks(boolean z) {
            this.suppressPatternChecks = z;
            return this;
        }

        public final boolean getSuppressRequiredParameterChecks() {
            return this.suppressRequiredParameterChecks;
        }

        public Builder setSuppressRequiredParameterChecks(boolean z) {
            this.suppressRequiredParameterChecks = z;
            return this;
        }

        public Builder setSuppressAllChecks(boolean z) {
            return setSuppressPatternChecks(true).setSuppressRequiredParameterChecks(true);
        }
    }

    protected AbstractGoogleClient(Builder builder) {
        HttpRequestFactory httpRequestFactory;
        this.googleClientRequestInitializer = builder.googleClientRequestInitializer;
        this.rootUrl = normalizeRootUrl(builder.rootUrl);
        this.servicePath = normalizeServicePath(builder.servicePath);
        this.batchPath = builder.batchPath;
        if (Strings.isNullOrEmpty(builder.applicationName)) {
            logger.warning("Application name is not set. Call Builder#setApplicationName.");
        }
        this.applicationName = builder.applicationName;
        if (builder.httpRequestInitializer == null) {
            httpRequestFactory = builder.transport.createRequestFactory();
        } else {
            httpRequestFactory = builder.transport.createRequestFactory(builder.httpRequestInitializer);
        }
        this.requestFactory = httpRequestFactory;
        this.objectParser = builder.objectParser;
        this.suppressPatternChecks = builder.suppressPatternChecks;
        this.suppressRequiredParameterChecks = builder.suppressRequiredParameterChecks;
    }

    public final String getRootUrl() {
        return this.rootUrl;
    }

    public final String getServicePath() {
        return this.servicePath;
    }

    public final String getBaseUrl() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.rootUrl);
        sb.append(this.servicePath);
        return sb.toString();
    }

    public final String getApplicationName() {
        return this.applicationName;
    }

    public final HttpRequestFactory getRequestFactory() {
        return this.requestFactory;
    }

    public final GoogleClientRequestInitializer getGoogleClientRequestInitializer() {
        return this.googleClientRequestInitializer;
    }

    public ObjectParser getObjectParser() {
        return this.objectParser;
    }

    /* access modifiers changed from: protected */
    public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
        if (getGoogleClientRequestInitializer() != null) {
            getGoogleClientRequestInitializer().initialize(abstractGoogleClientRequest);
        }
    }

    public final BatchRequest batch() {
        return batch(null);
    }

    public final BatchRequest batch(HttpRequestInitializer httpRequestInitializer) {
        BatchRequest batchRequest = new BatchRequest(getRequestFactory().getTransport(), httpRequestInitializer);
        if (Strings.isNullOrEmpty(this.batchPath)) {
            StringBuilder sb = new StringBuilder();
            sb.append(getRootUrl());
            sb.append("batch");
            batchRequest.setBatchUrl(new GenericUrl(sb.toString()));
        } else {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(getRootUrl());
            sb2.append(this.batchPath);
            batchRequest.setBatchUrl(new GenericUrl(sb2.toString()));
        }
        return batchRequest;
    }

    public final boolean getSuppressPatternChecks() {
        return this.suppressPatternChecks;
    }

    public final boolean getSuppressRequiredParameterChecks() {
        return this.suppressRequiredParameterChecks;
    }

    static String normalizeRootUrl(String str) {
        Preconditions.checkNotNull(str, "root URL cannot be null.");
        if (str.endsWith("/")) {
            return str;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append("/");
        return sb.toString();
    }

    static String normalizeServicePath(String str) {
        Preconditions.checkNotNull(str, "service path cannot be null");
        if (str.length() == 1) {
            Preconditions.checkArgument("/".equals(str), "service path must equal \"/\" if it is of length 1.");
            return "";
        } else if (str.length() <= 0) {
            return str;
        } else {
            if (!str.endsWith("/")) {
                StringBuilder sb = new StringBuilder();
                sb.append(str);
                sb.append("/");
                str = sb.toString();
            }
            return str.startsWith("/") ? str.substring(1) : str;
        }
    }
}
