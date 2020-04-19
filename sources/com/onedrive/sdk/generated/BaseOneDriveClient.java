package com.onedrive.sdk.generated;

import com.onedrive.sdk.core.BaseClient;
import com.onedrive.sdk.extensions.DriveCollectionRequestBuilder;
import com.onedrive.sdk.extensions.DriveRequestBuilder;
import com.onedrive.sdk.extensions.IDriveCollectionRequestBuilder;
import com.onedrive.sdk.extensions.IDriveRequestBuilder;
import com.onedrive.sdk.extensions.IOneDriveClient;
import com.onedrive.sdk.extensions.IShareCollectionRequestBuilder;
import com.onedrive.sdk.extensions.IShareRequestBuilder;
import com.onedrive.sdk.extensions.ShareCollectionRequestBuilder;
import com.onedrive.sdk.extensions.ShareRequestBuilder;

public class BaseOneDriveClient extends BaseClient implements IBaseOneDriveClient {
    public IDriveCollectionRequestBuilder getDrives() {
        StringBuilder sb = new StringBuilder();
        sb.append(getServiceRoot());
        sb.append("/drives");
        return new DriveCollectionRequestBuilder(sb.toString(), (IOneDriveClient) this, null);
    }

    public IDriveRequestBuilder getDrive(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(getServiceRoot());
        sb.append("/drives/");
        sb.append(str);
        return new DriveRequestBuilder(sb.toString(), (IOneDriveClient) this, null);
    }

    public IShareCollectionRequestBuilder getShares() {
        StringBuilder sb = new StringBuilder();
        sb.append(getServiceRoot());
        sb.append("/shares");
        return new ShareCollectionRequestBuilder(sb.toString(), (IOneDriveClient) this, null);
    }

    public IShareRequestBuilder getShare(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(getServiceRoot());
        sb.append("/shares/");
        sb.append(str);
        return new ShareRequestBuilder(sb.toString(), (IOneDriveClient) this, null);
    }
}
