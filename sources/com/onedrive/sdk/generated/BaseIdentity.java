package com.onedrive.sdk.generated;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.onedrive.sdk.extensions.ThumbnailSet;
import com.onedrive.sdk.serializer.IJsonBackedObject;
import com.onedrive.sdk.serializer.ISerializer;

public class BaseIdentity implements IJsonBackedObject {
    @SerializedName("displayName")
    public String displayName;
    @SerializedName("id")

    /* renamed from: id */
    public String f299id;
    private transient JsonObject mRawObject;
    private transient ISerializer mSerializer;
    @SerializedName("thumbnails")
    public ThumbnailSet thumbnails;

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
