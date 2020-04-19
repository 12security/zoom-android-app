package com.box.androidsdk.content.models;

import com.box.androidsdk.content.utils.BoxDateFormat;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonObject.Member;
import com.eclipsesource.json.JsonValue;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;

public class BoxFileVersion extends BoxEntity {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    public static final String[] ALL_FIELDS = {"name", "size", "sha1", "modified_by", "created_at", "modified_at", FIELD_DELETED_AT};
    public static final String FIELD_CREATED_AT = "created_at";
    public static final String FIELD_DELETED_AT = "deleted_at";
    public static final String FIELD_MODIFIED_AT = "modified_at";
    public static final String FIELD_MODIFIED_BY = "modified_by";
    public static final String FIELD_NAME = "name";
    public static final String FIELD_SHA1 = "sha1";
    public static final String FIELD_SIZE = "size";
    public static final String TYPE = "file_version";
    private static final long serialVersionUID = -1013756375421636876L;

    public BoxFileVersion() {
    }

    public BoxFileVersion(Map<String, Object> map) {
        super(map);
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

    public String getSha1() {
        return (String) this.mProperties.get("sha1");
    }

    public Date getDeletedAt() {
        return (Date) this.mProperties.get(FIELD_DELETED_AT);
    }

    public Long getSize() {
        return (Long) this.mProperties.get("size");
    }

    public BoxUser getModifiedBy() {
        return (BoxUser) this.mProperties.get("modified_by");
    }

    /* access modifiers changed from: protected */
    public void parseJSONMember(Member member) {
        try {
            String name = member.getName();
            JsonValue value = member.getValue();
            if (name.equals("name")) {
                this.mProperties.put("name", value.asString());
            } else if (name.equals("sha1")) {
                this.mProperties.put("sha1", value.asString());
            } else if (name.equals(FIELD_DELETED_AT)) {
                this.mProperties.put(FIELD_DELETED_AT, BoxDateFormat.parse(value.asString()));
            } else if (name.equals("size")) {
                this.mProperties.put("size", Long.valueOf(value.toString()));
            } else if (name.equals("modified_by")) {
                this.mProperties.put("modified_by", parseUserInfo(value.asObject()));
            } else if (name.equals("created_at")) {
                this.mProperties.put("created_at", BoxDateFormat.parse(value.asString()));
            } else {
                if (name.equals("modified_at")) {
                    this.mProperties.put("modified_at", BoxDateFormat.parse(value.asString()));
                    return;
                }
                super.parseJSONMember(member);
            }
        } catch (ParseException unused) {
        }
    }

    private BoxUser parseUserInfo(JsonObject jsonObject) {
        BoxUser boxUser = new BoxUser();
        boxUser.createFromJson(jsonObject);
        return boxUser;
    }
}
