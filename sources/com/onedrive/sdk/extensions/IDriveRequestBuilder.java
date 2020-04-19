package com.onedrive.sdk.extensions;

import com.onedrive.sdk.generated.IBaseDriveRequestBuilder;

public interface IDriveRequestBuilder extends IBaseDriveRequestBuilder {
    IItemRequestBuilder getRoot();
}
