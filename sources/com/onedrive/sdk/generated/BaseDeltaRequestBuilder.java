package com.onedrive.sdk.generated;

import com.onedrive.sdk.extensions.DeltaRequest;
import com.onedrive.sdk.extensions.IDeltaRequest;
import com.onedrive.sdk.extensions.IOneDriveClient;
import com.onedrive.sdk.http.BaseRequestBuilder;
import com.onedrive.sdk.options.Option;
import java.util.List;

public class BaseDeltaRequestBuilder extends BaseRequestBuilder {
    public final String mToken;

    public BaseDeltaRequestBuilder(String str, IOneDriveClient iOneDriveClient, List<Option> list, String str2) {
        super(str, iOneDriveClient, list);
        this.mToken = str2;
    }

    public IDeltaRequest buildRequest() {
        return buildRequest(getOptions());
    }

    public IDeltaRequest buildRequest(List<Option> list) {
        return new DeltaRequest(getRequestUrl(), getClient(), list, this.mToken);
    }
}
