package com.onedrive.sdk.http;

import java.io.IOException;

public class DefaultConnectionFactory implements IConnectionFactory {
    public IConnection createFromRequest(IHttpRequest iHttpRequest) throws IOException {
        return new UrlConnection(iHttpRequest);
    }
}
