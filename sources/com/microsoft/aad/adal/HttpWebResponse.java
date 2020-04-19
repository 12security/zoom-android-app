package com.microsoft.aad.adal;

import java.util.List;
import java.util.Map;

public class HttpWebResponse {
    private final String mResponseBody;
    private final Map<String, List<String>> mResponseHeaders;
    private final int mStatusCode;

    public HttpWebResponse(int i, String str, Map<String, List<String>> map) {
        this.mStatusCode = i;
        this.mResponseBody = str;
        this.mResponseHeaders = map;
    }

    public int getStatusCode() {
        return this.mStatusCode;
    }

    public Map<String, List<String>> getResponseHeaders() {
        return this.mResponseHeaders;
    }

    public String getBody() {
        return this.mResponseBody;
    }
}
