package com.onedrive.sdk.extensions;

import com.onedrive.sdk.generated.BaseRecentRequestBuilder;
import com.onedrive.sdk.options.Option;
import java.util.List;

public class RecentRequestBuilder extends BaseRecentRequestBuilder implements IRecentRequestBuilder {
    public RecentRequestBuilder(String str, IOneDriveClient iOneDriveClient, List<Option> list) {
        super(str, iOneDriveClient, list);
    }
}
