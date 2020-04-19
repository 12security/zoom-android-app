package com.onedrive.sdk.generated;

import com.onedrive.sdk.extensions.IPermissionRequest;
import com.onedrive.sdk.http.IRequestBuilder;
import com.onedrive.sdk.options.Option;
import java.util.List;

public interface IBasePermissionRequestBuilder extends IRequestBuilder {
    IPermissionRequest buildRequest();

    IPermissionRequest buildRequest(List<Option> list);
}
