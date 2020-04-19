package com.onedrive.sdk.generated;

import com.onedrive.sdk.concurrency.ICallback;
import com.onedrive.sdk.core.ClientException;
import com.onedrive.sdk.extensions.Share;
import com.onedrive.sdk.http.IHttpRequest;

public interface IBaseShareRequest extends IHttpRequest {
    @Deprecated
    Share create(Share share) throws ClientException;

    @Deprecated
    void create(Share share, ICallback<Share> iCallback);

    void delete() throws ClientException;

    void delete(ICallback<Void> iCallback);

    IBaseShareRequest expand(String str);

    Share get() throws ClientException;

    void get(ICallback<Share> iCallback);

    Share patch(Share share) throws ClientException;

    void patch(Share share, ICallback<Share> iCallback);

    Share post(Share share) throws ClientException;

    void post(Share share, ICallback<Share> iCallback);

    IBaseShareRequest select(String str);

    IBaseShareRequest top(int i);

    @Deprecated
    Share update(Share share) throws ClientException;

    @Deprecated
    void update(Share share, ICallback<Share> iCallback);
}
