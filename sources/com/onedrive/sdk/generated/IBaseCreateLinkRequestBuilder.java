package com.onedrive.sdk.generated;

import com.onedrive.sdk.extensions.ICreateLinkRequest;
import com.onedrive.sdk.http.IRequestBuilder;
import com.onedrive.sdk.options.Option;
import java.util.List;

public interface IBaseCreateLinkRequestBuilder extends IRequestBuilder {
    ICreateLinkRequest buildRequest();

    ICreateLinkRequest buildRequest(List<Option> list);
}
