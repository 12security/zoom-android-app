package org.apache.http.client.protocol;

import java.io.IOException;
import java.util.List;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.protocol.HttpContext;

@Contract(threading = ThreadingBehavior.IMMUTABLE)
public class RequestAcceptEncoding implements HttpRequestInterceptor {
    private final String acceptEncoding;

    public RequestAcceptEncoding(List<String> list) {
        if (list == null || list.isEmpty()) {
            this.acceptEncoding = "gzip,deflate";
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) {
                sb.append(PreferencesConstants.COOKIE_DELIMITER);
            }
            sb.append((String) list.get(i));
        }
        this.acceptEncoding = sb.toString();
    }

    public RequestAcceptEncoding() {
        this(null);
    }

    public void process(HttpRequest httpRequest, HttpContext httpContext) throws HttpException, IOException {
        RequestConfig requestConfig = HttpClientContext.adapt(httpContext).getRequestConfig();
        if (!httpRequest.containsHeader("Accept-Encoding") && requestConfig.isContentCompressionEnabled()) {
            httpRequest.addHeader("Accept-Encoding", this.acceptEncoding);
        }
    }
}
