package org.apache.http.client.protocol;

import java.io.IOException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.auth.AuthProtocolState;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.Credentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.conn.routing.RouteInfo;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

@Contract(threading = ThreadingBehavior.IMMUTABLE)
public class RequestAuthCache implements HttpRequestInterceptor {
    private final Log log = LogFactory.getLog(getClass());

    public void process(HttpRequest httpRequest, HttpContext httpContext) throws HttpException, IOException {
        Args.notNull(httpRequest, "HTTP request");
        Args.notNull(httpContext, "HTTP context");
        HttpClientContext adapt = HttpClientContext.adapt(httpContext);
        AuthCache authCache = adapt.getAuthCache();
        if (authCache == null) {
            this.log.debug("Auth cache not set in the context");
            return;
        }
        CredentialsProvider credentialsProvider = adapt.getCredentialsProvider();
        if (credentialsProvider == null) {
            this.log.debug("Credentials provider not set in the context");
            return;
        }
        RouteInfo httpRoute = adapt.getHttpRoute();
        if (httpRoute == null) {
            this.log.debug("Route info not set in the context");
            return;
        }
        HttpHost targetHost = adapt.getTargetHost();
        if (targetHost == null) {
            this.log.debug("Target host not set in the context");
            return;
        }
        if (targetHost.getPort() < 0) {
            targetHost = new HttpHost(targetHost.getHostName(), httpRoute.getTargetHost().getPort(), targetHost.getSchemeName());
        }
        AuthState targetAuthState = adapt.getTargetAuthState();
        if (targetAuthState != null && targetAuthState.getState() == AuthProtocolState.UNCHALLENGED) {
            AuthScheme authScheme = authCache.get(targetHost);
            if (authScheme != null) {
                doPreemptiveAuth(targetHost, authScheme, targetAuthState, credentialsProvider);
            }
        }
        HttpHost proxyHost = httpRoute.getProxyHost();
        AuthState proxyAuthState = adapt.getProxyAuthState();
        if (!(proxyHost == null || proxyAuthState == null || proxyAuthState.getState() != AuthProtocolState.UNCHALLENGED)) {
            AuthScheme authScheme2 = authCache.get(proxyHost);
            if (authScheme2 != null) {
                doPreemptiveAuth(proxyHost, authScheme2, proxyAuthState, credentialsProvider);
            }
        }
    }

    private void doPreemptiveAuth(HttpHost httpHost, AuthScheme authScheme, AuthState authState, CredentialsProvider credentialsProvider) {
        String schemeName = authScheme.getSchemeName();
        if (this.log.isDebugEnabled()) {
            Log log2 = this.log;
            StringBuilder sb = new StringBuilder();
            sb.append("Re-using cached '");
            sb.append(schemeName);
            sb.append("' auth scheme for ");
            sb.append(httpHost);
            log2.debug(sb.toString());
        }
        Credentials credentials = credentialsProvider.getCredentials(new AuthScope(httpHost, AuthScope.ANY_REALM, schemeName));
        if (credentials != null) {
            authState.update(authScheme, credentials);
        } else {
            this.log.debug("No credentials for preemptive authentication");
        }
    }
}
