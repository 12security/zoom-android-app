package com.onedrive.sdk.extensions;

import com.onedrive.sdk.generated.BaseItemRequest;
import com.onedrive.sdk.options.Option;
import java.util.List;

public class ItemRequest extends BaseItemRequest implements IItemRequest {
    public ItemRequest(String str, IOneDriveClient iOneDriveClient, List<Option> list) {
        super(str, iOneDriveClient, list);
    }
}
