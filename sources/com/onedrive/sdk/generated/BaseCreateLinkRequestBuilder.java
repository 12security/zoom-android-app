package com.onedrive.sdk.generated;

import com.onedrive.sdk.extensions.CreateLinkRequest;
import com.onedrive.sdk.extensions.ICreateLinkRequest;
import com.onedrive.sdk.extensions.IOneDriveClient;
import com.onedrive.sdk.http.BaseRequestBuilder;
import com.onedrive.sdk.options.Option;
import java.util.List;

public class BaseCreateLinkRequestBuilder extends BaseRequestBuilder {
    public final String mType;

    public BaseCreateLinkRequestBuilder(String str, IOneDriveClient iOneDriveClient, List<Option> list, String str2) {
        super(str, iOneDriveClient, list);
        this.mType = str2;
    }

    public ICreateLinkRequest buildRequest() {
        return buildRequest(getOptions());
    }

    public ICreateLinkRequest buildRequest(List<Option> list) {
        return new CreateLinkRequest(getRequestUrl(), getClient(), list, this.mType);
    }
}
