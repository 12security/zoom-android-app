package com.onedrive.sdk.extensions;

import com.onedrive.sdk.generated.BaseRecentRequest;
import com.onedrive.sdk.options.Option;
import java.util.List;

public class RecentRequest extends BaseRecentRequest implements IRecentRequest {
    public RecentRequest(String str, IOneDriveClient iOneDriveClient, List<Option> list) {
        super(str, iOneDriveClient, list);
    }
}
