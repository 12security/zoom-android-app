package com.box.androidsdk.content.models;

import com.eclipsesource.json.JsonObject.Member;
import com.eclipsesource.json.JsonValue;

public class BoxOrder extends BoxJsonObject {
    public static final String FIELD_BY = "by";
    public static final String FIELD_DIRECTION = "direction";

    /* access modifiers changed from: protected */
    public void parseJSONMember(Member member) {
        String name = member.getName();
        JsonValue value = member.getValue();
        if (name.equals(FIELD_BY)) {
            this.mProperties.put(FIELD_BY, value.asString());
        } else if (name.equals(FIELD_DIRECTION)) {
            this.mProperties.put(FIELD_DIRECTION, value.asString());
        } else {
            super.parseJSONMember(member);
        }
    }
}
