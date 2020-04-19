package com.onedrive.sdk.extensions;

import com.onedrive.sdk.generated.BaseDriveRequestBuilder;
import com.onedrive.sdk.options.Option;
import java.util.List;

public class DriveRequestBuilder extends BaseDriveRequestBuilder implements IDriveRequestBuilder {
    public DriveRequestBuilder(String str, IOneDriveClient iOneDriveClient, List<Option> list) {
        super(str, iOneDriveClient, list);
    }

    public IItemRequestBuilder getRoot() {
        return new ItemRequestBuilder(getRequestUrlWithAdditionalSegment("root"), getClient(), null);
    }
}
