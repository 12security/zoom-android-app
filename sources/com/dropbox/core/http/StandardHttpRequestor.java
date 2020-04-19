package com.dropbox.core.http;

import com.dropbox.core.http.HttpRequestor.Header;
import com.dropbox.core.http.HttpRequestor.Response;
import com.dropbox.core.util.IOUtil;
import com.dropbox.core.util.IOUtil.ProgressListener;
import com.dropbox.core.util.ProgressOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import javax.net.ssl.HttpsURLConnection;

public class StandardHttpRequestor extends HttpRequestor {
    public static final StandardHttpRequestor INSTANCE = new StandardHttpRequestor(Config.DEFAULT_INSTANCE);
    private static final Logger LOGGER = Logger.getLogger(StandardHttpRequestor.class.getCanonicalName());
    private static volatile boolean certPinningWarningLogged = false;
    private final Config config;

    public static final class Config {
        public static final Config DEFAULT_INSTANCE = builder().build();
        private final long connectTimeoutMillis;
        private final Proxy proxy;
        private final long readTimeoutMillis;

        public static final class Builder {
            private long connectTimeoutMillis;
            private Proxy proxy;
            private long readTimeoutMillis;

            private Builder() {
                this(Proxy.NO_PROXY, HttpRequestor.DEFAULT_CONNECT_TIMEOUT_MILLIS, HttpRequestor.DEFAULT_READ_TIMEOUT_MILLIS);
            }

            private Builder(Proxy proxy2, long j, long j2) {
                this.proxy = proxy2;
                this.connectTimeoutMillis = j;
                this.readTimeoutMillis = j2;
            }

            public Builder withProxy(Proxy proxy2) {
                if (proxy2 != null) {
                    this.proxy = proxy2;
                    return this;
                }
                throw new NullPointerException("proxy");
            }

            public Builder withNoConnectTimeout() {
                return withConnectTimeout(0, TimeUnit.MILLISECONDS);
            }

            public Builder withConnectTimeout(long j, TimeUnit timeUnit) {
                this.connectTimeoutMillis = checkTimeoutMillis(j, timeUnit);
                return this;
            }

            public Builder withNoReadTimeout() {
                return withReadTimeout(0, TimeUnit.MILLISECONDS);
            }

            public Builder withReadTimeout(long j, TimeUnit timeUnit) {
                this.readTimeoutMillis = checkTimeoutMillis(j, timeUnit);
                return this;
            }

            public Config build() {
                Config config = new Config(this.proxy, this.connectTimeoutMillis, this.readTimeoutMillis);
                return config;
            }

            private static long checkTimeoutMillis(long j, TimeUnit timeUnit) {
                if (timeUnit == null) {
                    throw new NullPointerException("unit");
                } else if (j >= 0) {
                    long millis = timeUnit.toMillis(j);
                    if (2147483647L >= millis) {
                        return millis;
                    }
                    throw new IllegalArgumentException("timeout too large, must be less than: 2147483647");
                } else {
                    throw new IllegalArgumentException("timeout must be non-negative");
                }
            }
        }

        private Config(Proxy proxy2, long j, long j2) {
            this.proxy = proxy2;
            this.connectTimeoutMillis = j;
            this.readTimeoutMillis = j2;
        }

        public Proxy getProxy() {
            return this.proxy;
        }

        public long getConnectTimeoutMillis() {
            return this.connectTimeoutMillis;
        }

        public long getReadTimeoutMillis() {
            return this.readTimeoutMillis;
        }

        public Builder copy() {
            Builder builder = new Builder(this.proxy, this.connectTimeoutMillis, this.readTimeoutMillis);
            return builder;
        }

        public static Builder builder() {
            return new Builder();
        }
    }

    private class Uploader extends com.dropbox.core.http.HttpRequestor.Uploader {
        private HttpURLConnection conn;
        private final ProgressOutputStream out;

        public Uploader(HttpURLConnection httpURLConnection) throws IOException {
            this.conn = httpURLConnection;
            this.out = new ProgressOutputStream(StandardHttpRequestor.getOutputStream(httpURLConnection));
            httpURLConnection.connect();
        }

        public OutputStream getBody() {
            return this.out;
        }

        public void abort() {
            HttpURLConnection httpURLConnection = this.conn;
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
                this.conn = null;
                return;
            }
            throw new IllegalStateException("Can't abort().  Uploader already closed.");
        }

        public void close() {
            HttpURLConnection httpURLConnection = this.conn;
            if (httpURLConnection != null) {
                if (httpURLConnection.getDoOutput()) {
                    try {
                        IOUtil.closeQuietly(this.conn.getOutputStream());
                    } catch (IOException unused) {
                    }
                }
                this.conn = null;
            }
        }

        public Response finish() throws IOException {
            HttpURLConnection httpURLConnection = this.conn;
            if (httpURLConnection != null) {
                try {
                    return StandardHttpRequestor.this.toResponse(httpURLConnection);
                } finally {
                    this.conn = null;
                }
            } else {
                throw new IllegalStateException("Can't finish().  Uploader already closed.");
            }
        }

        public void setProgressListener(ProgressListener progressListener) {
            this.out.setListener(progressListener);
        }
    }

    /* access modifiers changed from: protected */
    public void configure(HttpURLConnection httpURLConnection) throws IOException {
    }

    /* access modifiers changed from: protected */
    @Deprecated
    public void configureConnection(HttpsURLConnection httpsURLConnection) throws IOException {
    }

    /* access modifiers changed from: protected */
    public void interceptResponse(HttpURLConnection httpURLConnection) throws IOException {
    }

    public StandardHttpRequestor(Config config2) {
        this.config = config2;
    }

    /* access modifiers changed from: private */
    public Response toResponse(HttpURLConnection httpURLConnection) throws IOException {
        InputStream inputStream;
        int responseCode = httpURLConnection.getResponseCode();
        if (responseCode >= 400 || responseCode == -1) {
            inputStream = httpURLConnection.getErrorStream();
        } else {
            inputStream = httpURLConnection.getInputStream();
        }
        interceptResponse(httpURLConnection);
        return new Response(responseCode, inputStream, httpURLConnection.getHeaderFields());
    }

    public Response doGet(String str, Iterable<Header> iterable) throws IOException {
        HttpURLConnection prepRequest = prepRequest(str, iterable);
        prepRequest.setRequestMethod("GET");
        prepRequest.connect();
        return toResponse(prepRequest);
    }

    public Uploader startPost(String str, Iterable<Header> iterable) throws IOException {
        HttpURLConnection prepRequest = prepRequest(str, iterable);
        prepRequest.setRequestMethod("POST");
        return new Uploader(prepRequest);
    }

    public Uploader startPut(String str, Iterable<Header> iterable) throws IOException {
        HttpURLConnection prepRequest = prepRequest(str, iterable);
        prepRequest.setRequestMethod("PUT");
        return new Uploader(prepRequest);
    }

    /* access modifiers changed from: private */
    public static OutputStream getOutputStream(HttpURLConnection httpURLConnection) throws IOException {
        httpURLConnection.setDoOutput(true);
        return httpURLConnection.getOutputStream();
    }

    private HttpURLConnection prepRequest(String str, Iterable<Header> iterable) throws IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(str).openConnection(this.config.getProxy());
        httpURLConnection.setConnectTimeout((int) this.config.getConnectTimeoutMillis());
        httpURLConnection.setReadTimeout((int) this.config.getReadTimeoutMillis());
        httpURLConnection.setUseCaches(false);
        httpURLConnection.setAllowUserInteraction(false);
        httpURLConnection.setChunkedStreamingMode(16384);
        if (httpURLConnection instanceof HttpsURLConnection) {
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) httpURLConnection;
            SSLConfig.apply(httpsURLConnection);
            configureConnection(httpsURLConnection);
        } else {
            logCertificatePinningWarning();
        }
        configure(httpURLConnection);
        for (Header header : iterable) {
            httpURLConnection.addRequestProperty(header.getKey(), header.getValue());
        }
        return httpURLConnection;
    }

    private static void logCertificatePinningWarning() {
        if (!certPinningWarningLogged) {
            certPinningWarningLogged = true;
            LOGGER.warning("Certificate pinning disabled for HTTPS connections. This is likely because your JRE does not return javax.net.ssl.HttpsURLConnection objects for https network connections. Be aware your app may be prone to man-in-the-middle attacks without proper SSL certificate validation. If you are using Google App Engine, please configure DbxRequestConfig to use GoogleAppEngineRequestor.");
        }
    }
}
