package com.onedrive.sdk.generated;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.onedrive.sdk.extensions.IdentitySet;
import com.onedrive.sdk.extensions.Item;
import com.onedrive.sdk.extensions.ItemCollectionPage;
import com.onedrive.sdk.extensions.Quota;
import com.onedrive.sdk.serializer.IJsonBackedObject;
import com.onedrive.sdk.serializer.ISerializer;
import java.util.Arrays;

public class BaseDrive implements IJsonBackedObject {
    @SerializedName("driveType")
    public String driveType;
    @SerializedName("id")

    /* renamed from: id */
    public String f298id;
    public transient ItemCollectionPage items;
    private transient JsonObject mRawObject;
    private transient ISerializer mSerializer;
    @SerializedName("owner")
    public IdentitySet owner;
    @SerializedName("quota")
    public Quota quota;
    public transient ItemCollectionPage shared;
    public transient ItemCollectionPage special;

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
        if (jsonObject.has("shared")) {
            BaseItemCollectionResponse baseItemCollectionResponse2 = new BaseItemCollectionResponse();
            if (jsonObject.has("shared@odata.nextLink")) {
                baseItemCollectionResponse2.nextLink = jsonObject.get("shared@odata.nextLink").getAsString();
            }
            JsonObject[] jsonObjectArr2 = (JsonObject[]) iSerializer.deserializeObject(jsonObject.get("shared").toString(), JsonObject[].class);
            Item[] itemArr2 = new Item[jsonObjectArr2.length];
            for (int i2 = 0; i2 < jsonObjectArr2.length; i2++) {
                itemArr2[i2] = (Item) iSerializer.deserializeObject(jsonObjectArr2[i2].toString(), Item.class);
                itemArr2[i2].setRawObject(iSerializer, jsonObjectArr2[i2]);
            }
            baseItemCollectionResponse2.value = Arrays.asList(itemArr2);
            this.shared = new ItemCollectionPage(baseItemCollectionResponse2, null);
        }
        if (jsonObject.has("special")) {
            BaseItemCollectionResponse baseItemCollectionResponse3 = new BaseItemCollectionResponse();
            if (jsonObject.has("special@odata.nextLink")) {
                baseItemCollectionResponse3.nextLink = jsonObject.get("special@odata.nextLink").getAsString();
            }
            JsonObject[] jsonObjectArr3 = (JsonObject[]) iSerializer.deserializeObject(jsonObject.get("special").toString(), JsonObject[].class);
            Item[] itemArr3 = new Item[jsonObjectArr3.length];
            for (int i3 = 0; i3 < jsonObjectArr3.length; i3++) {
                itemArr3[i3] = (Item) iSerializer.deserializeObject(jsonObjectArr3[i3].toString(), Item.class);
                itemArr3[i3].setRawObject(iSerializer, jsonObjectArr3[i3]);
            }
            baseItemCollectionResponse3.value = Arrays.asList(itemArr3);
            this.special = new ItemCollectionPage(baseItemCollectionResponse3, null);
        }
    }
}
