package com.onedrive.sdk.generated;

import com.onedrive.sdk.concurrency.ICallback;
import com.onedrive.sdk.core.ClientException;
import com.onedrive.sdk.extensions.IDriveCollectionPage;
import com.onedrive.sdk.extensions.IDriveCollectionRequest;

public interface IBaseDriveCollectionRequest {
    IDriveCollectionRequest expand(String str);

    IDriveCollectionPage get() throws ClientException;

    void get(ICallback<IDriveCollectionPage> iCallback);

    IDriveCollectionRequest select(String str);

    IDriveCollectionRequest top(int i);
}
