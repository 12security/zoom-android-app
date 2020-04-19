package com.onedrive.sdk.authentication;

import com.google.gson.annotations.SerializedName;

public class ServiceInfo {
    @SerializedName("capability")
    public String capability;
    @SerializedName("serviceApiVersion")
    public String serviceApiVersion;
    @SerializedName("serviceEndpointUri")
    public String serviceEndpointUri;
    @SerializedName("serviceResourceId")
    public String serviceResourceId;
}
