package com.onedrive.sdk.generated;

import com.onedrive.sdk.extensions.IOneDriveClient;
import com.onedrive.sdk.extensions.IPermissionRequest;
import com.onedrive.sdk.extensions.PermissionRequest;
import com.onedrive.sdk.http.BaseRequestBuilder;
import com.onedrive.sdk.options.Option;
import java.util.List;

public class BasePermissionRequestBuilder extends BaseRequestBuilder implements IBasePermissionRequestBuilder {
    public BasePermissionRequestBuilder(String str, IOneDriveClient iOneDriveClient, List<Option> list) {
        super(str, iOneDriveClient, list);
    }

    public IPermissionRequest buildRequest() {
        return buildRequest(getOptions());
    }

    public IPermissionRequest buildRequest(List<Option> list) {
        return new PermissionRequest(getRequestUrl(), getClient(), list);
    }
}
