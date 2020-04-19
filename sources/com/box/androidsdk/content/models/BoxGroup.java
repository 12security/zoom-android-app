package com.box.androidsdk.content.models;

import java.util.LinkedHashMap;
import java.util.Map;

public class BoxGroup extends BoxCollaborator {
    public static final String TYPE = "group";
    private static final long serialVersionUID = 5872741782856508553L;

    public BoxGroup() {
    }

    public BoxGroup(Map<String, Object> map) {
        super(map);
    }

    public static BoxGroup createFromId(String str) {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        linkedHashMap.put("id", str);
        linkedHashMap.put("type", "user");
        return new BoxGroup(linkedHashMap);
    }
}
