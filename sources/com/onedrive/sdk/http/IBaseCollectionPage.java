package com.onedrive.sdk.http;

import com.google.gson.JsonObject;
import com.onedrive.sdk.http.IRequestBuilder;
import com.onedrive.sdk.serializer.IJsonBackedObject;
import java.util.List;

public interface IBaseCollectionPage<T1, T2 extends IRequestBuilder> extends IJsonBackedObject {
    List<T1> getCurrentPage();

    T2 getNextPage();

    JsonObject getRawObject();
}
