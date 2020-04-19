package com.onedrive.sdk.generated;

import com.onedrive.sdk.extensions.IShareRequest;
import com.onedrive.sdk.http.IRequestBuilder;
import com.onedrive.sdk.options.Option;
import java.util.List;

public interface IBaseShareRequestBuilder extends IRequestBuilder {
    IShareRequest buildRequest();

    IShareRequest buildRequest(List<Option> list);
}
