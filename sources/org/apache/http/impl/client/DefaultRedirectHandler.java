package org.apache.http.impl.client;

import com.google.firebase.analytics.FirebaseAnalytics.Param;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolException;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.client.CircularRedirectException;
import org.apache.http.client.RedirectHandler;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;

@Contract(threading = ThreadingBehavior.IMMUTABLE)
@Deprecated
public class DefaultRedirectHandler implements RedirectHandler {
    private static final String REDIRECT_LOCATIONS = "http.protocol.redirect-locations";
    private final Log log = LogFactory.getLog(getClass());

    public boolean isRedirectRequested(HttpResponse httpResponse, HttpContext httpContext) {
        Args.notNull(httpResponse, "HTTP response");
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        boolean z = true;
        if (statusCode != 307) {
            switch (statusCode) {
                case 301:
                case 302:
                    break;
                case 303:
                    return true;
                default:
                    return false;
            }
        }
        String method = ((HttpRequest) httpContext.getAttribute("http.request")).getRequestLine().getMethod();
        if (!method.equalsIgnoreCase("GET") && !method.equalsIgnoreCase("HEAD")) {
            z = false;
        }
        return z;
    }

    public URI getLocationURI(HttpResponse httpResponse, HttpContext httpContext) throws ProtocolException {
        URI uri;
        Args.notNull(httpResponse, "HTTP response");
        Header firstHeader = httpResponse.getFirstHeader(Param.LOCATION);
        if (firstHeader != null) {
            String value = firstHeader.getValue();
            if (this.log.isDebugEnabled()) {
                Log log2 = this.log;
                StringBuilder sb = new StringBuilder();
                sb.append("Redirect requested to location '");
                sb.append(value);
                sb.append("'");
                log2.debug(sb.toString());
            }
            try {
                URI uri2 = new URI(value);
                HttpParams params = httpResponse.getParams();
                if (!uri2.isAbsolute()) {
                    if (!params.isParameterTrue(ClientPNames.REJECT_RELATIVE_REDIRECT)) {
                        HttpHost httpHost = (HttpHost) httpContext.getAttribute("http.target_host");
                        Asserts.notNull(httpHost, "Target host");
                        try {
                            uri2 = URIUtils.resolve(URIUtils.rewriteURI(new URI(((HttpRequest) httpContext.getAttribute("http.request")).getRequestLine().getUri()), httpHost, true), uri2);
                        } catch (URISyntaxException e) {
                            throw new ProtocolException(e.getMessage(), e);
                        }
                    } else {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("Relative redirect location '");
                        sb2.append(uri2);
                        sb2.append("' not allowed");
                        throw new ProtocolException(sb2.toString());
                    }
                }
                if (params.isParameterFalse(ClientPNames.ALLOW_CIRCULAR_REDIRECTS)) {
                    RedirectLocations redirectLocations = (RedirectLocations) httpContext.getAttribute("http.protocol.redirect-locations");
                    if (redirectLocations == null) {
                        redirectLocations = new RedirectLocations();
                        httpContext.setAttribute("http.protocol.redirect-locations", redirectLocations);
                    }
                    if (uri2.getFragment() != null) {
                        try {
                            uri = URIUtils.rewriteURI(uri2, new HttpHost(uri2.getHost(), uri2.getPort(), uri2.getScheme()), true);
                        } catch (URISyntaxException e2) {
                            throw new ProtocolException(e2.getMessage(), e2);
                        }
                    } else {
                        uri = uri2;
                    }
                    if (!redirectLocations.contains(uri)) {
                        redirectLocations.add(uri);
                    } else {
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append("Circular redirect to '");
                        sb3.append(uri);
                        sb3.append("'");
                        throw new CircularRedirectException(sb3.toString());
                    }
                }
                return uri2;
            } catch (URISyntaxException e3) {
                StringBuilder sb4 = new StringBuilder();
                sb4.append("Invalid redirect URI: ");
                sb4.append(value);
                throw new ProtocolException(sb4.toString(), e3);
            }
        } else {
            StringBuilder sb5 = new StringBuilder();
            sb5.append("Received redirect response ");
            sb5.append(httpResponse.getStatusLine());
            sb5.append(" but no location header");
            throw new ProtocolException(sb5.toString());
        }
    }
}
