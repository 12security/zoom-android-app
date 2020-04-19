package org.apache.http.client.protocol;

import java.io.IOException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthState;
import org.apache.http.client.AuthCache;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

@Contract(threading = ThreadingBehavior.IMMUTABLE)
@Deprecated
public class ResponseAuthCache implements HttpResponseInterceptor {
    private final Log log = LogFactory.getLog(getClass());

    public void process(HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {
        Args.notNull(httpResponse, "HTTP request");
        Args.notNull(httpContext, "HTTP context");
        AuthCache authCache = (AuthCache) httpContext.getAttribute("http.auth.auth-cache");
        HttpHost httpHost = (HttpHost) httpContext.getAttribute("http.target_host");
        AuthState authState = (AuthState) httpContext.getAttribute("http.auth.target-scope");
        if (!(httpHost == null || authState == null)) {
            if (this.log.isDebugEnabled()) {
                Log log2 = this.log;
                StringBuilder sb = new StringBuilder();
                sb.append("Target auth state: ");
                sb.append(authState.getState());
                log2.debug(sb.toString());
            }
            if (isCachable(authState)) {
                SchemeRegistry schemeRegistry = (SchemeRegistry) httpContext.getAttribute(ClientContext.SCHEME_REGISTRY);
                if (httpHost.getPort() < 0) {
                    httpHost = new HttpHost(httpHost.getHostName(), schemeRegistry.getScheme(httpHost).resolvePort(httpHost.getPort()), httpHost.getSchemeName());
                }
                if (authCache == null) {
                    authCache = new BasicAuthCache();
                    httpContext.setAttribute("http.auth.auth-cache", authCache);
                }
                switch (authState.getState()) {
                    case CHALLENGED:
                        cache(authCache, httpHost, authState.getAuthScheme());
                        break;
                    case FAILURE:
                        uncache(authCache, httpHost, authState.getAuthScheme());
                        break;
                }
            }
        }
        HttpHost httpHost2 = (HttpHost) httpContext.getAttribute(ExecutionContext.HTTP_PROXY_HOST);
        AuthState authState2 = (AuthState) httpContext.getAttribute("http.auth.proxy-scope");
        if (httpHost2 != null && authState2 != null) {
            if (this.log.isDebugEnabled()) {
                Log log3 = this.log;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Proxy auth state: ");
                sb2.append(authState2.getState());
                log3.debug(sb2.toString());
            }
            if (isCachable(authState2)) {
                if (authCache == null) {
                    authCache = new BasicAuthCache();
                    httpContext.setAttribute("http.auth.auth-cache", authCache);
                }
                switch (authState2.getState()) {
                    case CHALLENGED:
                        cache(authCache, httpHost2, authState2.getAuthScheme());
                        return;
                    case FAILURE:
                        uncache(authCache, httpHost2, authState2.getAuthScheme());
                        return;
                    default:
                        return;
                }
            }
        }
    }

    private boolean isCachable(AuthState authState) {
        AuthScheme authScheme = authState.getAuthScheme();
        boolean z = false;
        if (authScheme == null || !authScheme.isComplete()) {
            return false;
        }
        String schemeName = authScheme.getSchemeName();
        if (schemeName.equalsIgnoreCase("Basic") || schemeName.equalsIgnoreCase("Digest")) {
            z = true;
        }
        return z;
    }

    private void cache(AuthCache authCache, HttpHost httpHost, AuthScheme authScheme) {
        if (this.log.isDebugEnabled()) {
            Log log2 = this.log;
            StringBuilder sb = new StringBuilder();
            sb.append("Caching '");
            sb.append(authScheme.getSchemeName());
            sb.append("' auth scheme for ");
            sb.append(httpHost);
            log2.debug(sb.toString());
        }
        authCache.put(httpHost, authScheme);
    }

    private void uncache(AuthCache authCache, HttpHost httpHost, AuthScheme authScheme) {
        if (this.log.isDebugEnabled()) {
            Log log2 = this.log;
            StringBuilder sb = new StringBuilder();
            sb.append("Removing from cache '");
            sb.append(authScheme.getSchemeName());
            sb.append("' auth scheme for ");
            sb.append(httpHost);
            log2.debug(sb.toString());
        }
        authCache.remove(httpHost);
    }
}
