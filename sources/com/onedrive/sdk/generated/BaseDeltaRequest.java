package com.onedrive.sdk.generated;

import com.onedrive.sdk.concurrency.ICallback;
import com.onedrive.sdk.concurrency.IExecutors;
import com.onedrive.sdk.core.ClientException;
import com.onedrive.sdk.extensions.DeltaCollectionPage;
import com.onedrive.sdk.extensions.DeltaRequest;
import com.onedrive.sdk.extensions.DeltaRequestBuilder;
import com.onedrive.sdk.extensions.IDeltaCollectionPage;
import com.onedrive.sdk.extensions.IDeltaRequest;
import com.onedrive.sdk.extensions.IOneDriveClient;
import com.onedrive.sdk.http.BaseCollectionRequest;
import com.onedrive.sdk.options.Option;
import com.onedrive.sdk.options.QueryOption;
import java.util.List;

public class BaseDeltaRequest extends BaseCollectionRequest<BaseDeltaCollectionResponse, IDeltaCollectionPage> implements IBaseDeltaRequest {
    public BaseDeltaRequest(String str, IOneDriveClient iOneDriveClient, List<Option> list, String str2) {
        super(str, iOneDriveClient, list, BaseDeltaCollectionResponse.class, IDeltaCollectionPage.class);
        if (str2 != null) {
            addQueryOption(new QueryOption("token", str2));
        }
    }

    public void get(final ICallback<IDeltaCollectionPage> iCallback) {
        final IExecutors executors = getBaseRequest().getClient().getExecutors();
        executors.performOnBackground(new Runnable() {
            public void run() {
                try {
                    executors.performOnForeground(BaseDeltaRequest.this.get(), iCallback);
                } catch (ClientException e) {
                    executors.performOnForeground(e, iCallback);
                }
            }
        });
    }

    public IDeltaCollectionPage get() throws ClientException {
        return buildFromResponse((BaseDeltaCollectionResponse) send());
    }

    public IDeltaRequest select(String str) {
        addQueryOption(new QueryOption("select", str));
        return (DeltaRequest) this;
    }

    public IDeltaRequest top(int i) {
        StringBuilder sb = new StringBuilder();
        sb.append(i);
        sb.append("");
        addQueryOption(new QueryOption("top", sb.toString()));
        return (DeltaRequest) this;
    }

    public IDeltaRequest expand(String str) {
        addQueryOption(new QueryOption("expand", str));
        return (DeltaRequest) this;
    }

    public IDeltaCollectionPage buildFromResponse(BaseDeltaCollectionResponse baseDeltaCollectionResponse) {
        DeltaCollectionPage deltaCollectionPage = new DeltaCollectionPage(baseDeltaCollectionResponse, baseDeltaCollectionResponse.nextLink != null ? new DeltaRequestBuilder(baseDeltaCollectionResponse.nextLink, getBaseRequest().getClient(), null, null) : null);
        deltaCollectionPage.setRawObject(baseDeltaCollectionResponse.getSerializer(), baseDeltaCollectionResponse.getRawObject());
        return deltaCollectionPage;
    }
}
