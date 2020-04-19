package com.onedrive.sdk.generated;

import com.onedrive.sdk.concurrency.ICallback;
import com.onedrive.sdk.concurrency.IExecutors;
import com.onedrive.sdk.core.ClientException;
import com.onedrive.sdk.extensions.IOneDriveClient;
import com.onedrive.sdk.extensions.IPermissionCollectionPage;
import com.onedrive.sdk.extensions.IPermissionCollectionRequest;
import com.onedrive.sdk.extensions.PermissionCollectionPage;
import com.onedrive.sdk.extensions.PermissionCollectionRequest;
import com.onedrive.sdk.extensions.PermissionCollectionRequestBuilder;
import com.onedrive.sdk.http.BaseCollectionRequest;
import com.onedrive.sdk.options.Option;
import com.onedrive.sdk.options.QueryOption;
import java.util.List;

public class BasePermissionCollectionRequest extends BaseCollectionRequest<BasePermissionCollectionResponse, IPermissionCollectionPage> implements IBasePermissionCollectionRequest {
    public BasePermissionCollectionRequest(String str, IOneDriveClient iOneDriveClient, List<Option> list) {
        super(str, iOneDriveClient, list, BasePermissionCollectionResponse.class, IPermissionCollectionPage.class);
    }

    public void get(final ICallback<IPermissionCollectionPage> iCallback) {
        final IExecutors executors = getBaseRequest().getClient().getExecutors();
        executors.performOnBackground(new Runnable() {
            public void run() {
                try {
                    executors.performOnForeground(BasePermissionCollectionRequest.this.get(), iCallback);
                } catch (ClientException e) {
                    executors.performOnForeground(e, iCallback);
                }
            }
        });
    }

    public IPermissionCollectionPage get() throws ClientException {
        return buildFromResponse((BasePermissionCollectionResponse) send());
    }

    public IPermissionCollectionRequest expand(String str) {
        addQueryOption(new QueryOption("expand", str));
        return (PermissionCollectionRequest) this;
    }

    public IPermissionCollectionRequest select(String str) {
        addQueryOption(new QueryOption("select", str));
        return (PermissionCollectionRequest) this;
    }

    public IPermissionCollectionRequest top(int i) {
        StringBuilder sb = new StringBuilder();
        sb.append(i);
        sb.append("");
        addQueryOption(new QueryOption("top", sb.toString()));
        return (PermissionCollectionRequest) this;
    }

    public IPermissionCollectionPage buildFromResponse(BasePermissionCollectionResponse basePermissionCollectionResponse) {
        PermissionCollectionPage permissionCollectionPage = new PermissionCollectionPage(basePermissionCollectionResponse, basePermissionCollectionResponse.nextLink != null ? new PermissionCollectionRequestBuilder(basePermissionCollectionResponse.nextLink, getBaseRequest().getClient(), null) : null);
        permissionCollectionPage.setRawObject(basePermissionCollectionResponse.getSerializer(), basePermissionCollectionResponse.getRawObject());
        return permissionCollectionPage;
    }
}
