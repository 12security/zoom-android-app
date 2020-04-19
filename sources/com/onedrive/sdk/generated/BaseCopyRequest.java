package com.onedrive.sdk.generated;

import com.onedrive.sdk.concurrency.AsyncMonitor;
import com.onedrive.sdk.concurrency.AsyncMonitorLocation;
import com.onedrive.sdk.concurrency.ICallback;
import com.onedrive.sdk.concurrency.ResultGetter;
import com.onedrive.sdk.core.ClientException;
import com.onedrive.sdk.extensions.CopyBody;
import com.onedrive.sdk.extensions.CopyRequest;
import com.onedrive.sdk.extensions.ICopyRequest;
import com.onedrive.sdk.extensions.IOneDriveClient;
import com.onedrive.sdk.extensions.Item;
import com.onedrive.sdk.extensions.ItemReference;
import com.onedrive.sdk.extensions.ItemRequest;
import com.onedrive.sdk.http.BaseRequest;
import com.onedrive.sdk.http.HttpMethod;
import com.onedrive.sdk.options.Option;
import com.onedrive.sdk.options.QueryOption;
import java.util.List;

public class BaseCopyRequest extends BaseRequest implements IBaseCopyRequest {
    protected final CopyBody mBody = new CopyBody();

    public BaseCopyRequest(String str, IOneDriveClient iOneDriveClient, List<Option> list, String str2, ItemReference itemReference) {
        super(str, iOneDriveClient, list, AsyncMonitorLocation.class);
        CopyBody copyBody = this.mBody;
        copyBody.name = str2;
        copyBody.parentReference = itemReference;
        addHeader("Prefer", "respond-async");
    }

    @Deprecated
    public void create(ICallback<AsyncMonitor<Item>> iCallback) {
        post(iCallback);
    }

    @Deprecated
    public AsyncMonitor<Item> create() throws ClientException {
        return post();
    }

    public void post(final ICallback<AsyncMonitor<Item>> iCallback) {
        getClient().getExecutors().performOnBackground(new Runnable() {
            public void run() {
                try {
                    BaseCopyRequest.this.getClient().getExecutors().performOnForeground(BaseCopyRequest.this.post(), iCallback);
                } catch (ClientException e) {
                    BaseCopyRequest.this.getClient().getExecutors().performOnForeground(e, iCallback);
                }
            }
        });
    }

    public AsyncMonitor<Item> post() throws ClientException {
        return new AsyncMonitor<>(getClient(), (AsyncMonitorLocation) send(HttpMethod.POST, this.mBody), new ResultGetter<Item>() {
            public Item getResultFrom(String str, IOneDriveClient iOneDriveClient) {
                return new ItemRequest(str, iOneDriveClient, null).get();
            }
        });
    }

    public ICopyRequest select(String str) {
        getQueryOptions().add(new QueryOption("select", str));
        return (CopyRequest) this;
    }

    public ICopyRequest top(int i) {
        List queryOptions = getQueryOptions();
        StringBuilder sb = new StringBuilder();
        sb.append(i);
        sb.append("");
        queryOptions.add(new QueryOption("top", sb.toString()));
        return (CopyRequest) this;
    }

    public ICopyRequest expand(String str) {
        getQueryOptions().add(new QueryOption("expand", str));
        return (CopyRequest) this;
    }
}
