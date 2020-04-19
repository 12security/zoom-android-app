package com.onedrive.sdk.generated;

import com.onedrive.sdk.extensions.IOneDriveClient;
import com.onedrive.sdk.extensions.ISearchRequest;
import com.onedrive.sdk.extensions.SearchRequest;
import com.onedrive.sdk.http.BaseRequestBuilder;
import com.onedrive.sdk.options.Option;
import java.util.List;

public class BaseSearchRequestBuilder extends BaseRequestBuilder {

    /* renamed from: mQ */
    public final String f303mQ;

    public BaseSearchRequestBuilder(String str, IOneDriveClient iOneDriveClient, List<Option> list, String str2) {
        super(str, iOneDriveClient, list);
        this.f303mQ = str2;
    }

    public ISearchRequest buildRequest() {
        return buildRequest(getOptions());
    }

    public ISearchRequest buildRequest(List<Option> list) {
        return new SearchRequest(getRequestUrl(), getClient(), list, this.f303mQ);
    }
}
