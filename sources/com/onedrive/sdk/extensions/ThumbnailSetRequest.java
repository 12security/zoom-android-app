package com.onedrive.sdk.extensions;

import com.onedrive.sdk.generated.BaseThumbnailSetRequest;
import com.onedrive.sdk.options.Option;
import java.util.List;

public class ThumbnailSetRequest extends BaseThumbnailSetRequest implements IThumbnailSetRequest {
    public ThumbnailSetRequest(String str, IOneDriveClient iOneDriveClient, List<Option> list) {
        super(str, iOneDriveClient, list);
    }
}
