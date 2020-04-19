package com.onedrive.sdk.generated;

import com.onedrive.sdk.extensions.IPermissionCollectionRequest;
import com.onedrive.sdk.extensions.IPermissionRequestBuilder;
import com.onedrive.sdk.http.IRequestBuilder;
import com.onedrive.sdk.options.Option;
import java.util.List;

public interface IBasePermissionCollectionRequestBuilder extends IRequestBuilder {
    IPermissionCollectionRequest buildRequest();

    IPermissionCollectionRequest buildRequest(List<Option> list);

    IPermissionRequestBuilder byId(String str);
}
