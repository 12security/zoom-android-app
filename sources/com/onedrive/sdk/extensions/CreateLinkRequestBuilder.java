package com.onedrive.sdk.extensions;

import com.onedrive.sdk.generated.BaseCreateLinkRequestBuilder;
import com.onedrive.sdk.options.Option;
import java.util.List;

public class CreateLinkRequestBuilder extends BaseCreateLinkRequestBuilder implements ICreateLinkRequestBuilder {
    public CreateLinkRequestBuilder(String str, IOneDriveClient iOneDriveClient, List<Option> list, String str2) {
        super(str, iOneDriveClient, list, str2);
    }
}
