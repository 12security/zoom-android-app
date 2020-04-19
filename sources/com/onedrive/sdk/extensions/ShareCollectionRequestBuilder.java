package com.onedrive.sdk.extensions;

import com.onedrive.sdk.generated.BaseShareCollectionRequestBuilder;
import com.onedrive.sdk.options.Option;
import java.util.List;

public class ShareCollectionRequestBuilder extends BaseShareCollectionRequestBuilder implements IShareCollectionRequestBuilder {
    public ShareCollectionRequestBuilder(String str, IOneDriveClient iOneDriveClient, List<Option> list) {
        super(str, iOneDriveClient, list);
    }
}
