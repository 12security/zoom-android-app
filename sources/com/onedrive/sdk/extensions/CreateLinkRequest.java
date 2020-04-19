package com.onedrive.sdk.extensions;

import com.onedrive.sdk.generated.BaseCreateLinkRequest;
import com.onedrive.sdk.options.Option;
import java.util.List;

public class CreateLinkRequest extends BaseCreateLinkRequest implements ICreateLinkRequest {
    public CreateLinkRequest(String str, IOneDriveClient iOneDriveClient, List<Option> list, String str2) {
        super(str, iOneDriveClient, list, str2);
    }
}
