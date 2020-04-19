package com.google.api.client.googleapis.services;

import com.google.api.client.googleapis.GoogleUtils;
import com.google.api.client.googleapis.MethodOverride;
import com.google.api.client.googleapis.batch.BatchCallback;
import com.google.api.client.googleapis.batch.BatchRequest;
import com.google.api.client.googleapis.media.MediaHttpDownloader;
import com.google.api.client.googleapis.media.MediaHttpUploader;
import com.google.api.client.http.AbstractInputStreamContent;
import com.google.api.client.http.EmptyContent;
import com.google.api.client.http.GZipEncoding;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.http.HttpResponseInterceptor;
import com.google.api.client.http.UriTemplate;
import com.google.api.client.util.GenericData;
import com.google.api.client.util.Preconditions;
import com.google.common.base.StandardSystemProperty;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractGoogleClientRequest<T> extends GenericData {
    private static final String API_VERSION_HEADER = "X-Goog-Api-Client";
    public static final String USER_AGENT_SUFFIX = "Google-API-Java-Client";
    private final AbstractGoogleClient abstractGoogleClient;
    private boolean disableGZipContent;
    private MediaHttpDownloader downloader;
    private final HttpContent httpContent;
    private HttpHeaders lastResponseHeaders;
    private int lastStatusCode = -1;
    private String lastStatusMessage;
    private HttpHeaders requestHeaders = new HttpHeaders();
    private final String requestMethod;
    private Class<T> responseClass;
    private MediaHttpUploader uploader;
    private final String uriTemplate;

    static class ApiClientVersion {
        private static final ApiClientVersion DEFAULT_VERSION = new ApiClientVersion();
        private final String headerTemplate;

        ApiClientVersion() {
            this(getJavaVersion(), StandardSystemProperty.OS_NAME.value(), StandardSystemProperty.OS_VERSION.value(), GoogleUtils.VERSION);
        }

        ApiClientVersion(String str, String str2, String str3, String str4) {
            StringBuilder sb = new StringBuilder("java/");
            sb.append(formatSemver(str));
            sb.append(" http-google-%s/");
            sb.append(formatSemver(str4));
            if (!(str2 == null || str3 == null)) {
                sb.append(OAuth.SCOPE_DELIMITER);
                sb.append(formatName(str2));
                sb.append("/");
                sb.append(formatSemver(str3));
            }
            this.headerTemplate = sb.toString();
        }

        /* access modifiers changed from: 0000 */
        public String build(String str) {
            return String.format(this.headerTemplate, new Object[]{formatName(str)});
        }

        /* access modifiers changed from: private */
        public static ApiClientVersion getDefault() {
            return DEFAULT_VERSION;
        }

        private static String getJavaVersion() {
            String property = System.getProperty("java.version");
            if (property.startsWith("9")) {
                return "9.0.0";
            }
            return formatSemver(property);
        }

        private static String formatName(String str) {
            return str.toLowerCase().replaceAll("[^\\w\\d\\-]", "-");
        }

        private static String formatSemver(String str) {
            if (str == null) {
                return null;
            }
            Matcher matcher = Pattern.compile("(\\d+\\.\\d+\\.\\d+).*").matcher(str);
            return matcher.find() ? matcher.group(1) : str;
        }
    }

    protected AbstractGoogleClientRequest(AbstractGoogleClient abstractGoogleClient2, String str, String str2, HttpContent httpContent2, Class<T> cls) {
        this.responseClass = (Class) Preconditions.checkNotNull(cls);
        this.abstractGoogleClient = (AbstractGoogleClient) Preconditions.checkNotNull(abstractGoogleClient2);
        this.requestMethod = (String) Preconditions.checkNotNull(str);
        this.uriTemplate = (String) Preconditions.checkNotNull(str2);
        this.httpContent = httpContent2;
        String applicationName = abstractGoogleClient2.getApplicationName();
        if (applicationName != null) {
            HttpHeaders httpHeaders = this.requestHeaders;
            StringBuilder sb = new StringBuilder();
            sb.append(applicationName);
            sb.append(OAuth.SCOPE_DELIMITER);
            sb.append(USER_AGENT_SUFFIX);
            httpHeaders.setUserAgent(sb.toString());
        } else {
            this.requestHeaders.setUserAgent(USER_AGENT_SUFFIX);
        }
        this.requestHeaders.set(API_VERSION_HEADER, (Object) ApiClientVersion.getDefault().build(abstractGoogleClient2.getClass().getSimpleName()));
    }

    public final boolean getDisableGZipContent() {
        return this.disableGZipContent;
    }

    public AbstractGoogleClientRequest<T> setDisableGZipContent(boolean z) {
        this.disableGZipContent = z;
        return this;
    }

    public final String getRequestMethod() {
        return this.requestMethod;
    }

    public final String getUriTemplate() {
        return this.uriTemplate;
    }

    public final HttpContent getHttpContent() {
        return this.httpContent;
    }

    public AbstractGoogleClient getAbstractGoogleClient() {
        return this.abstractGoogleClient;
    }

    public final HttpHeaders getRequestHeaders() {
        return this.requestHeaders;
    }

    public AbstractGoogleClientRequest<T> setRequestHeaders(HttpHeaders httpHeaders) {
        this.requestHeaders = httpHeaders;
        return this;
    }

    public final HttpHeaders getLastResponseHeaders() {
        return this.lastResponseHeaders;
    }

    public final int getLastStatusCode() {
        return this.lastStatusCode;
    }

    public final String getLastStatusMessage() {
        return this.lastStatusMessage;
    }

    public final Class<T> getResponseClass() {
        return this.responseClass;
    }

    public final MediaHttpUploader getMediaHttpUploader() {
        return this.uploader;
    }

    /* access modifiers changed from: protected */
    public final void initializeMediaUpload(AbstractInputStreamContent abstractInputStreamContent) {
        HttpRequestFactory requestFactory = this.abstractGoogleClient.getRequestFactory();
        this.uploader = new MediaHttpUploader(abstractInputStreamContent, requestFactory.getTransport(), requestFactory.getInitializer());
        this.uploader.setInitiationRequestMethod(this.requestMethod);
        HttpContent httpContent2 = this.httpContent;
        if (httpContent2 != null) {
            this.uploader.setMetadata(httpContent2);
        }
    }

    public final MediaHttpDownloader getMediaHttpDownloader() {
        return this.downloader;
    }

    /* access modifiers changed from: protected */
    public final void initializeMediaDownload() {
        HttpRequestFactory requestFactory = this.abstractGoogleClient.getRequestFactory();
        this.downloader = new MediaHttpDownloader(requestFactory.getTransport(), requestFactory.getInitializer());
    }

    public GenericUrl buildHttpRequestUrl() {
        return new GenericUrl(UriTemplate.expand(this.abstractGoogleClient.getBaseUrl(), this.uriTemplate, this, true));
    }

    public HttpRequest buildHttpRequest() throws IOException {
        return buildHttpRequest(false);
    }

    /* access modifiers changed from: protected */
    public HttpRequest buildHttpRequestUsingHead() throws IOException {
        return buildHttpRequest(true);
    }

    private HttpRequest buildHttpRequest(boolean z) throws IOException {
        String str;
        boolean z2 = true;
        Preconditions.checkArgument(this.uploader == null);
        if (z && !this.requestMethod.equals("GET")) {
            z2 = false;
        }
        Preconditions.checkArgument(z2);
        if (z) {
            str = "HEAD";
        } else {
            str = this.requestMethod;
        }
        final HttpRequest buildRequest = getAbstractGoogleClient().getRequestFactory().buildRequest(str, buildHttpRequestUrl(), this.httpContent);
        new MethodOverride().intercept(buildRequest);
        buildRequest.setParser(getAbstractGoogleClient().getObjectParser());
        if (this.httpContent == null && (this.requestMethod.equals("POST") || this.requestMethod.equals("PUT") || this.requestMethod.equals("PATCH"))) {
            buildRequest.setContent(new EmptyContent());
        }
        buildRequest.getHeaders().putAll(this.requestHeaders);
        if (!this.disableGZipContent) {
            buildRequest.setEncoding(new GZipEncoding());
        }
        final HttpResponseInterceptor responseInterceptor = buildRequest.getResponseInterceptor();
        buildRequest.setResponseInterceptor(new HttpResponseInterceptor() {
            public void interceptResponse(HttpResponse httpResponse) throws IOException {
                HttpResponseInterceptor httpResponseInterceptor = responseInterceptor;
                if (httpResponseInterceptor != null) {
                    httpResponseInterceptor.interceptResponse(httpResponse);
                }
                if (!httpResponse.isSuccessStatusCode() && buildRequest.getThrowExceptionOnExecuteError()) {
                    throw AbstractGoogleClientRequest.this.newExceptionOnError(httpResponse);
                }
            }
        });
        return buildRequest;
    }

    public HttpResponse executeUnparsed() throws IOException {
        return executeUnparsed(false);
    }

    /* access modifiers changed from: protected */
    public HttpResponse executeMedia() throws IOException {
        set("alt", (Object) "media");
        return executeUnparsed();
    }

    /* access modifiers changed from: protected */
    public HttpResponse executeUsingHead() throws IOException {
        Preconditions.checkArgument(this.uploader == null);
        HttpResponse executeUnparsed = executeUnparsed(true);
        executeUnparsed.ignore();
        return executeUnparsed;
    }

    private HttpResponse executeUnparsed(boolean z) throws IOException {
        HttpResponse httpResponse;
        if (this.uploader == null) {
            httpResponse = buildHttpRequest(z).execute();
        } else {
            GenericUrl buildHttpRequestUrl = buildHttpRequestUrl();
            boolean throwExceptionOnExecuteError = getAbstractGoogleClient().getRequestFactory().buildRequest(this.requestMethod, buildHttpRequestUrl, this.httpContent).getThrowExceptionOnExecuteError();
            httpResponse = this.uploader.setInitiationHeaders(this.requestHeaders).setDisableGZipContent(this.disableGZipContent).upload(buildHttpRequestUrl);
            httpResponse.getRequest().setParser(getAbstractGoogleClient().getObjectParser());
            if (throwExceptionOnExecuteError && !httpResponse.isSuccessStatusCode()) {
                throw newExceptionOnError(httpResponse);
            }
        }
        this.lastResponseHeaders = httpResponse.getHeaders();
        this.lastStatusCode = httpResponse.getStatusCode();
        this.lastStatusMessage = httpResponse.getStatusMessage();
        return httpResponse;
    }

    /* access modifiers changed from: protected */
    public IOException newExceptionOnError(HttpResponse httpResponse) {
        return new HttpResponseException(httpResponse);
    }

    public T execute() throws IOException {
        return executeUnparsed().parseAs(this.responseClass);
    }

    public InputStream executeAsInputStream() throws IOException {
        return executeUnparsed().getContent();
    }

    /* access modifiers changed from: protected */
    public InputStream executeMediaAsInputStream() throws IOException {
        return executeMedia().getContent();
    }

    public void executeAndDownloadTo(OutputStream outputStream) throws IOException {
        executeUnparsed().download(outputStream);
    }

    /* access modifiers changed from: protected */
    public void executeMediaAndDownloadTo(OutputStream outputStream) throws IOException {
        MediaHttpDownloader mediaHttpDownloader = this.downloader;
        if (mediaHttpDownloader == null) {
            executeMedia().download(outputStream);
        } else {
            mediaHttpDownloader.download(buildHttpRequestUrl(), this.requestHeaders, outputStream);
        }
    }

    public final <E> void queue(BatchRequest batchRequest, Class<E> cls, BatchCallback<T, E> batchCallback) throws IOException {
        Preconditions.checkArgument(this.uploader == null, "Batching media requests is not supported");
        batchRequest.queue(buildHttpRequest(), getResponseClass(), cls, batchCallback);
    }

    public AbstractGoogleClientRequest<T> set(String str, Object obj) {
        return (AbstractGoogleClientRequest) super.set(str, obj);
    }

    /* access modifiers changed from: protected */
    public final void checkRequiredParameter(Object obj, String str) {
        Preconditions.checkArgument(this.abstractGoogleClient.getSuppressRequiredParameterChecks() || obj != null, "Required parameter %s must be specified", str);
    }
}
