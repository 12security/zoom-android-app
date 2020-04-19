package com.google.api.client.http.apache;

import com.google.api.client.util.Preconditions;
import java.net.URI;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;

final class HttpExtensionMethod extends HttpEntityEnclosingRequestBase {
    private final String methodName;

    public HttpExtensionMethod(String str, String str2) {
        this.methodName = (String) Preconditions.checkNotNull(str);
        setURI(URI.create(str2));
    }

    public String getMethod() {
        return this.methodName;
    }
}
