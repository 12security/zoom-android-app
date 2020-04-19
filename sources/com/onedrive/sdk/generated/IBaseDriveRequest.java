package com.onedrive.sdk.generated;

import com.onedrive.sdk.concurrency.ICallback;
import com.onedrive.sdk.core.ClientException;
import com.onedrive.sdk.extensions.Drive;
import com.onedrive.sdk.http.IHttpRequest;

public interface IBaseDriveRequest extends IHttpRequest {
    @Deprecated
    Drive create(Drive drive) throws ClientException;

    @Deprecated
    void create(Drive drive, ICallback<Drive> iCallback);

    void delete() throws ClientException;

    void delete(ICallback<Void> iCallback);

    IBaseDriveRequest expand(String str);

    Drive get() throws ClientException;

    void get(ICallback<Drive> iCallback);

    Drive patch(Drive drive) throws ClientException;

    void patch(Drive drive, ICallback<Drive> iCallback);

    Drive post(Drive drive) throws ClientException;

    void post(Drive drive, ICallback<Drive> iCallback);

    IBaseDriveRequest select(String str);

    IBaseDriveRequest top(int i);

    @Deprecated
    Drive update(Drive drive) throws ClientException;

    @Deprecated
    void update(Drive drive, ICallback<Drive> iCallback);
}
