package com.onedrive.sdk.extensions;

import com.onedrive.sdk.generated.BaseCopyRequest;
import com.onedrive.sdk.options.Option;
import java.util.List;

public class CopyRequest extends BaseCopyRequest implements ICopyRequest {
    public CopyRequest(String str, IOneDriveClient iOneDriveClient, List<Option> list, String str2, ItemReference itemReference) {
        super(str, iOneDriveClient, list, str2, itemReference);
    }
}
