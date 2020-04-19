package com.onedrive.sdk.extensions;

import com.onedrive.sdk.generated.BaseCopyRequestBuilder;
import com.onedrive.sdk.options.Option;
import java.util.List;

public class CopyRequestBuilder extends BaseCopyRequestBuilder implements ICopyRequestBuilder {
    public CopyRequestBuilder(String str, IOneDriveClient iOneDriveClient, List<Option> list, String str2, ItemReference itemReference) {
        super(str, iOneDriveClient, list, str2, itemReference);
    }
}
