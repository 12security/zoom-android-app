package com.onedrive.sdk.generated;

import com.onedrive.sdk.extensions.IOneDriveClient;
import com.onedrive.sdk.extensions.IPermissionCollectionRequest;
import com.onedrive.sdk.extensions.IPermissionRequestBuilder;
import com.onedrive.sdk.extensions.PermissionCollectionRequest;
import com.onedrive.sdk.extensions.PermissionRequestBuilder;
import com.onedrive.sdk.http.BaseRequestBuilder;
import com.onedrive.sdk.options.Option;
import java.util.List;

public class BasePermissionCollectionRequestBuilder extends BaseRequestBuilder implements IBasePermissionCollectionRequestBuilder {
    public BasePermissionCollectionRequestBuilder(String str, IOneDriveClient iOneDriveClient, List<Option> list) {
        super(str, iOneDriveClient, list);
    }

    public IPermissionCollectionRequest buildRequest() {
        return buildRequest(getOptions());
    }

    public IPermissionCollectionRequest buildRequest(List<Option> list) {
        return new PermissionCollectionRequest(getRequestUrl(), getClient(), list);
    }

    public IPermissionRequestBuilder byId(String str) {
        return new PermissionRequestBuilder(getRequestUrlWithAdditionalSegment(str), getClient(), getOptions());
    }
}
