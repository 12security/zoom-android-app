package com.box.androidsdk.content.models;

import android.text.TextUtils;
import com.eclipsesource.json.JsonObject.Member;
import com.eclipsesource.json.JsonValue;
import java.util.Locale;
import java.util.Map;

public class BoxUploadEmail extends BoxJsonObject {
    public static final String FIELD_ACCESS = "access";
    public static final String FIELD_EMAIL = "email";
    private static final long serialVersionUID = -1707312180661448119L;

    public enum Access {
        OPEN("open"),
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

    public BoxUploadEmail() {
    }

    public BoxUploadEmail(Map<String, Object> map) {
        super(map);
    }

    public Access getAccess() {
        return (Access) this.mProperties.get("access");
    }

    public String getEmail() {
        return (String) this.mProperties.get("email");
    }

    /* access modifiers changed from: protected */
    public void parseJSONMember(Member member) {
        JsonValue value = member.getValue();
        if (member.getName().equals("access")) {
            this.mProperties.put("access", Access.fromString(value.asString()));
        } else if (member.getName().equals("email")) {
            this.mProperties.put("email", value.asString());
        } else {
            super.parseJSONMember(member);
        }
    }
}
