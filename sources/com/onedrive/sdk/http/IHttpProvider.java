package com.onedrive.sdk.http;

import com.onedrive.sdk.concurrency.ICallback;
import com.onedrive.sdk.core.ClientException;
import com.onedrive.sdk.serializer.ISerializer;

public interface IHttpProvider {
    ISerializer getSerializer();

    <Result, BodyType> Result send(IHttpRequest iHttpRequest, Class<Result> cls, BodyType bodytype) throws ClientException;

    <Result, BodyType, DeserializeType> Result send(IHttpRequest iHttpRequest, Class<Result> cls, BodyType bodytype, IStatefulResponseHandler<Result, DeserializeType> iStatefulResponseHandler) throws ClientException;

    <Result, BodyType> void send(IHttpRequest iHttpRequest, ICallback<Result> iCallback, Class<Result> cls, BodyType bodytype);
}
