package com.onedrive.sdk.http;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.onedrive.sdk.serializer.IJsonBackedObject;
import com.onedrive.sdk.serializer.ISerializer;

public class OneDriveErrorResponse implements IJsonBackedObject {
    @SerializedName("error")
    public OneDriveError error;
    @Expose(deserialize = false, serialize = false)
    public JsonObject rawObject;

    public void setRawObject(ISerializer iSerializer, JsonObject jsonObject) {
        this.rawObject = jsonObject;
    }
}
