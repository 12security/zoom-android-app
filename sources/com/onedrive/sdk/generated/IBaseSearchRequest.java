package com.onedrive.sdk.generated;

import com.onedrive.sdk.concurrency.ICallback;
import com.onedrive.sdk.core.ClientException;
import com.onedrive.sdk.extensions.ISearchCollectionPage;
import com.onedrive.sdk.extensions.ISearchRequest;

public interface IBaseSearchRequest {
    ISearchRequest expand(String str);

    ISearchCollectionPage get() throws ClientException;

    void get(ICallback<ISearchCollectionPage> iCallback);

    ISearchRequest select(String str);

    ISearchRequest top(int i);
}
