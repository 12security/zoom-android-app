package com.onedrive.sdk.concurrency;

import com.onedrive.sdk.core.ClientException;
import com.onedrive.sdk.core.OneDriveErrorCodes;
import com.onedrive.sdk.extensions.AsyncOperationStatus;

public class AsyncOperationException extends ClientException {
    private final AsyncOperationStatus mStatus;

    public AsyncOperationException(AsyncOperationStatus asyncOperationStatus) {
        StringBuilder sb = new StringBuilder();
        sb.append(asyncOperationStatus.status);
        sb.append(": ");
        sb.append(asyncOperationStatus.statusDescription);
        super(sb.toString(), null, OneDriveErrorCodes.AsyncTaskFailed);
        this.mStatus = asyncOperationStatus;
    }

    public AsyncOperationStatus getStatus() {
        return this.mStatus;
    }
}
