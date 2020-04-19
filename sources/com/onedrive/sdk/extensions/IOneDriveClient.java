package com.onedrive.sdk.extensions;

import android.app.Activity;
import com.onedrive.sdk.concurrency.ICallback;
import com.onedrive.sdk.core.ClientException;
import com.onedrive.sdk.generated.IBaseOneDriveClient;

public interface IOneDriveClient extends IBaseOneDriveClient {
    IDriveRequestBuilder getDrive();

    void login(Activity activity, ICallback<IOneDriveClient> iCallback) throws ClientException;
}
