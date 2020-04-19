package com.onedrive.sdk.extensions;

import com.onedrive.sdk.generated.BasePermissionCollectionRequestBuilder;
import com.onedrive.sdk.options.Option;
import java.util.List;

public class PermissionCollectionRequestBuilder extends BasePermissionCollectionRequestBuilder implements IPermissionCollectionRequestBuilder {
    public PermissionCollectionRequestBuilder(String str, IOneDriveClient iOneDriveClient, List<Option> list) {
        super(str, iOneDriveClient, list);
    }
}
