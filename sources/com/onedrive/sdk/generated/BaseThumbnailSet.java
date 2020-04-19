package com.onedrive.sdk.generated;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.onedrive.sdk.extensions.Thumbnail;
import com.onedrive.sdk.serializer.IJsonBackedObject;
import com.onedrive.sdk.serializer.ISerializer;

public class BaseThumbnailSet implements IJsonBackedObject {
    @SerializedName("id")

    /* renamed from: id */
    public String f305id;
    @SerializedName("large")
    public Thumbnail large;
    private transient JsonObject mRawObject;
    private transient ISerializer mSerializer;
    @SerializedName("medium")
    public Thumbnail medium;
    @SerializedName("small")
    public Thumbnail small;
    @SerializedName("source")
    public Thumbnail source;

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
