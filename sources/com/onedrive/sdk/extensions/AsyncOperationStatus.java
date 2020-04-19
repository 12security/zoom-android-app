package com.onedrive.sdk.extensions;

import com.onedrive.sdk.generated.BaseAsyncOperationStatus;

public class AsyncOperationStatus extends BaseAsyncOperationStatus {
    public String seeOther;
    public String statusDescription;

    public static AsyncOperationStatus createdCompleted(String str) {
        AsyncOperationStatus asyncOperationStatus = new AsyncOperationStatus();
        asyncOperationStatus.seeOther = str;
        asyncOperationStatus.percentageComplete = Double.valueOf(100.0d);
        asyncOperationStatus.status = "Completed";
        return asyncOperationStatus;
    }
}
