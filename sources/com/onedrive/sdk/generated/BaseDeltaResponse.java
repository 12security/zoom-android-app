package com.onedrive.sdk.generated;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.onedrive.sdk.extensions.ItemCollectionPage;
import com.onedrive.sdk.serializer.IJsonBackedObject;
import com.onedrive.sdk.serializer.ISerializer;

public class BaseDeltaResponse implements IJsonBackedObject {
    @SerializedName("@delta.token")
    public String delta_token;
    private transient JsonObject mRawObject;
    private transient ISerializer mSerializer;
    @SerializedName("@odata.deltaLink")
    public String odata_deltaLink;
    public transient ItemCollectionPage value;

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
