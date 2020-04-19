package com.onedrive.sdk.generated;

import com.onedrive.sdk.extensions.IDriveRequest;
import com.onedrive.sdk.extensions.IItemRequestBuilder;
import com.onedrive.sdk.extensions.IRecentRequestBuilder;
import com.onedrive.sdk.http.IRequestBuilder;
import com.onedrive.sdk.options.Option;
import java.util.List;

public interface IBaseDriveRequestBuilder extends IRequestBuilder {
    IDriveRequest buildRequest();

    IDriveRequest buildRequest(List<Option> list);

    IItemRequestBuilder getItems(String str);

    IRecentRequestBuilder getRecent();

    IItemRequestBuilder getShared(String str);

    IItemRequestBuilder getSpecial(String str);
}
