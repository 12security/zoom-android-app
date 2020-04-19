package com.onedrive.sdk.concurrency;

import com.onedrive.sdk.extensions.IOneDriveClient;

public interface ResultGetter<T> {
    T getResultFrom(String str, IOneDriveClient iOneDriveClient);
}
