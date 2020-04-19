package com.onedrive.sdk.generated;

import com.onedrive.sdk.concurrency.ICallback;
import com.onedrive.sdk.concurrency.IExecutors;
import com.onedrive.sdk.core.ClientException;
import com.onedrive.sdk.extensions.DriveCollectionPage;
import com.onedrive.sdk.extensions.DriveCollectionRequest;
import com.onedrive.sdk.extensions.DriveCollectionRequestBuilder;
import com.onedrive.sdk.extensions.IDriveCollectionPage;
import com.onedrive.sdk.extensions.IDriveCollectionRequest;
import com.onedrive.sdk.extensions.IOneDriveClient;
import com.onedrive.sdk.http.BaseCollectionRequest;
import com.onedrive.sdk.options.Option;
import com.onedrive.sdk.options.QueryOption;
import java.util.List;

public class BaseDriveCollectionRequest extends BaseCollectionRequest<BaseDriveCollectionResponse, IDriveCollectionPage> implements IBaseDriveCollectionRequest {
    public BaseDriveCollectionRequest(String str, IOneDriveClient iOneDriveClient, List<Option> list) {
        super(str, iOneDriveClient, list, BaseDriveCollectionResponse.class, IDriveCollectionPage.class);
    }

    public void get(final ICallback<IDriveCollectionPage> iCallback) {
        final IExecutors executors = getBaseRequest().getClient().getExecutors();
        executors.performOnBackground(new Runnable() {
            public void run() {
                try {
                    executors.performOnForeground(BaseDriveCollectionRequest.this.get(), iCallback);
                } catch (ClientException e) {
                    executors.performOnForeground(e, iCallback);
                }
            }
        });
    }

    public IDriveCollectionPage get() throws ClientException {
        return buildFromResponse((BaseDriveCollectionResponse) send());
    }

    public IDriveCollectionRequest expand(String str) {
        addQueryOption(new QueryOption("expand", str));
        return (DriveCollectionRequest) this;
    }

    public IDriveCollectionRequest select(String str) {
        addQueryOption(new QueryOption("select", str));
        return (DriveCollectionRequest) this;
    }

    public IDriveCollectionRequest top(int i) {
        StringBuilder sb = new StringBuilder();
        sb.append(i);
        sb.append("");
        addQueryOption(new QueryOption("top", sb.toString()));
        return (DriveCollectionRequest) this;
    }

    public IDriveCollectionPage buildFromResponse(BaseDriveCollectionResponse baseDriveCollectionResponse) {
        DriveCollectionPage driveCollectionPage = new DriveCollectionPage(baseDriveCollectionResponse, baseDriveCollectionResponse.nextLink != null ? new DriveCollectionRequestBuilder(baseDriveCollectionResponse.nextLink, getBaseRequest().getClient(), null) : null);
        driveCollectionPage.setRawObject(baseDriveCollectionResponse.getSerializer(), baseDriveCollectionResponse.getRawObject());
        return driveCollectionPage;
    }
}
