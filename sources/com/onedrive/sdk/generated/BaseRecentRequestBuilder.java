package com.onedrive.sdk.generated;

import com.onedrive.sdk.extensions.IOneDriveClient;
import com.onedrive.sdk.extensions.IRecentRequest;
import com.onedrive.sdk.extensions.RecentRequest;
import com.onedrive.sdk.http.BaseRequestBuilder;
import com.onedrive.sdk.options.Option;
import java.util.List;

public class BaseRecentRequestBuilder extends BaseRequestBuilder {
    public BaseRecentRequestBuilder(String str, IOneDriveClient iOneDriveClient, List<Option> list) {
        super(str, iOneDriveClient, list);
    }

    public IRecentRequest buildRequest() {
        return buildRequest(getOptions());
    }

    public IRecentRequest buildRequest(List<Option> list) {
        return new RecentRequest(getRequestUrl(), getClient(), list);
    }
}
