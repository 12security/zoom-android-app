package com.onedrive.sdk.extensions;

import com.onedrive.sdk.generated.BaseDriveCollectionRequestBuilder;
import com.onedrive.sdk.options.Option;
import java.util.List;

public class DriveCollectionRequestBuilder extends BaseDriveCollectionRequestBuilder implements IDriveCollectionRequestBuilder {
    public DriveCollectionRequestBuilder(String str, IOneDriveClient iOneDriveClient, List<Option> list) {
        super(str, iOneDriveClient, list);
    }
}
