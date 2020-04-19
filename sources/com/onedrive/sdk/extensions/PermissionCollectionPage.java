package com.onedrive.sdk.extensions;

import com.onedrive.sdk.generated.BasePermissionCollectionPage;
import com.onedrive.sdk.generated.BasePermissionCollectionResponse;

public class PermissionCollectionPage extends BasePermissionCollectionPage implements IPermissionCollectionPage {
    public PermissionCollectionPage(BasePermissionCollectionResponse basePermissionCollectionResponse, IPermissionCollectionRequestBuilder iPermissionCollectionRequestBuilder) {
        super(basePermissionCollectionResponse, iPermissionCollectionRequestBuilder);
    }
}
