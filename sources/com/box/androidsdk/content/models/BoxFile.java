package com.box.androidsdk.content.models;

import android.text.TextUtils;
import com.box.androidsdk.content.models.BoxSharedLink.Permissions;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonObject.Member;
import com.eclipsesource.json.JsonValue;
import java.util.Date;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class BoxFile extends BoxItem {
    public static final String[] ALL_FIELDS = {"type", "id", "file_version", BoxItem.FIELD_SEQUENCE_ID, BoxItem.FIELD_ETAG, "sha1", "name", "created_at", "modified_at", BoxItem.FIELD_DESCRIPTION, "size", BoxItem.FIELD_PATH_COLLECTION, "created_by", "modified_by", BoxItem.FIELD_TRASHED_AT, BoxItem.FIELD_PURGED_AT, "content_created_at", "content_modified_at", BoxItem.FIELD_OWNED_BY, BoxItem.FIELD_SHARED_LINK, "parent", BoxItem.FIELD_ITEM_STATUS, FIELD_VERSION_NUMBER, "comment_count", "permissions", FIELD_EXTENSION, "is_package"};
    public static final String FIELD_COMMENT_COUNT = "comment_count";
    public static final String FIELD_CONTENT_CREATED_AT = "content_created_at";
    public static final String FIELD_CONTENT_MODIFIED_AT = "content_modified_at";
    public static final String FIELD_EXTENSION = "extension";
    public static final String FIELD_FILE_VERSION = "file_version";
    public static final String FIELD_IS_PACKAGE = "is_package";
    public static final String FIELD_SHA1 = "sha1";
    public static final String FIELD_SIZE = "size";
    public static final String FIELD_VERSION_NUMBER = "version_number";
    public static final String TYPE = "file";
    private static final long serialVersionUID = -4732748896882484735L;

    public enum Permission {
        CAN_DOWNLOAD(Permissions.FIELD_CAN_DOWNLOAD),
        CAN_UPLOAD("can_upload"),
        CAN_RENAME("can_rename"),
        CAN_DELETE("can_delete"),
        CAN_SHARE("can_share"),
        CAN_SET_SHARE_ACCESS("can_set_share_access"),
        CAN_PREVIEW("can_preview"),
        CAN_COMMENT("can_comment");
        
        private final String value;

        private Permission(String str) {
            this.value = str;
        }

        public static Permission fromString(String str) {
            Permission[] values;
            if (!TextUtils.isEmpty(str)) {
                for (Permission permission : values()) {
                    if (str.equalsIgnoreCase(permission.name())) {
                        return permission;
                    }
                }
            }
            throw new IllegalArgumentException(String.format(Locale.ENGLISH, "No enum with text %s found", new Object[]{str}));
        }

        public String toString() {
            return this.value;
        }
    }

    public BoxFile() {
    }

    public BoxFile(Map<String, Object> map) {
        super(map);
    }

    public static BoxFile createFromId(String str) {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        linkedHashMap.put("id", str);
        linkedHashMap.put("type", TYPE);
        return new BoxFile(linkedHashMap);
    }

    public BoxFileVersion getFileVersion() {
        return (BoxFileVersion) this.mProperties.get("file_version");
    }

    public String getSha1() {
        return (String) this.mProperties.get("sha1");
    }

    public String getVersionNumber() {
        return (String) this.mProperties.get(FIELD_VERSION_NUMBER);
    }

    public EnumSet<Permission> getPermissions() {
        return (EnumSet) this.mProperties.get("permissions");
    }

    public String getExtension() {
        return (String) this.mProperties.get(FIELD_EXTENSION);
    }

    public Boolean getIsPackage() {
        return (Boolean) this.mProperties.get("is_package");
    }

    public Date getContentCreatedAt() {
        return super.getContentCreatedAt();
    }

    public Long getSize() {
        return super.getSize();
    }

    public Date getContentModifiedAt() {
        return super.getContentModifiedAt();
    }

    public Long getCommentCount() {
        return super.getCommentCount();
    }

    /* access modifiers changed from: protected */
    public void parseJSONMember(Member member) {
        String name = member.getName();
        JsonValue value = member.getValue();
        if (name.equals("sha1")) {
            this.mProperties.put("sha1", value.asString());
        } else if (name.equals(FIELD_VERSION_NUMBER)) {
            this.mProperties.put(FIELD_VERSION_NUMBER, value.asString());
        } else if (name.equals("permissions")) {
            this.mProperties.put("permissions", parsePermissions(value.asObject()));
        } else if (name.equals(FIELD_EXTENSION)) {
            this.mProperties.put(FIELD_EXTENSION, value.asString());
        } else if (name.equals("is_package")) {
            this.mProperties.put("is_package", Boolean.valueOf(value.asBoolean()));
        } else if (name.equals("file_version")) {
            JsonObject asObject = value.asObject();
            BoxFileVersion boxFileVersion = new BoxFileVersion();
            boxFileVersion.createFromJson(asObject);
            this.mProperties.put("file_version", boxFileVersion);
        } else {
            super.parseJSONMember(member);
        }
    }

    private EnumSet<Permission> parsePermissions(JsonObject jsonObject) {
        EnumSet<Permission> noneOf = EnumSet.noneOf(Permission.class);
        Iterator it = jsonObject.iterator();
        while (it.hasNext()) {
            Member member = (Member) it.next();
            JsonValue value = member.getValue();
            if (!value.isNull() && value.asBoolean()) {
                String name = member.getName();
                if (name.equals(Permissions.FIELD_CAN_DOWNLOAD)) {
                    noneOf.add(Permission.CAN_DOWNLOAD);
                } else if (name.equals("can_upload")) {
                    noneOf.add(Permission.CAN_UPLOAD);
                } else if (name.equals("can_rename")) {
                    noneOf.add(Permission.CAN_RENAME);
                } else if (name.equals("can_delete")) {
                    noneOf.add(Permission.CAN_DELETE);
                } else if (name.equals("can_share")) {
                    noneOf.add(Permission.CAN_SHARE);
                } else if (name.equals("can_set_share_access")) {
                    noneOf.add(Permission.CAN_SET_SHARE_ACCESS);
                } else if (name.equals("can_preview")) {
                    noneOf.add(Permission.CAN_PREVIEW);
                } else if (name.equals("can_comment")) {
                    noneOf.add(Permission.CAN_COMMENT);
                }
            }
        }
        return noneOf;
    }
}
