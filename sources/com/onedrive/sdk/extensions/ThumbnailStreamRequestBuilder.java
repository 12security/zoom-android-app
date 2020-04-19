package com.onedrive.sdk.extensions;

import com.onedrive.sdk.generated.BaseThumbnailStreamRequestBuilder;
import com.onedrive.sdk.options.Option;
import java.util.List;

public class ThumbnailStreamRequestBuilder extends BaseThumbnailStreamRequestBuilder implements IThumbnailStreamRequestBuilder {
    public ThumbnailStreamRequestBuilder(String str, IOneDriveClient iOneDriveClient, List<Option> list) {
        super(str, iOneDriveClient, list);
    }
}
