package com.onedrive.sdk.generated;

import com.onedrive.sdk.concurrency.ICallback;
import com.onedrive.sdk.core.ClientException;
import com.onedrive.sdk.extensions.IOneDriveClient;
import com.onedrive.sdk.extensions.IThumbnailRequest;
import com.onedrive.sdk.extensions.Thumbnail;
import com.onedrive.sdk.extensions.ThumbnailRequest;
import com.onedrive.sdk.http.BaseRequest;
import com.onedrive.sdk.http.HttpMethod;
import com.onedrive.sdk.options.Option;
import com.onedrive.sdk.options.QueryOption;
import java.util.List;

public class BaseThumbnailRequest extends BaseRequest implements IBaseThumbnailRequest {
    public BaseThumbnailRequest(String str, IOneDriveClient iOneDriveClient, List<Option> list) {
        super(str, iOneDriveClient, list, Thumbnail.class);
    }

    public void get(ICallback<Thumbnail> iCallback) {
        send(HttpMethod.GET, iCallback, null);
    }

    public Thumbnail get() throws ClientException {
        return (Thumbnail) send(HttpMethod.GET, null);
    }

    @Deprecated
    public void update(Thumbnail thumbnail, ICallback<Thumbnail> iCallback) {
        patch(thumbnail, iCallback);
    }

    @Deprecated
    public Thumbnail update(Thumbnail thumbnail) throws ClientException {
        return patch(thumbnail);
    }

    public void patch(Thumbnail thumbnail, ICallback<Thumbnail> iCallback) {
        send(HttpMethod.PATCH, iCallback, thumbnail);
    }

    public Thumbnail patch(Thumbnail thumbnail) throws ClientException {
        return (Thumbnail) send(HttpMethod.PATCH, thumbnail);
    }

    public void delete(ICallback<Void> iCallback) {
        send(HttpMethod.DELETE, iCallback, null);
    }

    public void delete() throws ClientException {
        send(HttpMethod.DELETE, null);
    }

    @Deprecated
    public void create(Thumbnail thumbnail, ICallback<Thumbnail> iCallback) {
        post(thumbnail, iCallback);
    }

    @Deprecated
    public Thumbnail create(Thumbnail thumbnail) throws ClientException {
        return post(thumbnail);
    }

    public void post(Thumbnail thumbnail, ICallback<Thumbnail> iCallback) {
        send(HttpMethod.POST, iCallback, thumbnail);
    }

    public Thumbnail post(Thumbnail thumbnail) throws ClientException {
        return (Thumbnail) send(HttpMethod.POST, thumbnail);
    }

    public IThumbnailRequest select(String str) {
        getQueryOptions().add(new QueryOption("select", str));
        return (ThumbnailRequest) this;
    }

    public IThumbnailRequest top(int i) {
        List queryOptions = getQueryOptions();
        StringBuilder sb = new StringBuilder();
        sb.append(i);
        sb.append("");
        queryOptions.add(new QueryOption("top", sb.toString()));
        return (ThumbnailRequest) this;
    }

    public IThumbnailRequest expand(String str) {
        getQueryOptions().add(new QueryOption("expand", str));
        return (ThumbnailRequest) this;
    }
}
