package com.onedrive.sdk.generated;

import com.onedrive.sdk.extensions.IRecentRequest;
import com.onedrive.sdk.http.IRequestBuilder;
import com.onedrive.sdk.options.Option;
import java.util.List;

public interface IBaseRecentRequestBuilder extends IRequestBuilder {
    IRecentRequest buildRequest();

    IRecentRequest buildRequest(List<Option> list);
}
