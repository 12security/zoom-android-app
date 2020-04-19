package com.onedrive.sdk.generated;

import com.onedrive.sdk.extensions.IItemCollectionRequest;
import com.onedrive.sdk.extensions.IItemRequestBuilder;
import com.onedrive.sdk.extensions.IOneDriveClient;
import com.onedrive.sdk.extensions.ItemCollectionRequest;
import com.onedrive.sdk.extensions.ItemRequestBuilder;
import com.onedrive.sdk.http.BaseRequestBuilder;
import com.onedrive.sdk.options.Option;
import java.util.List;

public class BaseItemCollectionRequestBuilder extends BaseRequestBuilder implements IBaseItemCollectionRequestBuilder {
    public BaseItemCollectionRequestBuilder(String str, IOneDriveClient iOneDriveClient, List<Option> list) {
        super(str, iOneDriveClient, list);
    }

    public IItemCollectionRequest buildRequest() {
        return buildRequest(getOptions());
    }

    public IItemCollectionRequest buildRequest(List<Option> list) {
        return new ItemCollectionRequest(getRequestUrl(), getClient(), list);
    }

    public IItemRequestBuilder byId(String str) {
        return new ItemRequestBuilder(getRequestUrlWithAdditionalSegment(str), getClient(), getOptions());
    }
}
