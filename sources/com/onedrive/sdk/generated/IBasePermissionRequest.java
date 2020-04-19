package com.onedrive.sdk.generated;

import com.onedrive.sdk.concurrency.ICallback;
import com.onedrive.sdk.core.ClientException;
import com.onedrive.sdk.extensions.Permission;
import com.onedrive.sdk.http.IHttpRequest;

public interface IBasePermissionRequest extends IHttpRequest {
    @Deprecated
    Permission create(Permission permission) throws ClientException;

    @Deprecated
    void create(Permission permission, ICallback<Permission> iCallback);

    void delete() throws ClientException;

    void delete(ICallback<Void> iCallback);

    IBasePermissionRequest expand(String str);

    Permission get() throws ClientException;

    void get(ICallback<Permission> iCallback);

    Permission patch(Permission permission) throws ClientException;

    void patch(Permission permission, ICallback<Permission> iCallback);

    Permission post(Permission permission) throws ClientException;

    void post(Permission permission, ICallback<Permission> iCallback);

    IBasePermissionRequest select(String str);

    IBasePermissionRequest top(int i);

    @Deprecated
    Permission update(Permission permission) throws ClientException;

    @Deprecated
    void update(Permission permission, ICallback<Permission> iCallback);
}
