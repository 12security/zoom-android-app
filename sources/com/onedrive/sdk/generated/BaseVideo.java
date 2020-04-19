package com.onedrive.sdk.generated;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.onedrive.sdk.serializer.IJsonBackedObject;
import com.onedrive.sdk.serializer.ISerializer;

public class BaseVideo implements IJsonBackedObject {
    @SerializedName("bitrate")
    public Integer bitrate;
    @SerializedName("duration")
    public Long duration;
    @SerializedName("height")
    public Integer height;
    private transient JsonObject mRawObject;
    private transient ISerializer mSerializer;
    @SerializedName("width")
    public Integer width;

    public JsonObject getRawObject() {
        return this.mRawObject;
    }

    /* access modifiers changed from: protected */
    public ISerializer getSerializer() {
        return this.mSerializer;
    }

    public void setRawObject(ISerializer iSerializer, JsonObject jsonObject) {
        this.mSerializer = iSerializer;
        this.mRawObject = jsonObject;
    }
}
