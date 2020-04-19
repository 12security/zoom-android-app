package com.onedrive.sdk.extensions;

import com.onedrive.sdk.generated.BaseDriveCollectionPage;
import com.onedrive.sdk.generated.BaseDriveCollectionResponse;

public class DriveCollectionPage extends BaseDriveCollectionPage implements IDriveCollectionPage {
    public DriveCollectionPage(BaseDriveCollectionResponse baseDriveCollectionResponse, IDriveCollectionRequestBuilder iDriveCollectionRequestBuilder) {
        super(baseDriveCollectionResponse, iDriveCollectionRequestBuilder);
    }
}
