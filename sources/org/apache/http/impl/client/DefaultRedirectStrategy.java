package org.apache.http.impl.client;

import com.google.firebase.analytics.FirebaseAnalytics.Param;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;
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
import org.apache.http.client.RedirectStrategy;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;
import org.apache.http.util.TextUtils;

@Contract(threading = ThreadingBehavior.IMMUTABLE)
public class DefaultRedirectStrategy implements RedirectStrategy {
    public static final DefaultRedirectStrategy INSTANCE = new DefaultRedirectStrategy();
    @Deprecated
    public static final String REDIRECT_LOCATIONS = "http.protocol.redirect-locations";
    private static final String[] REDIRECT_METHODS = {"GET", "HEAD"};
    private final Log log = LogFactory.getLog(getClass());

    public boolean isRedirected(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws ProtocolException {
        Args.notNull(httpRequest, "HTTP request");
        Args.notNull(httpResponse, "HTTP response");
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        String method = httpRequest.getRequestLine().getMethod();
        Header firstHeader = httpResponse.getFirstHeader(Param.LOCATION);
        if (statusCode != 307) {
            boolean z = true;
            switch (statusCode) {
                case 301:
                    break;
                case 302:
                    if (!isRedirectable(method) || firstHeader == null) {
                        z = false;
                    }
                    return z;
                case 303:
                    return true;
                default:
                    return false;
            }
        }
        return isRedirectable(method);
    }

    public URI getLocationURI(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws ProtocolException {
        Args.notNull(httpRequest, "HTTP request");
        Args.notNull(httpResponse, "HTTP response");
        Args.notNull(httpContext, "HTTP context");
        HttpClientContext adapt = HttpClientContext.adapt(httpContext);
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
            RequestConfig requestConfig = adapt.getRequestConfig();
            URI createLocationURI = createLocationURI(value);
            try {
                if (!createLocationURI.isAbsolute()) {
                    if (requestConfig.isRelativeRedirectsAllowed()) {
                        HttpHost targetHost = adapt.getTargetHost();
                        Asserts.notNull(targetHost, "Target host");
                        createLocationURI = URIUtils.resolve(URIUtils.rewriteURI(new URI(httpRequest.getRequestLine().getUri()), targetHost, false), createLocationURI);
                    } else {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("Relative redirect location '");
                        sb2.append(createLocationURI);
                        sb2.append("' not allowed");
                        throw new ProtocolException(sb2.toString());
                    }
                }
                RedirectLocations redirectLocations = (RedirectLocations) adapt.getAttribute("http.protocol.redirect-locations");
                if (redirectLocations == null) {
                    redirectLocations = new RedirectLocations();
                    httpContext.setAttribute("http.protocol.redirect-locations", redirectLocations);
                }
                if (requestConfig.isCircularRedirectsAllowed() || !redirectLocations.contains(createLocationURI)) {
                    redirectLocations.add(createLocationURI);
                    return createLocationURI;
                }
                StringBuilder sb3 = new StringBuilder();
                sb3.append("Circular redirect to '");
                sb3.append(createLocationURI);
                sb3.append("'");
                throw new CircularRedirectException(sb3.toString());
            } catch (URISyntaxException e) {
                throw new ProtocolException(e.getMessage(), e);
            }
        } else {
            StringBuilder sb4 = new StringBuilder();
            sb4.append("Received redirect response ");
            sb4.append(httpResponse.getStatusLine());
            sb4.append(" but no location header");
            throw new ProtocolException(sb4.toString());
        }
    }

    /* access modifiers changed from: protected */
    public URI createLocationURI(String str) throws ProtocolException {
        try {
            URIBuilder uRIBuilder = new URIBuilder(new URI(str).normalize());
            String host = uRIBuilder.getHost();
            if (host != null) {
                uRIBuilder.setHost(host.toLowerCase(Locale.ROOT));
            }
            if (TextUtils.isEmpty(uRIBuilder.getPath())) {
                uRIBuilder.setPath("/");
            }
            return uRIBuilder.build();
        } catch (URISyntaxException e) {
            StringBuilder sb = new StringBuilder();
            sb.append("Invalid redirect URI: ");
            sb.append(str);
            throw new ProtocolException(sb.toString(), e);
        }
    }

    /* access modifiers changed from: protected */
    public boolean isRedirectable(String str) {
        for (String equalsIgnoreCase : REDIRECT_METHODS) {
            if (equalsIgnoreCase.equalsIgnoreCase(str)) {
                return true;
            }
        }
        return false;
    }

    public HttpUriRequest getRedirect(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws ProtocolException {
        URI locationURI = getLocationURI(httpRequest, httpResponse, httpContext);
        String method = httpRequest.getRequestLine().getMethod();
        if (method.equalsIgnoreCase("HEAD")) {
            return new HttpHead(locationURI);
        }
        if (method.equalsIgnoreCase("GET")) {
            return new HttpGet(locationURI);
        }
        if (httpResponse.getStatusLine().getStatusCode() == 307) {
            return RequestBuilder.copy(httpRequest).setUri(locationURI).build();
        }
        return new HttpGet(locationURI);
    }
}
