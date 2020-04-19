package com.box.androidsdk.content.models;

import com.box.androidsdk.content.models.BoxSharedLink.Access;
import com.box.androidsdk.content.utils.BoxDateFormat;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonObject.Member;
import com.eclipsesource.json.JsonValue;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public abstract class BoxItem extends BoxEntity {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    public static final String FIELD_ALLOWED_SHARED_LINK_ACCESS_LEVELS = "allowed_shared_link_access_levels";
    public static final String FIELD_CREATED_AT = "created_at";
    public static final String FIELD_CREATED_BY = "created_by";
    public static final String FIELD_DESCRIPTION = "description";
    public static final String FIELD_ETAG = "etag";
    public static final String FIELD_ITEM_STATUS = "item_status";
    public static final String FIELD_MODIFIED_AT = "modified_at";
    public static final String FIELD_MODIFIED_BY = "modified_by";
    public static final String FIELD_NAME = "name";
    public static final String FIELD_OWNED_BY = "owned_by";
    public static final String FIELD_PARENT = "parent";
    public static final String FIELD_PATH_COLLECTION = "path_collection";
    public static final String FIELD_PERMISSIONS = "permissions";
    public static final String FIELD_PURGED_AT = "purged_at";
    public static final String FIELD_SEQUENCE_ID = "sequence_id";
    public static final String FIELD_SHARED_LINK = "shared_link";
    public static final String FIELD_SYNCED = "synced";
    public static final String FIELD_TAGS = "tags";
    public static final String FIELD_TRASHED_AT = "trashed_at";
    private static final long serialVersionUID = 4876182952337609430L;

    public BoxItem() {
    }

    public BoxItem(Map<String, Object> map) {
        super(map);
    }

    public String getEtag() {
        return (String) this.mProperties.get(FIELD_ETAG);
    }

    public String getName() {
        return (String) this.mProperties.get("name");
    }

    public Date getCreatedAt() {
        return (Date) this.mProperties.get("created_at");
    }

    public Date getModifiedAt() {
        return (Date) this.mProperties.get("modified_at");
    }

    public String getDescription() {
        return (String) this.mProperties.get(FIELD_DESCRIPTION);
    }

    public Long getSize() {
        return (Long) this.mProperties.get("size");
    }

    public BoxList<BoxFolder> getPathCollection() {
        return (BoxList) this.mProperties.get(FIELD_PATH_COLLECTION);
    }

    public BoxUser getCreatedBy() {
        return (BoxUser) this.mProperties.get("created_by");
    }

    public BoxUser getModifiedBy() {
        return (BoxUser) this.mProperties.get("modified_by");
    }

    public Date getTrashedAt() {
        return (Date) this.mProperties.get(FIELD_TRASHED_AT);
    }

    public Date getPurgedAt() {
        return (Date) this.mProperties.get(FIELD_PURGED_AT);
    }

    /* access modifiers changed from: protected */
    public Date getContentCreatedAt() {
        return (Date) this.mProperties.get("content_created_at");
    }

    /* access modifiers changed from: protected */
    public Date getContentModifiedAt() {
        return (Date) this.mProperties.get("content_modified_at");
    }

    public BoxUser getOwnedBy() {
        return (BoxUser) this.mProperties.get(FIELD_OWNED_BY);
    }

    public BoxSharedLink getSharedLink() {
        return (BoxSharedLink) this.mProperties.get(FIELD_SHARED_LINK);
    }

    public String getSequenceID() {
        return (String) this.mProperties.get(FIELD_SEQUENCE_ID);
    }

    public ArrayList<Access> getAllowedSharedLinkAccessLevels() {
        return (ArrayList) this.mProperties.get(FIELD_ALLOWED_SHARED_LINK_ACCESS_LEVELS);
    }

    public BoxFolder getParent() {
        return (BoxFolder) this.mProperties.get("parent");
    }

    public String getItemStatus() {
        return (String) this.mProperties.get(FIELD_ITEM_STATUS);
    }

    public Boolean getIsSynced() {
        return (Boolean) this.mProperties.get(FIELD_SYNCED);
    }

    /* access modifiers changed from: protected */
    public Long getCommentCount() {
        return (Long) this.mProperties.get("comment_count");
    }

    /* access modifiers changed from: protected */
    public void parseJSONMember(Member member) {
        try {
            JsonValue value = member.getValue();
            if (member.getName().equals("name")) {
                this.mProperties.put("name", value.asString());
            } else if (member.getName().equals(FIELD_SEQUENCE_ID)) {
                this.mProperties.put(FIELD_SEQUENCE_ID, value.asString());
            } else if (member.getName().equals(FIELD_ETAG)) {
                this.mProperties.put(FIELD_ETAG, value.asString());
            } else if (member.getName().equals("created_at")) {
                this.mProperties.put("created_at", BoxDateFormat.parse(value.asString()));
            } else if (member.getName().equals("modified_at")) {
                this.mProperties.put("modified_at", BoxDateFormat.parse(value.asString()));
            } else if (member.getName().equals(FIELD_DESCRIPTION)) {
                this.mProperties.put(FIELD_DESCRIPTION, value.asString());
            } else if (member.getName().equals("size")) {
                this.mProperties.put("size", Long.valueOf(value.toString()));
            } else if (member.getName().equals(FIELD_TRASHED_AT)) {
                this.mProperties.put(FIELD_TRASHED_AT, BoxDateFormat.parse(value.asString()));
            } else if (member.getName().equals(FIELD_PURGED_AT)) {
                this.mProperties.put(FIELD_PURGED_AT, BoxDateFormat.parse(value.asString()));
            } else if (member.getName().equals("content_created_at")) {
                this.mProperties.put("content_created_at", BoxDateFormat.parse(value.asString()));
            } else if (member.getName().equals("content_modified_at")) {
                this.mProperties.put("content_modified_at", BoxDateFormat.parse(value.asString()));
            } else if (member.getName().equals(FIELD_PATH_COLLECTION)) {
                JsonObject asObject = value.asObject();
                BoxList boxList = new BoxList();
                boxList.createFromJson(asObject);
                this.mProperties.put(FIELD_PATH_COLLECTION, boxList);
            } else if (member.getName().equals("created_by")) {
                this.mProperties.put("created_by", parseUserInfo(value.asObject()));
            } else if (member.getName().equals("modified_by")) {
                this.mProperties.put("modified_by", parseUserInfo(value.asObject()));
            } else if (member.getName().equals(FIELD_OWNED_BY)) {
                this.mProperties.put(FIELD_OWNED_BY, parseUserInfo(value.asObject()));
            } else if (member.getName().equals(FIELD_SHARED_LINK)) {
                BoxSharedLink boxSharedLink = new BoxSharedLink();
                boxSharedLink.createFromJson(value.asObject());
                this.mProperties.put(FIELD_SHARED_LINK, boxSharedLink);
            } else if (member.getName().equals("parent")) {
                BoxFolder boxFolder = new BoxFolder();
                boxFolder.createFromJson(value.asObject());
                this.mProperties.put("parent", boxFolder);
            } else if (member.getName().equals(FIELD_ITEM_STATUS)) {
                this.mProperties.put(FIELD_ITEM_STATUS, value.asString());
            } else if (member.getName().equals(FIELD_SYNCED)) {
                this.mProperties.put(FIELD_SYNCED, Boolean.valueOf(value.asBoolean()));
            } else if (member.getName().equals("comment_count")) {
                this.mProperties.put("comment_count", Long.valueOf(value.asLong()));
            } else if (member.getName().equals(FIELD_ALLOWED_SHARED_LINK_ACCESS_LEVELS)) {
                JsonArray asArray = value.asArray();
                ArrayList arrayList = new ArrayList();
                Iterator it = asArray.iterator();
                while (it.hasNext()) {
                    arrayList.add(Access.fromString(((JsonValue) it.next()).asString()));
                }
                this.mProperties.put(FIELD_ALLOWED_SHARED_LINK_ACCESS_LEVELS, arrayList);
            } else {
                if (member.getName().equals(FIELD_TAGS)) {
                    this.mProperties.put(FIELD_TAGS, value.asArray());
                    return;
                }
                super.parseJSONMember(member);
            }
        } catch (Exception unused) {
        }
    }

    private List<BoxFolder> parsePathCollection(JsonObject jsonObject) {
        ArrayList arrayList = new ArrayList(jsonObject.get(BoxList.FIELD_TOTAL_COUNT).asInt());
        Iterator it = jsonObject.get(BoxList.FIELD_ENTRIES).asArray().iterator();
        while (it.hasNext()) {
            JsonObject asObject = ((JsonValue) it.next()).asObject();
            BoxFolder boxFolder = new BoxFolder();
            boxFolder.createFromJson(asObject);
            arrayList.add(boxFolder);
        }
        return arrayList;
    }

    private BoxUser parseUserInfo(JsonObject jsonObject) {
        BoxUser boxUser = new BoxUser();
        boxUser.createFromJson(jsonObject);
        return boxUser;
    }

    private List<String> parseTags(JsonArray jsonArray) {
        ArrayList arrayList = new ArrayList(jsonArray.size());
        Iterator it = jsonArray.iterator();
        while (it.hasNext()) {
            arrayList.add(((JsonValue) it.next()).asString());
        }
        return arrayList;
    }

    /* access modifiers changed from: protected */
    public JsonValue parseJsonObject(Entry<String, Object> entry) {
        return super.parseJsonObject(entry);
    }

    public static BoxItem createBoxItemFromJson(String str) {
        BoxEntity boxEntity = new BoxEntity();
        boxEntity.createFromJson(str);
        if (boxEntity.getType().equals(BoxFile.TYPE)) {
            BoxFile boxFile = new BoxFile();
            boxFile.createFromJson(str);
            return boxFile;
        } else if (boxEntity.getType().equals(BoxBookmark.TYPE)) {
            BoxBookmark boxBookmark = new BoxBookmark();
            boxBookmark.createFromJson(str);
            return boxBookmark;
        } else if (!boxEntity.getType().equals(BoxFolder.TYPE)) {
            return null;
        } else {
            BoxFolder boxFolder = new BoxFolder();
            boxFolder.createFromJson(str);
            return boxFolder;
        }
    }

    public static BoxItem createBoxItemFromJson(JsonObject jsonObject) {
        BoxEntity boxEntity = new BoxEntity();
        boxEntity.createFromJson(jsonObject);
        if (boxEntity.getType().equals(BoxFile.TYPE)) {
            BoxFile boxFile = new BoxFile();
            boxFile.createFromJson(jsonObject);
            return boxFile;
        } else if (boxEntity.getType().equals(BoxBookmark.TYPE)) {
            BoxBookmark boxBookmark = new BoxBookmark();
            boxBookmark.createFromJson(jsonObject);
            return boxBookmark;
        } else if (!boxEntity.getType().equals(BoxFolder.TYPE)) {
            return null;
        } else {
            BoxFolder boxFolder = new BoxFolder();
            boxFolder.createFromJson(jsonObject);
            return boxFolder;
        }
    }
}
