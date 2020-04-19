package com.onedrive.sdk.http;

import com.onedrive.sdk.concurrency.ICallback;
import com.onedrive.sdk.core.ClientException;
import com.onedrive.sdk.extensions.IOneDriveClient;
import com.onedrive.sdk.options.HeaderOption;
import com.onedrive.sdk.options.Option;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

public abstract class BaseStreamRequest<T> implements IHttpStreamRequest {
    private final BaseRequest mBaseRequest;

    public BaseStreamRequest(String str, IOneDriveClient iOneDriveClient, List<Option> list, Class<T> cls) {
        C18051 r0 = new BaseRequest(str, iOneDriveClient, list, cls) {
        };
        this.mBaseRequest = r0;
    }

    /* access modifiers changed from: protected */
    public void send(ICallback<InputStream> iCallback) {
        this.mBaseRequest.setHttpMethod(HttpMethod.GET);
        this.mBaseRequest.getClient().getHttpProvider().send((IHttpRequest) this, iCallback, InputStream.class, null);
    }

    /* access modifiers changed from: protected */
    public InputStream send() throws ClientException {
        this.mBaseRequest.setHttpMethod(HttpMethod.GET);
        return (InputStream) this.mBaseRequest.getClient().getHttpProvider().send(this, InputStream.class, null);
    }

    /* access modifiers changed from: protected */
    public void send(byte[] bArr, ICallback<T> iCallback) {
        this.mBaseRequest.setHttpMethod(HttpMethod.PUT);
        this.mBaseRequest.getClient().getHttpProvider().send((IHttpRequest) this, iCallback, this.mBaseRequest.getResponseType(), bArr);
    }

    /* access modifiers changed from: protected */
    public T send(byte[] bArr) {
        this.mBaseRequest.setHttpMethod(HttpMethod.PUT);
        return this.mBaseRequest.getClient().getHttpProvider().send(this, this.mBaseRequest.getResponseType(), bArr);
    }

    public URL getRequestUrl() {
        return this.mBaseRequest.getRequestUrl();
    }

    public HttpMethod getHttpMethod() {
        return this.mBaseRequest.getHttpMethod();
    }

    public void addHeader(String str, String str2) {
        this.mBaseRequest.addHeader(str, str2);
    }

    public List<HeaderOption> getHeaders() {
        return this.mBaseRequest.getHeaders();
    }

    public List<Option> getOptions() {
        return this.mBaseRequest.getOptions();
    }
}
