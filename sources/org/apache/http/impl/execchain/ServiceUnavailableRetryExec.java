package org.apache.http.impl.execchain;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.client.ServiceUnavailableRetryStrategy;
import org.apache.http.util.Args;

@Contract(threading = ThreadingBehavior.IMMUTABLE_CONDITIONAL)
public class ServiceUnavailableRetryExec implements ClientExecChain {
    private final Log log = LogFactory.getLog(getClass());
    private final ClientExecChain requestExecutor;
    private final ServiceUnavailableRetryStrategy retryStrategy;

    public ServiceUnavailableRetryExec(ClientExecChain clientExecChain, ServiceUnavailableRetryStrategy serviceUnavailableRetryStrategy) {
        Args.notNull(clientExecChain, "HTTP request executor");
        Args.notNull(serviceUnavailableRetryStrategy, "Retry strategy");
        this.requestExecutor = clientExecChain;
        this.retryStrategy = serviceUnavailableRetryStrategy;
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(3:10|11|12) */
    /* JADX WARNING: Code restructure failed: missing block: B:13:?, code lost:
        java.lang.Thread.currentThread().interrupt();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x004e, code lost:
        throw new java.io.InterruptedIOException();
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:12:0x0042 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public org.apache.http.client.methods.CloseableHttpResponse execute(org.apache.http.conn.routing.HttpRoute r9, org.apache.http.client.methods.HttpRequestWrapper r10, org.apache.http.client.protocol.HttpClientContext r11, org.apache.http.client.methods.HttpExecutionAware r12) throws java.io.IOException, org.apache.http.HttpException {
        /*
            r8 = this;
            org.apache.http.Header[] r0 = r10.getAllHeaders()
            r1 = 1
        L_0x0005:
            org.apache.http.impl.execchain.ClientExecChain r2 = r8.requestExecutor
            org.apache.http.client.methods.CloseableHttpResponse r2 = r2.execute(r9, r10, r11, r12)
            org.apache.http.client.ServiceUnavailableRetryStrategy r3 = r8.retryStrategy     // Catch:{ RuntimeException -> 0x0056 }
            boolean r3 = r3.retryRequest(r2, r1, r11)     // Catch:{ RuntimeException -> 0x0056 }
            if (r3 == 0) goto L_0x0055
            boolean r3 = org.apache.http.impl.execchain.RequestEntityProxy.isRepeatable(r10)     // Catch:{ RuntimeException -> 0x0056 }
            if (r3 == 0) goto L_0x0055
            r2.close()     // Catch:{ RuntimeException -> 0x0056 }
            org.apache.http.client.ServiceUnavailableRetryStrategy r3 = r8.retryStrategy     // Catch:{ RuntimeException -> 0x0056 }
            long r3 = r3.getRetryInterval()     // Catch:{ RuntimeException -> 0x0056 }
            r5 = 0
            int r5 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1))
            if (r5 <= 0) goto L_0x004f
            org.apache.commons.logging.Log r5 = r8.log     // Catch:{ InterruptedException -> 0x0042 }
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ InterruptedException -> 0x0042 }
            r6.<init>()     // Catch:{ InterruptedException -> 0x0042 }
            java.lang.String r7 = "Wait for "
            r6.append(r7)     // Catch:{ InterruptedException -> 0x0042 }
            r6.append(r3)     // Catch:{ InterruptedException -> 0x0042 }
            java.lang.String r6 = r6.toString()     // Catch:{ InterruptedException -> 0x0042 }
            r5.trace(r6)     // Catch:{ InterruptedException -> 0x0042 }
            java.lang.Thread.sleep(r3)     // Catch:{ InterruptedException -> 0x0042 }
            goto L_0x004f
        L_0x0042:
            java.lang.Thread r9 = java.lang.Thread.currentThread()     // Catch:{ RuntimeException -> 0x0056 }
            r9.interrupt()     // Catch:{ RuntimeException -> 0x0056 }
            java.io.InterruptedIOException r9 = new java.io.InterruptedIOException     // Catch:{ RuntimeException -> 0x0056 }
            r9.<init>()     // Catch:{ RuntimeException -> 0x0056 }
            throw r9     // Catch:{ RuntimeException -> 0x0056 }
        L_0x004f:
            r10.setHeaders(r0)     // Catch:{ RuntimeException -> 0x0056 }
            int r1 = r1 + 1
            goto L_0x0005
        L_0x0055:
            return r2
        L_0x0056:
            r9 = move-exception
            r2.close()
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.http.impl.execchain.ServiceUnavailableRetryExec.execute(org.apache.http.conn.routing.HttpRoute, org.apache.http.client.methods.HttpRequestWrapper, org.apache.http.client.protocol.HttpClientContext, org.apache.http.client.methods.HttpExecutionAware):org.apache.http.client.methods.CloseableHttpResponse");
    }
}
