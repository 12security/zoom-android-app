package com.onedrive.sdk.extensions;

import com.onedrive.sdk.generated.BaseThumbnailSetCollectionRequestBuilder;
import com.onedrive.sdk.options.Option;
import java.util.List;

public class ThumbnailSetCollectionRequestBuilder extends BaseThumbnailSetCollectionRequestBuilder implements IThumbnailSetCollectionRequestBuilder {
    public ThumbnailSetCollectionRequestBuilder(String str, IOneDriveClient iOneDriveClient, List<Option> list) {
        super(str, iOneDriveClient, list);
    }
}
