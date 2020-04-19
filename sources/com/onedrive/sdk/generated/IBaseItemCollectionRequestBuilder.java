package com.onedrive.sdk.generated;

import com.onedrive.sdk.extensions.IItemCollectionRequest;
import com.onedrive.sdk.extensions.IItemRequestBuilder;
import com.onedrive.sdk.http.IRequestBuilder;
import com.onedrive.sdk.options.Option;
import java.util.List;

public interface IBaseItemCollectionRequestBuilder extends IRequestBuilder {
    IItemCollectionRequest buildRequest();

    IItemCollectionRequest buildRequest(List<Option> list);

    IItemRequestBuilder byId(String str);
}
