package com.onedrive.sdk.http;

import com.onedrive.sdk.extensions.IOneDriveClient;

public interface IRequestBuilder {
    IOneDriveClient getClient();

    String getRequestUrl();

    String getRequestUrlWithAdditionalSegment(String str);
}
