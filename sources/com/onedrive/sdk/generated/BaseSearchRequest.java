package com.onedrive.sdk.generated;

import com.onedrive.sdk.concurrency.ICallback;
import com.onedrive.sdk.concurrency.IExecutors;
import com.onedrive.sdk.core.ClientException;
import com.onedrive.sdk.extensions.IOneDriveClient;
import com.onedrive.sdk.extensions.ISearchCollectionPage;
import com.onedrive.sdk.extensions.ISearchRequest;
import com.onedrive.sdk.extensions.SearchCollectionPage;
import com.onedrive.sdk.extensions.SearchRequest;
import com.onedrive.sdk.extensions.SearchRequestBuilder;
import com.onedrive.sdk.http.BaseCollectionRequest;
import com.onedrive.sdk.options.Option;
import com.onedrive.sdk.options.QueryOption;
import java.util.List;

public class BaseSearchRequest extends BaseCollectionRequest<BaseSearchCollectionResponse, ISearchCollectionPage> implements IBaseSearchRequest {
    public BaseSearchRequest(String str, IOneDriveClient iOneDriveClient, List<Option> list, String str2) {
        super(str, iOneDriveClient, list, BaseSearchCollectionResponse.class, ISearchCollectionPage.class);
        if (str2 != null) {
            addQueryOption(new QueryOption("q", str2));
        }
    }

    public void get(final ICallback<ISearchCollectionPage> iCallback) {
        final IExecutors executors = getBaseRequest().getClient().getExecutors();
        executors.performOnBackground(new Runnable() {
            public void run() {
                try {
                    executors.performOnForeground(BaseSearchRequest.this.get(), iCallback);
                } catch (ClientException e) {
                    executors.performOnForeground(e, iCallback);
                }
            }
        });
    }

    public ISearchCollectionPage get() throws ClientException {
        return buildFromResponse((BaseSearchCollectionResponse) send());
    }

    public ISearchRequest select(String str) {
        addQueryOption(new QueryOption("select", str));
        return (SearchRequest) this;
    }

    public ISearchRequest top(int i) {
        StringBuilder sb = new StringBuilder();
        sb.append(i);
        sb.append("");
        addQueryOption(new QueryOption("top", sb.toString()));
        return (SearchRequest) this;
    }

    public ISearchRequest expand(String str) {
        addQueryOption(new QueryOption("expand", str));
        return (SearchRequest) this;
    }

    public ISearchCollectionPage buildFromResponse(BaseSearchCollectionResponse baseSearchCollectionResponse) {
        SearchCollectionPage searchCollectionPage = new SearchCollectionPage(baseSearchCollectionResponse, baseSearchCollectionResponse.nextLink != null ? new SearchRequestBuilder(baseSearchCollectionResponse.nextLink, getBaseRequest().getClient(), null, null) : null);
        searchCollectionPage.setRawObject(baseSearchCollectionResponse.getSerializer(), baseSearchCollectionResponse.getRawObject());
        return searchCollectionPage;
    }
}
