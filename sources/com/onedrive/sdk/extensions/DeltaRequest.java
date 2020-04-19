package com.onedrive.sdk.extensions;

import com.onedrive.sdk.generated.BaseDeltaRequest;
import com.onedrive.sdk.options.Option;
import java.util.List;

public class DeltaRequest extends BaseDeltaRequest implements IDeltaRequest {
    public DeltaRequest(String str, IOneDriveClient iOneDriveClient, List<Option> list, String str2) {
        super(str, iOneDriveClient, list, str2);
    }
}
