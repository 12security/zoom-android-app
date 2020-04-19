package com.onedrive.sdk.generated;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.onedrive.sdk.extensions.IdentitySet;
import com.onedrive.sdk.serializer.IJsonBackedObject;
import com.onedrive.sdk.serializer.ISerializer;
import java.util.List;

public class BaseShared implements IJsonBackedObject {
    @SerializedName("effectiveRoles")
    public List<String> effectiveRoles;
    private transient JsonObject mRawObject;
    private transient ISerializer mSerializer;
    @SerializedName("owner")
    public IdentitySet owner;
    @SerializedName("scope")
    public String scope;

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
