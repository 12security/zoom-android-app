package com.onedrive.sdk.generated;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.onedrive.sdk.extensions.IdentitySet;
import com.onedrive.sdk.extensions.Item;
import com.onedrive.sdk.extensions.ItemCollectionPage;
import com.onedrive.sdk.serializer.IJsonBackedObject;
import com.onedrive.sdk.serializer.ISerializer;
import java.util.Arrays;

public class BaseShare implements IJsonBackedObject {
    @SerializedName("id")

    /* renamed from: id */
    public String f304id;
    public transient ItemCollectionPage items;
    private transient JsonObject mRawObject;
    private transient ISerializer mSerializer;
    @SerializedName("name")
    public String name;
    @SerializedName("owner")
    public IdentitySet owner;

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
        if (jsonObject.has("items")) {
            BaseItemCollectionResponse baseItemCollectionResponse = new BaseItemCollectionResponse();
            if (jsonObject.has("items@odata.nextLink")) {
                baseItemCollectionResponse.nextLink = jsonObject.get("items@odata.nextLink").getAsString();
            }
            JsonObject[] jsonObjectArr = (JsonObject[]) iSerializer.deserializeObject(jsonObject.get("items").toString(), JsonObject[].class);
            Item[] itemArr = new Item[jsonObjectArr.length];
            for (int i = 0; i < jsonObjectArr.length; i++) {
                itemArr[i] = (Item) iSerializer.deserializeObject(jsonObjectArr[i].toString(), Item.class);
                itemArr[i].setRawObject(iSerializer, jsonObjectArr[i]);
            }
            baseItemCollectionResponse.value = Arrays.asList(itemArr);
            this.items = new ItemCollectionPage(baseItemCollectionResponse, null);
        }
    }
}
