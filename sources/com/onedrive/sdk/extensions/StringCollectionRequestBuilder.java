package com.onedrive.sdk.extensions;

import com.onedrive.sdk.generated.BaseStringCollectionRequestBuilder;
import com.onedrive.sdk.options.Option;
import java.util.List;

public class StringCollectionRequestBuilder extends BaseStringCollectionRequestBuilder implements IStringCollectionRequestBuilder {
    public StringCollectionRequestBuilder(String str, IOneDriveClient iOneDriveClient, List<Option> list) {
        super(str, iOneDriveClient, list);
    }
}
