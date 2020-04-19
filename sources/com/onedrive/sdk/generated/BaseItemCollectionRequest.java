package com.onedrive.sdk.generated;

import com.onedrive.sdk.concurrency.ICallback;
import com.onedrive.sdk.concurrency.IExecutors;
import com.onedrive.sdk.core.ClientException;
import com.onedrive.sdk.extensions.IItemCollectionPage;
import com.onedrive.sdk.extensions.IItemCollectionRequest;
import com.onedrive.sdk.extensions.IOneDriveClient;
import com.onedrive.sdk.extensions.Item;
import com.onedrive.sdk.extensions.ItemCollectionPage;
import com.onedrive.sdk.extensions.ItemCollectionRequest;
import com.onedrive.sdk.extensions.ItemCollectionRequestBuilder;
import com.onedrive.sdk.extensions.ItemRequestBuilder;
import com.onedrive.sdk.http.BaseCollectionRequest;
import com.onedrive.sdk.options.Option;
import com.onedrive.sdk.options.QueryOption;
import java.util.List;

public class BaseItemCollectionRequest extends BaseCollectionRequest<BaseItemCollectionResponse, IItemCollectionPage> implements IBaseItemCollectionRequest {
    public BaseItemCollectionRequest(String str, IOneDriveClient iOneDriveClient, List<Option> list) {
        super(str, iOneDriveClient, list, BaseItemCollectionResponse.class, IItemCollectionPage.class);
    }

    public void get(final ICallback<IItemCollectionPage> iCallback) {
        final IExecutors executors = getBaseRequest().getClient().getExecutors();
        executors.performOnBackground(new Runnable() {
            public void run() {
                try {
                    executors.performOnForeground(BaseItemCollectionRequest.this.get(), iCallback);
                } catch (ClientException e) {
                    executors.performOnForeground(e, iCallback);
                }
            }
        });
    }

    public IItemCollectionPage get() throws ClientException {
        return buildFromResponse((BaseItemCollectionResponse) send());
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
        new ItemRequestBuilder(getBaseRequest().getRequestUrl().toString(), getBaseRequest().getClient(), null).buildRequest().post(item, iCallback);
    }

    public Item post(Item item) throws ClientException {
        return new ItemRequestBuilder(getBaseRequest().getRequestUrl().toString(), getBaseRequest().getClient(), null).buildRequest().post(item);
    }

    public IItemCollectionRequest expand(String str) {
        addQueryOption(new QueryOption("expand", str));
        return (ItemCollectionRequest) this;
    }

    public IItemCollectionRequest select(String str) {
        addQueryOption(new QueryOption("select", str));
        return (ItemCollectionRequest) this;
    }

    public IItemCollectionRequest top(int i) {
        StringBuilder sb = new StringBuilder();
        sb.append(i);
        sb.append("");
        addQueryOption(new QueryOption("top", sb.toString()));
        return (ItemCollectionRequest) this;
    }

    public IItemCollectionPage buildFromResponse(BaseItemCollectionResponse baseItemCollectionResponse) {
        ItemCollectionPage itemCollectionPage = new ItemCollectionPage(baseItemCollectionResponse, baseItemCollectionResponse.nextLink != null ? new ItemCollectionRequestBuilder(baseItemCollectionResponse.nextLink, getBaseRequest().getClient(), null) : null);
        itemCollectionPage.setRawObject(baseItemCollectionResponse.getSerializer(), baseItemCollectionResponse.getRawObject());
        return itemCollectionPage;
    }
}
