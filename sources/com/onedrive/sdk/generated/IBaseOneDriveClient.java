package com.onedrive.sdk.generated;

import com.onedrive.sdk.core.IBaseClient;
import com.onedrive.sdk.extensions.IDriveCollectionRequestBuilder;
import com.onedrive.sdk.extensions.IDriveRequestBuilder;
import com.onedrive.sdk.extensions.IShareCollectionRequestBuilder;
import com.onedrive.sdk.extensions.IShareRequestBuilder;

public interface IBaseOneDriveClient extends IBaseClient {
    IDriveRequestBuilder getDrive(String str);

    IDriveCollectionRequestBuilder getDrives();

    IShareRequestBuilder getShare(String str);

    IShareCollectionRequestBuilder getShares();
}
