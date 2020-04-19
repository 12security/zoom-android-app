package com.onedrive.sdk.http;

import com.google.gson.annotations.SerializedName;

public class OneDriveInnerError {
    @SerializedName("code")
    public String code;
    @SerializedName("debugMessage")
    public String debugMessage;
    @SerializedName("errorType")
    public String errorType;
    @SerializedName("innererror")
    public OneDriveInnerError innererror;
    @SerializedName("stackTrace")
    public String stackTrace;
    @SerializedName("throwSite")
    public String throwSite;
}
