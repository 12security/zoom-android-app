package com.onedrive.sdk.generated;

import com.onedrive.sdk.extensions.IShareCollectionRequest;
import com.onedrive.sdk.extensions.IShareRequestBuilder;
import com.onedrive.sdk.http.IRequestBuilder;
import com.onedrive.sdk.options.Option;
import java.util.List;

public interface IBaseShareCollectionRequestBuilder extends IRequestBuilder {
    IShareCollectionRequest buildRequest();

    IShareCollectionRequest buildRequest(List<Option> list);

    IShareRequestBuilder byId(String str);
}
