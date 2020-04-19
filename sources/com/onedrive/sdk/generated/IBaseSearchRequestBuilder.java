package com.onedrive.sdk.generated;

import com.onedrive.sdk.extensions.ISearchRequest;
import com.onedrive.sdk.http.IRequestBuilder;
import com.onedrive.sdk.options.Option;
import java.util.List;

public interface IBaseSearchRequestBuilder extends IRequestBuilder {
    ISearchRequest buildRequest();

    ISearchRequest buildRequest(List<Option> list);
}
