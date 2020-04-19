package org.apache.http.impl.conn;

import java.net.InetAddress;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.ProtocolException;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.SchemePortResolver;
import org.apache.http.conn.UnsupportedSchemeException;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.routing.HttpRoutePlanner;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

@Contract(threading = ThreadingBehavior.IMMUTABLE_CONDITIONAL)
public class DefaultRoutePlanner implements HttpRoutePlanner {
    private final SchemePortResolver schemePortResolver;

    /* access modifiers changed from: protected */
    public HttpHost determineProxy(HttpHost httpHost, HttpRequest httpRequest, HttpContext httpContext) throws HttpException {
        return null;
    }

    public DefaultRoutePlanner(SchemePortResolver schemePortResolver2) {
        if (schemePortResolver2 == null) {
            schemePortResolver2 = DefaultSchemePortResolver.INSTANCE;
        }
        this.schemePortResolver = schemePortResolver2;
    }

    public HttpRoute determineRoute(HttpHost httpHost, HttpRequest httpRequest, HttpContext httpContext) throws HttpException {
        Args.notNull(httpRequest, "Request");
        if (httpHost != null) {
            RequestConfig requestConfig = HttpClientContext.adapt(httpContext).getRequestConfig();
            InetAddress localAddress = requestConfig.getLocalAddress();
            HttpHost proxy = requestConfig.getProxy();
            if (proxy == null) {
                proxy = determineProxy(httpHost, httpRequest, httpContext);
            }
            if (httpHost.getPort() <= 0) {
                try {
                    httpHost = new HttpHost(httpHost.getHostName(), this.schemePortResolver.resolve(httpHost), httpHost.getSchemeName());
                } catch (UnsupportedSchemeException e) {
                    throw new HttpException(e.getMessage());
                }
            }
            boolean equalsIgnoreCase = httpHost.getSchemeName().equalsIgnoreCase("https");
            if (proxy == null) {
                return new HttpRoute(httpHost, localAddress, equalsIgnoreCase);
            }
            return new HttpRoute(httpHost, localAddress, proxy, equalsIgnoreCase);
        }
        throw new ProtocolException("Target host is not specified");
    }
}
