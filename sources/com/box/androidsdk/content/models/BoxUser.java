package com.box.androidsdk.content.models;

import android.text.TextUtils;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject.Member;
import com.eclipsesource.json.JsonValue;
import com.google.android.gms.measurement.api.AppMeasurementSdk.ConditionalUserProperty;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class BoxUser extends BoxCollaborator {
    public static final String[] ALL_FIELDS = {"type", "id", "name", "login", "created_at", "modified_at", "role", FIELD_LANGUAGE, "timezone", FIELD_SPACE_AMOUNT, FIELD_SPACE_USED, FIELD_MAX_UPLOAD_SIZE, FIELD_TRACKING_CODES, FIELD_CAN_SEE_MANAGED_USERS, FIELD_IS_SYNC_ENABLED, FIELD_IS_EXTERNAL_COLLAB_RESTRICTED, "status", FIELD_JOB_TITLE, FIELD_PHONE, FIELD_ADDRESS, FIELD_AVATAR_URL, FIELD_IS_EXEMPT_FROM_DEVICE_LIMITS, FIELD_IS_EXEMPT_FROM_LOGIN_VERIFICATION, "enterprise", FIELD_HOSTNAME, FIELD_MY_TAGS};
    public static final String FIELD_ADDRESS = "address";
    public static final String FIELD_AVATAR_URL = "avatar_url";
    public static final String FIELD_CAN_SEE_MANAGED_USERS = "can_see_managed_users";
    public static final String FIELD_ENTERPRISE = "enterprise";
    public static final String FIELD_HOSTNAME = "hostname";
    public static final String FIELD_IS_EXEMPT_FROM_DEVICE_LIMITS = "is_exempt_from_device_limits";
    public static final String FIELD_IS_EXEMPT_FROM_LOGIN_VERIFICATION = "is_exempt_from_login_verification";
    public static final String FIELD_IS_EXTERNAL_COLLAB_RESTRICTED = "is_external_collab_restricted";
    public static final String FIELD_IS_SYNC_ENABLED = "is_sync_enabled";
    public static final String FIELD_JOB_TITLE = "job_title";
    public static final String FIELD_LANGUAGE = "language";
    public static final String FIELD_LOGIN = "login";
    public static final String FIELD_MAX_UPLOAD_SIZE = "max_upload_size";
    public static final String FIELD_MY_TAGS = "my_tags";
    public static final String FIELD_PHONE = "phone";
    public static final String FIELD_ROLE = "role";
    public static final String FIELD_SPACE_AMOUNT = "space_amount";
    public static final String FIELD_SPACE_USED = "space_used";
    public static final String FIELD_STATUS = "status";
    public static final String FIELD_TIMEZONE = "timezone";
    public static final String FIELD_TRACKING_CODES = "tracking_codes";
    public static final String TYPE = "user";
    private static final long serialVersionUID = -9176113409457879123L;

    public enum Role {
        ADMIN("admin"),
        COADMIN("coadmin"),
        USER("user");
        
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
        ACTIVE(ConditionalUserProperty.ACTIVE),
        INACTIVE("inactive"),
        CANNOT_DELETE_EDIT("cannot_delete_edit"),
        CANNOT_DELETE_EDIT_UPLOAD("cannot_delete_edit_upload");
        
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

    public BoxUser() {
    }

    public BoxUser(Map<String, Object> map) {
        super(map);
    }

    public static BoxUser createFromId(String str) {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        linkedHashMap.put("id", str);
        linkedHashMap.put("type", "user");
        return new BoxUser(linkedHashMap);
    }

    public String getLogin() {
        return (String) this.mProperties.get("login");
    }

    public Role getRole() {
        return (Role) this.mProperties.get("role");
    }

    public String getLanguage() {
        return (String) this.mProperties.get(FIELD_LANGUAGE);
    }

    public String getTimezone() {
        return (String) this.mProperties.get("timezone");
    }

    public Long getSpaceAmount() {
        return (Long) this.mProperties.get(FIELD_SPACE_AMOUNT);
    }

    public Long getSpaceUsed() {
        return (Long) this.mProperties.get(FIELD_SPACE_USED);
    }

    public Long getMaxUploadSize() {
        return (Long) this.mProperties.get(FIELD_MAX_UPLOAD_SIZE);
    }

    public Status getStatus() {
        return (Status) this.mProperties.get("status");
    }

    public String getJobTitle() {
        return (String) this.mProperties.get(FIELD_JOB_TITLE);
    }

    public String getPhone() {
        return (String) this.mProperties.get(FIELD_PHONE);
    }

    public String getAddress() {
        return (String) this.mProperties.get(FIELD_ADDRESS);
    }

    public String getAvatarURL() {
        return (String) this.mProperties.get(FIELD_AVATAR_URL);
    }

    public List<String> getTrackingCodes() {
        return (List) this.mProperties.get(FIELD_TRACKING_CODES);
    }

    public Boolean getCanSeeManagedUsers() {
        return (Boolean) this.mProperties.get(FIELD_CAN_SEE_MANAGED_USERS);
    }

    public Boolean getIsSyncEnabled() {
        return (Boolean) this.mProperties.get(FIELD_IS_SYNC_ENABLED);
    }

    public Boolean getIsExternalCollabRestricted() {
        return (Boolean) this.mProperties.get(FIELD_IS_EXTERNAL_COLLAB_RESTRICTED);
    }

    public Boolean getIsExemptFromDeviceLimits() {
        return (Boolean) this.mProperties.get(FIELD_IS_EXEMPT_FROM_DEVICE_LIMITS);
    }

    public Boolean getIsExemptFromLoginVerification() {
        return (Boolean) this.mProperties.get(FIELD_IS_EXEMPT_FROM_LOGIN_VERIFICATION);
    }

    public BoxEnterprise getEnterprise() {
        return (BoxEnterprise) this.mProperties.get("enterprise");
    }

    public String getHostname() {
        return (String) this.mProperties.get(FIELD_HOSTNAME);
    }

    public List<String> getMyTags() {
        return (List) this.mProperties.get(FIELD_MY_TAGS);
    }

    /* access modifiers changed from: protected */
    public void parseJSONMember(Member member) {
        String name = member.getName();
        JsonValue value = member.getValue();
        if (name.equals("login")) {
            this.mProperties.put("login", value.asString());
        } else if (name.equals("role")) {
            this.mProperties.put("role", parseRole(value));
        } else if (name.equals(FIELD_LANGUAGE)) {
            this.mProperties.put(FIELD_LANGUAGE, value.asString());
        } else if (name.equals("timezone")) {
            this.mProperties.put("timezone", value.asString());
        } else if (name.equals(FIELD_SPACE_AMOUNT)) {
            this.mProperties.put(FIELD_SPACE_AMOUNT, Long.valueOf(Double.valueOf(value.toString()).longValue()));
        } else if (name.equals(FIELD_SPACE_USED)) {
            this.mProperties.put(FIELD_SPACE_USED, Long.valueOf(Double.valueOf(value.toString()).longValue()));
        } else if (name.equals(FIELD_MAX_UPLOAD_SIZE)) {
            this.mProperties.put(FIELD_MAX_UPLOAD_SIZE, Long.valueOf(Double.valueOf(value.toString()).longValue()));
        } else if (name.equals("status")) {
            this.mProperties.put("status", parseStatus(value));
        } else if (name.equals(FIELD_JOB_TITLE)) {
            this.mProperties.put(FIELD_JOB_TITLE, value.asString());
        } else if (name.equals(FIELD_PHONE)) {
            this.mProperties.put(FIELD_PHONE, value.asString());
        } else if (name.equals(FIELD_ADDRESS)) {
            this.mProperties.put(FIELD_ADDRESS, value.asString());
        } else if (name.equals(FIELD_AVATAR_URL)) {
            this.mProperties.put(FIELD_AVATAR_URL, value.asString());
        } else if (name.equals(FIELD_TRACKING_CODES)) {
            this.mProperties.put(FIELD_TRACKING_CODES, parseJsonArray(value.asArray()));
        } else if (name.equals(FIELD_CAN_SEE_MANAGED_USERS)) {
            this.mProperties.put(FIELD_CAN_SEE_MANAGED_USERS, Boolean.valueOf(value.asBoolean()));
        } else if (name.equals(FIELD_IS_SYNC_ENABLED)) {
            this.mProperties.put(FIELD_IS_SYNC_ENABLED, Boolean.valueOf(value.asBoolean()));
        } else if (name.equals(FIELD_IS_EXTERNAL_COLLAB_RESTRICTED)) {
            this.mProperties.put(FIELD_IS_EXTERNAL_COLLAB_RESTRICTED, Boolean.valueOf(value.asBoolean()));
        } else if (name.equals(FIELD_IS_EXEMPT_FROM_DEVICE_LIMITS)) {
            this.mProperties.put(FIELD_IS_EXEMPT_FROM_DEVICE_LIMITS, Boolean.valueOf(value.asBoolean()));
        } else if (name.equals(FIELD_IS_EXEMPT_FROM_LOGIN_VERIFICATION)) {
            this.mProperties.put(FIELD_IS_EXEMPT_FROM_LOGIN_VERIFICATION, Boolean.valueOf(value.asBoolean()));
        } else if (name.equals("enterprise")) {
            BoxEnterprise boxEnterprise = new BoxEnterprise();
            boxEnterprise.createFromJson(value.asObject());
            this.mProperties.put("enterprise", boxEnterprise);
        } else if (name.equals(FIELD_HOSTNAME)) {
            this.mProperties.put(FIELD_HOSTNAME, value.asString());
        } else if (name.equals(FIELD_MY_TAGS)) {
            this.mProperties.put(FIELD_MY_TAGS, parseJsonArray(value.asArray()));
        } else {
            super.parseJSONMember(member);
        }
    }

    private Role parseRole(JsonValue jsonValue) {
        return Role.valueOf(jsonValue.asString().toUpperCase());
    }

    private Status parseStatus(JsonValue jsonValue) {
        return Status.valueOf(jsonValue.asString().toUpperCase());
    }

    private List<String> parseJsonArray(JsonArray jsonArray) {
        ArrayList arrayList = new ArrayList(jsonArray.size());
        Iterator it = jsonArray.iterator();
        while (it.hasNext()) {
            arrayList.add(((JsonValue) it.next()).asString());
        }
        return arrayList;
    }
}
