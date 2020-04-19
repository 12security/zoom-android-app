package com.onedrive.sdk.extensions;

import com.onedrive.sdk.generated.BaseCreateSessionRequest;
import com.onedrive.sdk.options.Option;
import java.util.List;

public class CreateSessionRequest extends BaseCreateSessionRequest implements ICreateSessionRequest {
    public CreateSessionRequest(String str, IOneDriveClient iOneDriveClient, List<Option> list, ChunkedUploadSessionDescriptor chunkedUploadSessionDescriptor) {
        super(str, iOneDriveClient, list, chunkedUploadSessionDescriptor);
    }
}
