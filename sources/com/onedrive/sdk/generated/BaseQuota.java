package com.onedrive.sdk.generated;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.onedrive.sdk.serializer.IJsonBackedObject;
import com.onedrive.sdk.serializer.ISerializer;

public class BaseQuota implements IJsonBackedObject {
    @SerializedName("deleted")
    public Long deleted;
    private transient JsonObject mRawObject;
    private transient ISerializer mSerializer;
    @SerializedName("remaining")
    public Long remaining;
    @SerializedName("state")
    public String state;
    @SerializedName("total")
    public Long total;
    @SerializedName("used")
    public Long used;

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
