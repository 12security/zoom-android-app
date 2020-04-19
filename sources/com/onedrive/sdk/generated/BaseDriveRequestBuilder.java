package com.onedrive.sdk.generated;

import com.onedrive.sdk.extensions.DriveRequest;
import com.onedrive.sdk.extensions.IDriveRequest;
import com.onedrive.sdk.extensions.IItemRequestBuilder;
import com.onedrive.sdk.extensions.IOneDriveClient;
import com.onedrive.sdk.extensions.IRecentRequestBuilder;
import com.onedrive.sdk.extensions.ItemRequestBuilder;
import com.onedrive.sdk.extensions.RecentRequestBuilder;
import com.onedrive.sdk.http.BaseRequestBuilder;
import com.onedrive.sdk.options.Option;
import java.util.List;

public class BaseDriveRequestBuilder extends BaseRequestBuilder implements IBaseDriveRequestBuilder {
    public BaseDriveRequestBuilder(String str, IOneDriveClient iOneDriveClient, List<Option> list) {
        super(str, iOneDriveClient, list);
    }

    public IDriveRequest buildRequest() {
        return buildRequest(getOptions());
    }

    public IDriveRequest buildRequest(List<Option> list) {
        return new DriveRequest(getRequestUrl(), getClient(), list);
    }

    public IItemRequestBuilder getItems(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(getRequestUrlWithAdditionalSegment("items"));
        sb.append("/");
        sb.append(str);
        return new ItemRequestBuilder(sb.toString(), getClient(), null);
    }

    public IItemRequestBuilder getShared(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(getRequestUrlWithAdditionalSegment("shared"));
        sb.append("/");
        sb.append(str);
        return new ItemRequestBuilder(sb.toString(), getClient(), null);
    }

    public IItemRequestBuilder getSpecial(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(getRequestUrlWithAdditionalSegment("special"));
        sb.append("/");
        sb.append(str);
        return new ItemRequestBuilder(sb.toString(), getClient(), null);
    }

    public IRecentRequestBuilder getRecent() {
        return new RecentRequestBuilder(getRequestUrlWithAdditionalSegment("view.recent"), getClient(), null);
    }
}
