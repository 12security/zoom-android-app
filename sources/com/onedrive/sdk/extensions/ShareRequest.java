package com.onedrive.sdk.extensions;

import com.onedrive.sdk.generated.BaseShareRequest;
import com.onedrive.sdk.options.Option;
import java.util.List;

public class ShareRequest extends BaseShareRequest implements IShareRequest {
    public ShareRequest(String str, IOneDriveClient iOneDriveClient, List<Option> list) {
        super(str, iOneDriveClient, list);
    }
}
