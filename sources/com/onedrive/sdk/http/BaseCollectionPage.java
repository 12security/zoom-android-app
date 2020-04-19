package com.onedrive.sdk.http;

import com.google.gson.JsonObject;
import com.onedrive.sdk.http.IRequestBuilder;
import com.onedrive.sdk.serializer.ISerializer;
import java.util.Collections;
import java.util.List;

public abstract class BaseCollectionPage<T1, T2 extends IRequestBuilder> implements IBaseCollectionPage<T1, T2> {
    private final List<T1> mPageContents;
    private transient JsonObject mRawObject;
    private final T2 mRequestBuilder;
    private transient ISerializer mSerializer;

    public BaseCollectionPage(List<T1> list, T2 t2) {
        this.mPageContents = Collections.unmodifiableList(list);
        this.mRequestBuilder = t2;
    }

    public T2 getNextPage() {
        return this.mRequestBuilder;
    }

    public List<T1> getCurrentPage() {
        return this.mPageContents;
    }

    public JsonObject getRawObject() {
        return this.mRawObject;
    }

    /* access modifiers changed from: protected */
    public ISerializer getSerializer() {
        return this.mSerializer;
    }

    public void setRawObject(ISerializer iSerializer, JsonObject jsonObject) {
        this.mSerializer = iSerializer;
        this.mRawObject = jsonObject;
    }
}
