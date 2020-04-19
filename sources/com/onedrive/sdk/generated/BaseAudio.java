package com.onedrive.sdk.generated;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.onedrive.sdk.serializer.IJsonBackedObject;
import com.onedrive.sdk.serializer.ISerializer;

public class BaseAudio implements IJsonBackedObject {
    @SerializedName("album")
    public String album;
    @SerializedName("albumArtist")
    public String albumArtist;
    @SerializedName("artist")
    public String artist;
    @SerializedName("bitrate")
    public Long bitrate;
    @SerializedName("composers")
    public String composers;
    @SerializedName("copyright")
    public String copyright;
    @SerializedName("disc")
    public Short disc;
    @SerializedName("discCount")
    public Short discCount;
    @SerializedName("duration")
    public Long duration;
    @SerializedName("genre")
    public String genre;
    @SerializedName("hasDrm")
    public Boolean hasDrm;
    @SerializedName("isVariableBitrate")
    public Boolean isVariableBitrate;
    private transient JsonObject mRawObject;
    private transient ISerializer mSerializer;
    @SerializedName("title")
    public String title;
    @SerializedName("track")
    public Integer track;
    @SerializedName("trackCount")
    public Integer trackCount;
    @SerializedName("year")
    public Integer year;

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
