package com.onedrive.sdk.generated;

import com.onedrive.sdk.concurrency.ICallback;
import com.onedrive.sdk.core.ClientException;
import com.onedrive.sdk.extensions.IThumbnailSetCollectionPage;
import com.onedrive.sdk.extensions.IThumbnailSetCollectionRequest;

public interface IBaseThumbnailSetCollectionRequest {
    IThumbnailSetCollectionRequest expand(String str);

    IThumbnailSetCollectionPage get() throws ClientException;

    void get(ICallback<IThumbnailSetCollectionPage> iCallback);

    IThumbnailSetCollectionRequest select(String str);

    IThumbnailSetCollectionRequest top(int i);
}
