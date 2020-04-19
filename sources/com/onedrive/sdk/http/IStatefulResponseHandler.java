package com.onedrive.sdk.http;

import com.onedrive.sdk.logger.ILogger;
import com.onedrive.sdk.serializer.ISerializer;

public interface IStatefulResponseHandler<ResultType, DeserializedType> {
    void configConnection(IConnection iConnection);

    ResultType generateResult(IHttpRequest iHttpRequest, IConnection iConnection, ISerializer iSerializer, ILogger iLogger) throws Exception;
}
