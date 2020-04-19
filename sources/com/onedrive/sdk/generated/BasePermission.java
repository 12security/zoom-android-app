package com.onedrive.sdk.generated;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.onedrive.sdk.extensions.IdentitySet;
import com.onedrive.sdk.extensions.ItemReference;
import com.onedrive.sdk.extensions.SharingInvitation;
import com.onedrive.sdk.extensions.SharingLink;
import com.onedrive.sdk.serializer.IJsonBackedObject;
import com.onedrive.sdk.serializer.ISerializer;
import java.util.List;

public class BasePermission implements IJsonBackedObject {
    @SerializedName("grantedTo")
    public IdentitySet grantedTo;
    @SerializedName("id")

    /* renamed from: id */
    public String f302id;
    @SerializedName("inheritedFrom")
    public ItemReference inheritedFrom;
    @SerializedName("invitation")
    public SharingInvitation invitation;
    @SerializedName("link")
    public SharingLink link;
    private transient JsonObject mRawObject;
    private transient ISerializer mSerializer;
    @SerializedName("roles")
    public List<String> roles;
    @SerializedName("shareId")
    public String shareId;

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
