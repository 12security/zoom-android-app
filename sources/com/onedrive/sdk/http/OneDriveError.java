package com.onedrive.sdk.http;

import com.google.gson.annotations.SerializedName;
import com.onedrive.sdk.core.OneDriveErrorCodes;

public class OneDriveError {
    @SerializedName("code")
    public String code;
    @SerializedName("innererror")
    public OneDriveInnerError innererror;
    @SerializedName("message")
    public String message;

    public boolean isError(OneDriveErrorCodes oneDriveErrorCodes) {
        if (this.code.equalsIgnoreCase(oneDriveErrorCodes.toString())) {
            return true;
        }
        for (OneDriveInnerError oneDriveInnerError = this.innererror; oneDriveInnerError != null; oneDriveInnerError = oneDriveInnerError.innererror) {
            if (oneDriveInnerError.code.equalsIgnoreCase(oneDriveErrorCodes.toString())) {
                return true;
            }
        }
        return false;
    }
}
