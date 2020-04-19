package com.onedrive.sdk.generated;

import com.onedrive.sdk.concurrency.ICallback;
import com.onedrive.sdk.core.ClientException;
import com.onedrive.sdk.extensions.IItemCollectionPage;
import com.onedrive.sdk.extensions.IItemCollectionRequest;
import com.onedrive.sdk.extensions.Item;

public interface IBaseItemCollectionRequest {
    @Deprecated
    Item create(Item item) throws ClientException;

    @Deprecated
    void create(Item item, ICallback<Item> iCallback);

    IItemCollectionRequest expand(String str);

    IItemCollectionPage get() throws ClientException;

    void get(ICallback<IItemCollectionPage> iCallback);

    Item post(Item item) throws ClientException;

    void post(Item item, ICallback<Item> iCallback);

    IItemCollectionRequest select(String str);

    IItemCollectionRequest top(int i);
}
