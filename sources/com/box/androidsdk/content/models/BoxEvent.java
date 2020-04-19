package com.box.androidsdk.content.models;

import com.box.androidsdk.content.utils.BoxDateFormat;
import com.eclipsesource.json.JsonObject.Member;
import com.eclipsesource.json.JsonValue;
import java.text.ParseException;
import java.util.Map;

public class BoxEvent extends BoxEntity {
    public static final String FIELD_CREATED_AT = "created_at";
    public static final String FIELD_CREATED_BY = "created_by";
    public static final String FIELD_EVENT_ID = "event_id";
    public static final String FIELD_EVENT_TYPE = "event_type";
    public static final String FIELD_IS_PACKAGE = "is_package";
    public static final String FIELD_RECORDED_AT = "recorded_at";
    public static final String FIELD_SESSION_ID = "session_id";
    public static final String FIELD_SOURCE = "source";
    public static final String FIELD_TYPE = "type";
    public static final String TYPE = "event";
    private static final long serialVersionUID = -2242620054949669032L;

    public enum Type {
        ITEM_CREATE,
        ITEM_UPLOAD,
        COMMENT_CREATE,
        ITEM_DOWNLOAD,
        ITEM_PREVIEW,
        ITEM_MOVE,
        ITEM_COPY,
        TASK_ASSIGNMENT_CREATE,
        LOCK_CREATE,
        LOCK_DESTROY,
        ITEM_TRASH,
        ITEM_UNDELETE_VIA_TRASH,
        COLLAB_ADD_COLLABORATOR,
        COLLAB_REMOVE_COLLABORATOR,
        COLLAB_INVITE_COLLABORATOR,
        COLLAB_ROLE_CHANGE,
        ITEM_SYNC,
        ITEM_UNSYNC,
        ITEM_RENAME,
        ITEM_SHARED_CREATE,
        ITEM_SHARED_UNSHARE,
        ITEM_SHARED,
        TAG_ITEM_CREATE,
        ADD_LOGIN_ACTIVITY_DEVICE,
        REMOVE_LOGIN_ACTIVITY_DEVICE,
        CHANGE_ADMIN_ROLE
    }

    /* access modifiers changed from: protected */
    public void parseJSONMember(Member member) {
        String name = member.getName();
        JsonValue value = member.getValue();
        if (name.equals("type")) {
            this.mProperties.put("type", value.asString());
        } else if (name.equals("event_id")) {
            this.mProperties.put("event_id", value.asString());
        } else if (name.equals("created_by")) {
            this.mProperties.put("created_by", BoxCollaborator.createCollaboratorFromJson(value.asObject()));
        } else if (name.equals(FIELD_EVENT_TYPE)) {
            this.mProperties.put(FIELD_EVENT_TYPE, value.asString());
        } else if (name.equals("session_id")) {
            this.mProperties.put("session_id", value.asString());
        } else if (name.equals("is_package")) {
            this.mProperties.put("is_package", Boolean.valueOf(value.asBoolean()));
        } else if (name.equals("source")) {
            this.mProperties.put("source", BoxEntity.createEntityFromJson(value.asObject()));
        } else if (name.equals("created_at")) {
            try {
                this.mProperties.put("created_at", BoxDateFormat.parse(value.asString()));
            } catch (ParseException unused) {
                this.mProperties.put("created_at", null);
            }
        } else if (name.equals(FIELD_RECORDED_AT)) {
            try {
                this.mProperties.put(FIELD_RECORDED_AT, BoxDateFormat.parse(value.asString()));
            } catch (ParseException unused2) {
                this.mProperties.put(FIELD_RECORDED_AT, null);
            }
        } else {
            super.parseJSONMember(member);
        }
    }

    public String getType() {
        return (String) this.mProperties.get("event");
    }

    public String getEventId() {
        return (String) this.mProperties.get("event_id");
    }

    public BoxCollaborator getCreatedBy() {
        return (BoxCollaborator) this.mProperties.get("created_by");
    }

    public String getEventType() {
        return (String) this.mProperties.get(FIELD_EVENT_TYPE);
    }

    public String getSessionId() {
        return (String) this.mProperties.get("session_id");
    }

    public Boolean getIsPackage() {
        return (Boolean) this.mProperties.get("is_package");
    }

    public BoxEntity getSource() {
        return (BoxEntity) this.mProperties.get("source");
    }

    public BoxEvent() {
    }

    public BoxEvent(Map<String, Object> map) {
        super(map);
    }
}
