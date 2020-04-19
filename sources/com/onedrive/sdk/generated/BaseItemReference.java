package com.onedrive.sdk.generated;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.onedrive.sdk.serializer.IJsonBackedObject;
import com.onedrive.sdk.serializer.ISerializer;

public class BaseItemReference implements IJsonBackedObject {
    @SerializedName("driveId")
    public String driveId;
    @SerializedName("id")

    /* renamed from: id */
    public String f301id;
    private transient JsonObject mRawObject;
    private transient ISerializer mSerializer;
    @SerializedName("path")
    public String path;

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
