package com.google.api.client.http;

import com.google.api.client.util.Beta;
import com.google.api.client.util.ObjectParser;
import com.google.api.client.util.Preconditions;
import com.google.api.client.util.Sleeper;
import com.zipow.videobox.util.NotificationMgr;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public final class HttpRequest {
    public static final int DEFAULT_NUMBER_OF_RETRIES = 10;
    public static final String USER_AGENT_SUFFIX = "Google-HTTP-Java-Client/1.27.0 (gzip)";
    public static final String VERSION = "1.27.0";
    @Beta
    @Deprecated
    private BackOffPolicy backOffPolicy;
    private int connectTimeout = NotificationMgr.ZOOM_SIP_MISSED_NOTICICATION_ID_START;
    private HttpContent content;
    private int contentLoggingLimit = 16384;
    private boolean curlLoggingEnabled = true;
    private HttpEncoding encoding;
    private HttpExecuteInterceptor executeInterceptor;
    private boolean followRedirects = true;
    private HttpHeaders headers = new HttpHeaders();
    @Beta
    private HttpIOExceptionHandler ioExceptionHandler;
    private boolean loggingEnabled = true;
    private int numRetries = 10;
    private ObjectParser objectParser;
    private int readTimeout = NotificationMgr.ZOOM_SIP_MISSED_NOTICICATION_ID_START;
    private String requestMethod;
    private HttpHeaders responseHeaders = new HttpHeaders();
    private HttpResponseInterceptor responseInterceptor;
    @Beta
    @Deprecated
    private boolean retryOnExecuteIOException = false;
    private Sleeper sleeper = Sleeper.DEFAULT;
    private boolean suppressUserAgentSuffix;
    private boolean throwExceptionOnExecuteError = true;
    private final HttpTransport transport;
    private HttpUnsuccessfulResponseHandler unsuccessfulResponseHandler;
    private GenericUrl url;
    private int writeTimeout = 0;

    HttpRequest(HttpTransport httpTransport, String str) {
        this.transport = httpTransport;
        setRequestMethod(str);
    }

    public HttpTransport getTransport() {
        return this.transport;
    }

    public String getRequestMethod() {
        return this.requestMethod;
    }

    public HttpRequest setRequestMethod(String str) {
        Preconditions.checkArgument(str == null || HttpMediaType.matchesToken(str));
        this.requestMethod = str;
        return this;
    }

    public GenericUrl getUrl() {
        return this.url;
    }

    public HttpRequest setUrl(GenericUrl genericUrl) {
        this.url = (GenericUrl) Preconditions.checkNotNull(genericUrl);
        return this;
    }

    public HttpContent getContent() {
        return this.content;
    }

    public HttpRequest setContent(HttpContent httpContent) {
        this.content = httpContent;
        return this;
    }

    public HttpEncoding getEncoding() {
        return this.encoding;
    }

    public HttpRequest setEncoding(HttpEncoding httpEncoding) {
        this.encoding = httpEncoding;
        return this;
    }

    @Beta
    @Deprecated
    public BackOffPolicy getBackOffPolicy() {
        return this.backOffPolicy;
    }

    @Beta
    @Deprecated
    public HttpRequest setBackOffPolicy(BackOffPolicy backOffPolicy2) {
        this.backOffPolicy = backOffPolicy2;
        return this;
    }

    public int getContentLoggingLimit() {
        return this.contentLoggingLimit;
    }

    public HttpRequest setContentLoggingLimit(int i) {
        Preconditions.checkArgument(i >= 0, "The content logging limit must be non-negative.");
        this.contentLoggingLimit = i;
        return this;
    }

    public boolean isLoggingEnabled() {
        return this.loggingEnabled;
    }

    public HttpRequest setLoggingEnabled(boolean z) {
        this.loggingEnabled = z;
        return this;
    }

    public boolean isCurlLoggingEnabled() {
        return this.curlLoggingEnabled;
    }

    public HttpRequest setCurlLoggingEnabled(boolean z) {
        this.curlLoggingEnabled = z;
        return this;
    }

    public int getConnectTimeout() {
        return this.connectTimeout;
    }

    public HttpRequest setConnectTimeout(int i) {
        Preconditions.checkArgument(i >= 0);
        this.connectTimeout = i;
        return this;
    }

    public int getReadTimeout() {
        return this.readTimeout;
    }

    public HttpRequest setReadTimeout(int i) {
        Preconditions.checkArgument(i >= 0);
        this.readTimeout = i;
        return this;
    }

    public int getWriteTimeout() {
        return this.writeTimeout;
    }

    public HttpRequest setWriteTimeout(int i) {
        Preconditions.checkArgument(i >= 0);
        this.writeTimeout = i;
        return this;
    }

    public HttpHeaders getHeaders() {
        return this.headers;
    }

    public HttpRequest setHeaders(HttpHeaders httpHeaders) {
        this.headers = (HttpHeaders) Preconditions.checkNotNull(httpHeaders);
        return this;
    }

    public HttpHeaders getResponseHeaders() {
        return this.responseHeaders;
    }

    public HttpRequest setResponseHeaders(HttpHeaders httpHeaders) {
        this.responseHeaders = (HttpHeaders) Preconditions.checkNotNull(httpHeaders);
        return this;
    }

    public HttpExecuteInterceptor getInterceptor() {
        return this.executeInterceptor;
    }

    public HttpRequest setInterceptor(HttpExecuteInterceptor httpExecuteInterceptor) {
        this.executeInterceptor = httpExecuteInterceptor;
        return this;
    }

    public HttpUnsuccessfulResponseHandler getUnsuccessfulResponseHandler() {
        return this.unsuccessfulResponseHandler;
    }

    public HttpRequest setUnsuccessfulResponseHandler(HttpUnsuccessfulResponseHandler httpUnsuccessfulResponseHandler) {
        this.unsuccessfulResponseHandler = httpUnsuccessfulResponseHandler;
        return this;
    }

    @Beta
    public HttpIOExceptionHandler getIOExceptionHandler() {
        return this.ioExceptionHandler;
    }

    @Beta
    public HttpRequest setIOExceptionHandler(HttpIOExceptionHandler httpIOExceptionHandler) {
        this.ioExceptionHandler = httpIOExceptionHandler;
        return this;
    }

    public HttpResponseInterceptor getResponseInterceptor() {
        return this.responseInterceptor;
    }

    public HttpRequest setResponseInterceptor(HttpResponseInterceptor httpResponseInterceptor) {
        this.responseInterceptor = httpResponseInterceptor;
        return this;
    }

    public int getNumberOfRetries() {
        return this.numRetries;
    }

    public HttpRequest setNumberOfRetries(int i) {
        Preconditions.checkArgument(i >= 0);
        this.numRetries = i;
        return this;
    }

    public HttpRequest setParser(ObjectParser objectParser2) {
        this.objectParser = objectParser2;
        return this;
    }

    public final ObjectParser getParser() {
        return this.objectParser;
    }

    public boolean getFollowRedirects() {
        return this.followRedirects;
    }

    public HttpRequest setFollowRedirects(boolean z) {
        this.followRedirects = z;
        return this;
    }

    public boolean getThrowExceptionOnExecuteError() {
        return this.throwExceptionOnExecuteError;
    }

    public HttpRequest setThrowExceptionOnExecuteError(boolean z) {
        this.throwExceptionOnExecuteError = z;
        return this;
    }

    @Beta
    @Deprecated
    public boolean getRetryOnExecuteIOException() {
        return this.retryOnExecuteIOException;
    }

    @Beta
    @Deprecated
    public HttpRequest setRetryOnExecuteIOException(boolean z) {
        this.retryOnExecuteIOException = z;
        return this;
    }

    public boolean getSuppressUserAgentSuffix() {
        return this.suppressUserAgentSuffix;
    }

    public HttpRequest setSuppressUserAgentSuffix(boolean z) {
        this.suppressUserAgentSuffix = z;
        return this;
    }

    /* JADX WARNING: type inference failed for: r11v3 */
    /* JADX WARNING: type inference failed for: r2v4, types: [com.google.api.client.util.StreamingContent] */
    /* JADX WARNING: type inference failed for: r3v12, types: [com.google.api.client.http.HttpEncodingStreamingContent, com.google.api.client.util.StreamingContent] */
    /* JADX WARNING: type inference failed for: r2v11 */
    /* JADX WARNING: type inference failed for: r2v12, types: [com.google.api.client.util.LoggingStreamingContent] */
    /* JADX WARNING: type inference failed for: r2v13 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:157:0x02b3 A[LOOP:0: B:8:0x0021->B:157:0x02b3, LOOP_END] */
    /* JADX WARNING: Removed duplicated region for block: B:160:0x0292 A[SYNTHETIC] */
    /* JADX WARNING: Unknown variable types count: 3 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.google.api.client.http.HttpResponse execute() throws java.io.IOException {
        /*
            r16 = this;
            r1 = r16
            int r0 = r1.numRetries
            if (r0 < 0) goto L_0x0008
            r0 = 1
            goto L_0x0009
        L_0x0008:
            r0 = 0
        L_0x0009:
            com.google.api.client.util.Preconditions.checkArgument(r0)
            int r0 = r1.numRetries
            com.google.api.client.http.BackOffPolicy r4 = r1.backOffPolicy
            if (r4 == 0) goto L_0x0015
            r4.reset()
        L_0x0015:
            java.lang.String r4 = r1.requestMethod
            com.google.api.client.util.Preconditions.checkNotNull(r4)
            com.google.api.client.http.GenericUrl r4 = r1.url
            com.google.api.client.util.Preconditions.checkNotNull(r4)
            r5 = r0
            r0 = 0
        L_0x0021:
            if (r0 == 0) goto L_0x0026
            r0.ignore()
        L_0x0026:
            com.google.api.client.http.HttpExecuteInterceptor r0 = r1.executeInterceptor
            if (r0 == 0) goto L_0x002d
            r0.intercept(r1)
        L_0x002d:
            com.google.api.client.http.GenericUrl r0 = r1.url
            java.lang.String r0 = r0.build()
            com.google.api.client.http.HttpTransport r6 = r1.transport
            java.lang.String r7 = r1.requestMethod
            com.google.api.client.http.LowLevelHttpRequest r6 = r6.buildRequest(r7, r0)
            java.util.logging.Logger r7 = com.google.api.client.http.HttpTransport.LOGGER
            boolean r8 = r1.loggingEnabled
            if (r8 == 0) goto L_0x004b
            java.util.logging.Level r8 = java.util.logging.Level.CONFIG
            boolean r8 = r7.isLoggable(r8)
            if (r8 == 0) goto L_0x004b
            r8 = 1
            goto L_0x004c
        L_0x004b:
            r8 = 0
        L_0x004c:
            if (r8 == 0) goto L_0x0091
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>()
            java.lang.String r10 = "-------------- REQUEST  --------------"
            r9.append(r10)
            java.lang.String r10 = com.google.api.client.util.StringUtils.LINE_SEPARATOR
            r9.append(r10)
            java.lang.String r10 = r1.requestMethod
            r9.append(r10)
            r10 = 32
            r9.append(r10)
            r9.append(r0)
            java.lang.String r10 = com.google.api.client.util.StringUtils.LINE_SEPARATOR
            r9.append(r10)
            boolean r10 = r1.curlLoggingEnabled
            if (r10 == 0) goto L_0x008f
            java.lang.StringBuilder r10 = new java.lang.StringBuilder
            java.lang.String r11 = "curl -v --compressed"
            r10.<init>(r11)
            java.lang.String r11 = r1.requestMethod
            java.lang.String r12 = "GET"
            boolean r11 = r11.equals(r12)
            if (r11 != 0) goto L_0x0093
            java.lang.String r11 = " -X "
            r10.append(r11)
            java.lang.String r11 = r1.requestMethod
            r10.append(r11)
            goto L_0x0093
        L_0x008f:
            r10 = 0
            goto L_0x0093
        L_0x0091:
            r9 = 0
            r10 = 0
        L_0x0093:
            com.google.api.client.http.HttpHeaders r11 = r1.headers
            java.lang.String r11 = r11.getUserAgent()
            boolean r12 = r1.suppressUserAgentSuffix
            if (r12 != 0) goto L_0x00c2
            if (r11 != 0) goto L_0x00a7
            com.google.api.client.http.HttpHeaders r12 = r1.headers
            java.lang.String r13 = "Google-HTTP-Java-Client/1.27.0 (gzip)"
            r12.setUserAgent(r13)
            goto L_0x00c2
        L_0x00a7:
            com.google.api.client.http.HttpHeaders r12 = r1.headers
            java.lang.StringBuilder r13 = new java.lang.StringBuilder
            r13.<init>()
            r13.append(r11)
            java.lang.String r14 = " "
            r13.append(r14)
            java.lang.String r14 = "Google-HTTP-Java-Client/1.27.0 (gzip)"
            r13.append(r14)
            java.lang.String r13 = r13.toString()
            r12.setUserAgent(r13)
        L_0x00c2:
            com.google.api.client.http.HttpHeaders r12 = r1.headers
            com.google.api.client.http.HttpHeaders.serializeHeaders(r12, r9, r10, r7, r6)
            boolean r12 = r1.suppressUserAgentSuffix
            if (r12 != 0) goto L_0x00d0
            com.google.api.client.http.HttpHeaders r12 = r1.headers
            r12.setUserAgent(r11)
        L_0x00d0:
            com.google.api.client.http.HttpContent r11 = r1.content
            if (r11 == 0) goto L_0x00dd
            boolean r12 = r11.retrySupported()
            if (r12 == 0) goto L_0x00db
            goto L_0x00dd
        L_0x00db:
            r12 = 0
            goto L_0x00de
        L_0x00dd:
            r12 = 1
        L_0x00de:
            if (r11 == 0) goto L_0x01b8
            com.google.api.client.http.HttpContent r15 = r1.content
            java.lang.String r15 = r15.getType()
            if (r8 == 0) goto L_0x00f4
            com.google.api.client.util.LoggingStreamingContent r2 = new com.google.api.client.util.LoggingStreamingContent
            java.util.logging.Logger r3 = com.google.api.client.http.HttpTransport.LOGGER
            java.util.logging.Level r4 = java.util.logging.Level.CONFIG
            int r13 = r1.contentLoggingLimit
            r2.<init>(r11, r3, r4, r13)
            goto L_0x00f5
        L_0x00f4:
            r2 = r11
        L_0x00f5:
            com.google.api.client.http.HttpEncoding r3 = r1.encoding
            if (r3 != 0) goto L_0x0103
            com.google.api.client.http.HttpContent r3 = r1.content
            long r3 = r3.getLength()
            r11 = r2
            r13 = r3
            r4 = 0
            goto L_0x0118
        L_0x0103:
            java.lang.String r4 = r3.getName()
            com.google.api.client.http.HttpEncodingStreamingContent r3 = new com.google.api.client.http.HttpEncodingStreamingContent
            com.google.api.client.http.HttpEncoding r11 = r1.encoding
            r3.<init>(r2, r11)
            if (r12 == 0) goto L_0x0115
            long r13 = com.google.api.client.util.IOUtils.computeLength(r3)
            goto L_0x0117
        L_0x0115:
            r13 = -1
        L_0x0117:
            r11 = r3
        L_0x0118:
            if (r8 == 0) goto L_0x01a5
            if (r15 == 0) goto L_0x0150
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "Content-Type: "
            r2.append(r3)
            r2.append(r15)
            java.lang.String r2 = r2.toString()
            r9.append(r2)
            java.lang.String r3 = com.google.api.client.util.StringUtils.LINE_SEPARATOR
            r9.append(r3)
            if (r10 == 0) goto L_0x0150
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r1 = " -H '"
            r3.append(r1)
            r3.append(r2)
            java.lang.String r1 = "'"
            r3.append(r1)
            java.lang.String r1 = r3.toString()
            r10.append(r1)
        L_0x0150:
            if (r4 == 0) goto L_0x0186
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "Content-Encoding: "
            r1.append(r2)
            r1.append(r4)
            java.lang.String r1 = r1.toString()
            r9.append(r1)
            java.lang.String r2 = com.google.api.client.util.StringUtils.LINE_SEPARATOR
            r9.append(r2)
            if (r10 == 0) goto L_0x0186
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = " -H '"
            r2.append(r3)
            r2.append(r1)
            java.lang.String r1 = "'"
            r2.append(r1)
            java.lang.String r1 = r2.toString()
            r10.append(r1)
        L_0x0186:
            r1 = 0
            int r1 = (r13 > r1 ? 1 : (r13 == r1 ? 0 : -1))
            if (r1 < 0) goto L_0x01a5
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "Content-Length: "
            r1.append(r2)
            r1.append(r13)
            java.lang.String r1 = r1.toString()
            r9.append(r1)
            java.lang.String r1 = com.google.api.client.util.StringUtils.LINE_SEPARATOR
            r9.append(r1)
        L_0x01a5:
            if (r10 == 0) goto L_0x01ac
            java.lang.String r1 = " -d '@-'"
            r10.append(r1)
        L_0x01ac:
            r6.setContentType(r15)
            r6.setContentEncoding(r4)
            r6.setContentLength(r13)
            r6.setStreamingContent(r11)
        L_0x01b8:
            if (r8 == 0) goto L_0x01e6
            java.lang.String r1 = r9.toString()
            r7.config(r1)
            if (r10 == 0) goto L_0x01e6
            java.lang.String r1 = " -- '"
            r10.append(r1)
            java.lang.String r1 = "'"
            java.lang.String r2 = "'\"'\"'"
            java.lang.String r0 = r0.replaceAll(r1, r2)
            r10.append(r0)
            java.lang.String r0 = "'"
            r10.append(r0)
            if (r11 == 0) goto L_0x01df
            java.lang.String r0 = " << $$$"
            r10.append(r0)
        L_0x01df:
            java.lang.String r0 = r10.toString()
            r7.config(r0)
        L_0x01e6:
            if (r12 == 0) goto L_0x01ee
            if (r5 <= 0) goto L_0x01ee
            r1 = r16
            r2 = 1
            goto L_0x01f1
        L_0x01ee:
            r1 = r16
            r2 = 0
        L_0x01f1:
            int r0 = r1.connectTimeout
            int r3 = r1.readTimeout
            r6.setTimeout(r0, r3)
            int r0 = r1.writeTimeout
            r6.setWriteTimeout(r0)
            com.google.api.client.http.LowLevelHttpResponse r3 = r6.execute()     // Catch:{ IOException -> 0x0214 }
            com.google.api.client.http.HttpResponse r0 = new com.google.api.client.http.HttpResponse     // Catch:{ all -> 0x0209 }
            r0.<init>(r1, r3)     // Catch:{ all -> 0x0209 }
            r3 = r0
            r4 = 0
            goto L_0x0230
        L_0x0209:
            r0 = move-exception
            java.io.InputStream r3 = r3.getContent()     // Catch:{ IOException -> 0x0214 }
            if (r3 == 0) goto L_0x0213
            r3.close()     // Catch:{ IOException -> 0x0214 }
        L_0x0213:
            throw r0     // Catch:{ IOException -> 0x0214 }
        L_0x0214:
            r0 = move-exception
            r4 = r0
            boolean r0 = r1.retryOnExecuteIOException
            if (r0 != 0) goto L_0x0226
            com.google.api.client.http.HttpIOExceptionHandler r0 = r1.ioExceptionHandler
            if (r0 == 0) goto L_0x0225
            boolean r0 = r0.handleIOException(r1, r2)
            if (r0 == 0) goto L_0x0225
            goto L_0x0226
        L_0x0225:
            throw r4
        L_0x0226:
            if (r8 == 0) goto L_0x022f
            java.util.logging.Level r0 = java.util.logging.Level.WARNING
            java.lang.String r3 = "exception thrown while executing request"
            r7.log(r0, r3, r4)
        L_0x022f:
            r3 = 0
        L_0x0230:
            if (r3 == 0) goto L_0x0288
            boolean r0 = r3.isSuccessStatusCode()     // Catch:{ all -> 0x0281 }
            if (r0 != 0) goto L_0x0288
            com.google.api.client.http.HttpUnsuccessfulResponseHandler r0 = r1.unsuccessfulResponseHandler     // Catch:{ all -> 0x0281 }
            if (r0 == 0) goto L_0x0243
            com.google.api.client.http.HttpUnsuccessfulResponseHandler r0 = r1.unsuccessfulResponseHandler     // Catch:{ all -> 0x0281 }
            boolean r0 = r0.handleResponse(r1, r3, r2)     // Catch:{ all -> 0x0281 }
            goto L_0x0244
        L_0x0243:
            r0 = 0
        L_0x0244:
            if (r0 != 0) goto L_0x027a
            int r6 = r3.getStatusCode()     // Catch:{ all -> 0x0281 }
            com.google.api.client.http.HttpHeaders r7 = r3.getHeaders()     // Catch:{ all -> 0x0281 }
            boolean r6 = r1.handleRedirect(r6, r7)     // Catch:{ all -> 0x0281 }
            if (r6 == 0) goto L_0x0256
            r0 = 1
            goto L_0x027a
        L_0x0256:
            if (r2 == 0) goto L_0x027a
            com.google.api.client.http.BackOffPolicy r6 = r1.backOffPolicy     // Catch:{ all -> 0x0281 }
            if (r6 == 0) goto L_0x027a
            com.google.api.client.http.BackOffPolicy r6 = r1.backOffPolicy     // Catch:{ all -> 0x0281 }
            int r7 = r3.getStatusCode()     // Catch:{ all -> 0x0281 }
            boolean r6 = r6.isBackOffRequired(r7)     // Catch:{ all -> 0x0281 }
            if (r6 == 0) goto L_0x027a
            com.google.api.client.http.BackOffPolicy r6 = r1.backOffPolicy     // Catch:{ all -> 0x0281 }
            long r6 = r6.getNextBackOffMillis()     // Catch:{ all -> 0x0281 }
            r8 = -1
            int r8 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1))
            if (r8 == 0) goto L_0x027a
            com.google.api.client.util.Sleeper r0 = r1.sleeper     // Catch:{ InterruptedException -> 0x0279 }
            r0.sleep(r6)     // Catch:{ InterruptedException -> 0x0279 }
        L_0x0279:
            r0 = 1
        L_0x027a:
            r0 = r0 & r2
            if (r0 == 0) goto L_0x028e
            r3.ignore()     // Catch:{ all -> 0x0281 }
            goto L_0x028e
        L_0x0281:
            r0 = move-exception
            if (r3 == 0) goto L_0x0287
            r3.disconnect()
        L_0x0287:
            throw r0
        L_0x0288:
            if (r3 != 0) goto L_0x028c
            r0 = 1
            goto L_0x028d
        L_0x028c:
            r0 = 0
        L_0x028d:
            r0 = r0 & r2
        L_0x028e:
            int r5 = r5 + -1
            if (r0 != 0) goto L_0x02b3
            if (r3 == 0) goto L_0x02b2
            com.google.api.client.http.HttpResponseInterceptor r0 = r1.responseInterceptor
            if (r0 == 0) goto L_0x029b
            r0.interceptResponse(r3)
        L_0x029b:
            boolean r0 = r1.throwExceptionOnExecuteError
            if (r0 == 0) goto L_0x02b1
            boolean r0 = r3.isSuccessStatusCode()
            if (r0 == 0) goto L_0x02a6
            goto L_0x02b1
        L_0x02a6:
            com.google.api.client.http.HttpResponseException r0 = new com.google.api.client.http.HttpResponseException     // Catch:{ all -> 0x02ac }
            r0.<init>(r3)     // Catch:{ all -> 0x02ac }
            throw r0     // Catch:{ all -> 0x02ac }
        L_0x02ac:
            r0 = move-exception
            r3.disconnect()
            throw r0
        L_0x02b1:
            return r3
        L_0x02b2:
            throw r4
        L_0x02b3:
            r0 = r3
            goto L_0x0021
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.api.client.http.HttpRequest.execute():com.google.api.client.http.HttpResponse");
    }

    @Beta
    public Future<HttpResponse> executeAsync(Executor executor) {
        FutureTask futureTask = new FutureTask(new Callable<HttpResponse>() {
            public HttpResponse call() throws Exception {
                return HttpRequest.this.execute();
            }
        });
        executor.execute(futureTask);
        return futureTask;
    }

    @Beta
    public Future<HttpResponse> executeAsync() {
        return executeAsync(Executors.newSingleThreadExecutor());
    }

    public boolean handleRedirect(int i, HttpHeaders httpHeaders) {
        String location = httpHeaders.getLocation();
        if (!getFollowRedirects() || !HttpStatusCodes.isRedirect(i) || location == null) {
            return false;
        }
        setUrl(new GenericUrl(this.url.toURL(location)));
        if (i == 303) {
            setRequestMethod("GET");
            setContent(null);
        }
        String str = null;
        this.headers.setAuthorization(str);
        this.headers.setIfMatch(str);
        this.headers.setIfNoneMatch(str);
        this.headers.setIfModifiedSince(str);
        this.headers.setIfUnmodifiedSince(str);
        this.headers.setIfRange(str);
        return true;
    }

    public Sleeper getSleeper() {
        return this.sleeper;
    }

    public HttpRequest setSleeper(Sleeper sleeper2) {
        this.sleeper = (Sleeper) Preconditions.checkNotNull(sleeper2);
        return this;
    }
}
