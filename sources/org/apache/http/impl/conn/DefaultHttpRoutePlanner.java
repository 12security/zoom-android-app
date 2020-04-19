package org.apache.http.impl.conn;

import java.net.InetAddress;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.routing.HttpRoutePlanner;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;

@Contract(threading = ThreadingBehavior.SAFE)
@Deprecated
public class DefaultHttpRoutePlanner implements HttpRoutePlanner {
    protected final SchemeRegistry schemeRegistry;

    public DefaultHttpRoutePlanner(SchemeRegistry schemeRegistry2) {
        Args.notNull(schemeRegistry2, "Scheme registry");
        this.schemeRegistry = schemeRegistry2;
    }

    public HttpRoute determineRoute(HttpHost httpHost, HttpRequest httpRequest, HttpContext httpContext) throws HttpException {
        HttpRoute httpRoute;
        Args.notNull(httpRequest, "HTTP request");
        HttpRoute forcedRoute = ConnRouteParams.getForcedRoute(httpRequest.getParams());
        if (forcedRoute != null) {
            return forcedRoute;
        }
        Asserts.notNull(httpHost, "Target host");
        InetAddress localAddress = ConnRouteParams.getLocalAddress(httpRequest.getParams());
        HttpHost defaultProxy = ConnRouteParams.getDefaultProxy(httpRequest.getParams());
        try {
            boolean isLayered = this.schemeRegistry.getScheme(httpHost.getSchemeName()).isLayered();
            if (defaultProxy == null) {
                httpRoute = new HttpRoute(httpHost, localAddress, isLayered);
            } else {
                httpRoute = new HttpRoute(httpHost, localAddress, defaultProxy, isLayered);
            }
            return httpRoute;
        } catch (IllegalStateException e) {
            throw new HttpException(e.getMessage());
        }
    }
}
