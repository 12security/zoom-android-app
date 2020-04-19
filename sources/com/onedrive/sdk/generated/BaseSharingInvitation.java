package com.onedrive.sdk.generated;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.onedrive.sdk.extensions.IdentitySet;
import com.onedrive.sdk.serializer.IJsonBackedObject;
import com.onedrive.sdk.serializer.ISerializer;

public class BaseSharingInvitation implements IJsonBackedObject {
    @SerializedName("email")
    public String email;
    @SerializedName("inviteErrorResolveUrl")
    public String inviteErrorResolveUrl;
    @SerializedName("invitedBy")
    public IdentitySet invitedBy;
    private transient JsonObject mRawObject;
    private transient ISerializer mSerializer;
    @SerializedName("sendInvitationStatus")
    public String sendInvitationStatus;
    @SerializedName("signInRequired")
    public Boolean signInRequired;

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
