package com.onedrive.sdk.generated;

import com.onedrive.sdk.concurrency.ICallback;
import com.onedrive.sdk.core.ClientException;
import com.onedrive.sdk.extensions.IPermissionCollectionPage;
import com.onedrive.sdk.extensions.IPermissionCollectionRequest;

public interface IBasePermissionCollectionRequest {
    IPermissionCollectionRequest expand(String str);

    IPermissionCollectionPage get() throws ClientException;

    void get(ICallback<IPermissionCollectionPage> iCallback);

    IPermissionCollectionRequest select(String str);

    IPermissionCollectionRequest top(int i);
}
