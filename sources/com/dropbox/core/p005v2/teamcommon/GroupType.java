package com.dropbox.core.p005v2.teamcommon;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.teamcommon.GroupType */
public enum GroupType {
    TEAM,
    USER_MANAGED,
    OTHER;

    /* renamed from: com.dropbox.core.v2.teamcommon.GroupType$Serializer */
    public static class Serializer extends UnionSerializer<GroupType> {
        public static final Serializer INSTANCE = null;

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(GroupType groupType, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (groupType) {
                case TEAM:
                    jsonGenerator.writeString("team");
                    return;
                case USER_MANAGED:
                    jsonGenerator.writeString("user_managed");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public GroupType deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            GroupType groupType;
            if (jsonParser.getCurrentToken() == JsonToken.VALUE_STRING) {
                z = true;
                str = getStringValue(jsonParser);
                jsonParser.nextToken();
            } else {
                z = false;
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            }
            if (str != null) {
                if ("team".equals(str)) {
                    groupType = GroupType.TEAM;
                } else if ("user_managed".equals(str)) {
                    groupType = GroupType.USER_MANAGED;
                } else {
                    groupType = GroupType.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return groupType;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
