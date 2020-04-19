package com.box.androidsdk.content.models;

import com.eclipsesource.json.JsonObject.Member;
import com.eclipsesource.json.JsonValue;
import java.util.Iterator;

public class BoxListRealTimeServers extends BoxList<BoxRealTimeServer> {
    public static final String FIELD_CHUNK_SIZE = "chunk_size";
    private static final long serialVersionUID = -4986489348666966126L;

    /* access modifiers changed from: protected */
    public void parseJSONMember(Member member) {
        String name = member.getName();
        JsonValue value = member.getValue();
        if (name.equals("chunk_size")) {
            this.mProperties.put("chunk_size", Long.valueOf(value.asLong()));
        } else if (name.equals(BoxList.FIELD_ENTRIES)) {
            Iterator it = value.asArray().iterator();
            while (it.hasNext()) {
                JsonValue jsonValue = (JsonValue) it.next();
                BoxRealTimeServer boxRealTimeServer = new BoxRealTimeServer();
                boxRealTimeServer.createFromJson(jsonValue.asObject());
                add(boxRealTimeServer);
            }
            this.mProperties.put(BoxList.FIELD_ENTRIES, this.collection);
        } else {
            super.parseJSONMember(member);
        }
    }
}
