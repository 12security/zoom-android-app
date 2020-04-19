package com.onedrive.sdk.generated;

import com.onedrive.sdk.extensions.IOneDriveClient;
import com.onedrive.sdk.extensions.IThumbnailSetCollectionRequest;
import com.onedrive.sdk.extensions.IThumbnailSetRequestBuilder;
import com.onedrive.sdk.extensions.ThumbnailSetCollectionRequest;
import com.onedrive.sdk.extensions.ThumbnailSetRequestBuilder;
import com.onedrive.sdk.http.BaseRequestBuilder;
import com.onedrive.sdk.options.Option;
import java.util.List;

public class BaseThumbnailSetCollectionRequestBuilder extends BaseRequestBuilder implements IBaseThumbnailSetCollectionRequestBuilder {
    public BaseThumbnailSetCollectionRequestBuilder(String str, IOneDriveClient iOneDriveClient, List<Option> list) {
        super(str, iOneDriveClient, list);
    }

    public IThumbnailSetCollectionRequest buildRequest() {
        return buildRequest(getOptions());
    }

    public IThumbnailSetCollectionRequest buildRequest(List<Option> list) {
        return new ThumbnailSetCollectionRequest(getRequestUrl(), getClient(), list);
    }

    public IThumbnailSetRequestBuilder byId(String str) {
        return new ThumbnailSetRequestBuilder(getRequestUrlWithAdditionalSegment(str), getClient(), getOptions());
    }
}
