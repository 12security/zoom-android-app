package com.onedrive.sdk.extensions;

import com.onedrive.sdk.generated.BaseItemStreamRequest;
import com.onedrive.sdk.options.Option;
import java.util.List;

public class ItemStreamRequest extends BaseItemStreamRequest implements IItemStreamRequest {
    public ItemStreamRequest(String str, IOneDriveClient iOneDriveClient, List<Option> list) {
        super(str, iOneDriveClient, list);
    }
}
