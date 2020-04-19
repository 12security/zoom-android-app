package com.onedrive.sdk.generated;

import com.onedrive.sdk.extensions.IOneDriveClient;
import com.onedrive.sdk.extensions.IShareCollectionRequest;
import com.onedrive.sdk.extensions.IShareRequestBuilder;
import com.onedrive.sdk.extensions.ShareCollectionRequest;
import com.onedrive.sdk.extensions.ShareRequestBuilder;
import com.onedrive.sdk.http.BaseRequestBuilder;
import com.onedrive.sdk.options.Option;
import java.util.List;

public class BaseShareCollectionRequestBuilder extends BaseRequestBuilder implements IBaseShareCollectionRequestBuilder {
    public BaseShareCollectionRequestBuilder(String str, IOneDriveClient iOneDriveClient, List<Option> list) {
        super(str, iOneDriveClient, list);
    }

    public IShareCollectionRequest buildRequest() {
        return buildRequest(getOptions());
    }

    public IShareCollectionRequest buildRequest(List<Option> list) {
        return new ShareCollectionRequest(getRequestUrl(), getClient(), list);
    }

    public IShareRequestBuilder byId(String str) {
        return new ShareRequestBuilder(getRequestUrlWithAdditionalSegment(str), getClient(), getOptions());
    }
}
