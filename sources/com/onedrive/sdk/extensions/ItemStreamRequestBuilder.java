package com.onedrive.sdk.extensions;

import com.onedrive.sdk.generated.BaseItemStreamRequestBuilder;
import com.onedrive.sdk.options.Option;
import java.util.List;

public class ItemStreamRequestBuilder extends BaseItemStreamRequestBuilder implements IItemStreamRequestBuilder {
    public ItemStreamRequestBuilder(String str, IOneDriveClient iOneDriveClient, List<Option> list) {
        super(str, iOneDriveClient, list);
    }
}
