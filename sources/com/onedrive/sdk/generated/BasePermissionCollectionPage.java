package com.onedrive.sdk.generated;

import com.onedrive.sdk.extensions.IPermissionCollectionRequestBuilder;
import com.onedrive.sdk.extensions.Permission;
import com.onedrive.sdk.http.BaseCollectionPage;

public class BasePermissionCollectionPage extends BaseCollectionPage<Permission, IPermissionCollectionRequestBuilder> implements IBasePermissionCollectionPage {
    public BasePermissionCollectionPage(BasePermissionCollectionResponse basePermissionCollectionResponse, IPermissionCollectionRequestBuilder iPermissionCollectionRequestBuilder) {
        super(basePermissionCollectionResponse.value, iPermissionCollectionRequestBuilder);
    }
}
