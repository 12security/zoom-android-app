package com.box.androidsdk.content.models;

import com.eclipsesource.json.JsonObject.Member;
import com.eclipsesource.json.JsonValue;
import java.util.List;
import java.util.Map;

public class BoxMetadata extends BoxJsonObject {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    public static final String FIELD_PARENT = "parent";
    public static final String FIELD_SCOPE = "scope";
    public static final String FIELD_TEMPLATE = "template";
    private List<String> mMetadataKeys;

    public BoxMetadata() {
    }

    public BoxMetadata(Map<String, Object> map) {
        super(map);
    }

    public String getParent() {
        return (String) this.mProperties.get("parent");
    }

    public String getTemplate() {
        return (String) this.mProperties.get(FIELD_TEMPLATE);
    }

    public String getScope() {
        return (String) this.mProperties.get("scope");
    }

    /* access modifiers changed from: protected */
    public void parseJSONMember(Member member) {
        try {
            String name = member.getName();
            JsonValue value = member.getValue();
            if (name.equals("parent")) {
                this.mProperties.put("parent", value.asString());
            } else if (name.equals(FIELD_TEMPLATE)) {
                this.mProperties.put(FIELD_TEMPLATE, value.asString());
            } else if (name.equals("scope")) {
                this.mProperties.put("scope", value.asString());
            } else {
                if (!this.mMetadataKeys.contains(name)) {
                    this.mProperties.put(name, value.asString());
                    this.mMetadataKeys.add(name);
                    return;
                }
                super.parseJSONMember(member);
            }
        } catch (Exception unused) {
        }
    }
}
