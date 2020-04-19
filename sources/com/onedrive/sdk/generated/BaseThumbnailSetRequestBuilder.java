package com.onedrive.sdk.generated;

import com.onedrive.sdk.extensions.IOneDriveClient;
import com.onedrive.sdk.extensions.IThumbnailSetRequest;
import com.onedrive.sdk.extensions.ThumbnailSetRequest;
import com.onedrive.sdk.http.BaseRequestBuilder;
import com.onedrive.sdk.options.Option;
import java.util.List;

public class BaseThumbnailSetRequestBuilder extends BaseRequestBuilder implements IBaseThumbnailSetRequestBuilder {
    public BaseThumbnailSetRequestBuilder(String str, IOneDriveClient iOneDriveClient, List<Option> list) {
        super(str, iOneDriveClient, list);
    }

    public IThumbnailSetRequest buildRequest() {
        return buildRequest(getOptions());
    }

    public IThumbnailSetRequest buildRequest(List<Option> list) {
        return new ThumbnailSetRequest(getRequestUrl(), getClient(), list);
    }
}
