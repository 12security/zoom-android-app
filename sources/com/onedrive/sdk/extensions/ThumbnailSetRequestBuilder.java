package com.onedrive.sdk.extensions;

import com.onedrive.sdk.generated.BaseThumbnailSetRequestBuilder;
import com.onedrive.sdk.options.Option;
import java.util.List;

public class ThumbnailSetRequestBuilder extends BaseThumbnailSetRequestBuilder implements IThumbnailSetRequestBuilder {
    public ThumbnailSetRequestBuilder(String str, IOneDriveClient iOneDriveClient, List<Option> list) {
        super(str, iOneDriveClient, list);
    }

    public IThumbnailRequestBuilder getThumbnailSize(String str) {
        return new ThumbnailRequestBuilder(getRequestUrlWithAdditionalSegment(str), getClient(), null);
    }
}
