package com.onedrive.sdk.generated;

import com.onedrive.sdk.extensions.IOneDriveClient;
import com.onedrive.sdk.extensions.IShareCollectionPage;
import com.onedrive.sdk.extensions.IShareCollectionRequest;
import com.onedrive.sdk.extensions.ShareCollectionPage;
import com.onedrive.sdk.extensions.ShareCollectionRequest;
import com.onedrive.sdk.extensions.ShareCollectionRequestBuilder;
import com.onedrive.sdk.http.BaseCollectionRequest;
import com.onedrive.sdk.options.Option;
import com.onedrive.sdk.options.QueryOption;
import java.util.List;

public class BaseShareCollectionRequest extends BaseCollectionRequest<BaseShareCollectionResponse, IShareCollectionPage> implements IBaseShareCollectionRequest {
    public BaseShareCollectionRequest(String str, IOneDriveClient iOneDriveClient, List<Option> list) {
        super(str, iOneDriveClient, list, BaseShareCollectionResponse.class, IShareCollectionPage.class);
    }

    public IShareCollectionRequest expand(String str) {
        addQueryOption(new QueryOption("expand", str));
        return (ShareCollectionRequest) this;
    }

    public IShareCollectionRequest select(String str) {
        addQueryOption(new QueryOption("select", str));
        return (ShareCollectionRequest) this;
    }

    public IShareCollectionRequest top(int i) {
        StringBuilder sb = new StringBuilder();
        sb.append(i);
        sb.append("");
        addQueryOption(new QueryOption("top", sb.toString()));
        return (ShareCollectionRequest) this;
    }

    public IShareCollectionPage buildFromResponse(BaseShareCollectionResponse baseShareCollectionResponse) {
        ShareCollectionPage shareCollectionPage = new ShareCollectionPage(baseShareCollectionResponse, baseShareCollectionResponse.nextLink != null ? new ShareCollectionRequestBuilder(baseShareCollectionResponse.nextLink, getBaseRequest().getClient(), null) : null);
        shareCollectionPage.setRawObject(baseShareCollectionResponse.getSerializer(), baseShareCollectionResponse.getRawObject());
        return shareCollectionPage;
    }
}
