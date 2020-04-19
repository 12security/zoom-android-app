package com.onedrive.sdk.generated;

import com.onedrive.sdk.extensions.IOneDriveClient;
import com.onedrive.sdk.extensions.IThumbnailStreamRequest;
import com.onedrive.sdk.extensions.ThumbnailStreamRequest;
import com.onedrive.sdk.http.BaseRequestBuilder;
import com.onedrive.sdk.options.Option;
import java.util.List;

public class BaseThumbnailStreamRequestBuilder extends BaseRequestBuilder implements IBaseThumbnailStreamRequestBuilder {
    public BaseThumbnailStreamRequestBuilder(String str, IOneDriveClient iOneDriveClient, List<Option> list) {
        super(str, iOneDriveClient, list);
    }

    public IThumbnailStreamRequest buildRequest() {
        return buildRequest(getOptions());
    }

    public IThumbnailStreamRequest buildRequest(List<Option> list) {
        return new ThumbnailStreamRequest(getRequestUrl(), getClient(), list);
    }
}
