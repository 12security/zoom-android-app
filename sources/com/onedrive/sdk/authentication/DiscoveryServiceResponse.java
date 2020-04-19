package com.onedrive.sdk.authentication;

import com.google.gson.annotations.SerializedName;

public class DiscoveryServiceResponse {
    @SerializedName("value")
    public ServiceInfo[] services;
}
