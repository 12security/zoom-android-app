package com.box.androidsdk.content.models;

import android.text.TextUtils;
import com.box.androidsdk.content.utils.BoxDateFormat;
import com.eclipsesource.json.JsonObject.Member;
import com.eclipsesource.json.JsonValue;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class BoxSharedLink extends BoxJsonObject {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    public static final String FIELD_ACCESS = "access";
    public static final String FIELD_DOWNLOAD_COUNT = "download_count";
    public static final String FIELD_DOWNLOAD_URL = "download_url";
    public static final String FIELD_EFFECTIVE_ACCESS = "effective_access";
    public static final String FIELD_IS_PASSWORD_ENABLED = "is_password_enabled";
    public static final String FIELD_PASSWORD = "password";
    public static final String FIELD_PERMISSIONS = "permissions";
    public static final String FIELD_PREVIEW_COUNT = "preview_count";
    public static final String FIELD_UNSHARED_AT = "unshared_at";
    public static final String FIELD_URL = "url";
    public static final String FIELD_VANITY_URL = "vanity_url";
    private static final long serialVersionUID = -4595593930118314932L;

    public enum Access {
        DEFAULT(null),
        OPEN("open"),
        COMPANY("company"),
        COLLABORATORS("collaborators");
        
        private final String mValue;

        public static Access fromString(String str) {
            Access[] values;
            if (!TextUtils.isEmpty(str)) {
                for (Access access : values()) {
                    if (str.equalsIgnoreCase(access.toString())) {
                        return access;
                    }
                }
            }
            throw new IllegalArgumentException(String.format(Locale.ENGLISH, "No enum with text %s found", new Object[]{str}));
        }

        private Access(String str) {
            this.mValue = str;
        }

        public String toString() {
            return this.mValue;
        }
    }

    public static class Permissions extends BoxJsonObject {
        public static final String FIELD_CAN_DOWNLOAD = "can_download";
        private static final String FIELD_CAN_PREVIEW = "can_preview";

        public Permissions() {
        }

        public Permissions(Map<String, Object> map) {
            super(map);
        }

        public Boolean getCanDownload() {
            return (Boolean) this.mProperties.get(FIELD_CAN_DOWNLOAD);
        }

        /* access modifiers changed from: protected */
        public void parseJSONMember(Member member) {
            JsonValue value = member.getValue();
            if (member.getName().equals(FIELD_CAN_DOWNLOAD)) {
                this.mProperties.put(FIELD_CAN_DOWNLOAD, Boolean.valueOf(value.asBoolean()));
            } else if (member.getName().equals(FIELD_CAN_PREVIEW)) {
                this.mProperties.put(FIELD_CAN_PREVIEW, Boolean.valueOf(value.asBoolean()));
            }
        }
    }

    public BoxSharedLink() {
    }

    public BoxSharedLink(Map<String, Object> map) {
        super(map);
    }

    public String getURL() {
        return (String) this.mProperties.get("url");
    }

    public String getDownloadURL() {
        return (String) this.mProperties.get(FIELD_DOWNLOAD_URL);
    }

    public String getVanityURL() {
        return (String) this.mProperties.get(FIELD_VANITY_URL);
    }

    public Boolean getIsPasswordEnabled() {
        return (Boolean) this.mProperties.get(FIELD_IS_PASSWORD_ENABLED);
    }

    public Date getUnsharedDate() {
        return (Date) this.mProperties.get(FIELD_UNSHARED_AT);
    }

    public Long getDownloadCount() {
        return (Long) this.mProperties.get(FIELD_DOWNLOAD_COUNT);
    }

    public Long getPreviewCount() {
        return (Long) this.mProperties.get(FIELD_PREVIEW_COUNT);
    }

    public Access getAccess() {
        return (Access) this.mProperties.get("access");
    }

    public String getPassword() {
        return (String) this.mProperties.get(FIELD_PASSWORD);
    }

    public Access getEffectiveAccess() {
        return (Access) this.mProperties.get(FIELD_EFFECTIVE_ACCESS);
    }

    public Permissions getPermissions() {
        return (Permissions) this.mProperties.get("permissions");
    }

    /* access modifiers changed from: protected */
    public void parseJSONMember(Member member) {
        JsonValue value = member.getValue();
        try {
            if (member.getName().equals("url")) {
                this.mProperties.put("url", value.asString());
            } else if (member.getName().equals(FIELD_DOWNLOAD_URL)) {
                this.mProperties.put(FIELD_DOWNLOAD_URL, value.asString());
            } else if (member.getName().equals(FIELD_VANITY_URL)) {
                this.mProperties.put(FIELD_VANITY_URL, value.asString());
            } else if (member.getName().equals(FIELD_IS_PASSWORD_ENABLED)) {
                this.mProperties.put(FIELD_IS_PASSWORD_ENABLED, Boolean.valueOf(value.asBoolean()));
            } else if (member.getName().equals(FIELD_UNSHARED_AT)) {
                this.mProperties.put(FIELD_UNSHARED_AT, BoxDateFormat.parse(value.asString()));
            } else if (member.getName().equals(FIELD_DOWNLOAD_COUNT)) {
                this.mProperties.put(FIELD_DOWNLOAD_COUNT, Long.valueOf(Double.valueOf(value.toString()).longValue()));
            } else if (member.getName().equals(FIELD_PREVIEW_COUNT)) {
                this.mProperties.put(FIELD_PREVIEW_COUNT, Long.valueOf(Double.valueOf(value.toString()).longValue()));
            } else if (member.getName().equals("access")) {
                this.mProperties.put("access", Access.fromString(value.asString()));
            } else if (member.getName().equals(FIELD_EFFECTIVE_ACCESS)) {
                this.mProperties.put(FIELD_EFFECTIVE_ACCESS, Access.fromString(value.asString()));
            } else {
                if (member.getName().equals("permissions")) {
                    Permissions permissions = new Permissions();
                    permissions.createFromJson(value.asObject());
                    this.mProperties.put("permissions", permissions);
                    return;
                }
                super.parseJSONMember(member);
            }
        } catch (ParseException unused) {
        }
    }
}
