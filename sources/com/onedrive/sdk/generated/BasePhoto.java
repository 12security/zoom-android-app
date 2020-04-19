package com.onedrive.sdk.generated;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.onedrive.sdk.serializer.IJsonBackedObject;
import com.onedrive.sdk.serializer.ISerializer;
import java.util.Calendar;

public class BasePhoto implements IJsonBackedObject {
    @SerializedName("cameraMake")
    public String cameraMake;
    @SerializedName("cameraModel")
    public String cameraModel;
    @SerializedName("exposureDenominator")
    public Double exposureDenominator;
    @SerializedName("exposureNumerator")
    public Double exposureNumerator;
    @SerializedName("fNumber")
    public Double fNumber;
    @SerializedName("focalLength")
    public Double focalLength;
    @SerializedName("iso")
    public Integer iso;
    private transient JsonObject mRawObject;
    private transient ISerializer mSerializer;
    @SerializedName("takenDateTime")
    public Calendar takenDateTime;

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
