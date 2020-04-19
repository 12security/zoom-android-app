package com.onedrive.sdk.generated;

import com.onedrive.sdk.extensions.IOneDriveClient;
import com.onedrive.sdk.extensions.IShareRequest;
import com.onedrive.sdk.extensions.ShareRequest;
import com.onedrive.sdk.http.BaseRequestBuilder;
import com.onedrive.sdk.options.Option;
import java.util.List;

public class BaseShareRequestBuilder extends BaseRequestBuilder implements IBaseShareRequestBuilder {
    public BaseShareRequestBuilder(String str, IOneDriveClient iOneDriveClient, List<Option> list) {
        super(str, iOneDriveClient, list);
    }

    public IShareRequest buildRequest() {
        return buildRequest(getOptions());
    }

    public IShareRequest buildRequest(List<Option> list) {
        return new ShareRequest(getRequestUrl(), getClient(), list);
    }
}
