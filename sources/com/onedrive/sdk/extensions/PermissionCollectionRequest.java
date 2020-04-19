package com.onedrive.sdk.extensions;

import com.onedrive.sdk.generated.BasePermissionCollectionRequest;
import com.onedrive.sdk.options.Option;
import java.util.List;

public class PermissionCollectionRequest extends BasePermissionCollectionRequest implements IPermissionCollectionRequest {
    public PermissionCollectionRequest(String str, IOneDriveClient iOneDriveClient, List<Option> list) {
        super(str, iOneDriveClient, list);
    }
}
