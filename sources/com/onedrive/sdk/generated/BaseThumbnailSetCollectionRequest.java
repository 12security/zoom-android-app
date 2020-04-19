package com.onedrive.sdk.generated;

import com.onedrive.sdk.concurrency.ICallback;
import com.onedrive.sdk.concurrency.IExecutors;
import com.onedrive.sdk.core.ClientException;
import com.onedrive.sdk.extensions.IOneDriveClient;
import com.onedrive.sdk.extensions.IThumbnailSetCollectionPage;
import com.onedrive.sdk.extensions.IThumbnailSetCollectionRequest;
import com.onedrive.sdk.extensions.ThumbnailSetCollectionPage;
import com.onedrive.sdk.extensions.ThumbnailSetCollectionRequest;
import com.onedrive.sdk.extensions.ThumbnailSetCollectionRequestBuilder;
import com.onedrive.sdk.http.BaseCollectionRequest;
import com.onedrive.sdk.options.Option;
import com.onedrive.sdk.options.QueryOption;
import java.util.List;

public class BaseThumbnailSetCollectionRequest extends BaseCollectionRequest<BaseThumbnailSetCollectionResponse, IThumbnailSetCollectionPage> implements IBaseThumbnailSetCollectionRequest {
    public BaseThumbnailSetCollectionRequest(String str, IOneDriveClient iOneDriveClient, List<Option> list) {
        super(str, iOneDriveClient, list, BaseThumbnailSetCollectionResponse.class, IThumbnailSetCollectionPage.class);
    }

    public void get(final ICallback<IThumbnailSetCollectionPage> iCallback) {
        final IExecutors executors = getBaseRequest().getClient().getExecutors();
        executors.performOnBackground(new Runnable() {
            public void run() {
                try {
                    executors.performOnForeground(BaseThumbnailSetCollectionRequest.this.get(), iCallback);
                } catch (ClientException e) {
                    executors.performOnForeground(e, iCallback);
                }
            }
        });
    }

    public IThumbnailSetCollectionPage get() throws ClientException {
        return buildFromResponse((BaseThumbnailSetCollectionResponse) send());
    }

    public IThumbnailSetCollectionRequest expand(String str) {
        addQueryOption(new QueryOption("expand", str));
        return (ThumbnailSetCollectionRequest) this;
    }

    public IThumbnailSetCollectionRequest select(String str) {
        addQueryOption(new QueryOption("select", str));
        return (ThumbnailSetCollectionRequest) this;
    }

    public IThumbnailSetCollectionRequest top(int i) {
        StringBuilder sb = new StringBuilder();
        sb.append(i);
        sb.append("");
        addQueryOption(new QueryOption("top", sb.toString()));
        return (ThumbnailSetCollectionRequest) this;
    }

    public IThumbnailSetCollectionPage buildFromResponse(BaseThumbnailSetCollectionResponse baseThumbnailSetCollectionResponse) {
        ThumbnailSetCollectionPage thumbnailSetCollectionPage = new ThumbnailSetCollectionPage(baseThumbnailSetCollectionResponse, baseThumbnailSetCollectionResponse.nextLink != null ? new ThumbnailSetCollectionRequestBuilder(baseThumbnailSetCollectionResponse.nextLink, getBaseRequest().getClient(), null) : null);
        thumbnailSetCollectionPage.setRawObject(baseThumbnailSetCollectionResponse.getSerializer(), baseThumbnailSetCollectionResponse.getRawObject());
        return thumbnailSetCollectionPage;
    }
}
