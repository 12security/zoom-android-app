package com.onedrive.sdk.extensions;

import com.onedrive.sdk.generated.BaseItemRequestBuilder;
import com.onedrive.sdk.options.Option;
import java.util.List;

public class ItemRequestBuilder extends BaseItemRequestBuilder implements IItemRequestBuilder {
    public ItemRequestBuilder(String str, IOneDriveClient iOneDriveClient, List<Option> list) {
        super(str, iOneDriveClient, list);
    }

    public IItemRequestBuilder getItemWithPath(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(getRequestUrl());
        sb.append(":/");
        sb.append(str);
        sb.append(":");
        return new ItemRequestBuilder(sb.toString(), getClient(), null);
    }
}
