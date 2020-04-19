package com.onedrive.sdk.extensions;

import com.onedrive.sdk.generated.BaseItemCollectionRequest;
import com.onedrive.sdk.options.Option;
import java.util.List;

public class ItemCollectionRequest extends BaseItemCollectionRequest implements IItemCollectionRequest {
    public ItemCollectionRequest(String str, IOneDriveClient iOneDriveClient, List<Option> list) {
        super(str, iOneDriveClient, list);
    }
}
