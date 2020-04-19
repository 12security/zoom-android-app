package com.onedrive.sdk.extensions;

import com.onedrive.sdk.generated.BasePermissionRequest;
import com.onedrive.sdk.options.Option;
import java.util.List;

public class PermissionRequest extends BasePermissionRequest implements IPermissionRequest {
    public PermissionRequest(String str, IOneDriveClient iOneDriveClient, List<Option> list) {
        super(str, iOneDriveClient, list);
    }
}
