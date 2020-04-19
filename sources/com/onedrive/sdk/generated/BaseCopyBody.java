package com.onedrive.sdk.generated;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.onedrive.sdk.extensions.ItemReference;
import com.onedrive.sdk.serializer.ISerializer;

public class BaseCopyBody {
    private transient JsonObject mRawObject;
    private transient ISerializer mSerializer;
    @SerializedName("name")
    public String name;
    @SerializedName("parentReference")
    public ItemReference parentReference;

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
