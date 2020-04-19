package org.apache.http.impl.execchain;

import com.google.api.client.http.HttpMethods;
import java.io.IOException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.HttpClientConnection;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.auth.AuthState;
import org.apache.http.client.AuthenticationStrategy;
import org.apache.http.client.UserTokenHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.routing.BasicRouteDirector;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.routing.HttpRouteDirector;
import org.apache.http.conn.routing.RouteTracker;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.auth.HttpAuthenticator;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpRequestExecutor;
import org.apache.http.protocol.ImmutableHttpProcessor;
import org.apache.http.protocol.RequestTargetHost;
import org.apache.http.util.Args;
import org.apache.http.util.EntityUtils;

@Contract(threading = ThreadingBehavior.IMMUTABLE_CONDITIONAL)
public class MainClientExec implements ClientExecChain {
    private final HttpAuthenticator authenticator;
    private final HttpClientConnectionManager connManager;
    private final ConnectionKeepAliveStrategy keepAliveStrategy;
    private final Log log;
    private final AuthenticationStrategy proxyAuthStrategy;
    private final HttpProcessor proxyHttpProcessor;
    private final HttpRequestExecutor requestExecutor;
    private final ConnectionReuseStrategy reuseStrategy;
    private final HttpRouteDirector routeDirector;
    private final AuthenticationStrategy targetAuthStrategy;
    private final UserTokenHandler userTokenHandler;

    public MainClientExec(HttpRequestExecutor httpRequestExecutor, HttpClientConnectionManager httpClientConnectionManager, ConnectionReuseStrategy connectionReuseStrategy, ConnectionKeepAliveStrategy connectionKeepAliveStrategy, HttpProcessor httpProcessor, AuthenticationStrategy authenticationStrategy, AuthenticationStrategy authenticationStrategy2, UserTokenHandler userTokenHandler2) {
        this.log = LogFactory.getLog(getClass());
        Args.notNull(httpRequestExecutor, "HTTP request executor");
        Args.notNull(httpClientConnectionManager, "Client connection manager");
        Args.notNull(connectionReuseStrategy, "Connection reuse strategy");
        Args.notNull(connectionKeepAliveStrategy, "Connection keep alive strategy");
        Args.notNull(httpProcessor, "Proxy HTTP processor");
        Args.notNull(authenticationStrategy, "Target authentication strategy");
        Args.notNull(authenticationStrategy2, "Proxy authentication strategy");
        Args.notNull(userTokenHandler2, "User token handler");
        this.authenticator = new HttpAuthenticator();
        this.routeDirector = new BasicRouteDirector();
        this.requestExecutor = httpRequestExecutor;
        this.connManager = httpClientConnectionManager;
        this.reuseStrategy = connectionReuseStrategy;
        this.keepAliveStrategy = connectionKeepAliveStrategy;
        this.proxyHttpProcessor = httpProcessor;
        this.targetAuthStrategy = authenticationStrategy;
        this.proxyAuthStrategy = authenticationStrategy2;
        this.userTokenHandler = userTokenHandler2;
    }

    public MainClientExec(HttpRequestExecutor httpRequestExecutor, HttpClientConnectionManager httpClientConnectionManager, ConnectionReuseStrategy connectionReuseStrategy, ConnectionKeepAliveStrategy connectionKeepAliveStrategy, AuthenticationStrategy authenticationStrategy, AuthenticationStrategy authenticationStrategy2, UserTokenHandler userTokenHandler2) {
        ImmutableHttpProcessor immutableHttpProcessor = new ImmutableHttpProcessor(new RequestTargetHost());
        this(httpRequestExecutor, httpClientConnectionManager, connectionReuseStrategy, connectionKeepAliveStrategy, immutableHttpProcessor, authenticationStrategy, authenticationStrategy2, userTokenHandler2);
    }

    /* JADX WARNING: type inference failed for: r2v2, types: [java.lang.Throwable] */
    /* JADX WARNING: type inference failed for: r1v10, types: [java.lang.Throwable] */
    /* JADX WARNING: Code restructure failed: missing block: B:152:0x030a, code lost:
        r0 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:153:0x030b, code lost:
        r8 = r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:157:0x0311, code lost:
        r0 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:158:0x0312, code lost:
        r8 = r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:162:0x0318, code lost:
        r0 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:163:0x0319, code lost:
        r8 = r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:167:0x031f, code lost:
        r2 = new java.io.InterruptedIOException("Connection has been shut down");
        r2.initCause(r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:168:0x0329, code lost:
        throw r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x00b7, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x00b8, code lost:
        r1 = r0;
        r8 = r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x00bc, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x00bd, code lost:
        r1 = r0;
        r8 = r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x00c1, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x00c2, code lost:
        r1 = r0;
        r8 = r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:0x00c6, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x00c7, code lost:
        r1 = r0;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x00c6 A[Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x00c1, IOException -> 0x00bc, RuntimeException -> 0x00b7 }, ExcHandler: ConnectionShutdownException (r0v12 'e' org.apache.http.impl.conn.ConnectionShutdownException A[CUSTOM_DECLARE, Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x00c1, IOException -> 0x00bc, RuntimeException -> 0x00b7 }]), Splitter:B:34:0x00b3] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public org.apache.http.client.methods.CloseableHttpResponse execute(org.apache.http.conn.routing.HttpRoute r22, org.apache.http.client.methods.HttpRequestWrapper r23, org.apache.http.client.protocol.HttpClientContext r24, org.apache.http.client.methods.HttpExecutionAware r25) throws java.io.IOException, org.apache.http.HttpException {
        /*
            r21 = this;
            r7 = r21
            r8 = r22
            r9 = r23
            r10 = r24
            r11 = r25
            java.lang.String r1 = "HTTP route"
            org.apache.http.util.Args.notNull(r8, r1)
            java.lang.String r1 = "HTTP request"
            org.apache.http.util.Args.notNull(r9, r1)
            java.lang.String r1 = "HTTP context"
            org.apache.http.util.Args.notNull(r10, r1)
            org.apache.http.auth.AuthState r1 = r24.getTargetAuthState()
            if (r1 != 0) goto L_0x002b
            org.apache.http.auth.AuthState r1 = new org.apache.http.auth.AuthState
            r1.<init>()
            java.lang.String r2 = "http.auth.target-scope"
            r10.setAttribute(r2, r1)
            r12 = r1
            goto L_0x002c
        L_0x002b:
            r12 = r1
        L_0x002c:
            org.apache.http.auth.AuthState r1 = r24.getProxyAuthState()
            if (r1 != 0) goto L_0x003e
            org.apache.http.auth.AuthState r1 = new org.apache.http.auth.AuthState
            r1.<init>()
            java.lang.String r2 = "http.auth.proxy-scope"
            r10.setAttribute(r2, r1)
            r13 = r1
            goto L_0x003f
        L_0x003e:
            r13 = r1
        L_0x003f:
            boolean r1 = r9 instanceof org.apache.http.HttpEntityEnclosingRequest
            if (r1 == 0) goto L_0x0049
            r1 = r9
            org.apache.http.HttpEntityEnclosingRequest r1 = (org.apache.http.HttpEntityEnclosingRequest) r1
            org.apache.http.impl.execchain.RequestEntityProxy.enhance(r1)
        L_0x0049:
            java.lang.Object r14 = r24.getUserToken()
            org.apache.http.conn.HttpClientConnectionManager r1 = r7.connManager
            org.apache.http.conn.ConnectionRequest r1 = r1.requestConnection(r8, r14)
            if (r11 == 0) goto L_0x006a
            boolean r2 = r25.isAborted()
            if (r2 != 0) goto L_0x005f
            r11.setCancellable(r1)
            goto L_0x006a
        L_0x005f:
            r1.cancel()
            org.apache.http.impl.execchain.RequestAbortedException r1 = new org.apache.http.impl.execchain.RequestAbortedException
            java.lang.String r2 = "Request aborted"
            r1.<init>(r2)
            throw r1
        L_0x006a:
            org.apache.http.client.config.RequestConfig r15 = r24.getRequestConfig()
            int r2 = r15.getConnectionRequestTimeout()     // Catch:{ InterruptedException -> 0x033c, ExecutionException -> 0x032a }
            r16 = 0
            if (r2 <= 0) goto L_0x0078
            long r2 = (long) r2     // Catch:{ InterruptedException -> 0x033c, ExecutionException -> 0x032a }
            goto L_0x007a
        L_0x0078:
            r2 = r16
        L_0x007a:
            java.util.concurrent.TimeUnit r4 = java.util.concurrent.TimeUnit.MILLISECONDS     // Catch:{ InterruptedException -> 0x033c, ExecutionException -> 0x032a }
            org.apache.http.HttpClientConnection r6 = r1.get(r2, r4)     // Catch:{ InterruptedException -> 0x033c, ExecutionException -> 0x032a }
            java.lang.String r1 = "http.connection"
            r10.setAttribute(r1, r6)
            boolean r1 = r15.isStaleConnectionCheckEnabled()
            if (r1 == 0) goto L_0x00a8
            boolean r1 = r6.isOpen()
            if (r1 == 0) goto L_0x00a8
            org.apache.commons.logging.Log r1 = r7.log
            java.lang.String r2 = "Stale connection check"
            r1.debug(r2)
            boolean r1 = r6.isStale()
            if (r1 == 0) goto L_0x00a8
            org.apache.commons.logging.Log r1 = r7.log
            java.lang.String r2 = "Stale connection detected"
            r1.debug(r2)
            r6.close()
        L_0x00a8:
            org.apache.http.impl.execchain.ConnectionHolder r5 = new org.apache.http.impl.execchain.ConnectionHolder
            org.apache.commons.logging.Log r1 = r7.log
            org.apache.http.conn.HttpClientConnectionManager r2 = r7.connManager
            r5.<init>(r1, r2, r6)
            if (r11 == 0) goto L_0x00ca
            r11.setCancellable(r5)     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x00c1, IOException -> 0x00bc, RuntimeException -> 0x00b7 }
            goto L_0x00ca
        L_0x00b7:
            r0 = move-exception
            r1 = r0
            r8 = r5
            goto L_0x030d
        L_0x00bc:
            r0 = move-exception
            r1 = r0
            r8 = r5
            goto L_0x0314
        L_0x00c1:
            r0 = move-exception
            r1 = r0
            r8 = r5
            goto L_0x031b
        L_0x00c6:
            r0 = move-exception
            r1 = r0
            goto L_0x031f
        L_0x00ca:
            r4 = 1
            r3 = 1
        L_0x00cc:
            if (r3 <= r4) goto L_0x00dd
            boolean r1 = org.apache.http.impl.execchain.RequestEntityProxy.isRepeatable(r23)     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x00c1, IOException -> 0x00bc, RuntimeException -> 0x00b7 }
            if (r1 == 0) goto L_0x00d5
            goto L_0x00dd
        L_0x00d5:
            org.apache.http.client.NonRepeatableRequestException r1 = new org.apache.http.client.NonRepeatableRequestException     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x00c1, IOException -> 0x00bc, RuntimeException -> 0x00b7 }
            java.lang.String r2 = "Cannot retry request with a non-repeatable request entity."
            r1.<init>(r2)     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x00c1, IOException -> 0x00bc, RuntimeException -> 0x00b7 }
            throw r1     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x00c1, IOException -> 0x00bc, RuntimeException -> 0x00b7 }
        L_0x00dd:
            if (r11 == 0) goto L_0x00ee
            boolean r1 = r25.isAborted()     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x00c1, IOException -> 0x00bc, RuntimeException -> 0x00b7 }
            if (r1 != 0) goto L_0x00e6
            goto L_0x00ee
        L_0x00e6:
            org.apache.http.impl.execchain.RequestAbortedException r1 = new org.apache.http.impl.execchain.RequestAbortedException     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x00c1, IOException -> 0x00bc, RuntimeException -> 0x00b7 }
            java.lang.String r2 = "Request aborted"
            r1.<init>(r2)     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x00c1, IOException -> 0x00bc, RuntimeException -> 0x00b7 }
            throw r1     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x00c1, IOException -> 0x00bc, RuntimeException -> 0x00b7 }
        L_0x00ee:
            boolean r1 = r6.isOpen()     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0318, IOException -> 0x0311, RuntimeException -> 0x030a }
            if (r1 != 0) goto L_0x013a
            org.apache.commons.logging.Log r1 = r7.log     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0318, IOException -> 0x0311, RuntimeException -> 0x030a }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0318, IOException -> 0x0311, RuntimeException -> 0x030a }
            r2.<init>()     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0318, IOException -> 0x0311, RuntimeException -> 0x030a }
            java.lang.String r4 = "Opening connection "
            r2.append(r4)     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0318, IOException -> 0x0311, RuntimeException -> 0x030a }
            r2.append(r8)     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0318, IOException -> 0x0311, RuntimeException -> 0x030a }
            java.lang.String r2 = r2.toString()     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0318, IOException -> 0x0311, RuntimeException -> 0x030a }
            r1.debug(r2)     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0318, IOException -> 0x0311, RuntimeException -> 0x030a }
            r1 = r21
            r2 = r13
            r19 = r3
            r3 = r6
            r18 = 1
            r4 = r22
            r8 = r5
            r5 = r23
            r20 = r14
            r14 = r6
            r6 = r24
            r1.establishRoute(r2, r3, r4, r5, r6)     // Catch:{ TunnelRefusedException -> 0x0120 }
            goto L_0x0142
        L_0x0120:
            r0 = move-exception
            r1 = r0
            org.apache.commons.logging.Log r2 = r7.log     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            boolean r2 = r2.isDebugEnabled()     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            if (r2 == 0) goto L_0x0133
            org.apache.commons.logging.Log r2 = r7.log     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            java.lang.String r3 = r1.getMessage()     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            r2.debug(r3)     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
        L_0x0133:
            org.apache.http.HttpResponse r1 = r1.getResponse()     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            r11 = r1
            goto L_0x02d2
        L_0x013a:
            r19 = r3
            r8 = r5
            r20 = r14
            r18 = 1
            r14 = r6
        L_0x0142:
            int r1 = r15.getSocketTimeout()     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            if (r1 < 0) goto L_0x014b
            r14.setSocketTimeout(r1)     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
        L_0x014b:
            if (r11 == 0) goto L_0x015c
            boolean r1 = r25.isAborted()     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            if (r1 != 0) goto L_0x0154
            goto L_0x015c
        L_0x0154:
            org.apache.http.impl.execchain.RequestAbortedException r1 = new org.apache.http.impl.execchain.RequestAbortedException     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            java.lang.String r2 = "Request aborted"
            r1.<init>(r2)     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            throw r1     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
        L_0x015c:
            org.apache.commons.logging.Log r1 = r7.log     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            boolean r1 = r1.isDebugEnabled()     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            if (r1 == 0) goto L_0x017e
            org.apache.commons.logging.Log r1 = r7.log     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            r2.<init>()     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            java.lang.String r3 = "Executing request "
            r2.append(r3)     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            org.apache.http.RequestLine r3 = r23.getRequestLine()     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            r2.append(r3)     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            java.lang.String r2 = r2.toString()     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            r1.debug(r2)     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
        L_0x017e:
            java.lang.String r1 = "Authorization"
            boolean r1 = r9.containsHeader(r1)     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            if (r1 != 0) goto L_0x01ad
            org.apache.commons.logging.Log r1 = r7.log     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            boolean r1 = r1.isDebugEnabled()     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            if (r1 == 0) goto L_0x01a8
            org.apache.commons.logging.Log r1 = r7.log     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            r2.<init>()     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            java.lang.String r3 = "Target auth state: "
            r2.append(r3)     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            org.apache.http.auth.AuthProtocolState r3 = r12.getState()     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            r2.append(r3)     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            java.lang.String r2 = r2.toString()     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            r1.debug(r2)     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
        L_0x01a8:
            org.apache.http.impl.auth.HttpAuthenticator r1 = r7.authenticator     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            r1.generateAuthResponse(r9, r12, r10)     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
        L_0x01ad:
            java.lang.String r1 = "Proxy-Authorization"
            boolean r1 = r9.containsHeader(r1)     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            if (r1 != 0) goto L_0x01e2
            boolean r1 = r22.isTunnelled()     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            if (r1 != 0) goto L_0x01e2
            org.apache.commons.logging.Log r1 = r7.log     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            boolean r1 = r1.isDebugEnabled()     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            if (r1 == 0) goto L_0x01dd
            org.apache.commons.logging.Log r1 = r7.log     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            r2.<init>()     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            java.lang.String r3 = "Proxy auth state: "
            r2.append(r3)     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            org.apache.http.auth.AuthProtocolState r3 = r13.getState()     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            r2.append(r3)     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            java.lang.String r2 = r2.toString()     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            r1.debug(r2)     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
        L_0x01dd:
            org.apache.http.impl.auth.HttpAuthenticator r1 = r7.authenticator     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            r1.generateAuthResponse(r9, r13, r10)     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
        L_0x01e2:
            org.apache.http.protocol.HttpRequestExecutor r1 = r7.requestExecutor     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            org.apache.http.HttpResponse r6 = r1.execute(r9, r14, r10)     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            org.apache.http.ConnectionReuseStrategy r1 = r7.reuseStrategy     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            boolean r1 = r1.keepAlive(r6, r10)     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            if (r1 == 0) goto L_0x023f
            org.apache.http.conn.ConnectionKeepAliveStrategy r1 = r7.keepAliveStrategy     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            long r1 = r1.getKeepAliveDuration(r6, r10)     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            org.apache.commons.logging.Log r3 = r7.log     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            boolean r3 = r3.isDebugEnabled()     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            if (r3 == 0) goto L_0x0236
            int r3 = (r1 > r16 ? 1 : (r1 == r16 ? 0 : -1))
            if (r3 <= 0) goto L_0x021e
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            r3.<init>()     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            java.lang.String r4 = "for "
            r3.append(r4)     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            r3.append(r1)     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            java.lang.String r4 = " "
            r3.append(r4)     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            java.util.concurrent.TimeUnit r4 = java.util.concurrent.TimeUnit.MILLISECONDS     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            r3.append(r4)     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            java.lang.String r3 = r3.toString()     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            goto L_0x0220
        L_0x021e:
            java.lang.String r3 = "indefinitely"
        L_0x0220:
            org.apache.commons.logging.Log r4 = r7.log     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            r5.<init>()     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            java.lang.String r11 = "Connection can be kept alive "
            r5.append(r11)     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            r5.append(r3)     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            java.lang.String r3 = r5.toString()     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            r4.debug(r3)     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
        L_0x0236:
            java.util.concurrent.TimeUnit r3 = java.util.concurrent.TimeUnit.MILLISECONDS     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            r8.setValidFor(r1, r3)     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            r8.markReusable()     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            goto L_0x0242
        L_0x023f:
            r8.markNonReusable()     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
        L_0x0242:
            r1 = r21
            r2 = r12
            r3 = r13
            r4 = r22
            r5 = r6
            r11 = r6
            r6 = r24
            boolean r1 = r1.needAuthentication(r2, r3, r4, r5, r6)     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            if (r1 == 0) goto L_0x02d2
            org.apache.http.HttpEntity r1 = r11.getEntity()     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            boolean r2 = r8.isReusable()     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            if (r2 == 0) goto L_0x0260
            org.apache.http.util.EntityUtils.consume(r1)     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            goto L_0x02a7
        L_0x0260:
            r14.close()     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            org.apache.http.auth.AuthProtocolState r1 = r13.getState()     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            org.apache.http.auth.AuthProtocolState r2 = org.apache.http.auth.AuthProtocolState.SUCCESS     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            if (r1 != r2) goto L_0x0285
            org.apache.http.auth.AuthScheme r1 = r13.getAuthScheme()     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            if (r1 == 0) goto L_0x0285
            org.apache.http.auth.AuthScheme r1 = r13.getAuthScheme()     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            boolean r1 = r1.isConnectionBased()     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            if (r1 == 0) goto L_0x0285
            org.apache.commons.logging.Log r1 = r7.log     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            java.lang.String r2 = "Resetting proxy auth state"
            r1.debug(r2)     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            r13.reset()     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
        L_0x0285:
            org.apache.http.auth.AuthProtocolState r1 = r12.getState()     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            org.apache.http.auth.AuthProtocolState r2 = org.apache.http.auth.AuthProtocolState.SUCCESS     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            if (r1 != r2) goto L_0x02a7
            org.apache.http.auth.AuthScheme r1 = r12.getAuthScheme()     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            if (r1 == 0) goto L_0x02a7
            org.apache.http.auth.AuthScheme r1 = r12.getAuthScheme()     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            boolean r1 = r1.isConnectionBased()     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            if (r1 == 0) goto L_0x02a7
            org.apache.commons.logging.Log r1 = r7.log     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            java.lang.String r2 = "Resetting target auth state"
            r1.debug(r2)     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            r12.reset()     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
        L_0x02a7:
            org.apache.http.HttpRequest r1 = r23.getOriginal()     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            java.lang.String r2 = "Authorization"
            boolean r2 = r1.containsHeader(r2)     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            if (r2 != 0) goto L_0x02b8
            java.lang.String r2 = "Authorization"
            r9.removeHeaders(r2)     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
        L_0x02b8:
            java.lang.String r2 = "Proxy-Authorization"
            boolean r1 = r1.containsHeader(r2)     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            if (r1 != 0) goto L_0x02c5
            java.lang.String r1 = "Proxy-Authorization"
            r9.removeHeaders(r1)     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
        L_0x02c5:
            int r3 = r19 + 1
            r5 = r8
            r6 = r14
            r14 = r20
            r4 = 1
            r8 = r22
            r11 = r25
            goto L_0x00cc
        L_0x02d2:
            if (r20 != 0) goto L_0x02e0
            org.apache.http.client.UserTokenHandler r1 = r7.userTokenHandler     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            java.lang.Object r14 = r1.getUserToken(r10)     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            java.lang.String r1 = "http.user-token"
            r10.setAttribute(r1, r14)     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            goto L_0x02e2
        L_0x02e0:
            r14 = r20
        L_0x02e2:
            if (r14 == 0) goto L_0x02e7
            r8.setState(r14)     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
        L_0x02e7:
            org.apache.http.HttpEntity r1 = r11.getEntity()     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            if (r1 == 0) goto L_0x02fa
            boolean r1 = r1.isStreaming()     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            if (r1 != 0) goto L_0x02f4
            goto L_0x02fa
        L_0x02f4:
            org.apache.http.impl.execchain.HttpResponseProxy r1 = new org.apache.http.impl.execchain.HttpResponseProxy     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            r1.<init>(r11, r8)     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            return r1
        L_0x02fa:
            r8.releaseConnection()     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            org.apache.http.impl.execchain.HttpResponseProxy r1 = new org.apache.http.impl.execchain.HttpResponseProxy     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            r2 = 0
            r1.<init>(r11, r2)     // Catch:{ ConnectionShutdownException -> 0x00c6, HttpException -> 0x0308, IOException -> 0x0306, RuntimeException -> 0x0304 }
            return r1
        L_0x0304:
            r0 = move-exception
            goto L_0x030c
        L_0x0306:
            r0 = move-exception
            goto L_0x0313
        L_0x0308:
            r0 = move-exception
            goto L_0x031a
        L_0x030a:
            r0 = move-exception
            r8 = r5
        L_0x030c:
            r1 = r0
        L_0x030d:
            r8.abortConnection()
            throw r1
        L_0x0311:
            r0 = move-exception
            r8 = r5
        L_0x0313:
            r1 = r0
        L_0x0314:
            r8.abortConnection()
            throw r1
        L_0x0318:
            r0 = move-exception
            r8 = r5
        L_0x031a:
            r1 = r0
        L_0x031b:
            r8.abortConnection()
            throw r1
        L_0x031f:
            java.io.InterruptedIOException r2 = new java.io.InterruptedIOException
            java.lang.String r3 = "Connection has been shut down"
            r2.<init>(r3)
            r2.initCause(r1)
            throw r2
        L_0x032a:
            r0 = move-exception
            r1 = r0
            java.lang.Throwable r2 = r1.getCause()
            if (r2 != 0) goto L_0x0333
            goto L_0x0334
        L_0x0333:
            r1 = r2
        L_0x0334:
            org.apache.http.impl.execchain.RequestAbortedException r2 = new org.apache.http.impl.execchain.RequestAbortedException
            java.lang.String r3 = "Request execution failed"
            r2.<init>(r3, r1)
            throw r2
        L_0x033c:
            r0 = move-exception
            r1 = r0
            java.lang.Thread r2 = java.lang.Thread.currentThread()
            r2.interrupt()
            org.apache.http.impl.execchain.RequestAbortedException r2 = new org.apache.http.impl.execchain.RequestAbortedException
            java.lang.String r3 = "Request aborted"
            r2.<init>(r3, r1)
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.http.impl.execchain.MainClientExec.execute(org.apache.http.conn.routing.HttpRoute, org.apache.http.client.methods.HttpRequestWrapper, org.apache.http.client.protocol.HttpClientContext, org.apache.http.client.methods.HttpExecutionAware):org.apache.http.client.methods.CloseableHttpResponse");
    }

    /* access modifiers changed from: 0000 */
    public void establishRoute(AuthState authState, HttpClientConnection httpClientConnection, HttpRoute httpRoute, HttpRequest httpRequest, HttpClientContext httpClientContext) throws HttpException, IOException {
        int nextStep;
        int connectTimeout = httpClientContext.getRequestConfig().getConnectTimeout();
        RouteTracker routeTracker = new RouteTracker(httpRoute);
        do {
            HttpRoute route = routeTracker.toRoute();
            nextStep = this.routeDirector.nextStep(httpRoute, route);
            int i = 0;
            switch (nextStep) {
                case -1:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unable to establish route: planned = ");
                    sb.append(httpRoute);
                    sb.append("; current = ");
                    sb.append(route);
                    throw new HttpException(sb.toString());
                case 0:
                    this.connManager.routeComplete(httpClientConnection, httpRoute, httpClientContext);
                    continue;
                case 1:
                    HttpClientConnectionManager httpClientConnectionManager = this.connManager;
                    if (connectTimeout > 0) {
                        i = connectTimeout;
                    }
                    httpClientConnectionManager.connect(httpClientConnection, httpRoute, i, httpClientContext);
                    routeTracker.connectTarget(httpRoute.isSecure());
                    continue;
                case 2:
                    this.connManager.connect(httpClientConnection, httpRoute, connectTimeout > 0 ? connectTimeout : 0, httpClientContext);
                    routeTracker.connectProxy(httpRoute.getProxyHost(), false);
                    continue;
                case 3:
                    boolean createTunnelToTarget = createTunnelToTarget(authState, httpClientConnection, httpRoute, httpRequest, httpClientContext);
                    this.log.debug("Tunnel to target created.");
                    routeTracker.tunnelTarget(createTunnelToTarget);
                    continue;
                case 4:
                    int hopCount = route.getHopCount() - 1;
                    boolean createTunnelToProxy = createTunnelToProxy(httpRoute, hopCount, httpClientContext);
                    this.log.debug("Tunnel to proxy created.");
                    routeTracker.tunnelProxy(httpRoute.getHopTarget(hopCount), createTunnelToProxy);
                    continue;
                case 5:
                    this.connManager.upgrade(httpClientConnection, httpRoute, httpClientContext);
                    routeTracker.layerProtocol(httpRoute.isSecure());
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

    private boolean createTunnelToTarget(AuthState authState, HttpClientConnection httpClientConnection, HttpRoute httpRoute, HttpRequest httpRequest, HttpClientContext httpClientContext) throws HttpException, IOException {
        HttpResponse httpResponse;
        HttpRoute httpRoute2;
        HttpClientConnection httpClientConnection2 = httpClientConnection;
        HttpClientContext httpClientContext2 = httpClientContext;
        RequestConfig requestConfig = httpClientContext.getRequestConfig();
        int connectTimeout = requestConfig.getConnectTimeout();
        HttpHost targetHost = httpRoute.getTargetHost();
        HttpHost proxyHost = httpRoute.getProxyHost();
        BasicHttpRequest basicHttpRequest = new BasicHttpRequest(HttpMethods.CONNECT, targetHost.toHostString(), httpRequest.getProtocolVersion());
        this.requestExecutor.preProcess(basicHttpRequest, this.proxyHttpProcessor, httpClientContext2);
        HttpResponse httpResponse2 = null;
        while (true) {
            int i = 0;
            if (httpResponse2 == null) {
                if (!httpClientConnection.isOpen()) {
                    HttpClientConnectionManager httpClientConnectionManager = this.connManager;
                    if (connectTimeout > 0) {
                        httpRoute2 = httpRoute;
                        i = connectTimeout;
                    } else {
                        httpRoute2 = httpRoute;
                    }
                    httpClientConnectionManager.connect(httpClientConnection2, httpRoute2, i, httpClientContext2);
                } else {
                    HttpRoute httpRoute3 = httpRoute;
                }
                basicHttpRequest.removeHeaders("Proxy-Authorization");
                this.authenticator.generateAuthResponse(basicHttpRequest, authState, httpClientContext2);
                HttpResponse execute = this.requestExecutor.execute(basicHttpRequest, httpClientConnection2, httpClientContext2);
                if (execute.getStatusLine().getStatusCode() >= 200) {
                    if (requestConfig.isAuthenticationEnabled()) {
                        HttpResponse httpResponse3 = execute;
                        if (this.authenticator.isAuthenticationRequested(proxyHost, execute, this.proxyAuthStrategy, authState, httpClientContext)) {
                            if (this.authenticator.handleAuthChallenge(proxyHost, httpResponse3, this.proxyAuthStrategy, authState, httpClientContext)) {
                                HttpResponse httpResponse4 = httpResponse3;
                                if (this.reuseStrategy.keepAlive(httpResponse4, httpClientContext2)) {
                                    this.log.debug("Connection kept alive");
                                    EntityUtils.consume(httpResponse4.getEntity());
                                } else {
                                    httpClientConnection.close();
                                }
                                httpResponse2 = null;
                            } else {
                                httpResponse = httpResponse3;
                            }
                        } else {
                            httpResponse = httpResponse3;
                        }
                    } else {
                        httpResponse = execute;
                    }
                    httpResponse2 = httpResponse;
                } else {
                    HttpResponse httpResponse5 = execute;
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unexpected response to CONNECT request: ");
                    sb.append(httpResponse5.getStatusLine());
                    throw new HttpException(sb.toString());
                }
            } else if (httpResponse2.getStatusLine().getStatusCode() <= 299) {
                return false;
            } else {
                HttpEntity entity = httpResponse2.getEntity();
                if (entity != null) {
                    httpResponse2.setEntity(new BufferedHttpEntity(entity));
                }
                httpClientConnection.close();
                StringBuilder sb2 = new StringBuilder();
                sb2.append("CONNECT refused by proxy: ");
                sb2.append(httpResponse2.getStatusLine());
                throw new TunnelRefusedException(sb2.toString(), httpResponse2);
            }
        }
    }

    private boolean createTunnelToProxy(HttpRoute httpRoute, int i, HttpClientContext httpClientContext) throws HttpException {
        throw new HttpException("Proxy chains are not supported.");
    }

    private boolean needAuthentication(AuthState authState, AuthState authState2, HttpRoute httpRoute, HttpResponse httpResponse, HttpClientContext httpClientContext) {
        if (httpClientContext.getRequestConfig().isAuthenticationEnabled()) {
            HttpHost targetHost = httpClientContext.getTargetHost();
            if (targetHost == null) {
                targetHost = httpRoute.getTargetHost();
            }
            if (targetHost.getPort() < 0) {
                targetHost = new HttpHost(targetHost.getHostName(), httpRoute.getTargetHost().getPort(), targetHost.getSchemeName());
            }
            boolean isAuthenticationRequested = this.authenticator.isAuthenticationRequested(targetHost, httpResponse, this.targetAuthStrategy, authState, httpClientContext);
            HttpHost proxyHost = httpRoute.getProxyHost();
            if (proxyHost == null) {
                proxyHost = httpRoute.getTargetHost();
            }
            boolean isAuthenticationRequested2 = this.authenticator.isAuthenticationRequested(proxyHost, httpResponse, this.proxyAuthStrategy, authState2, httpClientContext);
            if (isAuthenticationRequested) {
                return this.authenticator.handleAuthChallenge(targetHost, httpResponse, this.targetAuthStrategy, authState, httpClientContext);
            } else if (isAuthenticationRequested2) {
                return this.authenticator.handleAuthChallenge(proxyHost, httpResponse, this.proxyAuthStrategy, authState2, httpClientContext);
            }
        }
        return false;
    }
}
