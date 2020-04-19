package com.onedrive.sdk.generated;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.onedrive.sdk.extensions.Identity;
import com.onedrive.sdk.serializer.IJsonBackedObject;
import com.onedrive.sdk.serializer.ISerializer;

public class BaseOpenWithApp implements IJsonBackedObject {
    @SerializedName("app")
    public Identity app;
    @SerializedName("editPostParameters")
    public String editPostParameters;
    @SerializedName("editUrl")
    public String editUrl;
    private transient JsonObject mRawObject;
    private transient ISerializer mSerializer;
    @SerializedName("viewPostParameters")
    public String viewPostParameters;
    @SerializedName("viewUrl")
    public String viewUrl;

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
