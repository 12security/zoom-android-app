package org.apache.http.impl.client;

import java.net.URI;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolException;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.client.RedirectHandler;
import org.apache.http.client.RedirectStrategy;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.protocol.HttpContext;

@Contract(threading = ThreadingBehavior.IMMUTABLE)
@Deprecated
class DefaultRedirectStrategyAdaptor implements RedirectStrategy {
    private final RedirectHandler handler;

    public DefaultRedirectStrategyAdaptor(RedirectHandler redirectHandler) {
        this.handler = redirectHandler;
    }

    public boolean isRedirected(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws ProtocolException {
        return this.handler.isRedirectRequested(httpResponse, httpContext);
    }

    public HttpUriRequest getRedirect(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws ProtocolException {
        URI locationURI = this.handler.getLocationURI(httpResponse, httpContext);
        if (httpRequest.getRequestLine().getMethod().equalsIgnoreCase("HEAD")) {
            return new HttpHead(locationURI);
        }
        return new HttpGet(locationURI);
    }

    public RedirectHandler getHandler() {
        return this.handler;
    }
}
