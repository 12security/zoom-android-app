package com.onedrive.sdk.extensions;

import com.onedrive.sdk.generated.BaseThumbnailStreamRequest;
import com.onedrive.sdk.options.Option;
import java.util.List;

public class ThumbnailStreamRequest extends BaseThumbnailStreamRequest implements IThumbnailStreamRequest {
    public ThumbnailStreamRequest(String str, IOneDriveClient iOneDriveClient, List<Option> list) {
        super(str, iOneDriveClient, list);
    }
}
