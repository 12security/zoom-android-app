package com.onedrive.sdk.generated;

import com.onedrive.sdk.concurrency.ICallback;
import com.onedrive.sdk.core.ClientException;
import com.onedrive.sdk.extensions.IOneDriveClient;
import com.onedrive.sdk.extensions.Item;
import com.onedrive.sdk.http.BaseStreamRequest;
import com.onedrive.sdk.options.Option;
import java.io.InputStream;
import java.util.List;

public class BaseItemStreamRequest extends BaseStreamRequest<Item> implements IBaseItemStreamRequest {
    public BaseItemStreamRequest(String str, IOneDriveClient iOneDriveClient, List<Option> list) {
        super(str, iOneDriveClient, list, Item.class);
    }

    public void get(ICallback<InputStream> iCallback) {
        send(iCallback);
    }

    public InputStream get() throws ClientException {
        return send();
    }

    public void put(byte[] bArr, ICallback<Item> iCallback) {
        send(bArr, iCallback);
    }

    public Item put(byte[] bArr) throws ClientException {
        return (Item) send(bArr);
    }
}
