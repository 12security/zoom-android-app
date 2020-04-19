package com.onedrive.sdk.generated;

import com.onedrive.sdk.concurrency.ICallback;
import com.onedrive.sdk.core.ClientException;
import com.onedrive.sdk.extensions.ICreateLinkRequest;
import com.onedrive.sdk.extensions.Permission;

public interface IBaseCreateLinkRequest {
    @Deprecated
    Permission create() throws ClientException;

    @Deprecated
    void create(ICallback<Permission> iCallback);

    ICreateLinkRequest expand(String str);

    Permission post() throws ClientException;

    void post(ICallback<Permission> iCallback);

    ICreateLinkRequest select(String str);

    ICreateLinkRequest top(int i);
}
