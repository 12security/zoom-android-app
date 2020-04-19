package com.onedrive.sdk.generated;

import com.onedrive.sdk.concurrency.ICallback;
import com.onedrive.sdk.core.ClientException;
import com.onedrive.sdk.extensions.IDeltaCollectionPage;
import com.onedrive.sdk.extensions.IDeltaRequest;

public interface IBaseDeltaRequest {
    IDeltaRequest expand(String str);

    IDeltaCollectionPage get() throws ClientException;

    void get(ICallback<IDeltaCollectionPage> iCallback);

    IDeltaRequest select(String str);

    IDeltaRequest top(int i);
}
