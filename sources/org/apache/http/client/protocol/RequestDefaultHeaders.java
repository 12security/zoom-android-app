package org.apache.http.client.protocol;

import com.google.api.client.http.HttpMethods;
import java.io.IOException;
import java.util.Collection;
import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

@Contract(threading = ThreadingBehavior.IMMUTABLE_CONDITIONAL)
public class RequestDefaultHeaders implements HttpRequestInterceptor {
    private final Collection<? extends Header> defaultHeaders;

    public RequestDefaultHeaders(Collection<? extends Header> collection) {
        this.defaultHeaders = collection;
    }

    public RequestDefaultHeaders() {
        this(null);
    }

    public void process(HttpRequest httpRequest, HttpContext httpContext) throws HttpException, IOException {
        Args.notNull(httpRequest, "HTTP request");
        if (!httpRequest.getRequestLine().getMethod().equalsIgnoreCase(HttpMethods.CONNECT)) {
            Collection<? extends Header> collection = (Collection) httpRequest.getParams().getParameter(ClientPNames.DEFAULT_HEADERS);
            if (collection == null) {
                collection = this.defaultHeaders;
            }
            if (collection != null) {
                for (Header addHeader : collection) {
                    httpRequest.addHeader(addHeader);
                }
            }
        }
    }
}
