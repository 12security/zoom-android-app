package com.box.androidsdk.content.models;

import com.eclipsesource.json.JsonObject.Member;
import com.eclipsesource.json.JsonValue;
import java.util.Map;

public class BoxCollection extends BoxEntity {
    public static final String FIELD_COLLECTION_TYPE = "collection_type";
    public static final String FIELD_NAME = "name";
    public static final String TYPE = "collection";

    public BoxCollection() {
    }

    public BoxCollection(Map<String, Object> map) {
        super(map);
    }

    public String getName() {
        return (String) this.mProperties.get("name");
    }

    public String getCollectionType() {
        return (String) this.mProperties.get(FIELD_COLLECTION_TYPE);
    }

    /* access modifiers changed from: protected */
    public void parseJSONMember(Member member) {
        String name = member.getName();
        JsonValue value = member.getValue();
        if (name.equals("name")) {
            this.mProperties.put("name", value.asString());
        } else if (name.equals(FIELD_COLLECTION_TYPE)) {
            this.mProperties.put(FIELD_COLLECTION_TYPE, value.asString());
        } else {
            super.parseJSONMember(member);
        }
    }
}
