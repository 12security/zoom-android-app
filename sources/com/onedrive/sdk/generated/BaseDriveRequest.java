package com.onedrive.sdk.generated;

import com.onedrive.sdk.concurrency.ICallback;
import com.onedrive.sdk.core.ClientException;
import com.onedrive.sdk.extensions.Drive;
import com.onedrive.sdk.extensions.DriveRequest;
import com.onedrive.sdk.extensions.IDriveRequest;
import com.onedrive.sdk.extensions.IOneDriveClient;
import com.onedrive.sdk.http.BaseRequest;
import com.onedrive.sdk.http.HttpMethod;
import com.onedrive.sdk.options.Option;
import com.onedrive.sdk.options.QueryOption;
import java.util.List;

public class BaseDriveRequest extends BaseRequest implements IBaseDriveRequest {
    public BaseDriveRequest(String str, IOneDriveClient iOneDriveClient, List<Option> list) {
        super(str, iOneDriveClient, list, Drive.class);
    }

    public void get(ICallback<Drive> iCallback) {
        send(HttpMethod.GET, iCallback, null);
    }

    public Drive get() throws ClientException {
        return (Drive) send(HttpMethod.GET, null);
    }

    @Deprecated
    public void update(Drive drive, ICallback<Drive> iCallback) {
        patch(drive, iCallback);
    }

    @Deprecated
    public Drive update(Drive drive) throws ClientException {
        return patch(drive);
    }

    public void patch(Drive drive, ICallback<Drive> iCallback) {
        send(HttpMethod.PATCH, iCallback, drive);
    }

    public Drive patch(Drive drive) throws ClientException {
        return (Drive) send(HttpMethod.PATCH, drive);
    }

    public void delete(ICallback<Void> iCallback) {
        send(HttpMethod.DELETE, iCallback, null);
    }

    public void delete() throws ClientException {
        send(HttpMethod.DELETE, null);
    }

    @Deprecated
    public void create(Drive drive, ICallback<Drive> iCallback) {
        post(drive, iCallback);
    }

    @Deprecated
    public Drive create(Drive drive) throws ClientException {
        return post(drive);
    }

    public void post(Drive drive, ICallback<Drive> iCallback) {
        send(HttpMethod.POST, iCallback, drive);
    }

    public Drive post(Drive drive) throws ClientException {
        return (Drive) send(HttpMethod.POST, drive);
    }

    public IDriveRequest select(String str) {
        getQueryOptions().add(new QueryOption("select", str));
        return (DriveRequest) this;
    }

    public IDriveRequest top(int i) {
        List queryOptions = getQueryOptions();
        StringBuilder sb = new StringBuilder();
        sb.append(i);
        sb.append("");
        queryOptions.add(new QueryOption("top", sb.toString()));
        return (DriveRequest) this;
    }

    public IDriveRequest expand(String str) {
        getQueryOptions().add(new QueryOption("expand", str));
        return (DriveRequest) this;
    }
}
