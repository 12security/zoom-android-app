package com.onedrive.sdk.generated;

import com.onedrive.sdk.extensions.IStringCollectionRequest;
import com.onedrive.sdk.http.IRequestBuilder;
import com.onedrive.sdk.options.Option;
import java.util.List;

public interface IBaseStringCollectionRequestBuilder extends IRequestBuilder {
    IStringCollectionRequest buildRequest();

    IStringCollectionRequest buildRequest(List<Option> list);
}
