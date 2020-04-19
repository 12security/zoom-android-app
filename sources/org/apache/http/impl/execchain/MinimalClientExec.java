package org.apache.http.impl.execchain;

import java.net.URI;
import java.net.URISyntaxException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.ProtocolException;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.client.methods.HttpRequestWrapper;
import org.apache.http.client.protocol.RequestClientConnControl;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpRequestExecutor;
import org.apache.http.protocol.ImmutableHttpProcessor;
import org.apache.http.protocol.RequestContent;
import org.apache.http.protocol.RequestTargetHost;
import org.apache.http.protocol.RequestUserAgent;
import org.apache.http.util.Args;
import org.apache.http.util.VersionInfo;

@Contract(threading = ThreadingBehavior.IMMUTABLE_CONDITIONAL)
public class MinimalClientExec implements ClientExecChain {
    private final HttpClientConnectionManager connManager;
    private final HttpProcessor httpProcessor;
    private final ConnectionKeepAliveStrategy keepAliveStrategy;
    private final Log log = LogFactory.getLog(getClass());
    private final HttpRequestExecutor requestExecutor;
    private final ConnectionReuseStrategy reuseStrategy;

    public MinimalClientExec(HttpRequestExecutor httpRequestExecutor, HttpClientConnectionManager httpClientConnectionManager, ConnectionReuseStrategy connectionReuseStrategy, ConnectionKeepAliveStrategy connectionKeepAliveStrategy) {
        Args.notNull(httpRequestExecutor, "HTTP request executor");
        Args.notNull(httpClientConnectionManager, "Client connection manager");
        Args.notNull(connectionReuseStrategy, "Connection reuse strategy");
        Args.notNull(connectionKeepAliveStrategy, "Connection keep alive strategy");
        this.httpProcessor = new ImmutableHttpProcessor(new RequestContent(), new RequestTargetHost(), new RequestClientConnControl(), new RequestUserAgent(VersionInfo.getUserAgent("Apache-HttpClient", "org.apache.http.client", getClass())));
        this.requestExecutor = httpRequestExecutor;
        this.connManager = httpClientConnectionManager;
        this.reuseStrategy = connectionReuseStrategy;
        this.keepAliveStrategy = connectionKeepAliveStrategy;
    }

    static void rewriteRequestURI(HttpRequestWrapper httpRequestWrapper, HttpRoute httpRoute) throws ProtocolException {
        URI uri;
        try {
            URI uri2 = httpRequestWrapper.getURI();
            if (uri2 != null) {
                if (uri2.isAbsolute()) {
                    uri = URIUtils.rewriteURI(uri2, null, true);
                } else {
                    uri = URIUtils.rewriteURI(uri2);
                }
                httpRequestWrapper.setURI(uri);
            }
        } catch (URISyntaxException e) {
            StringBuilder sb = new StringBuilder();
            sb.append("Invalid URI: ");
            sb.append(httpRequestWrapper.getRequestLine().getUri());
            throw new ProtocolException(sb.toString(), e);
        }
    }

    /* JADX WARNING: type inference failed for: r8v3, types: [java.lang.Throwable] */
    /* JADX WARNING: type inference failed for: r7v3, types: [java.lang.Throwable] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x00ae A[Catch:{ ConnectionShutdownException -> 0x011b, HttpException -> 0x0116, IOException -> 0x0111, RuntimeException -> 0x010c }] */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x00de A[Catch:{ ConnectionShutdownException -> 0x011b, HttpException -> 0x0116, IOException -> 0x0111, RuntimeException -> 0x010c }] */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x00ed A[Catch:{ ConnectionShutdownException -> 0x011b, HttpException -> 0x0116, IOException -> 0x0111, RuntimeException -> 0x010c }] */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x00f6 A[Catch:{ ConnectionShutdownException -> 0x011b, HttpException -> 0x0116, IOException -> 0x0111, RuntimeException -> 0x010c }] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public org.apache.http.client.methods.CloseableHttpResponse execute(org.apache.http.conn.routing.HttpRoute r7, org.apache.http.client.methods.HttpRequestWrapper r8, org.apache.http.client.protocol.HttpClientContext r9, org.apache.http.client.methods.HttpExecutionAware r10) throws java.io.IOException, org.apache.http.HttpException {
        /*
            r6 = this;
            java.lang.String r0 = "HTTP route"
            org.apache.http.util.Args.notNull(r7, r0)
            java.lang.String r0 = "HTTP request"
            org.apache.http.util.Args.notNull(r8, r0)
            java.lang.String r0 = "HTTP context"
            org.apache.http.util.Args.notNull(r9, r0)
            rewriteRequestURI(r8, r7)
            org.apache.http.conn.HttpClientConnectionManager r0 = r6.connManager
            r1 = 0
            org.apache.http.conn.ConnectionRequest r0 = r0.requestConnection(r7, r1)
            if (r10 == 0) goto L_0x0030
            boolean r2 = r10.isAborted()
            if (r2 != 0) goto L_0x0025
            r10.setCancellable(r0)
            goto L_0x0030
        L_0x0025:
            r0.cancel()
            org.apache.http.impl.execchain.RequestAbortedException r7 = new org.apache.http.impl.execchain.RequestAbortedException
            java.lang.String r8 = "Request aborted"
            r7.<init>(r8)
            throw r7
        L_0x0030:
            org.apache.http.client.config.RequestConfig r2 = r9.getRequestConfig()
            int r3 = r2.getConnectionRequestTimeout()     // Catch:{ InterruptedException -> 0x0138, ExecutionException -> 0x0127 }
            if (r3 <= 0) goto L_0x003c
            long r3 = (long) r3     // Catch:{ InterruptedException -> 0x0138, ExecutionException -> 0x0127 }
            goto L_0x003e
        L_0x003c:
            r3 = 0
        L_0x003e:
            java.util.concurrent.TimeUnit r5 = java.util.concurrent.TimeUnit.MILLISECONDS     // Catch:{ InterruptedException -> 0x0138, ExecutionException -> 0x0127 }
            org.apache.http.HttpClientConnection r0 = r0.get(r3, r5)     // Catch:{ InterruptedException -> 0x0138, ExecutionException -> 0x0127 }
            org.apache.http.impl.execchain.ConnectionHolder r3 = new org.apache.http.impl.execchain.ConnectionHolder
            org.apache.commons.logging.Log r4 = r6.log
            org.apache.http.conn.HttpClientConnectionManager r5 = r6.connManager
            r3.<init>(r4, r5, r0)
            if (r10 == 0) goto L_0x0064
            boolean r4 = r10.isAborted()     // Catch:{ ConnectionShutdownException -> 0x011b, HttpException -> 0x0116, IOException -> 0x0111, RuntimeException -> 0x010c }
            if (r4 != 0) goto L_0x0059
            r10.setCancellable(r3)     // Catch:{ ConnectionShutdownException -> 0x011b, HttpException -> 0x0116, IOException -> 0x0111, RuntimeException -> 0x010c }
            goto L_0x0064
        L_0x0059:
            r3.close()     // Catch:{ ConnectionShutdownException -> 0x011b, HttpException -> 0x0116, IOException -> 0x0111, RuntimeException -> 0x010c }
            org.apache.http.impl.execchain.RequestAbortedException r7 = new org.apache.http.impl.execchain.RequestAbortedException     // Catch:{ ConnectionShutdownException -> 0x011b, HttpException -> 0x0116, IOException -> 0x0111, RuntimeException -> 0x010c }
            java.lang.String r8 = "Request aborted"
            r7.<init>(r8)     // Catch:{ ConnectionShutdownException -> 0x011b, HttpException -> 0x0116, IOException -> 0x0111, RuntimeException -> 0x010c }
            throw r7     // Catch:{ ConnectionShutdownException -> 0x011b, HttpException -> 0x0116, IOException -> 0x0111, RuntimeException -> 0x010c }
        L_0x0064:
            boolean r10 = r0.isOpen()     // Catch:{ ConnectionShutdownException -> 0x011b, HttpException -> 0x0116, IOException -> 0x0111, RuntimeException -> 0x010c }
            if (r10 != 0) goto L_0x007c
            int r10 = r2.getConnectTimeout()     // Catch:{ ConnectionShutdownException -> 0x011b, HttpException -> 0x0116, IOException -> 0x0111, RuntimeException -> 0x010c }
            org.apache.http.conn.HttpClientConnectionManager r4 = r6.connManager     // Catch:{ ConnectionShutdownException -> 0x011b, HttpException -> 0x0116, IOException -> 0x0111, RuntimeException -> 0x010c }
            if (r10 <= 0) goto L_0x0073
            goto L_0x0074
        L_0x0073:
            r10 = 0
        L_0x0074:
            r4.connect(r0, r7, r10, r9)     // Catch:{ ConnectionShutdownException -> 0x011b, HttpException -> 0x0116, IOException -> 0x0111, RuntimeException -> 0x010c }
            org.apache.http.conn.HttpClientConnectionManager r10 = r6.connManager     // Catch:{ ConnectionShutdownException -> 0x011b, HttpException -> 0x0116, IOException -> 0x0111, RuntimeException -> 0x010c }
            r10.routeComplete(r0, r7, r9)     // Catch:{ ConnectionShutdownException -> 0x011b, HttpException -> 0x0116, IOException -> 0x0111, RuntimeException -> 0x010c }
        L_0x007c:
            int r10 = r2.getSocketTimeout()     // Catch:{ ConnectionShutdownException -> 0x011b, HttpException -> 0x0116, IOException -> 0x0111, RuntimeException -> 0x010c }
            if (r10 < 0) goto L_0x0085
            r0.setSocketTimeout(r10)     // Catch:{ ConnectionShutdownException -> 0x011b, HttpException -> 0x0116, IOException -> 0x0111, RuntimeException -> 0x010c }
        L_0x0085:
            org.apache.http.HttpRequest r10 = r8.getOriginal()     // Catch:{ ConnectionShutdownException -> 0x011b, HttpException -> 0x0116, IOException -> 0x0111, RuntimeException -> 0x010c }
            boolean r2 = r10 instanceof org.apache.http.client.methods.HttpUriRequest     // Catch:{ ConnectionShutdownException -> 0x011b, HttpException -> 0x0116, IOException -> 0x0111, RuntimeException -> 0x010c }
            if (r2 == 0) goto L_0x00ab
            org.apache.http.client.methods.HttpUriRequest r10 = (org.apache.http.client.methods.HttpUriRequest) r10     // Catch:{ ConnectionShutdownException -> 0x011b, HttpException -> 0x0116, IOException -> 0x0111, RuntimeException -> 0x010c }
            java.net.URI r10 = r10.getURI()     // Catch:{ ConnectionShutdownException -> 0x011b, HttpException -> 0x0116, IOException -> 0x0111, RuntimeException -> 0x010c }
            boolean r2 = r10.isAbsolute()     // Catch:{ ConnectionShutdownException -> 0x011b, HttpException -> 0x0116, IOException -> 0x0111, RuntimeException -> 0x010c }
            if (r2 == 0) goto L_0x00ab
            org.apache.http.HttpHost r2 = new org.apache.http.HttpHost     // Catch:{ ConnectionShutdownException -> 0x011b, HttpException -> 0x0116, IOException -> 0x0111, RuntimeException -> 0x010c }
            java.lang.String r4 = r10.getHost()     // Catch:{ ConnectionShutdownException -> 0x011b, HttpException -> 0x0116, IOException -> 0x0111, RuntimeException -> 0x010c }
            int r5 = r10.getPort()     // Catch:{ ConnectionShutdownException -> 0x011b, HttpException -> 0x0116, IOException -> 0x0111, RuntimeException -> 0x010c }
            java.lang.String r10 = r10.getScheme()     // Catch:{ ConnectionShutdownException -> 0x011b, HttpException -> 0x0116, IOException -> 0x0111, RuntimeException -> 0x010c }
            r2.<init>(r4, r5, r10)     // Catch:{ ConnectionShutdownException -> 0x011b, HttpException -> 0x0116, IOException -> 0x0111, RuntimeException -> 0x010c }
            goto L_0x00ac
        L_0x00ab:
            r2 = r1
        L_0x00ac:
            if (r2 != 0) goto L_0x00b2
            org.apache.http.HttpHost r2 = r7.getTargetHost()     // Catch:{ ConnectionShutdownException -> 0x011b, HttpException -> 0x0116, IOException -> 0x0111, RuntimeException -> 0x010c }
        L_0x00b2:
            java.lang.String r10 = "http.target_host"
            r9.setAttribute(r10, r2)     // Catch:{ ConnectionShutdownException -> 0x011b, HttpException -> 0x0116, IOException -> 0x0111, RuntimeException -> 0x010c }
            java.lang.String r10 = "http.request"
            r9.setAttribute(r10, r8)     // Catch:{ ConnectionShutdownException -> 0x011b, HttpException -> 0x0116, IOException -> 0x0111, RuntimeException -> 0x010c }
            java.lang.String r10 = "http.connection"
            r9.setAttribute(r10, r0)     // Catch:{ ConnectionShutdownException -> 0x011b, HttpException -> 0x0116, IOException -> 0x0111, RuntimeException -> 0x010c }
            java.lang.String r10 = "http.route"
            r9.setAttribute(r10, r7)     // Catch:{ ConnectionShutdownException -> 0x011b, HttpException -> 0x0116, IOException -> 0x0111, RuntimeException -> 0x010c }
            org.apache.http.protocol.HttpProcessor r7 = r6.httpProcessor     // Catch:{ ConnectionShutdownException -> 0x011b, HttpException -> 0x0116, IOException -> 0x0111, RuntimeException -> 0x010c }
            r7.process(r8, r9)     // Catch:{ ConnectionShutdownException -> 0x011b, HttpException -> 0x0116, IOException -> 0x0111, RuntimeException -> 0x010c }
            org.apache.http.protocol.HttpRequestExecutor r7 = r6.requestExecutor     // Catch:{ ConnectionShutdownException -> 0x011b, HttpException -> 0x0116, IOException -> 0x0111, RuntimeException -> 0x010c }
            org.apache.http.HttpResponse r7 = r7.execute(r8, r0, r9)     // Catch:{ ConnectionShutdownException -> 0x011b, HttpException -> 0x0116, IOException -> 0x0111, RuntimeException -> 0x010c }
            org.apache.http.protocol.HttpProcessor r8 = r6.httpProcessor     // Catch:{ ConnectionShutdownException -> 0x011b, HttpException -> 0x0116, IOException -> 0x0111, RuntimeException -> 0x010c }
            r8.process(r7, r9)     // Catch:{ ConnectionShutdownException -> 0x011b, HttpException -> 0x0116, IOException -> 0x0111, RuntimeException -> 0x010c }
            org.apache.http.ConnectionReuseStrategy r8 = r6.reuseStrategy     // Catch:{ ConnectionShutdownException -> 0x011b, HttpException -> 0x0116, IOException -> 0x0111, RuntimeException -> 0x010c }
            boolean r8 = r8.keepAlive(r7, r9)     // Catch:{ ConnectionShutdownException -> 0x011b, HttpException -> 0x0116, IOException -> 0x0111, RuntimeException -> 0x010c }
            if (r8 == 0) goto L_0x00ed
            org.apache.http.conn.ConnectionKeepAliveStrategy r8 = r6.keepAliveStrategy     // Catch:{ ConnectionShutdownException -> 0x011b, HttpException -> 0x0116, IOException -> 0x0111, RuntimeException -> 0x010c }
            long r8 = r8.getKeepAliveDuration(r7, r9)     // Catch:{ ConnectionShutdownException -> 0x011b, HttpException -> 0x0116, IOException -> 0x0111, RuntimeException -> 0x010c }
            java.util.concurrent.TimeUnit r10 = java.util.concurrent.TimeUnit.MILLISECONDS     // Catch:{ ConnectionShutdownException -> 0x011b, HttpException -> 0x0116, IOException -> 0x0111, RuntimeException -> 0x010c }
            r3.setValidFor(r8, r10)     // Catch:{ ConnectionShutdownException -> 0x011b, HttpException -> 0x0116, IOException -> 0x0111, RuntimeException -> 0x010c }
            r3.markReusable()     // Catch:{ ConnectionShutdownException -> 0x011b, HttpException -> 0x0116, IOException -> 0x0111, RuntimeException -> 0x010c }
            goto L_0x00f0
        L_0x00ed:
            r3.markNonReusable()     // Catch:{ ConnectionShutdownException -> 0x011b, HttpException -> 0x0116, IOException -> 0x0111, RuntimeException -> 0x010c }
        L_0x00f0:
            org.apache.http.HttpEntity r8 = r7.getEntity()     // Catch:{ ConnectionShutdownException -> 0x011b, HttpException -> 0x0116, IOException -> 0x0111, RuntimeException -> 0x010c }
            if (r8 == 0) goto L_0x0103
            boolean r8 = r8.isStreaming()     // Catch:{ ConnectionShutdownException -> 0x011b, HttpException -> 0x0116, IOException -> 0x0111, RuntimeException -> 0x010c }
            if (r8 != 0) goto L_0x00fd
            goto L_0x0103
        L_0x00fd:
            org.apache.http.impl.execchain.HttpResponseProxy r8 = new org.apache.http.impl.execchain.HttpResponseProxy     // Catch:{ ConnectionShutdownException -> 0x011b, HttpException -> 0x0116, IOException -> 0x0111, RuntimeException -> 0x010c }
            r8.<init>(r7, r3)     // Catch:{ ConnectionShutdownException -> 0x011b, HttpException -> 0x0116, IOException -> 0x0111, RuntimeException -> 0x010c }
            return r8
        L_0x0103:
            r3.releaseConnection()     // Catch:{ ConnectionShutdownException -> 0x011b, HttpException -> 0x0116, IOException -> 0x0111, RuntimeException -> 0x010c }
            org.apache.http.impl.execchain.HttpResponseProxy r8 = new org.apache.http.impl.execchain.HttpResponseProxy     // Catch:{ ConnectionShutdownException -> 0x011b, HttpException -> 0x0116, IOException -> 0x0111, RuntimeException -> 0x010c }
            r8.<init>(r7, r1)     // Catch:{ ConnectionShutdownException -> 0x011b, HttpException -> 0x0116, IOException -> 0x0111, RuntimeException -> 0x010c }
            return r8
        L_0x010c:
            r7 = move-exception
            r3.abortConnection()
            throw r7
        L_0x0111:
            r7 = move-exception
            r3.abortConnection()
            throw r7
        L_0x0116:
            r7 = move-exception
            r3.abortConnection()
            throw r7
        L_0x011b:
            r7 = move-exception
            java.io.InterruptedIOException r8 = new java.io.InterruptedIOException
            java.lang.String r9 = "Connection has been shut down"
            r8.<init>(r9)
            r8.initCause(r7)
            throw r8
        L_0x0127:
            r7 = move-exception
            java.lang.Throwable r8 = r7.getCause()
            if (r8 != 0) goto L_0x012f
            goto L_0x0130
        L_0x012f:
            r7 = r8
        L_0x0130:
            org.apache.http.impl.execchain.RequestAbortedException r8 = new org.apache.http.impl.execchain.RequestAbortedException
            java.lang.String r9 = "Request execution failed"
            r8.<init>(r9, r7)
            throw r8
        L_0x0138:
            r7 = move-exception
            java.lang.Thread r8 = java.lang.Thread.currentThread()
            r8.interrupt()
            org.apache.http.impl.execchain.RequestAbortedException r8 = new org.apache.http.impl.execchain.RequestAbortedException
            java.lang.String r9 = "Request aborted"
            r8.<init>(r9, r7)
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.http.impl.execchain.MinimalClientExec.execute(org.apache.http.conn.routing.HttpRoute, org.apache.http.client.methods.HttpRequestWrapper, org.apache.http.client.protocol.HttpClientContext, org.apache.http.client.methods.HttpExecutionAware):org.apache.http.client.methods.CloseableHttpResponse");
    }
}
