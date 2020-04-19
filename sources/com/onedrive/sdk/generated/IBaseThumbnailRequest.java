package com.onedrive.sdk.generated;

import com.onedrive.sdk.concurrency.ICallback;
import com.onedrive.sdk.core.ClientException;
import com.onedrive.sdk.extensions.Thumbnail;
import com.onedrive.sdk.http.IHttpRequest;

public interface IBaseThumbnailRequest extends IHttpRequest {
    @Deprecated
    Thumbnail create(Thumbnail thumbnail) throws ClientException;

    @Deprecated
    void create(Thumbnail thumbnail, ICallback<Thumbnail> iCallback);

    void delete() throws ClientException;

    void delete(ICallback<Void> iCallback);

    IBaseThumbnailRequest expand(String str);

    Thumbnail get() throws ClientException;

    void get(ICallback<Thumbnail> iCallback);

    Thumbnail patch(Thumbnail thumbnail) throws ClientException;

    void patch(Thumbnail thumbnail, ICallback<Thumbnail> iCallback);

    Thumbnail post(Thumbnail thumbnail) throws ClientException;

    void post(Thumbnail thumbnail, ICallback<Thumbnail> iCallback);

    IBaseThumbnailRequest select(String str);

    IBaseThumbnailRequest top(int i);

    @Deprecated
    Thumbnail update(Thumbnail thumbnail) throws ClientException;

    @Deprecated
    void update(Thumbnail thumbnail, ICallback<Thumbnail> iCallback);
}
