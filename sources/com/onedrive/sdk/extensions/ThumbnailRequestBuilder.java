package com.onedrive.sdk.extensions;

import com.onedrive.sdk.generated.BaseThumbnailRequestBuilder;
import com.onedrive.sdk.options.Option;
import java.util.List;

public class ThumbnailRequestBuilder extends BaseThumbnailRequestBuilder implements IThumbnailRequestBuilder {
    public ThumbnailRequestBuilder(String str, IOneDriveClient iOneDriveClient, List<Option> list) {
        super(str, iOneDriveClient, list);
    }
}
