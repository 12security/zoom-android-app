package com.onedrive.sdk.generated;

import com.onedrive.sdk.concurrency.ICallback;
import com.onedrive.sdk.core.ClientException;
import com.onedrive.sdk.extensions.ThumbnailSet;
import com.onedrive.sdk.http.IHttpRequest;

public interface IBaseThumbnailSetRequest extends IHttpRequest {
    @Deprecated
    ThumbnailSet create(ThumbnailSet thumbnailSet) throws ClientException;

    @Deprecated
    void create(ThumbnailSet thumbnailSet, ICallback<ThumbnailSet> iCallback);

    void delete() throws ClientException;

    void delete(ICallback<Void> iCallback);

    IBaseThumbnailSetRequest expand(String str);

    ThumbnailSet get() throws ClientException;

    void get(ICallback<ThumbnailSet> iCallback);

    ThumbnailSet patch(ThumbnailSet thumbnailSet) throws ClientException;

    void patch(ThumbnailSet thumbnailSet, ICallback<ThumbnailSet> iCallback);

    ThumbnailSet post(ThumbnailSet thumbnailSet) throws ClientException;

    void post(ThumbnailSet thumbnailSet, ICallback<ThumbnailSet> iCallback);

    IBaseThumbnailSetRequest select(String str);

    IBaseThumbnailSetRequest top(int i);

    @Deprecated
    ThumbnailSet update(ThumbnailSet thumbnailSet) throws ClientException;

    @Deprecated
    void update(ThumbnailSet thumbnailSet, ICallback<ThumbnailSet> iCallback);
}
