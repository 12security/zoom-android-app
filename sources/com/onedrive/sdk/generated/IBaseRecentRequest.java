package com.onedrive.sdk.generated;

import com.onedrive.sdk.concurrency.ICallback;
import com.onedrive.sdk.core.ClientException;
import com.onedrive.sdk.extensions.IRecentCollectionPage;
import com.onedrive.sdk.extensions.IRecentRequest;

public interface IBaseRecentRequest {
    IRecentRequest expand(String str);

    IRecentCollectionPage get() throws ClientException;

    void get(ICallback<IRecentCollectionPage> iCallback);

    IRecentRequest select(String str);

    IRecentRequest top(int i);
}
