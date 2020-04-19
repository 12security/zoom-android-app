package com.onedrive.sdk.generated;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.onedrive.sdk.extensions.Item;
import com.onedrive.sdk.serializer.IJsonBackedObject;
import com.onedrive.sdk.serializer.ISerializer;
import java.util.List;

public class BaseItemCollectionResponse implements IJsonBackedObject {
    private transient JsonObject mRawObject;
    private transient ISerializer mSerializer;
    @SerializedName("@odata.nextLink")
    public String nextLink;
    @SerializedName("value")
    public List<Item> value;

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
        if (jsonObject.has("value")) {
            JsonArray asJsonArray = jsonObject.getAsJsonArray("value");
            for (int i = 0; i < asJsonArray.size(); i++) {
                ((Item) this.value.get(i)).setRawObject(this.mSerializer, (JsonObject) asJsonArray.get(i));
            }
        }
    }
}
