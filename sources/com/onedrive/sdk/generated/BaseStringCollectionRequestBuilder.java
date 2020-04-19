package com.onedrive.sdk.generated;

import com.onedrive.sdk.extensions.IOneDriveClient;
import com.onedrive.sdk.extensions.IStringCollectionRequest;
import com.onedrive.sdk.extensions.StringCollectionRequest;
import com.onedrive.sdk.http.BaseRequestBuilder;
import com.onedrive.sdk.options.Option;
import java.util.List;

public class BaseStringCollectionRequestBuilder extends BaseRequestBuilder implements IBaseStringCollectionRequestBuilder {
    public BaseStringCollectionRequestBuilder(String str, IOneDriveClient iOneDriveClient, List<Option> list) {
        super(str, iOneDriveClient, list);
    }

    public IStringCollectionRequest buildRequest() {
        return buildRequest(getOptions());
    }

    public IStringCollectionRequest buildRequest(List<Option> list) {
        return new StringCollectionRequest(getRequestUrl(), getClient(), list);
    }
}
