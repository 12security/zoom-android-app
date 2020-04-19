package com.onedrive.sdk.http;

import java.io.IOException;

public interface IConnectionFactory {
    IConnection createFromRequest(IHttpRequest iHttpRequest) throws IOException;
}
