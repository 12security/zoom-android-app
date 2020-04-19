package com.google.api.client.http.javanet;

import com.google.api.client.http.LowLevelHttpRequest;
import com.google.api.client.http.LowLevelHttpResponse;
import com.google.api.client.util.Preconditions;
import com.google.api.client.util.StreamingContent;
import com.google.common.annotations.VisibleForTesting;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

final class NetHttpRequest extends LowLevelHttpRequest {
    private static final OutputWriter DEFAULT_CONNECTION_WRITER = new DefaultOutputWriter();
    private final HttpURLConnection connection;
    private int writeTimeout = 0;

    static class DefaultOutputWriter implements OutputWriter {
        DefaultOutputWriter() {
        }

        public void write(OutputStream outputStream, StreamingContent streamingContent) throws IOException {
            streamingContent.writeTo(outputStream);
        }
    }

    interface OutputWriter {
        void write(OutputStream outputStream, StreamingContent streamingContent) throws IOException;
    }

    NetHttpRequest(HttpURLConnection httpURLConnection) {
        this.connection = httpURLConnection;
        httpURLConnection.setInstanceFollowRedirects(false);
    }

    public void addHeader(String str, String str2) {
        this.connection.addRequestProperty(str, str2);
    }

    public void setTimeout(int i, int i2) {
        this.connection.setReadTimeout(i2);
        this.connection.setConnectTimeout(i);
    }

    public void setWriteTimeout(int i) throws IOException {
        this.writeTimeout = i;
    }

    public LowLevelHttpResponse execute() throws IOException {
        return execute(DEFAULT_CONNECTION_WRITER);
    }

    /* access modifiers changed from: 0000 */
    @VisibleForTesting
    public LowLevelHttpResponse execute(OutputWriter outputWriter) throws IOException {
        HttpURLConnection httpURLConnection = this.connection;
        if (getStreamingContent() != null) {
            String contentType = getContentType();
            if (contentType != null) {
                addHeader("Content-Type", contentType);
            }
            String contentEncoding = getContentEncoding();
            if (contentEncoding != null) {
                addHeader("Content-Encoding", contentEncoding);
            }
            long contentLength = getContentLength();
            int i = (contentLength > 0 ? 1 : (contentLength == 0 ? 0 : -1));
            if (i >= 0) {
                httpURLConnection.setRequestProperty("Content-Length", Long.toString(contentLength));
            }
            String requestMethod = httpURLConnection.getRequestMethod();
            if ("POST".equals(requestMethod) || "PUT".equals(requestMethod)) {
                httpURLConnection.setDoOutput(true);
                if (i < 0 || contentLength > 2147483647L) {
                    httpURLConnection.setChunkedStreamingMode(0);
                } else {
                    httpURLConnection.setFixedLengthStreamingMode((int) contentLength);
                }
                OutputStream outputStream = httpURLConnection.getOutputStream();
                try {
                    writeContentToOutputStream(outputWriter, outputStream);
                    try {
                    } catch (IOException e) {
                        throw e;
                    }
                } finally {
                    try {
                        outputStream.close();
                    } catch (IOException unused) {
                    }
                }
            } else {
                Preconditions.checkArgument(i == 0, "%s with non-zero content length is not supported", requestMethod);
            }
        }
        try {
            httpURLConnection.connect();
            return new NetHttpResponse(httpURLConnection);
        } catch (Throwable th) {
            httpURLConnection.disconnect();
            throw th;
        }
    }

    private void writeContentToOutputStream(final OutputWriter outputWriter, final OutputStream outputStream) throws IOException {
        if (this.writeTimeout == 0) {
            outputWriter.write(outputStream, getStreamingContent());
            return;
        }
        final StreamingContent streamingContent = getStreamingContent();
        C10681 r1 = new Callable<Boolean>() {
            public Boolean call() throws IOException {
                outputWriter.write(outputStream, streamingContent);
                return Boolean.TRUE;
            }
        };
        ExecutorService newSingleThreadExecutor = Executors.newSingleThreadExecutor();
        Future submit = newSingleThreadExecutor.submit(new FutureTask(r1), null);
        newSingleThreadExecutor.shutdown();
        try {
            submit.get((long) this.writeTimeout, TimeUnit.MILLISECONDS);
            if (!newSingleThreadExecutor.isTerminated()) {
                newSingleThreadExecutor.shutdown();
            }
        } catch (InterruptedException e) {
            throw new IOException("Socket write interrupted", e);
        } catch (ExecutionException e2) {
            throw new IOException("Exception in socket write", e2);
        } catch (TimeoutException e3) {
            throw new IOException("Socket write timed out", e3);
        }
    }
}
