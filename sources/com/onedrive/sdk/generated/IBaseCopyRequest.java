package com.onedrive.sdk.generated;

import com.onedrive.sdk.concurrency.AsyncMonitor;
import com.onedrive.sdk.concurrency.ICallback;
import com.onedrive.sdk.core.ClientException;
import com.onedrive.sdk.extensions.ICopyRequest;
import com.onedrive.sdk.extensions.Item;

public interface IBaseCopyRequest {
    @Deprecated
    AsyncMonitor<Item> create() throws ClientException;

    @Deprecated
    void create(ICallback<AsyncMonitor<Item>> iCallback);

    ICopyRequest expand(String str);

    AsyncMonitor<Item> post() throws ClientException;

    void post(ICallback<AsyncMonitor<Item>> iCallback);

    ICopyRequest select(String str);

    ICopyRequest top(int i);
}
