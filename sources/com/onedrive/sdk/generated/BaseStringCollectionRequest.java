package com.onedrive.sdk.generated;

import com.onedrive.sdk.extensions.IOneDriveClient;
import com.onedrive.sdk.extensions.IStringCollectionPage;
import com.onedrive.sdk.extensions.IStringCollectionRequest;
import com.onedrive.sdk.extensions.StringCollectionPage;
import com.onedrive.sdk.extensions.StringCollectionRequest;
import com.onedrive.sdk.extensions.StringCollectionRequestBuilder;
import com.onedrive.sdk.http.BaseCollectionRequest;
import com.onedrive.sdk.options.Option;
import com.onedrive.sdk.options.QueryOption;
import java.util.List;

public class BaseStringCollectionRequest extends BaseCollectionRequest<BaseStringCollectionResponse, IStringCollectionPage> implements IBaseStringCollectionRequest {
    public BaseStringCollectionRequest(String str, IOneDriveClient iOneDriveClient, List<Option> list) {
        super(str, iOneDriveClient, list, BaseStringCollectionResponse.class, IStringCollectionPage.class);
    }

    public IStringCollectionRequest expand(String str) {
        addQueryOption(new QueryOption("expand", str));
        return (StringCollectionRequest) this;
    }

    public IStringCollectionRequest select(String str) {
        addQueryOption(new QueryOption("select", str));
        return (StringCollectionRequest) this;
    }

    public IStringCollectionRequest top(int i) {
        StringBuilder sb = new StringBuilder();
        sb.append(i);
        sb.append("");
        addQueryOption(new QueryOption("top", sb.toString()));
        return (StringCollectionRequest) this;
    }

    public IStringCollectionPage buildFromResponse(BaseStringCollectionResponse baseStringCollectionResponse) {
        StringCollectionPage stringCollectionPage = new StringCollectionPage(baseStringCollectionResponse, baseStringCollectionResponse.nextLink != null ? new StringCollectionRequestBuilder(baseStringCollectionResponse.nextLink, getBaseRequest().getClient(), null) : null);
        stringCollectionPage.setRawObject(baseStringCollectionResponse.getSerializer(), baseStringCollectionResponse.getRawObject());
        return stringCollectionPage;
    }
}
