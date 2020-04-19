package com.onedrive.sdk.extensions;

import com.onedrive.sdk.concurrency.ChunkedUploadProvider;
import com.onedrive.sdk.generated.BaseUploadSession;
import java.io.InputStream;

public class UploadSession<UploadType> extends BaseUploadSession {
    public ChunkedUploadProvider createUploadProvider(IOneDriveClient iOneDriveClient, InputStream inputStream, int i, Class<UploadType> cls) {
        ChunkedUploadProvider chunkedUploadProvider = new ChunkedUploadProvider(this, iOneDriveClient, inputStream, i, cls);
        return chunkedUploadProvider;
    }
}
