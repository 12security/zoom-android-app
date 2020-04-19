package com.onedrive.sdk.generated;

import com.google.firebase.analytics.FirebaseAnalytics.Param;
import com.onedrive.sdk.extensions.IOneDriveClient;
import com.onedrive.sdk.extensions.IThumbnailRequest;
import com.onedrive.sdk.extensions.IThumbnailStreamRequestBuilder;
import com.onedrive.sdk.extensions.ThumbnailRequest;
import com.onedrive.sdk.extensions.ThumbnailStreamRequestBuilder;
import com.onedrive.sdk.http.BaseRequestBuilder;
import com.onedrive.sdk.options.Option;
import java.util.List;

public class BaseThumbnailRequestBuilder extends BaseRequestBuilder implements IBaseThumbnailRequestBuilder {
    public BaseThumbnailRequestBuilder(String str, IOneDriveClient iOneDriveClient, List<Option> list) {
        super(str, iOneDriveClient, list);
    }

    public IThumbnailRequest buildRequest() {
        return buildRequest(getOptions());
    }

    public IThumbnailRequest buildRequest(List<Option> list) {
        return new ThumbnailRequest(getRequestUrl(), getClient(), list);
    }

    public IThumbnailStreamRequestBuilder getContent() {
        return new ThumbnailStreamRequestBuilder(getRequestUrlWithAdditionalSegment(Param.CONTENT), getClient(), null);
    }
}
