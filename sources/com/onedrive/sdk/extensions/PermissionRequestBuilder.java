package com.onedrive.sdk.extensions;

import com.onedrive.sdk.generated.BasePermissionRequestBuilder;
import com.onedrive.sdk.options.Option;
import java.util.List;

public class PermissionRequestBuilder extends BasePermissionRequestBuilder implements IPermissionRequestBuilder {
    public PermissionRequestBuilder(String str, IOneDriveClient iOneDriveClient, List<Option> list) {
        super(str, iOneDriveClient, list);
    }
}
