package org.apache.http.impl.execchain;

import java.io.IOException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.NoHttpResponseException;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.NonRepeatableRequestException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpExecutionAware;
import org.apache.http.client.methods.HttpRequestWrapper;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.util.Args;

@Contract(threading = ThreadingBehavior.IMMUTABLE_CONDITIONAL)
public class RetryExec implements ClientExecChain {
    private final Log log = LogFactory.getLog(getClass());
    private final ClientExecChain requestExecutor;
    private final HttpRequestRetryHandler retryHandler;

    public RetryExec(ClientExecChain clientExecChain, HttpRequestRetryHandler httpRequestRetryHandler) {
        Args.notNull(clientExecChain, "HTTP request executor");
        Args.notNull(httpRequestRetryHandler, "HTTP request retry handler");
        this.requestExecutor = clientExecChain;
        this.retryHandler = httpRequestRetryHandler;
    }

    public CloseableHttpResponse execute(HttpRoute httpRoute, HttpRequestWrapper httpRequestWrapper, HttpClientContext httpClientContext, HttpExecutionAware httpExecutionAware) throws IOException, HttpException {
        Args.notNull(httpRoute, "HTTP route");
        Args.notNull(httpRequestWrapper, "HTTP request");
        Args.notNull(httpClientContext, "HTTP context");
        Header[] allHeaders = httpRequestWrapper.getAllHeaders();
        int i = 1;
        while (true) {
            try {
                return this.requestExecutor.execute(httpRoute, httpRequestWrapper, httpClientContext, httpExecutionAware);
            } catch (IOException e) {
                if (httpExecutionAware != null && httpExecutionAware.isAborted()) {
                    this.log.debug("Request has been aborted");
                    throw e;
                } else if (this.retryHandler.retryRequest(e, i, httpClientContext)) {
                    if (this.log.isInfoEnabled()) {
                        Log log2 = this.log;
                        StringBuilder sb = new StringBuilder();
                        sb.append("I/O exception (");
                        sb.append(e.getClass().getName());
                        sb.append(") caught when processing request to ");
                        sb.append(httpRoute);
                        sb.append(": ");
                        sb.append(e.getMessage());
                        log2.info(sb.toString());
                    }
                    if (this.log.isDebugEnabled()) {
                        this.log.debug(e.getMessage(), e);
                    }
                    if (RequestEntityProxy.isRepeatable(httpRequestWrapper)) {
                        httpRequestWrapper.setHeaders(allHeaders);
                        if (this.log.isInfoEnabled()) {
                            Log log3 = this.log;
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("Retrying request to ");
                            sb2.append(httpRoute);
                            log3.info(sb2.toString());
                        }
                        i++;
                    } else {
                        this.log.debug("Cannot retry non-repeatable request");
                        throw new NonRepeatableRequestException("Cannot retry request with a non-repeatable request entity", e);
                    }
                } else if (e instanceof NoHttpResponseException) {
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append(httpRoute.getTargetHost().toHostString());
                    sb3.append(" failed to respond");
                    NoHttpResponseException noHttpResponseException = new NoHttpResponseException(sb3.toString());
                    noHttpResponseException.setStackTrace(e.getStackTrace());
                    throw noHttpResponseException;
                } else {
                    throw e;
                }
            }
        }
    }
}
