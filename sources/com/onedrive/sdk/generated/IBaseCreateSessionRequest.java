package com.onedrive.sdk.generated;

import com.onedrive.sdk.concurrency.ICallback;
import com.onedrive.sdk.core.ClientException;
import com.onedrive.sdk.extensions.ICreateSessionRequest;
import com.onedrive.sdk.extensions.UploadSession;

public interface IBaseCreateSessionRequest {
    @Deprecated
    UploadSession create() throws ClientException;

    @Deprecated
    void create(ICallback<UploadSession> iCallback);

    ICreateSessionRequest expand(String str);

    UploadSession post() throws ClientException;

    void post(ICallback<UploadSession> iCallback);

    ICreateSessionRequest select(String str);

    ICreateSessionRequest top(int i);
}
