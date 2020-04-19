package com.onedrive.sdk.generated;

import com.onedrive.sdk.concurrency.ICallback;
import com.onedrive.sdk.core.ClientException;
import com.onedrive.sdk.extensions.IOneDriveClient;
import com.onedrive.sdk.extensions.Thumbnail;
import com.onedrive.sdk.http.BaseStreamRequest;
import com.onedrive.sdk.options.Option;
import java.io.InputStream;
import java.util.List;

public class BaseThumbnailStreamRequest extends BaseStreamRequest<Thumbnail> implements IBaseThumbnailStreamRequest {
    public BaseThumbnailStreamRequest(String str, IOneDriveClient iOneDriveClient, List<Option> list) {
        super(str, iOneDriveClient, list, Thumbnail.class);
    }

    public void get(ICallback<InputStream> iCallback) {
        send(iCallback);
    }

    public InputStream get() throws ClientException {
        return send();
    }

    public void put(byte[] bArr, ICallback<Thumbnail> iCallback) {
        send(bArr, iCallback);
    }

    public Thumbnail put(byte[] bArr) throws ClientException {
        return (Thumbnail) send(bArr);
    }
}
