package com.onedrive.sdk.generated;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.onedrive.sdk.serializer.IJsonBackedObject;
import com.onedrive.sdk.serializer.ISerializer;
import java.util.Calendar;

public class BaseFileSystemInfo implements IJsonBackedObject {
    @SerializedName("createdDateTime")
    public Calendar createdDateTime;
    @SerializedName("lastModifiedDateTime")
    public Calendar lastModifiedDateTime;
    private transient JsonObject mRawObject;
    private transient ISerializer mSerializer;

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
