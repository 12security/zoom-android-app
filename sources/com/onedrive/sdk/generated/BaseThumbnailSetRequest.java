package com.onedrive.sdk.generated;

import com.onedrive.sdk.concurrency.ICallback;
import com.onedrive.sdk.core.ClientException;
import com.onedrive.sdk.extensions.IOneDriveClient;
import com.onedrive.sdk.extensions.IThumbnailSetRequest;
import com.onedrive.sdk.extensions.ThumbnailSet;
import com.onedrive.sdk.extensions.ThumbnailSetRequest;
import com.onedrive.sdk.http.BaseRequest;
import com.onedrive.sdk.http.HttpMethod;
import com.onedrive.sdk.options.Option;
import com.onedrive.sdk.options.QueryOption;
import java.util.List;

public class BaseThumbnailSetRequest extends BaseRequest implements IBaseThumbnailSetRequest {
    public BaseThumbnailSetRequest(String str, IOneDriveClient iOneDriveClient, List<Option> list) {
        super(str, iOneDriveClient, list, ThumbnailSet.class);
    }

    public void get(ICallback<ThumbnailSet> iCallback) {
        send(HttpMethod.GET, iCallback, null);
    }

    public ThumbnailSet get() throws ClientException {
        return (ThumbnailSet) send(HttpMethod.GET, null);
    }

    @Deprecated
    public void update(ThumbnailSet thumbnailSet, ICallback<ThumbnailSet> iCallback) {
        patch(thumbnailSet, iCallback);
    }

    @Deprecated
    public ThumbnailSet update(ThumbnailSet thumbnailSet) throws ClientException {
        return patch(thumbnailSet);
    }

    public void patch(ThumbnailSet thumbnailSet, ICallback<ThumbnailSet> iCallback) {
        send(HttpMethod.PATCH, iCallback, thumbnailSet);
    }

    public ThumbnailSet patch(ThumbnailSet thumbnailSet) throws ClientException {
        return (ThumbnailSet) send(HttpMethod.PATCH, thumbnailSet);
    }

    public void delete(ICallback<Void> iCallback) {
        send(HttpMethod.DELETE, iCallback, null);
    }

    public void delete() throws ClientException {
        send(HttpMethod.DELETE, null);
    }

    @Deprecated
    public void create(ThumbnailSet thumbnailSet, ICallback<ThumbnailSet> iCallback) {
        post(thumbnailSet, iCallback);
    }

    @Deprecated
    public ThumbnailSet create(ThumbnailSet thumbnailSet) throws ClientException {
        return post(thumbnailSet);
    }

    public void post(ThumbnailSet thumbnailSet, ICallback<ThumbnailSet> iCallback) {
        send(HttpMethod.POST, iCallback, thumbnailSet);
    }

    public ThumbnailSet post(ThumbnailSet thumbnailSet) throws ClientException {
        return (ThumbnailSet) send(HttpMethod.POST, thumbnailSet);
    }

    public IThumbnailSetRequest select(String str) {
        getQueryOptions().add(new QueryOption("select", str));
        return (ThumbnailSetRequest) this;
    }

    public IThumbnailSetRequest top(int i) {
        List queryOptions = getQueryOptions();
        StringBuilder sb = new StringBuilder();
        sb.append(i);
        sb.append("");
        queryOptions.add(new QueryOption("top", sb.toString()));
        return (ThumbnailSetRequest) this;
    }

    public IThumbnailSetRequest expand(String str) {
        getQueryOptions().add(new QueryOption("expand", str));
        return (ThumbnailSetRequest) this;
    }
}
