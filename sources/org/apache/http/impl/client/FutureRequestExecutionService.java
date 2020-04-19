package org.apache.http.impl.client;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.protocol.HttpContext;

@Contract(threading = ThreadingBehavior.SAFE)
public class FutureRequestExecutionService implements Closeable {
    private final AtomicBoolean closed = new AtomicBoolean(false);
    private final ExecutorService executorService;
    private final HttpClient httpclient;
    private final FutureRequestExecutionMetrics metrics = new FutureRequestExecutionMetrics();

    public FutureRequestExecutionService(HttpClient httpClient, ExecutorService executorService2) {
        this.httpclient = httpClient;
        this.executorService = executorService2;
    }

    public <T> HttpRequestFutureTask<T> execute(HttpUriRequest httpUriRequest, HttpContext httpContext, ResponseHandler<T> responseHandler) {
        return execute(httpUriRequest, httpContext, responseHandler, null);
    }

    public <T> HttpRequestFutureTask<T> execute(HttpUriRequest httpUriRequest, HttpContext httpContext, ResponseHandler<T> responseHandler, FutureCallback<T> futureCallback) {
        if (!this.closed.get()) {
            this.metrics.getScheduledConnections().incrementAndGet();
            HttpRequestTaskCallable httpRequestTaskCallable = new HttpRequestTaskCallable(this.httpclient, httpUriRequest, httpContext, responseHandler, futureCallback, this.metrics);
            HttpRequestFutureTask<T> httpRequestFutureTask = new HttpRequestFutureTask<>(httpUriRequest, httpRequestTaskCallable);
            this.executorService.execute(httpRequestFutureTask);
            return httpRequestFutureTask;
        }
        throw new IllegalStateException("Close has been called on this httpclient instance.");
    }

    public FutureRequestExecutionMetrics metrics() {
        return this.metrics;
    }

    public void close() throws IOException {
        this.closed.set(true);
        this.executorService.shutdownNow();
        HttpClient httpClient = this.httpclient;
        if (httpClient instanceof Closeable) {
            ((Closeable) httpClient).close();
        }
    }
}
