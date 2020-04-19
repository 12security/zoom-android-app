package com.onedrive.sdk.generated;

import com.onedrive.sdk.extensions.Drive;
import com.onedrive.sdk.extensions.IDriveCollectionRequestBuilder;
import com.onedrive.sdk.http.BaseCollectionPage;

public class BaseDriveCollectionPage extends BaseCollectionPage<Drive, IDriveCollectionRequestBuilder> implements IBaseDriveCollectionPage {
    public BaseDriveCollectionPage(BaseDriveCollectionResponse baseDriveCollectionResponse, IDriveCollectionRequestBuilder iDriveCollectionRequestBuilder) {
        super(baseDriveCollectionResponse.value, iDriveCollectionRequestBuilder);
    }
}
