package com.onedrive.sdk.extensions;

import com.onedrive.sdk.generated.BaseItemCollectionRequestBuilder;
import com.onedrive.sdk.options.Option;
import java.util.List;

public class ItemCollectionRequestBuilder extends BaseItemCollectionRequestBuilder implements IItemCollectionRequestBuilder {
    public ItemCollectionRequestBuilder(String str, IOneDriveClient iOneDriveClient, List<Option> list) {
        super(str, iOneDriveClient, list);
    }
}
