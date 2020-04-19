package org.apache.http.impl.client;

import com.google.api.client.http.HttpMethods;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NoHttpResponseException;
import org.apache.http.ProtocolException;
import org.apache.http.auth.AuthProtocolState;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthenticationHandler;
import org.apache.http.client.AuthenticationStrategy;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.NonRepeatableRequestException;
import org.apache.http.client.RedirectException;
import org.apache.http.client.RedirectHandler;
import org.apache.http.client.RedirectStrategy;
import org.apache.http.client.RequestDirector;
import org.apache.http.client.UserTokenHandler;
import org.apache.http.client.methods.AbortableHttpRequest;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.conn.BasicManagedEntity;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ClientConnectionRequest;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.ManagedClientConnection;
import org.apache.http.conn.routing.BasicRouteDirector;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.routing.HttpRoutePlanner;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.conn.ConnectionShutdownException;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpRequestExecutor;
import org.apache.http.util.Args;
import org.apache.http.util.EntityUtils;

@Deprecated
public class DefaultRequestDirector implements RequestDirector {
    private final HttpAuthenticator authenticator;
    protected final ClientConnectionManager connManager;
    private int execCount;
    protected final HttpProcessor httpProcessor;
    protected final ConnectionKeepAliveStrategy keepAliveStrategy;
    private final Log log;
    protected ManagedClientConnection managedConn;
    private final int maxRedirects;
    protected final HttpParams params;
    @Deprecated
    protected final AuthenticationHandler proxyAuthHandler;
    protected final AuthState proxyAuthState;
    protected final AuthenticationStrategy proxyAuthStrategy;
    private int redirectCount;
    @Deprecated
    protected final RedirectHandler redirectHandler;
    protected final RedirectStrategy redirectStrategy;
    protected final HttpRequestExecutor requestExec;
    protected final HttpRequestRetryHandler retryHandler;
    protected final ConnectionReuseStrategy reuseStrategy;
    protected final HttpRoutePlanner routePlanner;
    @Deprecated
    protected final AuthenticationHandler targetAuthHandler;
    protected final AuthState targetAuthState;
    protected final AuthenticationStrategy targetAuthStrategy;
    protected final UserTokenHandler userTokenHandler;
    private HttpHost virtualHost;

    @Deprecated
    public DefaultRequestDirector(HttpRequestExecutor httpRequestExecutor, ClientConnectionManager clientConnectionManager, ConnectionReuseStrategy connectionReuseStrategy, ConnectionKeepAliveStrategy connectionKeepAliveStrategy, HttpRoutePlanner httpRoutePlanner, HttpProcessor httpProcessor2, HttpRequestRetryHandler httpRequestRetryHandler, RedirectHandler redirectHandler2, AuthenticationHandler authenticationHandler, AuthenticationHandler authenticationHandler2, UserTokenHandler userTokenHandler2, HttpParams httpParams) {
        this(LogFactory.getLog(DefaultRequestDirector.class), httpRequestExecutor, clientConnectionManager, connectionReuseStrategy, connectionKeepAliveStrategy, httpRoutePlanner, httpProcessor2, httpRequestRetryHandler, (RedirectStrategy) new DefaultRedirectStrategyAdaptor(redirectHandler2), (AuthenticationStrategy) new AuthenticationStrategyAdaptor(authenticationHandler), (AuthenticationStrategy) new AuthenticationStrategyAdaptor(authenticationHandler2), userTokenHandler2, httpParams);
    }

    @Deprecated
    public DefaultRequestDirector(Log log2, HttpRequestExecutor httpRequestExecutor, ClientConnectionManager clientConnectionManager, ConnectionReuseStrategy connectionReuseStrategy, ConnectionKeepAliveStrategy connectionKeepAliveStrategy, HttpRoutePlanner httpRoutePlanner, HttpProcessor httpProcessor2, HttpRequestRetryHandler httpRequestRetryHandler, RedirectStrategy redirectStrategy2, AuthenticationHandler authenticationHandler, AuthenticationHandler authenticationHandler2, UserTokenHandler userTokenHandler2, HttpParams httpParams) {
        this(LogFactory.getLog(DefaultRequestDirector.class), httpRequestExecutor, clientConnectionManager, connectionReuseStrategy, connectionKeepAliveStrategy, httpRoutePlanner, httpProcessor2, httpRequestRetryHandler, redirectStrategy2, (AuthenticationStrategy) new AuthenticationStrategyAdaptor(authenticationHandler), (AuthenticationStrategy) new AuthenticationStrategyAdaptor(authenticationHandler2), userTokenHandler2, httpParams);
    }

    public DefaultRequestDirector(Log log2, HttpRequestExecutor httpRequestExecutor, ClientConnectionManager clientConnectionManager, ConnectionReuseStrategy connectionReuseStrategy, ConnectionKeepAliveStrategy connectionKeepAliveStrategy, HttpRoutePlanner httpRoutePlanner, HttpProcessor httpProcessor2, HttpRequestRetryHandler httpRequestRetryHandler, RedirectStrategy redirectStrategy2, AuthenticationStrategy authenticationStrategy, AuthenticationStrategy authenticationStrategy2, UserTokenHandler userTokenHandler2, HttpParams httpParams) {
        Args.notNull(log2, "Log");
        Args.notNull(httpRequestExecutor, "Request executor");
        Args.notNull(clientConnectionManager, "Client connection manager");
        Args.notNull(connectionReuseStrategy, "Connection reuse strategy");
        Args.notNull(connectionKeepAliveStrategy, "Connection keep alive strategy");
        Args.notNull(httpRoutePlanner, "Route planner");
        Args.notNull(httpProcessor2, "HTTP protocol processor");
        Args.notNull(httpRequestRetryHandler, "HTTP request retry handler");
        Args.notNull(redirectStrategy2, "Redirect strategy");
        Args.notNull(authenticationStrategy, "Target authentication strategy");
        Args.notNull(authenticationStrategy2, "Proxy authentication strategy");
        Args.notNull(userTokenHandler2, "User token handler");
        Args.notNull(httpParams, "HTTP parameters");
        this.log = log2;
        this.authenticator = new HttpAuthenticator(log2);
        this.requestExec = httpRequestExecutor;
        this.connManager = clientConnectionManager;
        this.reuseStrategy = connectionReuseStrategy;
        this.keepAliveStrategy = connectionKeepAliveStrategy;
        this.routePlanner = httpRoutePlanner;
        this.httpProcessor = httpProcessor2;
        this.retryHandler = httpRequestRetryHandler;
        this.redirectStrategy = redirectStrategy2;
        this.targetAuthStrategy = authenticationStrategy;
        this.proxyAuthStrategy = authenticationStrategy2;
        this.userTokenHandler = userTokenHandler2;
        this.params = httpParams;
        if (redirectStrategy2 instanceof DefaultRedirectStrategyAdaptor) {
            this.redirectHandler = ((DefaultRedirectStrategyAdaptor) redirectStrategy2).getHandler();
        } else {
            this.redirectHandler = null;
        }
        if (authenticationStrategy instanceof AuthenticationStrategyAdaptor) {
            this.targetAuthHandler = ((AuthenticationStrategyAdaptor) authenticationStrategy).getHandler();
        } else {
            this.targetAuthHandler = null;
        }
        if (authenticationStrategy2 instanceof AuthenticationStrategyAdaptor) {
            this.proxyAuthHandler = ((AuthenticationStrategyAdaptor) authenticationStrategy2).getHandler();
        } else {
            this.proxyAuthHandler = null;
        }
        this.managedConn = null;
        this.execCount = 0;
        this.redirectCount = 0;
        this.targetAuthState = new AuthState();
        this.proxyAuthState = new AuthState();
        this.maxRedirects = this.params.getIntParameter(ClientPNames.MAX_REDIRECTS, 100);
    }

    private RequestWrapper wrapRequest(HttpRequest httpRequest) throws ProtocolException {
        if (httpRequest instanceof HttpEntityEnclosingRequest) {
            return new EntityEnclosingRequestWrapper((HttpEntityEnclosingRequest) httpRequest);
        }
        return new RequestWrapper(httpRequest);
    }

    /* access modifiers changed from: protected */
    public void rewriteRequestURI(RequestWrapper requestWrapper, HttpRoute httpRoute) throws ProtocolException {
        URI uri;
        try {
            URI uri2 = requestWrapper.getURI();
            if (httpRoute.getProxyHost() == null || httpRoute.isTunnelled()) {
                if (uri2.isAbsolute()) {
                    uri = URIUtils.rewriteURI(uri2, null, true);
                } else {
                    uri = URIUtils.rewriteURI(uri2);
                }
            } else if (!uri2.isAbsolute()) {
                uri = URIUtils.rewriteURI(uri2, httpRoute.getTargetHost(), true);
            } else {
                uri = URIUtils.rewriteURI(uri2);
            }
            requestWrapper.setURI(uri);
        } catch (URISyntaxException e) {
            StringBuilder sb = new StringBuilder();
            sb.append("Invalid URI: ");
            sb.append(requestWrapper.getRequestLine().getUri());
            throw new ProtocolException(sb.toString(), e);
        }
    }

    public HttpResponse execute(HttpHost httpHost, HttpRequest httpRequest, HttpContext httpContext) throws HttpException, IOException {
        String str;
        HttpHost httpHost2;
        httpContext.setAttribute("http.auth.target-scope", this.targetAuthState);
        httpContext.setAttribute("http.auth.proxy-scope", this.proxyAuthState);
        RequestWrapper wrapRequest = wrapRequest(httpRequest);
        wrapRequest.setParams(this.params);
        HttpRoute determineRoute = determineRoute(httpHost, wrapRequest, httpContext);
        this.virtualHost = (HttpHost) wrapRequest.getParams().getParameter(ClientPNames.VIRTUAL_HOST);
        HttpHost httpHost3 = this.virtualHost;
        if (httpHost3 != null && httpHost3.getPort() == -1) {
            if (httpHost != null) {
                httpHost2 = httpHost;
            } else {
                httpHost2 = determineRoute.getTargetHost();
            }
            int port = httpHost2.getPort();
            if (port != -1) {
                this.virtualHost = new HttpHost(this.virtualHost.getHostName(), port, this.virtualHost.getSchemeName());
            }
        }
        RoutedRequest routedRequest = new RoutedRequest(wrapRequest, determineRoute);
        HttpResponse httpResponse = null;
        boolean z = false;
        HttpHost httpHost4 = httpHost;
        boolean z2 = false;
        while (!z) {
            try {
                RequestWrapper request = routedRequest.getRequest();
                HttpRoute route = routedRequest.getRoute();
                Object attribute = httpContext.getAttribute("http.user-token");
                if (this.managedConn == null) {
                    ClientConnectionRequest requestConnection = this.connManager.requestConnection(route, attribute);
                    if (httpRequest instanceof AbortableHttpRequest) {
                        ((AbortableHttpRequest) httpRequest).setConnectionRequest(requestConnection);
                    }
                    this.managedConn = requestConnection.getConnection(HttpClientParams.getConnectionManagerTimeout(this.params), TimeUnit.MILLISECONDS);
                    if (HttpConnectionParams.isStaleCheckingEnabled(this.params) && this.managedConn.isOpen()) {
                        this.log.debug("Stale connection check");
                        if (this.managedConn.isStale()) {
                            this.log.debug("Stale connection detected");
                            this.managedConn.close();
                        }
                    }
                }
                if (httpRequest instanceof AbortableHttpRequest) {
                    ((AbortableHttpRequest) httpRequest).setReleaseTrigger(this.managedConn);
                }
                try {
                    tryConnect(routedRequest, httpContext);
                    String userInfo = request.getURI().getUserInfo();
                    if (userInfo != null) {
                        this.targetAuthState.update(new BasicScheme(), new UsernamePasswordCredentials(userInfo));
                    }
                    if (this.virtualHost != null) {
                        httpHost4 = this.virtualHost;
                    } else {
                        URI uri = request.getURI();
                        if (uri.isAbsolute()) {
                            httpHost4 = URIUtils.extractHost(uri);
                        }
                    }
                    if (httpHost4 == null) {
                        httpHost4 = route.getTargetHost();
                    }
                    request.resetHeaders();
                    rewriteRequestURI(request, route);
                    httpContext.setAttribute("http.target_host", httpHost4);
                    httpContext.setAttribute("http.route", route);
                    httpContext.setAttribute("http.connection", this.managedConn);
                    this.requestExec.preProcess(request, this.httpProcessor, httpContext);
                    httpResponse = tryExecute(routedRequest, httpContext);
                    if (httpResponse != null) {
                        httpResponse.setParams(this.params);
                        this.requestExec.postProcess(httpResponse, this.httpProcessor, httpContext);
                        z2 = this.reuseStrategy.keepAlive(httpResponse, httpContext);
                        if (z2) {
                            long keepAliveDuration = this.keepAliveStrategy.getKeepAliveDuration(httpResponse, httpContext);
                            if (this.log.isDebugEnabled()) {
                                if (keepAliveDuration > 0) {
                                    StringBuilder sb = new StringBuilder();
                                    sb.append("for ");
                                    sb.append(keepAliveDuration);
                                    sb.append(OAuth.SCOPE_DELIMITER);
                                    sb.append(TimeUnit.MILLISECONDS);
                                    str = sb.toString();
                                } else {
                                    str = "indefinitely";
                                }
                                Log log2 = this.log;
                                StringBuilder sb2 = new StringBuilder();
                                sb2.append("Connection can be kept alive ");
                                sb2.append(str);
                                log2.debug(sb2.toString());
                            }
                            this.managedConn.setIdleDuration(keepAliveDuration, TimeUnit.MILLISECONDS);
                        }
                        RoutedRequest handleResponse = handleResponse(routedRequest, httpResponse, httpContext);
                        if (handleResponse == null) {
                            z = true;
                        } else {
                            if (z2) {
                                EntityUtils.consume(httpResponse.getEntity());
                                this.managedConn.markReusable();
                            } else {
                                this.managedConn.close();
                                if (this.proxyAuthState.getState().compareTo(AuthProtocolState.CHALLENGED) > 0 && this.proxyAuthState.getAuthScheme() != null && this.proxyAuthState.getAuthScheme().isConnectionBased()) {
                                    this.log.debug("Resetting proxy auth state");
                                    this.proxyAuthState.reset();
                                }
                                if (this.targetAuthState.getState().compareTo(AuthProtocolState.CHALLENGED) > 0 && this.targetAuthState.getAuthScheme() != null && this.targetAuthState.getAuthScheme().isConnectionBased()) {
                                    this.log.debug("Resetting target auth state");
                                    this.targetAuthState.reset();
                                }
                            }
                            if (!handleResponse.getRoute().equals(routedRequest.getRoute())) {
                                releaseConnection();
                            }
                            routedRequest = handleResponse;
                        }
                        if (this.managedConn != null) {
                            if (attribute == null) {
                                attribute = this.userTokenHandler.getUserToken(httpContext);
                                httpContext.setAttribute("http.user-token", attribute);
                            }
                            if (attribute != null) {
                                this.managedConn.setState(attribute);
                            }
                        }
                    }
                } catch (TunnelRefusedException e) {
                    if (this.log.isDebugEnabled()) {
                        this.log.debug(e.getMessage());
                    }
                    httpResponse = e.getResponse();
                }
            } catch (InterruptedException unused) {
                Thread.currentThread().interrupt();
                throw new InterruptedIOException();
            } catch (ConnectionShutdownException e2) {
                InterruptedIOException interruptedIOException = new InterruptedIOException("Connection has been shut down");
                interruptedIOException.initCause(e2);
                throw interruptedIOException;
            } catch (HttpException e3) {
                abortConnection();
                throw e3;
            } catch (IOException e4) {
                abortConnection();
                throw e4;
            } catch (RuntimeException e5) {
                abortConnection();
                throw e5;
            }
        }
        if (!(httpResponse == null || httpResponse.getEntity() == null)) {
            if (httpResponse.getEntity().isStreaming()) {
                httpResponse.setEntity(new BasicManagedEntity(httpResponse.getEntity(), this.managedConn, z2));
                return httpResponse;
            }
        }
        if (z2) {
            this.managedConn.markReusable();
        }
        releaseConnection();
        return httpResponse;
    }

    private void tryConnect(RoutedRequest routedRequest, HttpContext httpContext) throws HttpException, IOException {
        HttpRoute route = routedRequest.getRoute();
        RequestWrapper request = routedRequest.getRequest();
        int i = 0;
        while (true) {
            httpContext.setAttribute("http.request", request);
            i++;
            try {
                if (!this.managedConn.isOpen()) {
                    this.managedConn.open(route, httpContext, this.params);
                } else {
                    this.managedConn.setSocketTimeout(HttpConnectionParams.getSoTimeout(this.params));
                }
                establishRoute(route, httpContext);
                return;
            } catch (IOException e) {
                try {
                    this.managedConn.close();
                } catch (IOException unused) {
                }
                if (!this.retryHandler.retryRequest(e, i, httpContext)) {
                    throw e;
                } else if (this.log.isInfoEnabled()) {
                    Log log2 = this.log;
                    StringBuilder sb = new StringBuilder();
                    sb.append("I/O exception (");
                    sb.append(e.getClass().getName());
                    sb.append(") caught when connecting to ");
                    sb.append(route);
                    sb.append(": ");
                    sb.append(e.getMessage());
                    log2.info(sb.toString());
                    if (this.log.isDebugEnabled()) {
                        this.log.debug(e.getMessage(), e);
                    }
                    Log log3 = this.log;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("Retrying connect to ");
                    sb2.append(route);
                    log3.info(sb2.toString());
                }
            }
        }
    }

    private HttpResponse tryExecute(RoutedRequest routedRequest, HttpContext httpContext) throws HttpException, IOException {
        RequestWrapper request = routedRequest.getRequest();
        HttpRoute route = routedRequest.getRoute();
        IOException e = null;
        while (true) {
            this.execCount++;
            request.incrementExecCount();
            if (!request.isRepeatable()) {
                this.log.debug("Cannot retry non-repeatable request");
                if (e != null) {
                    throw new NonRepeatableRequestException("Cannot retry request with a non-repeatable request entity.  The cause lists the reason the original request failed.", e);
                }
                throw new NonRepeatableRequestException("Cannot retry request with a non-repeatable request entity.");
            }
            try {
                if (!this.managedConn.isOpen()) {
                    if (!route.isTunnelled()) {
                        this.log.debug("Reopening the direct connection.");
                        this.managedConn.open(route, httpContext, this.params);
                    } else {
                        this.log.debug("Proxied connection. Need to start over.");
                        return null;
                    }
                }
                if (this.log.isDebugEnabled()) {
                    Log log2 = this.log;
                    StringBuilder sb = new StringBuilder();
                    sb.append("Attempt ");
                    sb.append(this.execCount);
                    sb.append(" to execute request");
                    log2.debug(sb.toString());
                }
                return this.requestExec.execute(request, this.managedConn, httpContext);
            } catch (IOException e2) {
                e = e2;
                this.log.debug("Closing the connection.");
                try {
                    this.managedConn.close();
                } catch (IOException unused) {
                }
                if (this.retryHandler.retryRequest(e, request.getExecCount(), httpContext)) {
                    if (this.log.isInfoEnabled()) {
                        Log log3 = this.log;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("I/O exception (");
                        sb2.append(e.getClass().getName());
                        sb2.append(") caught when processing request to ");
                        sb2.append(route);
                        sb2.append(": ");
                        sb2.append(e.getMessage());
                        log3.info(sb2.toString());
                    }
                    if (this.log.isDebugEnabled()) {
                        this.log.debug(e.getMessage(), e);
                    }
                    if (this.log.isInfoEnabled()) {
                        Log log4 = this.log;
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append("Retrying request to ");
                        sb3.append(route);
                        log4.info(sb3.toString());
                    }
                } else if (e instanceof NoHttpResponseException) {
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append(route.getTargetHost().toHostString());
                    sb4.append(" failed to respond");
                    NoHttpResponseException noHttpResponseException = new NoHttpResponseException(sb4.toString());
                    noHttpResponseException.setStackTrace(e.getStackTrace());
                    throw noHttpResponseException;
                } else {
                    throw e;
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void releaseConnection() {
        try {
            this.managedConn.releaseConnection();
        } catch (IOException e) {
            this.log.debug("IOException releasing connection", e);
        }
        this.managedConn = null;
    }

    /* access modifiers changed from: protected */
    public HttpRoute determineRoute(HttpHost httpHost, HttpRequest httpRequest, HttpContext httpContext) throws HttpException {
        HttpRoutePlanner httpRoutePlanner = this.routePlanner;
        if (httpHost == null) {
            httpHost = (HttpHost) httpRequest.getParams().getParameter(ClientPNames.DEFAULT_HOST);
        }
        return httpRoutePlanner.determineRoute(httpHost, httpRequest, httpContext);
    }

    /* access modifiers changed from: protected */
    public void establishRoute(HttpRoute httpRoute, HttpContext httpContext) throws HttpException, IOException {
        int nextStep;
        BasicRouteDirector basicRouteDirector = new BasicRouteDirector();
        do {
            HttpRoute route = this.managedConn.getRoute();
            nextStep = basicRouteDirector.nextStep(httpRoute, route);
            switch (nextStep) {
                case -1:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unable to establish route: planned = ");
                    sb.append(httpRoute);
                    sb.append("; current = ");
                    sb.append(route);
                    throw new HttpException(sb.toString());
                case 0:
                    break;
                case 1:
                case 2:
                    this.managedConn.open(httpRoute, httpContext, this.params);
                    continue;
                case 3:
                    boolean createTunnelToTarget = createTunnelToTarget(httpRoute, httpContext);
                    this.log.debug("Tunnel to target created.");
                    this.managedConn.tunnelTarget(createTunnelToTarget, this.params);
                    continue;
                case 4:
                    int hopCount = route.getHopCount() - 1;
                    boolean createTunnelToProxy = createTunnelToProxy(httpRoute, hopCount, httpContext);
                    this.log.debug("Tunnel to proxy created.");
                    this.managedConn.tunnelProxy(httpRoute.getHopTarget(hopCount), createTunnelToProxy, this.params);
                    continue;
                case 5:
                    this.managedConn.layerProtocol(httpContext, this.params);
                    continue;
                default:
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("Unknown step indicator ");
                    sb2.append(nextStep);
                    sb2.append(" from RouteDirector.");
                    throw new IllegalStateException(sb2.toString());
            }
        } while (nextStep > 0);
    }

    /* access modifiers changed from: protected */
    public boolean createTunnelToTarget(HttpRoute httpRoute, HttpContext httpContext) throws HttpException, IOException {
        HttpResponse execute;
        HttpHost proxyHost = httpRoute.getProxyHost();
        HttpHost targetHost = httpRoute.getTargetHost();
        while (true) {
            if (!this.managedConn.isOpen()) {
                this.managedConn.open(httpRoute, httpContext, this.params);
            }
            HttpRequest createConnectRequest = createConnectRequest(httpRoute, httpContext);
            createConnectRequest.setParams(this.params);
            httpContext.setAttribute("http.target_host", targetHost);
            httpContext.setAttribute("http.route", httpRoute);
            httpContext.setAttribute(ExecutionContext.HTTP_PROXY_HOST, proxyHost);
            httpContext.setAttribute("http.connection", this.managedConn);
            httpContext.setAttribute("http.request", createConnectRequest);
            this.requestExec.preProcess(createConnectRequest, this.httpProcessor, httpContext);
            execute = this.requestExec.execute(createConnectRequest, this.managedConn, httpContext);
            execute.setParams(this.params);
            this.requestExec.postProcess(execute, this.httpProcessor, httpContext);
            if (execute.getStatusLine().getStatusCode() < 200) {
                StringBuilder sb = new StringBuilder();
                sb.append("Unexpected response to CONNECT request: ");
                sb.append(execute.getStatusLine());
                throw new HttpException(sb.toString());
            } else if (HttpClientParams.isAuthenticating(this.params)) {
                if (!this.authenticator.isAuthenticationRequested(proxyHost, execute, this.proxyAuthStrategy, this.proxyAuthState, httpContext)) {
                    break;
                }
                if (!this.authenticator.authenticate(proxyHost, execute, this.proxyAuthStrategy, this.proxyAuthState, httpContext)) {
                    break;
                } else if (this.reuseStrategy.keepAlive(execute, httpContext)) {
                    this.log.debug("Connection kept alive");
                    EntityUtils.consume(execute.getEntity());
                } else {
                    this.managedConn.close();
                }
            }
        }
        if (execute.getStatusLine().getStatusCode() > 299) {
            HttpEntity entity = execute.getEntity();
            if (entity != null) {
                execute.setEntity(new BufferedHttpEntity(entity));
            }
            this.managedConn.close();
            StringBuilder sb2 = new StringBuilder();
            sb2.append("CONNECT refused by proxy: ");
            sb2.append(execute.getStatusLine());
            throw new TunnelRefusedException(sb2.toString(), execute);
        }
        this.managedConn.markReusable();
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean createTunnelToProxy(HttpRoute httpRoute, int i, HttpContext httpContext) throws HttpException, IOException {
        throw new HttpException("Proxy chains are not supported.");
    }

    /* access modifiers changed from: protected */
    public HttpRequest createConnectRequest(HttpRoute httpRoute, HttpContext httpContext) {
        HttpHost targetHost = httpRoute.getTargetHost();
        String hostName = targetHost.getHostName();
        int port = targetHost.getPort();
        if (port < 0) {
            port = this.connManager.getSchemeRegistry().getScheme(targetHost.getSchemeName()).getDefaultPort();
        }
        StringBuilder sb = new StringBuilder(hostName.length() + 6);
        sb.append(hostName);
        sb.append(':');
        sb.append(Integer.toString(port));
        return new BasicHttpRequest(HttpMethods.CONNECT, sb.toString(), HttpProtocolParams.getVersion(this.params));
    }

    /* access modifiers changed from: protected */
    public RoutedRequest handleResponse(RoutedRequest routedRequest, HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {
        HttpResponse httpResponse2 = httpResponse;
        HttpContext httpContext2 = httpContext;
        HttpRoute route = routedRequest.getRoute();
        RequestWrapper request = routedRequest.getRequest();
        HttpParams params2 = request.getParams();
        if (HttpClientParams.isAuthenticating(params2)) {
            HttpHost httpHost = (HttpHost) httpContext2.getAttribute("http.target_host");
            if (httpHost == null) {
                httpHost = route.getTargetHost();
            }
            HttpHost httpHost2 = httpHost.getPort() < 0 ? new HttpHost(httpHost.getHostName(), this.connManager.getSchemeRegistry().getScheme(httpHost).getDefaultPort(), httpHost.getSchemeName()) : httpHost;
            boolean isAuthenticationRequested = this.authenticator.isAuthenticationRequested(httpHost2, httpResponse, this.targetAuthStrategy, this.targetAuthState, httpContext);
            HttpHost proxyHost = route.getProxyHost();
            HttpHost targetHost = proxyHost == null ? route.getTargetHost() : proxyHost;
            boolean isAuthenticationRequested2 = this.authenticator.isAuthenticationRequested(targetHost, httpResponse, this.proxyAuthStrategy, this.proxyAuthState, httpContext);
            if (isAuthenticationRequested) {
                if (this.authenticator.authenticate(httpHost2, httpResponse, this.targetAuthStrategy, this.targetAuthState, httpContext)) {
                    return routedRequest;
                }
            }
            if (isAuthenticationRequested2) {
                if (this.authenticator.authenticate(targetHost, httpResponse, this.proxyAuthStrategy, this.proxyAuthState, httpContext)) {
                    return routedRequest;
                }
            }
        }
        if (!HttpClientParams.isRedirecting(params2) || !this.redirectStrategy.isRedirected(request, httpResponse2, httpContext2)) {
            return null;
        }
        int i = this.redirectCount;
        if (i < this.maxRedirects) {
            this.redirectCount = i + 1;
            this.virtualHost = null;
            HttpUriRequest redirect = this.redirectStrategy.getRedirect(request, httpResponse2, httpContext2);
            redirect.setHeaders(request.getOriginal().getAllHeaders());
            URI uri = redirect.getURI();
            HttpHost extractHost = URIUtils.extractHost(uri);
            if (extractHost != null) {
                if (!route.getTargetHost().equals(extractHost)) {
                    this.log.debug("Resetting target auth state");
                    this.targetAuthState.reset();
                    AuthScheme authScheme = this.proxyAuthState.getAuthScheme();
                    if (authScheme != null && authScheme.isConnectionBased()) {
                        this.log.debug("Resetting proxy auth state");
                        this.proxyAuthState.reset();
                    }
                }
                RequestWrapper wrapRequest = wrapRequest(redirect);
                wrapRequest.setParams(params2);
                HttpRoute determineRoute = determineRoute(extractHost, wrapRequest, httpContext2);
                RoutedRequest routedRequest2 = new RoutedRequest(wrapRequest, determineRoute);
                if (this.log.isDebugEnabled()) {
                    Log log2 = this.log;
                    StringBuilder sb = new StringBuilder();
                    sb.append("Redirecting to '");
                    sb.append(uri);
                    sb.append("' via ");
                    sb.append(determineRoute);
                    log2.debug(sb.toString());
                }
                return routedRequest2;
            }
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Redirect URI does not specify a valid host name: ");
            sb2.append(uri);
            throw new ProtocolException(sb2.toString());
        }
        StringBuilder sb3 = new StringBuilder();
        sb3.append("Maximum redirects (");
        sb3.append(this.maxRedirects);
        sb3.append(") exceeded");
        throw new RedirectException(sb3.toString());
    }

    private void abortConnection() {
        ManagedClientConnection managedClientConnection = this.managedConn;
        if (managedClientConnection != null) {
            this.managedConn = null;
            try {
                managedClientConnection.abortConnection();
            } catch (IOException e) {
                if (this.log.isDebugEnabled()) {
                    this.log.debug(e.getMessage(), e);
                }
            }
            try {
                managedClientConnection.releaseConnection();
            } catch (IOException e2) {
                this.log.debug("Error releasing connection", e2);
            }
        }
    }
}
