package org.apache.http.impl.client;

import java.io.Closeable;
import java.io.IOException;
import java.net.URI;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.apache.http.util.EntityUtils;

@Contract(threading = ThreadingBehavior.SAFE)
public abstract class CloseableHttpClient implements HttpClient, Closeable {
    private final Log log = LogFactory.getLog(getClass());

    /* access modifiers changed from: protected */
    public abstract CloseableHttpResponse doExecute(HttpHost httpHost, HttpRequest httpRequest, HttpContext httpContext) throws IOException, ClientProtocolException;

    public CloseableHttpResponse execute(HttpHost httpHost, HttpRequest httpRequest, HttpContext httpContext) throws IOException, ClientProtocolException {
        return doExecute(httpHost, httpRequest, httpContext);
    }

    public CloseableHttpResponse execute(HttpUriRequest httpUriRequest, HttpContext httpContext) throws IOException, ClientProtocolException {
        Args.notNull(httpUriRequest, "HTTP request");
        return doExecute(determineTarget(httpUriRequest), httpUriRequest, httpContext);
    }

    private static HttpHost determineTarget(HttpUriRequest httpUriRequest) throws ClientProtocolException {
        URI uri = httpUriRequest.getURI();
        if (!uri.isAbsolute()) {
            return null;
        }
        HttpHost extractHost = URIUtils.extractHost(uri);
        if (extractHost != null) {
            return extractHost;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("URI does not specify a valid host name: ");
        sb.append(uri);
        throw new ClientProtocolException(sb.toString());
    }

    public CloseableHttpResponse execute(HttpUriRequest httpUriRequest) throws IOException, ClientProtocolException {
        return execute(httpUriRequest, (HttpContext) null);
    }

    public CloseableHttpResponse execute(HttpHost httpHost, HttpRequest httpRequest) throws IOException, ClientProtocolException {
        return doExecute(httpHost, httpRequest, null);
    }

    public <T> T execute(HttpUriRequest httpUriRequest, ResponseHandler<? extends T> responseHandler) throws IOException, ClientProtocolException {
        return execute(httpUriRequest, responseHandler, (HttpContext) null);
    }

    public <T> T execute(HttpUriRequest httpUriRequest, ResponseHandler<? extends T> responseHandler, HttpContext httpContext) throws IOException, ClientProtocolException {
        return execute(determineTarget(httpUriRequest), httpUriRequest, responseHandler, httpContext);
    }

    public <T> T execute(HttpHost httpHost, HttpRequest httpRequest, ResponseHandler<? extends T> responseHandler) throws IOException, ClientProtocolException {
        return execute(httpHost, httpRequest, responseHandler, null);
    }

    public <T> T execute(HttpHost httpHost, HttpRequest httpRequest, ResponseHandler<? extends T> responseHandler, HttpContext httpContext) throws IOException, ClientProtocolException {
        Args.notNull(responseHandler, "Response handler");
        CloseableHttpResponse execute = execute(httpHost, httpRequest, httpContext);
        try {
            T handleResponse = responseHandler.handleResponse(execute);
            EntityUtils.consume(execute.getEntity());
            execute.close();
            return handleResponse;
        } catch (ClientProtocolException e) {
            try {
                EntityUtils.consume(execute.getEntity());
            } catch (Exception e2) {
                this.log.warn("Error consuming content after an exception.", e2);
            }
            throw e;
        } catch (Throwable th) {
            execute.close();
            throw th;
        }
    }
}
