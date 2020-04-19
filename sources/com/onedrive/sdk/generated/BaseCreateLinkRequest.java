package com.onedrive.sdk.generated;

import com.onedrive.sdk.concurrency.ICallback;
import com.onedrive.sdk.core.ClientException;
import com.onedrive.sdk.extensions.CreateLinkBody;
import com.onedrive.sdk.extensions.CreateLinkRequest;
import com.onedrive.sdk.extensions.ICreateLinkRequest;
import com.onedrive.sdk.extensions.IOneDriveClient;
import com.onedrive.sdk.extensions.Permission;
import com.onedrive.sdk.http.BaseRequest;
import com.onedrive.sdk.http.HttpMethod;
import com.onedrive.sdk.options.Option;
import com.onedrive.sdk.options.QueryOption;
import java.util.List;

public class BaseCreateLinkRequest extends BaseRequest implements IBaseCreateLinkRequest {
    protected final CreateLinkBody mBody = new CreateLinkBody();

    public BaseCreateLinkRequest(String str, IOneDriveClient iOneDriveClient, List<Option> list, String str2) {
        super(str, iOneDriveClient, list, Permission.class);
        this.mBody.type = str2;
    }

    @Deprecated
    public void create(ICallback<Permission> iCallback) {
        post(iCallback);
    }

    @Deprecated
    public Permission create() throws ClientException {
        return post();
    }

    public void post(ICallback<Permission> iCallback) {
        send(HttpMethod.POST, iCallback, this.mBody);
    }

    public Permission post() throws ClientException {
        return (Permission) send(HttpMethod.POST, this.mBody);
    }

    public ICreateLinkRequest select(String str) {
        getQueryOptions().add(new QueryOption("select", str));
        return (CreateLinkRequest) this;
    }

    public ICreateLinkRequest top(int i) {
        List queryOptions = getQueryOptions();
        StringBuilder sb = new StringBuilder();
        sb.append(i);
        sb.append("");
        queryOptions.add(new QueryOption("top", sb.toString()));
        return (CreateLinkRequest) this;
    }

    public ICreateLinkRequest expand(String str) {
        getQueryOptions().add(new QueryOption("expand", str));
        return (CreateLinkRequest) this;
    }
}
