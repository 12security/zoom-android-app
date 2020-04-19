package com.onedrive.sdk.extensions;

import com.onedrive.sdk.generated.BaseShareCollectionRequest;
import com.onedrive.sdk.options.Option;
import java.util.List;

public class ShareCollectionRequest extends BaseShareCollectionRequest implements IShareCollectionRequest {
    public ShareCollectionRequest(String str, IOneDriveClient iOneDriveClient, List<Option> list) {
        super(str, iOneDriveClient, list);
    }
}
