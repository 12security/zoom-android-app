package com.onedrive.sdk.generated;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.onedrive.sdk.extensions.Audio;
import com.onedrive.sdk.extensions.Deleted;
import com.onedrive.sdk.extensions.File;
import com.onedrive.sdk.extensions.FileSystemInfo;
import com.onedrive.sdk.extensions.Folder;
import com.onedrive.sdk.extensions.IdentitySet;
import com.onedrive.sdk.extensions.Image;
import com.onedrive.sdk.extensions.Item;
import com.onedrive.sdk.extensions.ItemCollectionPage;
import com.onedrive.sdk.extensions.ItemReference;
import com.onedrive.sdk.extensions.Location;
import com.onedrive.sdk.extensions.OpenWithSet;
import com.onedrive.sdk.extensions.Permission;
import com.onedrive.sdk.extensions.PermissionCollectionPage;
import com.onedrive.sdk.extensions.Photo;
import com.onedrive.sdk.extensions.SearchResult;
import com.onedrive.sdk.extensions.Shared;
import com.onedrive.sdk.extensions.SpecialFolder;
import com.onedrive.sdk.extensions.ThumbnailSet;
import com.onedrive.sdk.extensions.ThumbnailSetCollectionPage;
import com.onedrive.sdk.extensions.Video;
import com.onedrive.sdk.serializer.IJsonBackedObject;
import com.onedrive.sdk.serializer.ISerializer;
import java.util.Arrays;
import java.util.Calendar;

public class BaseItem implements IJsonBackedObject {
    @SerializedName("audio")
    public Audio audio;
    @SerializedName("cTag")
    public String cTag;
    public transient ItemCollectionPage children;
    @SerializedName("createdBy")
    public IdentitySet createdBy;
    @SerializedName("createdDateTime")
    public Calendar createdDateTime;
    @SerializedName("deleted")
    public Deleted deleted;
    @SerializedName("description")
    public String description;
    @SerializedName("eTag")
    public String eTag;
    @SerializedName("file")
    public File file;
    @SerializedName("fileSystemInfo")
    public FileSystemInfo fileSystemInfo;
    @SerializedName("folder")
    public Folder folder;
    @SerializedName("id")

    /* renamed from: id */
    public String f300id;
    @SerializedName("image")
    public Image image;
    @SerializedName("lastModifiedBy")
    public IdentitySet lastModifiedBy;
    @SerializedName("lastModifiedDateTime")
    public Calendar lastModifiedDateTime;
    @SerializedName("location")
    public Location location;
    private transient JsonObject mRawObject;
    private transient ISerializer mSerializer;
    @SerializedName("name")
    public String name;
    @SerializedName("openWith")
    public OpenWithSet openWith;
    @SerializedName("parentReference")
    public ItemReference parentReference;
    public transient PermissionCollectionPage permissions;
    @SerializedName("photo")
    public Photo photo;
    @SerializedName("remoteItem")
    public Item remoteItem;
    @SerializedName("searchResult")
    public SearchResult searchResult;
    @SerializedName("shared")
    public Shared shared;
    @SerializedName("size")
    public Long size;
    @SerializedName("specialFolder")
    public SpecialFolder specialFolder;
    public transient ThumbnailSetCollectionPage thumbnails;
    public transient ItemCollectionPage versions;
    @SerializedName("video")
    public Video video;
    @SerializedName("webUrl")
    public String webUrl;

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
        if (jsonObject.has("permissions")) {
            BasePermissionCollectionResponse basePermissionCollectionResponse = new BasePermissionCollectionResponse();
            if (jsonObject.has("permissions@odata.nextLink")) {
                basePermissionCollectionResponse.nextLink = jsonObject.get("permissions@odata.nextLink").getAsString();
            }
            JsonObject[] jsonObjectArr = (JsonObject[]) iSerializer.deserializeObject(jsonObject.get("permissions").toString(), JsonObject[].class);
            Permission[] permissionArr = new Permission[jsonObjectArr.length];
            for (int i = 0; i < jsonObjectArr.length; i++) {
                permissionArr[i] = (Permission) iSerializer.deserializeObject(jsonObjectArr[i].toString(), Permission.class);
                permissionArr[i].setRawObject(iSerializer, jsonObjectArr[i]);
            }
            basePermissionCollectionResponse.value = Arrays.asList(permissionArr);
            this.permissions = new PermissionCollectionPage(basePermissionCollectionResponse, null);
        }
        if (jsonObject.has("versions")) {
            BaseItemCollectionResponse baseItemCollectionResponse = new BaseItemCollectionResponse();
            if (jsonObject.has("versions@odata.nextLink")) {
                baseItemCollectionResponse.nextLink = jsonObject.get("versions@odata.nextLink").getAsString();
            }
            JsonObject[] jsonObjectArr2 = (JsonObject[]) iSerializer.deserializeObject(jsonObject.get("versions").toString(), JsonObject[].class);
            Item[] itemArr = new Item[jsonObjectArr2.length];
            for (int i2 = 0; i2 < jsonObjectArr2.length; i2++) {
                itemArr[i2] = (Item) iSerializer.deserializeObject(jsonObjectArr2[i2].toString(), Item.class);
                itemArr[i2].setRawObject(iSerializer, jsonObjectArr2[i2]);
            }
            baseItemCollectionResponse.value = Arrays.asList(itemArr);
            this.versions = new ItemCollectionPage(baseItemCollectionResponse, null);
        }
        if (jsonObject.has("children")) {
            BaseItemCollectionResponse baseItemCollectionResponse2 = new BaseItemCollectionResponse();
            if (jsonObject.has("children@odata.nextLink")) {
                baseItemCollectionResponse2.nextLink = jsonObject.get("children@odata.nextLink").getAsString();
            }
            JsonObject[] jsonObjectArr3 = (JsonObject[]) iSerializer.deserializeObject(jsonObject.get("children").toString(), JsonObject[].class);
            Item[] itemArr2 = new Item[jsonObjectArr3.length];
            for (int i3 = 0; i3 < jsonObjectArr3.length; i3++) {
                itemArr2[i3] = (Item) iSerializer.deserializeObject(jsonObjectArr3[i3].toString(), Item.class);
                itemArr2[i3].setRawObject(iSerializer, jsonObjectArr3[i3]);
            }
            baseItemCollectionResponse2.value = Arrays.asList(itemArr2);
            this.children = new ItemCollectionPage(baseItemCollectionResponse2, null);
        }
        if (jsonObject.has("thumbnails")) {
            BaseThumbnailSetCollectionResponse baseThumbnailSetCollectionResponse = new BaseThumbnailSetCollectionResponse();
            if (jsonObject.has("thumbnails@odata.nextLink")) {
                baseThumbnailSetCollectionResponse.nextLink = jsonObject.get("thumbnails@odata.nextLink").getAsString();
            }
            JsonObject[] jsonObjectArr4 = (JsonObject[]) iSerializer.deserializeObject(jsonObject.get("thumbnails").toString(), JsonObject[].class);
            ThumbnailSet[] thumbnailSetArr = new ThumbnailSet[jsonObjectArr4.length];
            for (int i4 = 0; i4 < jsonObjectArr4.length; i4++) {
                thumbnailSetArr[i4] = (ThumbnailSet) iSerializer.deserializeObject(jsonObjectArr4[i4].toString(), ThumbnailSet.class);
                thumbnailSetArr[i4].setRawObject(iSerializer, jsonObjectArr4[i4]);
            }
            baseThumbnailSetCollectionResponse.value = Arrays.asList(thumbnailSetArr);
            this.thumbnails = new ThumbnailSetCollectionPage(baseThumbnailSetCollectionResponse, null);
        }
    }
}
