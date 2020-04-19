package com.google.api.client.http;

import com.google.api.client.util.Charsets;
import com.google.api.client.util.IOUtils;
import com.google.api.client.util.LoggingInputStream;
import com.google.api.client.util.Preconditions;
import com.google.api.client.util.StringUtils;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import org.apache.http.message.TokenParser;

public final class HttpResponse {
    private InputStream content;
    private final String contentEncoding;
    private int contentLoggingLimit;
    private boolean contentRead;
    private final String contentType;
    private boolean loggingEnabled;
    private final HttpMediaType mediaType;
    private final HttpRequest request;
    LowLevelHttpResponse response;
    private final int statusCode;
    private final String statusMessage;

    HttpResponse(HttpRequest httpRequest, LowLevelHttpResponse lowLevelHttpResponse) throws IOException {
        StringBuilder sb;
        this.request = httpRequest;
        this.contentLoggingLimit = httpRequest.getContentLoggingLimit();
        this.loggingEnabled = httpRequest.isLoggingEnabled();
        this.response = lowLevelHttpResponse;
        this.contentEncoding = lowLevelHttpResponse.getContentEncoding();
        int statusCode2 = lowLevelHttpResponse.getStatusCode();
        boolean z = false;
        if (statusCode2 < 0) {
            statusCode2 = 0;
        }
        this.statusCode = statusCode2;
        String reasonPhrase = lowLevelHttpResponse.getReasonPhrase();
        this.statusMessage = reasonPhrase;
        Logger logger = HttpTransport.LOGGER;
        if (this.loggingEnabled && logger.isLoggable(Level.CONFIG)) {
            z = true;
        }
        HttpMediaType httpMediaType = null;
        if (z) {
            sb = new StringBuilder();
            sb.append("-------------- RESPONSE --------------");
            sb.append(StringUtils.LINE_SEPARATOR);
            String statusLine = lowLevelHttpResponse.getStatusLine();
            if (statusLine != null) {
                sb.append(statusLine);
            } else {
                sb.append(this.statusCode);
                if (reasonPhrase != null) {
                    sb.append(TokenParser.f498SP);
                    sb.append(reasonPhrase);
                }
            }
            sb.append(StringUtils.LINE_SEPARATOR);
        } else {
            sb = null;
        }
        httpRequest.getResponseHeaders().fromHttpResponse(lowLevelHttpResponse, z ? sb : null);
        String contentType2 = lowLevelHttpResponse.getContentType();
        if (contentType2 == null) {
            contentType2 = httpRequest.getResponseHeaders().getContentType();
        }
        this.contentType = contentType2;
        if (contentType2 != null) {
            httpMediaType = new HttpMediaType(contentType2);
        }
        this.mediaType = httpMediaType;
        if (z) {
            logger.config(sb.toString());
        }
    }

    public int getContentLoggingLimit() {
        return this.contentLoggingLimit;
    }

    public HttpResponse setContentLoggingLimit(int i) {
        Preconditions.checkArgument(i >= 0, "The content logging limit must be non-negative.");
        this.contentLoggingLimit = i;
        return this;
    }

    public boolean isLoggingEnabled() {
        return this.loggingEnabled;
    }

    public HttpResponse setLoggingEnabled(boolean z) {
        this.loggingEnabled = z;
        return this;
    }

    public String getContentEncoding() {
        return this.contentEncoding;
    }

    public String getContentType() {
        return this.contentType;
    }

    public HttpMediaType getMediaType() {
        return this.mediaType;
    }

    public HttpHeaders getHeaders() {
        return this.request.getResponseHeaders();
    }

    public boolean isSuccessStatusCode() {
        return HttpStatusCodes.isSuccess(this.statusCode);
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    public String getStatusMessage() {
        return this.statusMessage;
    }

    public HttpTransport getTransport() {
        return this.request.getTransport();
    }

    public HttpRequest getRequest() {
        return this.request;
    }

    public InputStream getContent() throws IOException {
        if (!this.contentRead) {
            InputStream content2 = this.response.getContent();
            if (content2 != null) {
                try {
                    String str = this.contentEncoding;
                    if (str != null && str.contains("gzip")) {
                        content2 = new GZIPInputStream(content2);
                    }
                    Logger logger = HttpTransport.LOGGER;
                    if (this.loggingEnabled && logger.isLoggable(Level.CONFIG)) {
                        content2 = new LoggingInputStream(content2, logger, Level.CONFIG, this.contentLoggingLimit);
                    }
                    this.content = content2;
                } catch (EOFException unused) {
                    content2.close();
                } catch (Throwable th) {
                    content2.close();
                    throw th;
                }
            }
            this.contentRead = true;
        }
        return this.content;
    }

    public void download(OutputStream outputStream) throws IOException {
        IOUtils.copy(getContent(), outputStream);
    }

    public void ignore() throws IOException {
        InputStream content2 = getContent();
        if (content2 != null) {
            content2.close();
        }
    }

    public void disconnect() throws IOException {
        ignore();
        this.response.disconnect();
    }

    public <T> T parseAs(Class<T> cls) throws IOException {
        if (!hasMessageBody()) {
            return null;
        }
        return this.request.getParser().parseAndClose(getContent(), getContentCharset(), cls);
    }

    private boolean hasMessageBody() throws IOException {
        int statusCode2 = getStatusCode();
        if (!getRequest().getRequestMethod().equals("HEAD") && statusCode2 / 100 != 1 && statusCode2 != 204 && statusCode2 != 304) {
            return true;
        }
        ignore();
        return false;
    }

    public Object parseAs(Type type) throws IOException {
        if (!hasMessageBody()) {
            return null;
        }
        return this.request.getParser().parseAndClose(getContent(), getContentCharset(), type);
    }

    public String parseAsString() throws IOException {
        InputStream content2 = getContent();
        if (content2 == null) {
            return "";
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        IOUtils.copy(content2, byteArrayOutputStream);
        return byteArrayOutputStream.toString(getContentCharset().name());
    }

    public Charset getContentCharset() {
        HttpMediaType httpMediaType = this.mediaType;
        if (httpMediaType == null || httpMediaType.getCharsetParameter() == null) {
            return Charsets.ISO_8859_1;
        }
        return this.mediaType.getCharsetParameter();
    }
}
