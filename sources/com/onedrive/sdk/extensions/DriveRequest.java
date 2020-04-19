package com.onedrive.sdk.extensions;

import com.onedrive.sdk.generated.BaseDriveRequest;
import com.onedrive.sdk.options.Option;
import java.util.List;

public class DriveRequest extends BaseDriveRequest implements IDriveRequest {
    public DriveRequest(String str, IOneDriveClient iOneDriveClient, List<Option> list) {
        super(str, iOneDriveClient, list);
    }
}
