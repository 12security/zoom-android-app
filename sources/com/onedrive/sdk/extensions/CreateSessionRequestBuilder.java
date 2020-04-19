package com.onedrive.sdk.extensions;

import com.onedrive.sdk.generated.BaseCreateSessionRequestBuilder;
import com.onedrive.sdk.options.Option;
import java.util.List;

public class CreateSessionRequestBuilder extends BaseCreateSessionRequestBuilder implements ICreateSessionRequestBuilder {
    public CreateSessionRequestBuilder(String str, IOneDriveClient iOneDriveClient, List<Option> list, ChunkedUploadSessionDescriptor chunkedUploadSessionDescriptor) {
        super(str, iOneDriveClient, list, chunkedUploadSessionDescriptor);
    }
}
