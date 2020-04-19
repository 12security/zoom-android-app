package com.onedrive.sdk.core;

import com.onedrive.sdk.authentication.IAuthenticator;
import com.onedrive.sdk.concurrency.IExecutors;
import com.onedrive.sdk.http.IHttpProvider;
import com.onedrive.sdk.logger.ILogger;
import com.onedrive.sdk.serializer.ISerializer;

public interface IClientConfig {
    IAuthenticator getAuthenticator();

    IExecutors getExecutors();

    IHttpProvider getHttpProvider();

    ILogger getLogger();

    ISerializer getSerializer();
}
