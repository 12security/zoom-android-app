package com.onedrive.sdk.generated;

import com.onedrive.sdk.concurrency.ICallback;
import com.onedrive.sdk.core.ClientException;
import com.onedrive.sdk.extensions.Item;
import com.onedrive.sdk.http.IHttpStreamRequest;
import java.io.InputStream;

public interface IBaseItemStreamRequest extends IHttpStreamRequest {
    InputStream get() throws ClientException;

    void get(ICallback<InputStream> iCallback);

    Item put(byte[] bArr) throws ClientException;

    void put(byte[] bArr, ICallback<Item> iCallback);
}
