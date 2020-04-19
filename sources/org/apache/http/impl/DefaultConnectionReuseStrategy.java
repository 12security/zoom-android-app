package org.apache.http.impl;

import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.ParseException;
import org.apache.http.ProtocolVersion;
import org.apache.http.TokenIterator;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.message.BasicTokenIterator;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

@Contract(threading = ThreadingBehavior.IMMUTABLE)
public class DefaultConnectionReuseStrategy implements ConnectionReuseStrategy {
    public static final DefaultConnectionReuseStrategy INSTANCE = new DefaultConnectionReuseStrategy();

    public boolean keepAlive(HttpResponse httpResponse, HttpContext httpContext) {
        Args.notNull(httpResponse, "HTTP response");
        Args.notNull(httpContext, "HTTP context");
        HttpRequest httpRequest = (HttpRequest) httpContext.getAttribute("http.request");
        if (httpRequest != null) {
            try {
                BasicTokenIterator basicTokenIterator = new BasicTokenIterator(httpRequest.headerIterator("Connection"));
                while (basicTokenIterator.hasNext()) {
                    if (HTTP.CONN_CLOSE.equalsIgnoreCase(basicTokenIterator.nextToken())) {
                        return false;
                    }
                }
            } catch (ParseException unused) {
                return false;
            }
        }
        ProtocolVersion protocolVersion = httpResponse.getStatusLine().getProtocolVersion();
        Header firstHeader = httpResponse.getFirstHeader("Transfer-Encoding");
        if (firstHeader != null) {
            if (!HTTP.CHUNK_CODING.equalsIgnoreCase(firstHeader.getValue())) {
                return false;
            }
        } else if (canResponseHaveBody(httpRequest, httpResponse)) {
            Header[] headers = httpResponse.getHeaders("Content-Length");
            if (headers.length != 1) {
                return false;
            }
            try {
                if (Integer.parseInt(headers[0].getValue()) < 0) {
                    return false;
                }
            } catch (NumberFormatException unused2) {
                return false;
            }
        }
        HeaderIterator headerIterator = httpResponse.headerIterator("Connection");
        if (!headerIterator.hasNext()) {
            headerIterator = httpResponse.headerIterator("Proxy-Connection");
        }
        if (headerIterator.hasNext()) {
            try {
                BasicTokenIterator basicTokenIterator2 = new BasicTokenIterator(headerIterator);
                boolean z = false;
                while (basicTokenIterator2.hasNext()) {
                    String nextToken = basicTokenIterator2.nextToken();
                    if (HTTP.CONN_CLOSE.equalsIgnoreCase(nextToken)) {
                        return false;
                    }
                    if (HTTP.CONN_KEEP_ALIVE.equalsIgnoreCase(nextToken)) {
                        z = true;
                    }
                }
                if (z) {
                    return true;
                }
            } catch (ParseException unused3) {
                return false;
            }
        }
        return !protocolVersion.lessEquals(HttpVersion.HTTP_1_0);
    }

    /* access modifiers changed from: protected */
    public TokenIterator createTokenIterator(HeaderIterator headerIterator) {
        return new BasicTokenIterator(headerIterator);
    }

    private boolean canResponseHaveBody(HttpRequest httpRequest, HttpResponse httpResponse) {
        boolean z = false;
        if (httpRequest != null && httpRequest.getRequestLine().getMethod().equalsIgnoreCase("HEAD")) {
            return false;
        }
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        if (!(statusCode < 200 || statusCode == 204 || statusCode == 304 || statusCode == 205)) {
            z = true;
        }
        return z;
    }
}
