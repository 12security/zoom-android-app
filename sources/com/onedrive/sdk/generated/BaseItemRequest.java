package com.onedrive.sdk.generated;

import com.onedrive.sdk.concurrency.ICallback;
import com.onedrive.sdk.core.ClientException;
import com.onedrive.sdk.extensions.IItemRequest;
import com.onedrive.sdk.extensions.IOneDriveClient;
import com.onedrive.sdk.extensions.Item;
import com.onedrive.sdk.extensions.ItemRequest;
import com.onedrive.sdk.http.BaseRequest;
import com.onedrive.sdk.http.HttpMethod;
import com.onedrive.sdk.options.Option;
import com.onedrive.sdk.options.QueryOption;
import java.util.List;

public class BaseItemRequest extends BaseRequest implements IBaseItemRequest {
    public BaseItemRequest(String str, IOneDriveClient iOneDriveClient, List<Option> list) {
        super(str, iOneDriveClient, list, Item.class);
    }

    public void get(ICallback<Item> iCallback) {
        send(HttpMethod.GET, iCallback, null);
    }

    public Item get() throws ClientException {
        return (Item) send(HttpMethod.GET, null);
    }

    @Deprecated
    public void update(Item item, ICallback<Item> iCallback) {
        patch(item, iCallback);
    }

    @Deprecated
    public Item update(Item item) throws ClientException {
        return patch(item);
    }

    public void patch(Item item, ICallback<Item> iCallback) {
        send(HttpMethod.PATCH, iCallback, item);
    }

    public Item patch(Item item) throws ClientException {
        return (Item) send(HttpMethod.PATCH, item);
    }

    public void delete(ICallback<Void> iCallback) {
        send(HttpMethod.DELETE, iCallback, null);
    }

    public void delete() throws ClientException {
        send(HttpMethod.DELETE, null);
    }

    @Deprecated
    public void create(Item item, ICallback<Item> iCallback) {
        post(item, iCallback);
    }

    @Deprecated
    public Item create(Item item) throws ClientException {
        return post(item);
    }

    public void post(Item item, ICallback<Item> iCallback) {
        send(HttpMethod.POST, iCallback, item);
    }

    public Item post(Item item) throws ClientException {
        return (Item) send(HttpMethod.POST, item);
    }

    public IItemRequest select(String str) {
        getQueryOptions().add(new QueryOption("select", str));
        return (ItemRequest) this;
    }

    public IItemRequest top(int i) {
        List queryOptions = getQueryOptions();
        StringBuilder sb = new StringBuilder();
        sb.append(i);
        sb.append("");
        queryOptions.add(new QueryOption("top", sb.toString()));
        return (ItemRequest) this;
    }

    public IItemRequest expand(String str) {
        getQueryOptions().add(new QueryOption("expand", str));
        return (ItemRequest) this;
    }
}
