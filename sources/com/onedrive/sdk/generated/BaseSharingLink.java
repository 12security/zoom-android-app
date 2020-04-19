package com.onedrive.sdk.generated;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.onedrive.sdk.extensions.Identity;
import com.onedrive.sdk.serializer.IJsonBackedObject;
import com.onedrive.sdk.serializer.ISerializer;

public class BaseSharingLink implements IJsonBackedObject {
    @SerializedName("application")
    public Identity application;
    @SerializedName("configuratorUrl")
    public String configuratorUrl;
    private transient JsonObject mRawObject;
    private transient ISerializer mSerializer;
    @SerializedName("type")
    public String type;
    @SerializedName("webHtml")
    public String webHtml;
    @SerializedName("webUrl")
    public String webUrl;

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
