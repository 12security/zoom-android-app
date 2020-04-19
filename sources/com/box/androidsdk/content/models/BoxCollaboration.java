package com.box.androidsdk.content.models;

import android.text.TextUtils;
import com.box.androidsdk.content.utils.BoxDateFormat;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonObject.Member;
import com.eclipsesource.json.JsonValue;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class BoxCollaboration extends BoxEntity {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    public static final String[] ALL_FIELDS = {"type", "id", "created_by", "created_at", "modified_at", FIELD_EXPIRES_AT, "status", "accessible_by", "role", FIELD_ACKNOWLEDGED_AT, "item"};
    public static final String FIELD_ACCESSIBLE_BY = "accessible_by";
    public static final String FIELD_ACKNOWLEDGED_AT = "acknowledged_at";
    public static final String FIELD_CREATED_AT = "created_at";
    public static final String FIELD_CREATED_BY = "created_by";
    public static final String FIELD_EXPIRES_AT = "expires_at";
    public static final String FIELD_ITEM = "item";
    public static final String FIELD_MODIFIED_AT = "modified_at";
    public static final String FIELD_ROLE = "role";
    public static final String FIELD_STATUS = "status";
    public static final String TYPE = "collaboration";
    private static final long serialVersionUID = 8125965031679671555L;

    public enum Role {
        EDITOR("editor"),
        VIEWER("viewer"),
        PREVIEWER("previewer"),
        UPLOADER("uploader"),
        PREVIEWER_UPLOADER("previewer uploader"),
        VIEWER_UPLOADER("viewer uploader"),
        CO_OWNER("co-owner"),
        OWNER("owner");
        
        private final String mValue;

        private Role(String str) {
            this.mValue = str;
        }

        public static Role fromString(String str) {
            Role[] values;
            if (!TextUtils.isEmpty(str)) {
                for (Role role : values()) {
                    if (str.equalsIgnoreCase(role.toString())) {
                        return role;
                    }
                }
            }
            throw new IllegalArgumentException(String.format(Locale.ENGLISH, "No enum with text %s found", new Object[]{str}));
        }

        public String toString() {
            return this.mValue;
        }
    }

    public enum Status {
        ACCEPTED("accepted"),
        PENDING("pending"),
        REJECTED("rejected");
        
        private final String mValue;

        private Status(String str) {
            this.mValue = str;
        }

        public static Status fromString(String str) {
            Status[] values;
            if (!TextUtils.isEmpty(str)) {
                for (Status status : values()) {
                    if (str.equalsIgnoreCase(status.toString())) {
                        return status;
                    }
                }
            }
            throw new IllegalArgumentException(String.format(Locale.ENGLISH, "No enum with text %s found", new Object[]{str}));
        }

        public String toString() {
            return this.mValue;
        }
    }

    public BoxCollaboration() {
    }

    public BoxCollaboration(Map<String, Object> map) {
        super(map);
    }

    public BoxCollaborator getCreatedBy() {
        return (BoxCollaborator) this.mProperties.get("created_by");
    }

    public Date getCreatedAt() {
        return (Date) this.mProperties.get("created_at");
    }

    public Date getModifiedAt() {
        return (Date) this.mProperties.get("modified_at");
    }

    public Date getExpiresAt() {
        return (Date) this.mProperties.get(FIELD_EXPIRES_AT);
    }

    public Status getStatus() {
        return (Status) this.mProperties.get("status");
    }

    public BoxCollaborator getAccessibleBy() {
        return (BoxCollaborator) this.mProperties.get("accessible_by");
    }

    public Role getRole() {
        return (Role) this.mProperties.get("role");
    }

    public Date getAcknowledgedAt() {
        return (Date) this.mProperties.get(FIELD_ACKNOWLEDGED_AT);
    }

    public BoxFolder getItem() {
        return (BoxFolder) this.mProperties.get("item");
    }

    /* access modifiers changed from: protected */
    public void parseJSONMember(Member member) {
        String name = member.getName();
        JsonValue value = member.getValue();
        try {
            if (name.equals("created_by")) {
                this.mProperties.put("created_by", BoxCollaborator.createCollaboratorFromJson(value.asObject()));
            } else if (name.equals("created_at")) {
                this.mProperties.put("created_at", BoxDateFormat.parse(value.asString()));
            } else if (name.equals("modified_at")) {
                this.mProperties.put("modified_at", BoxDateFormat.parse(value.asString()));
            } else if (name.equals(FIELD_EXPIRES_AT)) {
                this.mProperties.put(FIELD_EXPIRES_AT, BoxDateFormat.parse(value.asString()));
            } else if (name.equals("status")) {
                this.mProperties.put("status", Status.fromString(value.asString()));
            } else if (name.equals("accessible_by")) {
                BoxUser boxUser = new BoxUser();
                boxUser.createFromJson(value.asObject());
                this.mProperties.put("accessible_by", boxUser);
            } else if (name.equals("role")) {
                this.mProperties.put("role", Role.fromString(value.asString()));
            } else if (name.equals(FIELD_ACKNOWLEDGED_AT)) {
                this.mProperties.put(FIELD_ACKNOWLEDGED_AT, BoxDateFormat.parse(value.asString()));
            } else {
                if (name.equals("item")) {
                    JsonObject asObject = value.asObject();
                    String asString = asObject.get("type").asString();
                    if (asString.equals(BoxFolder.TYPE)) {
                        BoxFolder boxFolder = new BoxFolder();
                        boxFolder.createFromJson(asObject);
                        this.mProperties.put("item", boxFolder);
                        return;
                    }
                    throw new IllegalArgumentException(String.format(Locale.ENGLISH, "Unsupported type \"%s\" for collaboration found", new Object[]{asString}));
                }
                super.parseJSONMember(member);
            }
        } catch (ParseException unused) {
        }
    }
}
