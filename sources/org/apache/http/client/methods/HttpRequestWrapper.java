package org.apache.http.client.methods;

import java.net.URI;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.ProtocolVersion;
import org.apache.http.RequestLine;
import org.apache.http.message.AbstractHttpMessage;
import org.apache.http.message.BasicRequestLine;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.Args;

public class HttpRequestWrapper extends AbstractHttpMessage implements HttpUriRequest {
    private final String method;
    private final HttpRequest original;
    private RequestLine requestLine;
    private final HttpHost target;
    private URI uri;
    private ProtocolVersion version;

    static class HttpEntityEnclosingRequestWrapper extends HttpRequestWrapper implements HttpEntityEnclosingRequest {
        private HttpEntity entity;

        HttpEntityEnclosingRequestWrapper(HttpEntityEnclosingRequest httpEntityEnclosingRequest, HttpHost httpHost) {
            super(httpEntityEnclosingRequest, httpHost);
            this.entity = httpEntityEnclosingRequest.getEntity();
        }

        public HttpEntity getEntity() {
            return this.entity;
        }

        public void setEntity(HttpEntity httpEntity) {
            this.entity = httpEntity;
        }

        public boolean expectContinue() {
            Header firstHeader = getFirstHeader("Expect");
            return firstHeader != null && HTTP.EXPECT_CONTINUE.equalsIgnoreCase(firstHeader.getValue());
        }
    }

    public boolean isAborted() {
        return false;
    }

    private HttpRequestWrapper(HttpRequest httpRequest, HttpHost httpHost) {
        this.original = (HttpRequest) Args.notNull(httpRequest, "HTTP request");
        this.target = httpHost;
        this.version = this.original.getRequestLine().getProtocolVersion();
        this.method = this.original.getRequestLine().getMethod();
        if (httpRequest instanceof HttpUriRequest) {
            this.uri = ((HttpUriRequest) httpRequest).getURI();
        } else {
            this.uri = null;
        }
        setHeaders(httpRequest.getAllHeaders());
    }

    public ProtocolVersion getProtocolVersion() {
        ProtocolVersion protocolVersion = this.version;
        return protocolVersion != null ? protocolVersion : this.original.getProtocolVersion();
    }

    public void setProtocolVersion(ProtocolVersion protocolVersion) {
        this.version = protocolVersion;
        this.requestLine = null;
    }

    public URI getURI() {
        return this.uri;
    }

    public void setURI(URI uri2) {
        this.uri = uri2;
        this.requestLine = null;
    }

    public String getMethod() {
        return this.method;
    }

    public void abort() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    public RequestLine getRequestLine() {
        String str;
        if (this.requestLine == null) {
            URI uri2 = this.uri;
            if (uri2 != null) {
                str = uri2.toASCIIString();
            } else {
                str = this.original.getRequestLine().getUri();
            }
            if (str == null || str.isEmpty()) {
                str = "/";
            }
            this.requestLine = new BasicRequestLine(this.method, str, getProtocolVersion());
        }
        return this.requestLine;
    }

    public HttpRequest getOriginal() {
        return this.original;
    }

    public HttpHost getTarget() {
        return this.target;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getRequestLine());
        sb.append(OAuth.SCOPE_DELIMITER);
        sb.append(this.headergroup);
        return sb.toString();
    }

    public static HttpRequestWrapper wrap(HttpRequest httpRequest) {
        return wrap(httpRequest, null);
    }

    public static HttpRequestWrapper wrap(HttpRequest httpRequest, HttpHost httpHost) {
        Args.notNull(httpRequest, "HTTP request");
        if (httpRequest instanceof HttpEntityEnclosingRequest) {
            return new HttpEntityEnclosingRequestWrapper((HttpEntityEnclosingRequest) httpRequest, httpHost);
        }
        return new HttpRequestWrapper(httpRequest, httpHost);
    }

    @Deprecated
    public HttpParams getParams() {
        if (this.params == null) {
            this.params = this.original.getParams().copy();
        }
        return this.params;
    }
}
