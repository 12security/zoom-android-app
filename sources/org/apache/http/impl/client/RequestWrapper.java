package org.apache.http.impl.client;

import java.net.URI;
import java.net.URISyntaxException;
import org.apache.http.HttpRequest;
import org.apache.http.ProtocolException;
import org.apache.http.ProtocolVersion;
import org.apache.http.RequestLine;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.AbstractHttpMessage;
import org.apache.http.message.BasicRequestLine;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.Args;

@Deprecated
public class RequestWrapper extends AbstractHttpMessage implements HttpUriRequest {
    private int execCount;
    private String method;
    private final HttpRequest original;
    private URI uri;
    private ProtocolVersion version;

    public boolean isAborted() {
        return false;
    }

    public boolean isRepeatable() {
        return true;
    }

    public RequestWrapper(HttpRequest httpRequest) throws ProtocolException {
        Args.notNull(httpRequest, "HTTP request");
        this.original = httpRequest;
        setParams(httpRequest.getParams());
        setHeaders(httpRequest.getAllHeaders());
        if (httpRequest instanceof HttpUriRequest) {
            HttpUriRequest httpUriRequest = (HttpUriRequest) httpRequest;
            this.uri = httpUriRequest.getURI();
            this.method = httpUriRequest.getMethod();
            this.version = null;
        } else {
            RequestLine requestLine = httpRequest.getRequestLine();
            try {
                this.uri = new URI(requestLine.getUri());
                this.method = requestLine.getMethod();
                this.version = httpRequest.getProtocolVersion();
            } catch (URISyntaxException e) {
                StringBuilder sb = new StringBuilder();
                sb.append("Invalid request URI: ");
                sb.append(requestLine.getUri());
                throw new ProtocolException(sb.toString(), e);
            }
        }
        this.execCount = 0;
    }

    public void resetHeaders() {
        this.headergroup.clear();
        setHeaders(this.original.getAllHeaders());
    }

    public String getMethod() {
        return this.method;
    }

    public void setMethod(String str) {
        Args.notNull(str, "Method name");
        this.method = str;
    }

    public ProtocolVersion getProtocolVersion() {
        if (this.version == null) {
            this.version = HttpProtocolParams.getVersion(getParams());
        }
        return this.version;
    }

    public void setProtocolVersion(ProtocolVersion protocolVersion) {
        this.version = protocolVersion;
    }

    public URI getURI() {
        return this.uri;
    }

    public void setURI(URI uri2) {
        this.uri = uri2;
    }

    public RequestLine getRequestLine() {
        ProtocolVersion protocolVersion = getProtocolVersion();
        URI uri2 = this.uri;
        String aSCIIString = uri2 != null ? uri2.toASCIIString() : null;
        if (aSCIIString == null || aSCIIString.isEmpty()) {
            aSCIIString = "/";
        }
        return new BasicRequestLine(getMethod(), aSCIIString, protocolVersion);
    }

    public void abort() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    public HttpRequest getOriginal() {
        return this.original;
    }

    public int getExecCount() {
        return this.execCount;
    }

    public void incrementExecCount() {
        this.execCount++;
    }
}
