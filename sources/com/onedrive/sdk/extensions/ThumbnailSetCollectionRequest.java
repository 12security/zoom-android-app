package com.onedrive.sdk.extensions;

import com.onedrive.sdk.generated.BaseThumbnailSetCollectionRequest;
import com.onedrive.sdk.options.Option;
import java.util.List;

public class ThumbnailSetCollectionRequest extends BaseThumbnailSetCollectionRequest implements IThumbnailSetCollectionRequest {
    public ThumbnailSetCollectionRequest(String str, IOneDriveClient iOneDriveClient, List<Option> list) {
        super(str, iOneDriveClient, list);
    }
}
