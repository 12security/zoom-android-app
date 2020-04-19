package com.dropbox.core.http;

import com.dropbox.core.http.HttpRequestor.Header;
import com.dropbox.core.http.HttpRequestor.Uploader;
import com.dropbox.core.http.OkHttpUtil.PipedStream;
import com.dropbox.core.util.IOUtil.ProgressListener;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request.Builder;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

public class OkHttp3Requestor extends HttpRequestor {
    /* access modifiers changed from: private */
    public final OkHttpClient client;

    public static final class AsyncCallback implements Callback {
        private PipedRequestBody body;
        private IOException error;
        private Response response;

        private AsyncCallback(PipedRequestBody pipedRequestBody) {
            this.body = pipedRequestBody;
            this.error = null;
            this.response = null;
        }

        /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
        /* JADX WARNING: Missing exception handler attribute for start block: B:8:0x000d */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public synchronized okhttp3.Response getResponse() throws java.io.IOException {
            /*
                r1 = this;
                monitor-enter(r1)
            L_0x0001:
                java.io.IOException r0 = r1.error     // Catch:{ all -> 0x0025 }
                if (r0 != 0) goto L_0x001a
                okhttp3.Response r0 = r1.response     // Catch:{ all -> 0x0025 }
                if (r0 != 0) goto L_0x001a
                r1.wait()     // Catch:{ InterruptedException -> 0x000d }
                goto L_0x0001
            L_0x000d:
                java.lang.Thread r0 = java.lang.Thread.currentThread()     // Catch:{ all -> 0x0025 }
                r0.interrupt()     // Catch:{ all -> 0x0025 }
                java.io.InterruptedIOException r0 = new java.io.InterruptedIOException     // Catch:{ all -> 0x0025 }
                r0.<init>()     // Catch:{ all -> 0x0025 }
                throw r0     // Catch:{ all -> 0x0025 }
            L_0x001a:
                java.io.IOException r0 = r1.error     // Catch:{ all -> 0x0025 }
                if (r0 != 0) goto L_0x0022
                okhttp3.Response r0 = r1.response     // Catch:{ all -> 0x0025 }
                monitor-exit(r1)
                return r0
            L_0x0022:
                java.io.IOException r0 = r1.error     // Catch:{ all -> 0x0025 }
                throw r0     // Catch:{ all -> 0x0025 }
            L_0x0025:
                r0 = move-exception
                monitor-exit(r1)
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.http.OkHttp3Requestor.AsyncCallback.getResponse():okhttp3.Response");
        }

        public synchronized void onFailure(Call call, IOException iOException) {
            this.error = iOException;
            this.body.close();
            notifyAll();
        }

        public synchronized void onResponse(Call call, Response response2) throws IOException {
            this.response = response2;
            notifyAll();
        }
    }

    private class BufferedUploader extends Uploader {
        private RequestBody body = null;
        private Call call = null;
        private AsyncCallback callback = null;
        private boolean cancelled = false;
        private boolean closed = false;
        private final String method;
        private final Builder request;

        public BufferedUploader(String str, Builder builder) {
            this.method = str;
            this.request = builder;
        }

        private void assertNoBody() {
            if (this.body != null) {
                throw new IllegalStateException("Request body already set.");
            }
        }

        public OutputStream getBody() {
            RequestBody requestBody = this.body;
            if (requestBody instanceof PipedRequestBody) {
                return ((PipedRequestBody) requestBody).getOutputStream();
            }
            PipedRequestBody pipedRequestBody = new PipedRequestBody();
            if (this.progressListener != null) {
                pipedRequestBody.setListener(this.progressListener);
            }
            setBody(pipedRequestBody);
            this.callback = new AsyncCallback(pipedRequestBody);
            this.call = OkHttp3Requestor.this.client.newCall(this.request.build());
            this.call.enqueue(this.callback);
            return pipedRequestBody.getOutputStream();
        }

        private void setBody(RequestBody requestBody) {
            assertNoBody();
            this.body = requestBody;
            this.request.method(this.method, requestBody);
            OkHttp3Requestor.this.configureRequest(this.request);
        }

        public void upload(File file) {
            setBody(RequestBody.create((MediaType) null, file));
        }

        public void upload(byte[] bArr) {
            setBody(RequestBody.create((MediaType) null, bArr));
        }

        public void close() {
            RequestBody requestBody = this.body;
            if (requestBody != null && (requestBody instanceof Closeable)) {
                try {
                    ((Closeable) requestBody).close();
                } catch (IOException unused) {
                }
            }
            this.closed = true;
        }

        public void abort() {
            Call call2 = this.call;
            if (call2 != null) {
                call2.cancel();
            }
            this.cancelled = true;
            close();
        }

        public HttpRequestor.Response finish() throws IOException {
            Response response;
            if (!this.cancelled) {
                if (this.body == null) {
                    upload(new byte[0]);
                }
                if (this.callback != null) {
                    try {
                        getBody().close();
                    } catch (IOException unused) {
                    }
                    response = this.callback.getResponse();
                } else {
                    this.call = OkHttp3Requestor.this.client.newCall(this.request.build());
                    response = this.call.execute();
                }
                Response interceptResponse = OkHttp3Requestor.this.interceptResponse(response);
                return new HttpRequestor.Response(interceptResponse.code(), interceptResponse.body().byteStream(), OkHttp3Requestor.fromOkHttpHeaders(interceptResponse.headers()));
            }
            throw new IllegalStateException("Already aborted");
        }
    }

    private static class PipedRequestBody extends RequestBody implements Closeable {
        /* access modifiers changed from: private */
        public ProgressListener listener;
        private final PipedStream stream = new PipedStream();

        private final class CountingSink extends ForwardingSink {
            private long bytesWritten = 0;

            public CountingSink(Sink sink) {
                super(sink);
            }

            public void write(Buffer buffer, long j) throws IOException {
                super.write(buffer, j);
                this.bytesWritten += j;
                if (PipedRequestBody.this.listener != null) {
                    PipedRequestBody.this.listener.onProgress(this.bytesWritten);
                }
            }
        }

        public long contentLength() {
            return -1;
        }

        public MediaType contentType() {
            return null;
        }

        public void setListener(ProgressListener progressListener) {
            this.listener = progressListener;
        }

        public OutputStream getOutputStream() {
            return this.stream.getOutputStream();
        }

        public void close() {
            this.stream.close();
        }

        public void writeTo(BufferedSink bufferedSink) throws IOException {
            BufferedSink buffer = Okio.buffer((Sink) new CountingSink(bufferedSink));
            this.stream.writeTo(buffer);
            buffer.flush();
            close();
        }
    }

    /* access modifiers changed from: protected */
    public void configureRequest(Builder builder) {
    }

    /* access modifiers changed from: protected */
    public Response interceptResponse(Response response) {
        return response;
    }

    public static OkHttpClient defaultOkHttpClient() {
        return defaultOkHttpClientBuilder().build();
    }

    public static OkHttpClient.Builder defaultOkHttpClientBuilder() {
        return new OkHttpClient.Builder().connectTimeout(DEFAULT_CONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS).readTimeout(DEFAULT_READ_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS).writeTimeout(DEFAULT_READ_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS).sslSocketFactory(SSLConfig.getSSLSocketFactory(), SSLConfig.getTrustManager());
    }

    public OkHttp3Requestor(OkHttpClient okHttpClient) {
        if (okHttpClient != null) {
            OkHttpUtil.assertNotSameThreadExecutor(okHttpClient.dispatcher().executorService());
            this.client = okHttpClient;
            return;
        }
        throw new NullPointerException("client");
    }

    public OkHttpClient getClient() {
        return this.client;
    }

    public HttpRequestor.Response doGet(String str, Iterable<Header> iterable) throws IOException {
        Builder url = new Builder().get().url(str);
        toOkHttpHeaders(iterable, url);
        configureRequest(url);
        Response interceptResponse = interceptResponse(this.client.newCall(url.build()).execute());
        return new HttpRequestor.Response(interceptResponse.code(), interceptResponse.body().byteStream(), fromOkHttpHeaders(interceptResponse.headers()));
    }

    public Uploader startPost(String str, Iterable<Header> iterable) throws IOException {
        return startUpload(str, iterable, "POST");
    }

    public Uploader startPut(String str, Iterable<Header> iterable) throws IOException {
        return startUpload(str, iterable, "PUT");
    }

    private BufferedUploader startUpload(String str, Iterable<Header> iterable, String str2) {
        Builder url = new Builder().url(str);
        toOkHttpHeaders(iterable, url);
        return new BufferedUploader(str2, url);
    }

    private static void toOkHttpHeaders(Iterable<Header> iterable, Builder builder) {
        for (Header header : iterable) {
            builder.addHeader(header.getKey(), header.getValue());
        }
    }

    /* access modifiers changed from: private */
    public static Map<String, List<String>> fromOkHttpHeaders(Headers headers) {
        HashMap hashMap = new HashMap(headers.size());
        for (String str : headers.names()) {
            hashMap.put(str, headers.values(str));
        }
        return hashMap;
    }
}