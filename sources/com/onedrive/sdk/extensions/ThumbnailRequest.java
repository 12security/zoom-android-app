package com.onedrive.sdk.extensions;

import com.onedrive.sdk.generated.BaseThumbnailRequest;
import com.onedrive.sdk.options.Option;
import java.util.List;

public class ThumbnailRequest extends BaseThumbnailRequest implements IThumbnailRequest {
    public ThumbnailRequest(String str, IOneDriveClient iOneDriveClient, List<Option> list) {
        super(str, iOneDriveClient, list);
    }
}
