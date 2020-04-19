package com.onedrive.sdk.generated;

import com.onedrive.sdk.concurrency.ICallback;
import com.onedrive.sdk.concurrency.IExecutors;
import com.onedrive.sdk.core.ClientException;
import com.onedrive.sdk.extensions.IOneDriveClient;
import com.onedrive.sdk.extensions.IRecentCollectionPage;
import com.onedrive.sdk.extensions.IRecentRequest;
import com.onedrive.sdk.extensions.RecentCollectionPage;
import com.onedrive.sdk.extensions.RecentRequest;
import com.onedrive.sdk.extensions.RecentRequestBuilder;
import com.onedrive.sdk.http.BaseCollectionRequest;
import com.onedrive.sdk.options.Option;
import com.onedrive.sdk.options.QueryOption;
import java.util.List;

public class BaseRecentRequest extends BaseCollectionRequest<BaseRecentCollectionResponse, IRecentCollectionPage> implements IBaseRecentRequest {
    public BaseRecentRequest(String str, IOneDriveClient iOneDriveClient, List<Option> list) {
        super(str, iOneDriveClient, list, BaseRecentCollectionResponse.class, IRecentCollectionPage.class);
    }

    public void get(final ICallback<IRecentCollectionPage> iCallback) {
        final IExecutors executors = getBaseRequest().getClient().getExecutors();
        executors.performOnBackground(new Runnable() {
            public void run() {
                try {
                    executors.performOnForeground(BaseRecentRequest.this.get(), iCallback);
                } catch (ClientException e) {
                    executors.performOnForeground(e, iCallback);
                }
            }
        });
    }

    public IRecentCollectionPage get() throws ClientException {
        return buildFromResponse((BaseRecentCollectionResponse) send());
    }

    public IRecentRequest select(String str) {
        addQueryOption(new QueryOption("select", str));
        return (RecentRequest) this;
    }

    public IRecentRequest top(int i) {
        StringBuilder sb = new StringBuilder();
        sb.append(i);
        sb.append("");
        addQueryOption(new QueryOption("top", sb.toString()));
        return (RecentRequest) this;
    }

    public IRecentRequest expand(String str) {
        addQueryOption(new QueryOption("expand", str));
        return (RecentRequest) this;
    }

    public IRecentCollectionPage buildFromResponse(BaseRecentCollectionResponse baseRecentCollectionResponse) {
        RecentCollectionPage recentCollectionPage = new RecentCollectionPage(baseRecentCollectionResponse, baseRecentCollectionResponse.nextLink != null ? new RecentRequestBuilder(baseRecentCollectionResponse.nextLink, getBaseRequest().getClient(), null) : null);
        recentCollectionPage.setRawObject(baseRecentCollectionResponse.getSerializer(), baseRecentCollectionResponse.getRawObject());
        return recentCollectionPage;
    }
}
