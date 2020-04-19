package com.onedrive.sdk.generated;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.onedrive.sdk.serializer.IJsonBackedObject;
import com.onedrive.sdk.serializer.ISerializer;
import java.util.Calendar;
import java.util.List;

public class BaseUploadSession implements IJsonBackedObject {
    @SerializedName("expirationDateTime")
    public Calendar expirationDateTime;
    private transient JsonObject mRawObject;
    private transient ISerializer mSerializer;
    @SerializedName("nextExpectedRanges")
    public List<String> nextExpectedRanges;
    @SerializedName("uploadUrl")
    public String uploadUrl;

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
