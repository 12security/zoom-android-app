package com.onedrive.sdk.extensions;

import com.onedrive.sdk.generated.BaseStringCollectionRequest;
import com.onedrive.sdk.options.Option;
import java.util.List;

public class StringCollectionRequest extends BaseStringCollectionRequest implements IStringCollectionRequest {
    public StringCollectionRequest(String str, IOneDriveClient iOneDriveClient, List<Option> list) {
        super(str, iOneDriveClient, list);
    }
}
