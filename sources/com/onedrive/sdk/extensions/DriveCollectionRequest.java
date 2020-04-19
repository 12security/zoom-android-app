package com.onedrive.sdk.extensions;

import com.onedrive.sdk.generated.BaseDriveCollectionRequest;
import com.onedrive.sdk.options.Option;
import java.util.List;

public class DriveCollectionRequest extends BaseDriveCollectionRequest implements IDriveCollectionRequest {
    public DriveCollectionRequest(String str, IOneDriveClient iOneDriveClient, List<Option> list) {
        super(str, iOneDriveClient, list);
    }
}
