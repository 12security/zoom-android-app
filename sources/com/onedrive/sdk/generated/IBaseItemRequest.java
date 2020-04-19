package com.onedrive.sdk.generated;

import com.onedrive.sdk.concurrency.ICallback;
import com.onedrive.sdk.core.ClientException;
import com.onedrive.sdk.extensions.Item;
import com.onedrive.sdk.http.IHttpRequest;

public interface IBaseItemRequest extends IHttpRequest {
    @Deprecated
    Item create(Item item) throws ClientException;

    @Deprecated
    void create(Item item, ICallback<Item> iCallback);

    void delete() throws ClientException;

    void delete(ICallback<Void> iCallback);

    IBaseItemRequest expand(String str);

    Item get() throws ClientException;

    void get(ICallback<Item> iCallback);

    Item patch(Item item) throws ClientException;

    void patch(Item item, ICallback<Item> iCallback);

    Item post(Item item) throws ClientException;

    void post(Item item, ICallback<Item> iCallback);

    IBaseItemRequest select(String str);

    IBaseItemRequest top(int i);

    @Deprecated
    Item update(Item item) throws ClientException;

    @Deprecated
    void update(Item item, ICallback<Item> iCallback);
}
