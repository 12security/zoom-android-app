package com.onedrive.sdk.generated;

import com.google.firebase.analytics.FirebaseAnalytics.Param;
import com.onedrive.sdk.extensions.ChunkedUploadSessionDescriptor;
import com.onedrive.sdk.extensions.CopyRequestBuilder;
import com.onedrive.sdk.extensions.CreateLinkRequestBuilder;
import com.onedrive.sdk.extensions.CreateSessionRequestBuilder;
import com.onedrive.sdk.extensions.DeltaRequestBuilder;
import com.onedrive.sdk.extensions.ICopyRequestBuilder;
import com.onedrive.sdk.extensions.ICreateLinkRequestBuilder;
import com.onedrive.sdk.extensions.ICreateSessionRequestBuilder;
import com.onedrive.sdk.extensions.IDeltaRequestBuilder;
import com.onedrive.sdk.extensions.IItemCollectionRequestBuilder;
import com.onedrive.sdk.extensions.IItemRequest;
import com.onedrive.sdk.extensions.IItemRequestBuilder;
import com.onedrive.sdk.extensions.IItemStreamRequestBuilder;
import com.onedrive.sdk.extensions.IOneDriveClient;
import com.onedrive.sdk.extensions.IPermissionCollectionRequestBuilder;
import com.onedrive.sdk.extensions.IPermissionRequestBuilder;
import com.onedrive.sdk.extensions.ISearchRequestBuilder;
import com.onedrive.sdk.extensions.IThumbnailSetCollectionRequestBuilder;
import com.onedrive.sdk.extensions.IThumbnailSetRequestBuilder;
import com.onedrive.sdk.extensions.ItemCollectionRequestBuilder;
import com.onedrive.sdk.extensions.ItemReference;
import com.onedrive.sdk.extensions.ItemRequest;
import com.onedrive.sdk.extensions.ItemRequestBuilder;
import com.onedrive.sdk.extensions.ItemStreamRequestBuilder;
import com.onedrive.sdk.extensions.PermissionCollectionRequestBuilder;
import com.onedrive.sdk.extensions.PermissionRequestBuilder;
import com.onedrive.sdk.extensions.SearchRequestBuilder;
import com.onedrive.sdk.extensions.ThumbnailSetCollectionRequestBuilder;
import com.onedrive.sdk.extensions.ThumbnailSetRequestBuilder;
import com.onedrive.sdk.http.BaseRequestBuilder;
import com.onedrive.sdk.options.Option;
import java.util.List;

public class BaseItemRequestBuilder extends BaseRequestBuilder implements IBaseItemRequestBuilder {
    public BaseItemRequestBuilder(String str, IOneDriveClient iOneDriveClient, List<Option> list) {
        super(str, iOneDriveClient, list);
    }

    public IItemRequest buildRequest() {
        return buildRequest(getOptions());
    }

    public IItemRequest buildRequest(List<Option> list) {
        return new ItemRequest(getRequestUrl(), getClient(), list);
    }

    public IPermissionCollectionRequestBuilder getPermissions() {
        return new PermissionCollectionRequestBuilder(getRequestUrlWithAdditionalSegment("permissions"), getClient(), null);
    }

    public IPermissionRequestBuilder getPermissions(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(getRequestUrlWithAdditionalSegment("permissions"));
        sb.append("/");
        sb.append(str);
        return new PermissionRequestBuilder(sb.toString(), getClient(), null);
    }

    public IItemCollectionRequestBuilder getChildren() {
        return new ItemCollectionRequestBuilder(getRequestUrlWithAdditionalSegment("children"), getClient(), null);
    }

    public IItemRequestBuilder getChildren(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(getRequestUrlWithAdditionalSegment("children"));
        sb.append("/");
        sb.append(str);
        return new ItemRequestBuilder(sb.toString(), getClient(), null);
    }

    public IThumbnailSetCollectionRequestBuilder getThumbnails() {
        return new ThumbnailSetCollectionRequestBuilder(getRequestUrlWithAdditionalSegment("thumbnails"), getClient(), null);
    }

    public IThumbnailSetRequestBuilder getThumbnails(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(getRequestUrlWithAdditionalSegment("thumbnails"));
        sb.append("/");
        sb.append(str);
        return new ThumbnailSetRequestBuilder(sb.toString(), getClient(), null);
    }

    public IItemStreamRequestBuilder getContent() {
        return new ItemStreamRequestBuilder(getRequestUrlWithAdditionalSegment(Param.CONTENT), getClient(), null);
    }

    public ICreateSessionRequestBuilder getCreateSession(ChunkedUploadSessionDescriptor chunkedUploadSessionDescriptor) {
        return new CreateSessionRequestBuilder(getRequestUrlWithAdditionalSegment("action.createUploadSession"), getClient(), null, chunkedUploadSessionDescriptor);
    }

    public ICopyRequestBuilder getCopy(String str, ItemReference itemReference) {
        CopyRequestBuilder copyRequestBuilder = new CopyRequestBuilder(getRequestUrlWithAdditionalSegment("action.copy"), getClient(), null, str, itemReference);
        return copyRequestBuilder;
    }

    public ICreateLinkRequestBuilder getCreateLink(String str) {
        return new CreateLinkRequestBuilder(getRequestUrlWithAdditionalSegment("action.createLink"), getClient(), null, str);
    }

    public IDeltaRequestBuilder getDelta(String str) {
        return new DeltaRequestBuilder(getRequestUrlWithAdditionalSegment("view.delta"), getClient(), null, str);
    }

    public ISearchRequestBuilder getSearch(String str) {
        return new SearchRequestBuilder(getRequestUrlWithAdditionalSegment("view.search"), getClient(), null, str);
    }
}
