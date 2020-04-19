package com.onedrive.sdk.generated;

import com.onedrive.sdk.concurrency.ICallback;
import com.onedrive.sdk.core.ClientException;
import com.onedrive.sdk.extensions.Thumbnail;
import com.onedrive.sdk.http.IHttpStreamRequest;
import java.io.InputStream;

public interface IBaseThumbnailStreamRequest extends IHttpStreamRequest {
    InputStream get() throws ClientException;

    void get(ICallback<InputStream> iCallback);

    Thumbnail put(byte[] bArr) throws ClientException;

    void put(byte[] bArr, ICallback<Thumbnail> iCallback);
}
