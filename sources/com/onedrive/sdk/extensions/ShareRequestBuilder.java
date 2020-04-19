package com.onedrive.sdk.extensions;

import com.onedrive.sdk.generated.BaseShareRequestBuilder;
import com.onedrive.sdk.options.Option;
import java.util.List;

public class ShareRequestBuilder extends BaseShareRequestBuilder implements IShareRequestBuilder {
    public ShareRequestBuilder(String str, IOneDriveClient iOneDriveClient, List<Option> list) {
        super(str, iOneDriveClient, list);
    }
}
