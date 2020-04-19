package com.onedrive.sdk.http;

import com.onedrive.sdk.core.ClientException;
import com.onedrive.sdk.extensions.IOneDriveClient;
import com.onedrive.sdk.options.HeaderOption;
import com.onedrive.sdk.options.Option;
import com.onedrive.sdk.options.QueryOption;
import java.net.URL;
import java.util.List;

public abstract class BaseCollectionRequest<T1, T2> implements IHttpRequest {
    private final BaseRequest mBaseRequest;
    private final Class<T2> mCollectionPageClass;
    private final Class<T1> mResponseClass;

    public BaseCollectionRequest(String str, IOneDriveClient iOneDriveClient, List<Option> list, Class<T1> cls, Class<T2> cls2) {
        this.mResponseClass = cls;
        this.mCollectionPageClass = cls2;
        C18041 r0 = new BaseRequest(str, iOneDriveClient, list, this.mResponseClass) {
        };
        this.mBaseRequest = r0;
        this.mBaseRequest.setHttpMethod(HttpMethod.GET);
    }

    /* access modifiers changed from: protected */
    public T1 send() throws ClientException {
        return this.mBaseRequest.getClient().getHttpProvider().send(this, this.mResponseClass, null);
    }

    public URL getRequestUrl() {
        return this.mBaseRequest.getRequestUrl();
    }

    public HttpMethod getHttpMethod() {
        return this.mBaseRequest.getHttpMethod();
    }

    public List<HeaderOption> getHeaders() {
        return this.mBaseRequest.getHeaders();
    }

    public void addHeader(String str, String str2) {
        this.mBaseRequest.addHeader(str, str2);
    }

    public List<Option> getOptions() {
        return this.mBaseRequest.getOptions();
    }

    public void addQueryOption(QueryOption queryOption) {
        this.mBaseRequest.getQueryOptions().add(queryOption);
    }

    /* access modifiers changed from: protected */
    public BaseRequest getBaseRequest() {
        return this.mBaseRequest;
    }

    public Class<T2> getCollectionPageClass() {
        return this.mCollectionPageClass;
    }
}
