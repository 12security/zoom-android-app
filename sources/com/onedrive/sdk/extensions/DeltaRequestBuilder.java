package com.onedrive.sdk.extensions;

import com.onedrive.sdk.generated.BaseDeltaRequestBuilder;
import com.onedrive.sdk.options.Option;
import java.util.List;

public class DeltaRequestBuilder extends BaseDeltaRequestBuilder implements IDeltaRequestBuilder {
    public DeltaRequestBuilder(String str, IOneDriveClient iOneDriveClient, List<Option> list, String str2) {
        super(str, iOneDriveClient, list, str2);
    }
}
