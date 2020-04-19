package com.box.androidsdk.content.models;

import com.box.androidsdk.content.utils.BoxDateFormat;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonObject.Member;
import com.eclipsesource.json.JsonValue;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class BoxComment extends BoxEntity {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    public static final String[] ALL_FIELDS = {"type", "id", FIELD_IS_REPLY_COMMENT, "message", FIELD_TAGGED_MESSAGE, "created_by", "created_at", "item", "modified_at"};
    public static final String FIELD_CREATED_AT = "created_at";
    public static final String FIELD_CREATED_BY = "created_by";
    public static final String FIELD_IS_REPLY_COMMENT = "is_reply_comment";
    public static final String FIELD_ITEM = "item";
    public static final String FIELD_MESSAGE = "message";
    public static final String FIELD_MODIFIED_AT = "modified_at";
    public static final String FIELD_TAGGED_MESSAGE = "tagged_message";
    public static final String TYPE = "comment";
    private static final long serialVersionUID = 8873984774699405343L;

    public BoxComment() {
    }

    public BoxComment(Map<String, Object> map) {
        super(map);
    }

    public Boolean getIsReplyComment() {
        return (Boolean) this.mProperties.get(FIELD_IS_REPLY_COMMENT);
    }

    public String getMessage() {
        return (String) this.mProperties.get("message");
    }

    public BoxUser getCreatedBy() {
        return (BoxUser) this.mProperties.get("created_by");
    }

    public Date getCreatedAt() {
        return (Date) this.mProperties.get("created_at");
    }

    public BoxItem getItem() {
        return (BoxItem) this.mProperties.get("item");
    }

    public Date getModifiedAt() {
        return (Date) this.mProperties.get("modified_at");
    }

    /* access modifiers changed from: protected */
    public void parseJSONMember(Member member) {
        BoxEntity boxEntity;
        try {
            String name = member.getName();
            JsonValue value = member.getValue();
            if (name.equals(FIELD_IS_REPLY_COMMENT)) {
                this.mProperties.put(FIELD_IS_REPLY_COMMENT, Boolean.valueOf(value.asBoolean()));
            } else if (name.equals("message")) {
                this.mProperties.put("message", value.asString());
            } else if (name.equals(FIELD_TAGGED_MESSAGE)) {
                this.mProperties.put(FIELD_TAGGED_MESSAGE, value.asString());
            } else if (name.equals("created_by")) {
                BoxUser boxUser = new BoxUser();
                boxUser.createFromJson(value.asObject());
                this.mProperties.put("created_by", boxUser);
            } else if (name.equals("created_at")) {
                this.mProperties.put("created_at", BoxDateFormat.parse(value.asString()));
            } else if (name.equals("modified_at")) {
                this.mProperties.put("modified_at", BoxDateFormat.parse(value.asString()));
            } else {
                if (name.equals("item")) {
                    JsonObject asObject = value.asObject();
                    String asString = asObject.get("type").asString();
                    if (asString.equals(BoxFile.TYPE)) {
                        boxEntity = new BoxFile();
                        boxEntity.createFromJson(asObject);
                    } else if (asString.equals("comment")) {
                        boxEntity = new BoxComment();
                        boxEntity.createFromJson(asObject);
                    } else if (asString.equals(BoxBookmark.TYPE)) {
                        boxEntity = new BoxBookmark();
                        boxEntity.createFromJson(asObject);
                    } else {
                        throw new IllegalArgumentException(String.format(Locale.ENGLISH, "Unsupported type \"%s\" for comment found", new Object[]{asString}));
                    }
                    this.mProperties.put("item", boxEntity);
                    return;
                }
                super.parseJSONMember(member);
            }
        } catch (ParseException unused) {
        }
    }
}
