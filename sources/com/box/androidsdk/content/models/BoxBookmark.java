package com.box.androidsdk.content.models;

import android.text.TextUtils;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonObject.Member;
import com.eclipsesource.json.JsonValue;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class BoxBookmark extends BoxItem {
    public static final String[] ALL_FIELDS = {"type", "id", BoxItem.FIELD_SEQUENCE_ID, BoxItem.FIELD_ETAG, "name", "url", "created_at", "modified_at", BoxItem.FIELD_DESCRIPTION, BoxItem.FIELD_PATH_COLLECTION, "created_by", "modified_by", BoxItem.FIELD_TRASHED_AT, BoxItem.FIELD_PURGED_AT, BoxItem.FIELD_OWNED_BY, BoxItem.FIELD_SHARED_LINK, "parent", BoxItem.FIELD_ITEM_STATUS, "permissions", "comment_count"};
    public static final String FIELD_COMMENT_COUNT = "comment_count";
    public static final String FIELD_URL = "url";
    public static final String TYPE = "web_link";

    public enum Permission {
        CAN_RENAME("can_rename"),
        CAN_DELETE("can_delete"),
        CAN_SHARE("can_share"),
        CAN_SET_SHARE_ACCESS("can_set_share_access"),
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

    public Long getSize() {
        return null;
    }

    public BoxBookmark() {
    }

    public BoxBookmark(Map<String, Object> map) {
        super(map);
    }

    public static BoxBookmark createFromId(String str) {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        linkedHashMap.put("id", str);
        linkedHashMap.put("type", TYPE);
        return new BoxBookmark(linkedHashMap);
    }

    public String getUrl() {
        return (String) this.mProperties.get("url");
    }

    public Long getCommentCount() {
        return super.getCommentCount();
    }

    public EnumSet<Permission> getPermissions() {
        return (EnumSet) this.mProperties.get("permissions");
    }

    /* access modifiers changed from: protected */
    public void parseJSONMember(Member member) {
        String name = member.getName();
        JsonValue value = member.getValue();
        if (name.equals("url")) {
            this.mProperties.put("url", value.asString());
        } else if (name.equals("permissions")) {
            this.mProperties.put("permissions", parsePermissions(value.asObject()));
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
                if (name.equals("can_rename")) {
                    noneOf.add(Permission.CAN_RENAME);
                } else if (name.equals("can_delete")) {
                    noneOf.add(Permission.CAN_DELETE);
                } else if (name.equals("can_share")) {
                    noneOf.add(Permission.CAN_SHARE);
                } else if (name.equals("can_set_share_access")) {
                    noneOf.add(Permission.CAN_SET_SHARE_ACCESS);
                } else if (name.equals("can_comment")) {
                    noneOf.add(Permission.CAN_COMMENT);
                }
            }
        }
        return noneOf;
    }
}
