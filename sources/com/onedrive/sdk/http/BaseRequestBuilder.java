package com.onedrive.sdk.http;

import com.onedrive.sdk.extensions.IOneDriveClient;
import com.onedrive.sdk.options.Option;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class BaseRequestBuilder implements IRequestBuilder {
    private final IOneDriveClient mClient;
    private final List<Option> mOptions = new ArrayList();
    private final String mRequestUrl;

    public BaseRequestBuilder(String str, IOneDriveClient iOneDriveClient, List<Option> list) {
        this.mRequestUrl = str;
        this.mClient = iOneDriveClient;
        if (list != null) {
            this.mOptions.addAll(list);
        }
    }

    public IOneDriveClient getClient() {
        return this.mClient;
    }

    public String getRequestUrl() {
        return this.mRequestUrl;
    }

    public List<Option> getOptions() {
        return Collections.unmodifiableList(this.mOptions);
    }

    public String getRequestUrlWithAdditionalSegment(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(this.mRequestUrl);
        sb.append("/");
        sb.append(str);
        return sb.toString();
    }
}
