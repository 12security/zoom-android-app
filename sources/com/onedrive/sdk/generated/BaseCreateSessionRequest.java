package com.onedrive.sdk.generated;

import com.onedrive.sdk.concurrency.ICallback;
import com.onedrive.sdk.core.ClientException;
import com.onedrive.sdk.extensions.ChunkedUploadSessionDescriptor;
import com.onedrive.sdk.extensions.CreateSessionBody;
import com.onedrive.sdk.extensions.CreateSessionRequest;
import com.onedrive.sdk.extensions.ICreateSessionRequest;
import com.onedrive.sdk.extensions.IOneDriveClient;
import com.onedrive.sdk.extensions.UploadSession;
import com.onedrive.sdk.http.BaseRequest;
import com.onedrive.sdk.http.HttpMethod;
import com.onedrive.sdk.options.Option;
import com.onedrive.sdk.options.QueryOption;
import java.util.List;

public class BaseCreateSessionRequest extends BaseRequest implements IBaseCreateSessionRequest {
    protected final CreateSessionBody mBody = new CreateSessionBody();

    public BaseCreateSessionRequest(String str, IOneDriveClient iOneDriveClient, List<Option> list, ChunkedUploadSessionDescriptor chunkedUploadSessionDescriptor) {
        super(str, iOneDriveClient, list, UploadSession.class);
        this.mBody.item = chunkedUploadSessionDescriptor;
    }

    @Deprecated
    public void create(ICallback<UploadSession> iCallback) {
        post(iCallback);
    }

    @Deprecated
    public UploadSession create() throws ClientException {
        return post();
    }

    public void post(ICallback<UploadSession> iCallback) {
        send(HttpMethod.POST, iCallback, this.mBody);
    }

    public UploadSession post() throws ClientException {
        return (UploadSession) send(HttpMethod.POST, this.mBody);
    }

    public ICreateSessionRequest select(String str) {
        getQueryOptions().add(new QueryOption("select", str));
        return (CreateSessionRequest) this;
    }

    public ICreateSessionRequest top(int i) {
        List queryOptions = getQueryOptions();
        StringBuilder sb = new StringBuilder();
        sb.append(i);
        sb.append("");
        queryOptions.add(new QueryOption("top", sb.toString()));
        return (CreateSessionRequest) this;
    }

    public ICreateSessionRequest expand(String str) {
        getQueryOptions().add(new QueryOption("expand", str));
        return (CreateSessionRequest) this;
    }
}
