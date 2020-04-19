package com.onedrive.sdk.generated;

import com.onedrive.sdk.extensions.IItemStreamRequest;
import com.onedrive.sdk.extensions.IOneDriveClient;
import com.onedrive.sdk.extensions.ItemStreamRequest;
import com.onedrive.sdk.http.BaseRequestBuilder;
import com.onedrive.sdk.options.Option;
import java.util.List;

public class BaseItemStreamRequestBuilder extends BaseRequestBuilder implements IBaseItemStreamRequestBuilder {
    public BaseItemStreamRequestBuilder(String str, IOneDriveClient iOneDriveClient, List<Option> list) {
        super(str, iOneDriveClient, list);
    }

    public IItemStreamRequest buildRequest() {
        return buildRequest(getOptions());
    }

    public IItemStreamRequest buildRequest(List<Option> list) {
        return new ItemStreamRequest(getRequestUrl(), getClient(), list);
    }
}
