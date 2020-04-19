package com.onedrive.sdk.generated;

import com.onedrive.sdk.concurrency.ICallback;
import com.onedrive.sdk.core.ClientException;
import com.onedrive.sdk.extensions.IOneDriveClient;
import com.onedrive.sdk.extensions.IPermissionRequest;
import com.onedrive.sdk.extensions.Permission;
import com.onedrive.sdk.extensions.PermissionRequest;
import com.onedrive.sdk.http.BaseRequest;
import com.onedrive.sdk.http.HttpMethod;
import com.onedrive.sdk.options.Option;
import com.onedrive.sdk.options.QueryOption;
import java.util.List;

public class BasePermissionRequest extends BaseRequest implements IBasePermissionRequest {
    public BasePermissionRequest(String str, IOneDriveClient iOneDriveClient, List<Option> list) {
        super(str, iOneDriveClient, list, Permission.class);
    }

    public void get(ICallback<Permission> iCallback) {
        send(HttpMethod.GET, iCallback, null);
    }

    public Permission get() throws ClientException {
        return (Permission) send(HttpMethod.GET, null);
    }

    @Deprecated
    public void update(Permission permission, ICallback<Permission> iCallback) {
        patch(permission, iCallback);
    }

    @Deprecated
    public Permission update(Permission permission) throws ClientException {
        return patch(permission);
    }

    public void patch(Permission permission, ICallback<Permission> iCallback) {
        send(HttpMethod.PATCH, iCallback, permission);
    }

    public Permission patch(Permission permission) throws ClientException {
        return (Permission) send(HttpMethod.PATCH, permission);
    }

    public void delete(ICallback<Void> iCallback) {
        send(HttpMethod.DELETE, iCallback, null);
    }

    public void delete() throws ClientException {
        send(HttpMethod.DELETE, null);
    }

    @Deprecated
    public void create(Permission permission, ICallback<Permission> iCallback) {
        post(permission, iCallback);
    }

    @Deprecated
    public Permission create(Permission permission) throws ClientException {
        return post(permission);
    }

    public void post(Permission permission, ICallback<Permission> iCallback) {
        send(HttpMethod.POST, iCallback, permission);
    }

    public Permission post(Permission permission) throws ClientException {
        return (Permission) send(HttpMethod.POST, permission);
    }

    public IPermissionRequest select(String str) {
        getQueryOptions().add(new QueryOption("select", str));
        return (PermissionRequest) this;
    }

    public IPermissionRequest top(int i) {
        List queryOptions = getQueryOptions();
        StringBuilder sb = new StringBuilder();
        sb.append(i);
        sb.append("");
        queryOptions.add(new QueryOption("top", sb.toString()));
        return (PermissionRequest) this;
    }

    public IPermissionRequest expand(String str) {
        getQueryOptions().add(new QueryOption("expand", str));
        return (PermissionRequest) this;
    }
}
