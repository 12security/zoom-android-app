package com.onedrive.sdk.generated;

import com.onedrive.sdk.extensions.CopyRequest;
import com.onedrive.sdk.extensions.ICopyRequest;
import com.onedrive.sdk.extensions.IOneDriveClient;
import com.onedrive.sdk.extensions.ItemReference;
import com.onedrive.sdk.http.BaseRequestBuilder;
import com.onedrive.sdk.options.Option;
import java.util.List;

public class BaseCopyRequestBuilder extends BaseRequestBuilder {
    public final String mName;
    public final ItemReference mParentReference;

    public BaseCopyRequestBuilder(String str, IOneDriveClient iOneDriveClient, List<Option> list, String str2, ItemReference itemReference) {
        super(str, iOneDriveClient, list);
        this.mName = str2;
        this.mParentReference = itemReference;
    }

    public ICopyRequest buildRequest() {
        return buildRequest(getOptions());
    }

    public ICopyRequest buildRequest(List<Option> list) {
        CopyRequest copyRequest = new CopyRequest(getRequestUrl(), getClient(), list, this.mName, this.mParentReference);
        return copyRequest;
    }
}
