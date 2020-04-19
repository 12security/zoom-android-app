package com.onedrive.sdk.generated;

import com.onedrive.sdk.extensions.ChunkedUploadSessionDescriptor;
import com.onedrive.sdk.extensions.CreateSessionRequest;
import com.onedrive.sdk.extensions.ICreateSessionRequest;
import com.onedrive.sdk.extensions.IOneDriveClient;
import com.onedrive.sdk.http.BaseRequestBuilder;
import com.onedrive.sdk.options.Option;
import java.util.List;

public class BaseCreateSessionRequestBuilder extends BaseRequestBuilder {
    public final ChunkedUploadSessionDescriptor mItem;

    public BaseCreateSessionRequestBuilder(String str, IOneDriveClient iOneDriveClient, List<Option> list, ChunkedUploadSessionDescriptor chunkedUploadSessionDescriptor) {
        super(str, iOneDriveClient, list);
        this.mItem = chunkedUploadSessionDescriptor;
    }

    public ICreateSessionRequest buildRequest() {
        return buildRequest(getOptions());
    }

    public ICreateSessionRequest buildRequest(List<Option> list) {
        return new CreateSessionRequest(getRequestUrl(), getClient(), list, this.mItem);
    }
}
