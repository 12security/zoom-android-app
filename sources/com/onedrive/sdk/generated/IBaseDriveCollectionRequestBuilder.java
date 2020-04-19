package com.onedrive.sdk.generated;

import com.onedrive.sdk.extensions.IDriveCollectionRequest;
import com.onedrive.sdk.extensions.IDriveRequestBuilder;
import com.onedrive.sdk.http.IRequestBuilder;
import com.onedrive.sdk.options.Option;
import java.util.List;

public interface IBaseDriveCollectionRequestBuilder extends IRequestBuilder {
    IDriveCollectionRequest buildRequest();

    IDriveCollectionRequest buildRequest(List<Option> list);

    IDriveRequestBuilder byId(String str);
}
