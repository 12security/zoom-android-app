package com.box.androidsdk.content.models;

import com.eclipsesource.json.JsonObject.Member;
import com.eclipsesource.json.JsonValue;
import java.util.Map;

public class BoxEnterprise extends BoxEntity {
    public static final String FIELD_NAME = "name";
    public static final String TYPE = "enterprise";
    private static final long serialVersionUID = -3453999549970888942L;

    public BoxEnterprise() {
    }

    public BoxEnterprise(Map<String, Object> map) {
        super(map);
    }

    public String getName() {
        return (String) this.mProperties.get("name");
    }

    /* access modifiers changed from: protected */
    public void parseJSONMember(Member member) {
        JsonValue value = member.getValue();
        if (member.getName().equals("name")) {
            this.mProperties.put("name", value.asString());
        } else {
            super.parseJSONMember(member);
        }
    }
}
