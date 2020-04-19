package org.apache.http.client.methods;

import java.net.URI;

public class HttpTrace extends HttpRequestBase {
    public static final String METHOD_NAME = "TRACE";

    public String getMethod() {
        return "TRACE";
    }

    public HttpTrace() {
    }

    public HttpTrace(URI uri) {
        setURI(uri);
    }

    public HttpTrace(String str) {
        setURI(URI.create(str));
    }
}
