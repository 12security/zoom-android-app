package com.onedrive.sdk.extensions;

import com.onedrive.sdk.generated.IBaseItemRequestBuilder;

public interface IItemRequestBuilder extends IBaseItemRequestBuilder {
    IItemRequestBuilder getItemWithPath(String str);
}
