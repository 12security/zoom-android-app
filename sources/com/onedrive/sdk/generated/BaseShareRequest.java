package com.onedrive.sdk.generated;

import com.onedrive.sdk.concurrency.ICallback;
import com.onedrive.sdk.core.ClientException;
import com.onedrive.sdk.extensions.IOneDriveClient;
import com.onedrive.sdk.extensions.IShareRequest;
import com.onedrive.sdk.extensions.Share;
import com.onedrive.sdk.extensions.ShareRequest;
import com.onedrive.sdk.http.BaseRequest;
import com.onedrive.sdk.http.HttpMethod;
import com.onedrive.sdk.options.Option;
import com.onedrive.sdk.options.QueryOption;
import java.util.List;

public class BaseShareRequest extends BaseRequest implements IBaseShareRequest {
    public BaseShareRequest(String str, IOneDriveClient iOneDriveClient, List<Option> list) {
        super(str, iOneDriveClient, list, Share.class);
    }

    public void get(ICallback<Share> iCallback) {
        send(HttpMethod.GET, iCallback, null);
    }

    public Share get() throws ClientException {
        return (Share) send(HttpMethod.GET, null);
    }

    @Deprecated
    public void update(Share share, ICallback<Share> iCallback) {
        patch(share, iCallback);
    }

    @Deprecated
    public Share update(Share share) throws ClientException {
        return patch(share);
    }

    public void patch(Share share, ICallback<Share> iCallback) {
        send(HttpMethod.PATCH, iCallback, share);
    }

    public Share patch(Share share) throws ClientException {
        return (Share) send(HttpMethod.PATCH, share);
    }

    public void delete(ICallback<Void> iCallback) {
        send(HttpMethod.DELETE, iCallback, null);
    }

    public void delete() throws ClientException {
        send(HttpMethod.DELETE, null);
    }

    @Deprecated
    public void create(Share share, ICallback<Share> iCallback) {
        post(share, iCallback);
    }

    @Deprecated
    public Share create(Share share) throws ClientException {
        return post(share);
    }

    public void post(Share share, ICallback<Share> iCallback) {
        send(HttpMethod.POST, iCallback, share);
    }

    public Share post(Share share) throws ClientException {
        return (Share) send(HttpMethod.POST, share);
    }

    public IShareRequest select(String str) {
        getQueryOptions().add(new QueryOption("select", str));
        return (ShareRequest) this;
    }

    public IShareRequest top(int i) {
        List queryOptions = getQueryOptions();
        StringBuilder sb = new StringBuilder();
        sb.append(i);
        sb.append("");
        queryOptions.add(new QueryOption("top", sb.toString()));
        return (ShareRequest) this;
    }

    public IShareRequest expand(String str) {
        getQueryOptions().add(new QueryOption("expand", str));
        return (ShareRequest) this;
    }
}
