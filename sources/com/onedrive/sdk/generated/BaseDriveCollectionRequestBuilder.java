package com.onedrive.sdk.generated;

import com.onedrive.sdk.extensions.DriveCollectionRequest;
import com.onedrive.sdk.extensions.DriveRequestBuilder;
import com.onedrive.sdk.extensions.IDriveCollectionRequest;
import com.onedrive.sdk.extensions.IDriveRequestBuilder;
import com.onedrive.sdk.extensions.IOneDriveClient;
import com.onedrive.sdk.http.BaseRequestBuilder;
import com.onedrive.sdk.options.Option;
import java.util.List;

public class BaseDriveCollectionRequestBuilder extends BaseRequestBuilder implements IBaseDriveCollectionRequestBuilder {
    public BaseDriveCollectionRequestBuilder(String str, IOneDriveClient iOneDriveClient, List<Option> list) {
        super(str, iOneDriveClient, list);
    }

    public IDriveCollectionRequest buildRequest() {
        return buildRequest(getOptions());
    }

    public IDriveCollectionRequest buildRequest(List<Option> list) {
        return new DriveCollectionRequest(getRequestUrl(), getClient(), list);
    }

    public IDriveRequestBuilder byId(String str) {
        return new DriveRequestBuilder(getRequestUrlWithAdditionalSegment(str), getClient(), getOptions());
    }
}
