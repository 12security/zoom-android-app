package com.onedrive.sdk.generated;

import com.onedrive.sdk.extensions.ChunkedUploadSessionDescriptor;
import com.onedrive.sdk.extensions.ICopyRequestBuilder;
import com.onedrive.sdk.extensions.ICreateLinkRequestBuilder;
import com.onedrive.sdk.extensions.ICreateSessionRequestBuilder;
import com.onedrive.sdk.extensions.IDeltaRequestBuilder;
import com.onedrive.sdk.extensions.IItemCollectionRequestBuilder;
import com.onedrive.sdk.extensions.IItemRequest;
import com.onedrive.sdk.extensions.IItemRequestBuilder;
import com.onedrive.sdk.extensions.IItemStreamRequestBuilder;
import com.onedrive.sdk.extensions.IPermissionCollectionRequestBuilder;
import com.onedrive.sdk.extensions.IPermissionRequestBuilder;
import com.onedrive.sdk.extensions.ISearchRequestBuilder;
import com.onedrive.sdk.extensions.IThumbnailSetCollectionRequestBuilder;
import com.onedrive.sdk.extensions.IThumbnailSetRequestBuilder;
import com.onedrive.sdk.extensions.ItemReference;
import com.onedrive.sdk.http.IRequestBuilder;
import com.onedrive.sdk.options.Option;
import java.util.List;

public interface IBaseItemRequestBuilder extends IRequestBuilder {
    IItemRequest buildRequest();

    IItemRequest buildRequest(List<Option> list);

    IItemCollectionRequestBuilder getChildren();

    IItemRequestBuilder getChildren(String str);

    IItemStreamRequestBuilder getContent();

    ICopyRequestBuilder getCopy(String str, ItemReference itemReference);

    ICreateLinkRequestBuilder getCreateLink(String str);

    ICreateSessionRequestBuilder getCreateSession(ChunkedUploadSessionDescriptor chunkedUploadSessionDescriptor);

    IDeltaRequestBuilder getDelta(String str);

    IPermissionCollectionRequestBuilder getPermissions();

    IPermissionRequestBuilder getPermissions(String str);

    ISearchRequestBuilder getSearch(String str);

    IThumbnailSetCollectionRequestBuilder getThumbnails();

    IThumbnailSetRequestBuilder getThumbnails(String str);
}
