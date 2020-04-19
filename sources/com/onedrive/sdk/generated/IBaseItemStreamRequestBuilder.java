package com.onedrive.sdk.generated;

import com.onedrive.sdk.extensions.IItemStreamRequest;
import com.onedrive.sdk.http.IRequestBuilder;
import com.onedrive.sdk.options.Option;
import java.util.List;

public interface IBaseItemStreamRequestBuilder extends IRequestBuilder {
    IItemStreamRequest buildRequest();

    IItemStreamRequest buildRequest(List<Option> list);
}
