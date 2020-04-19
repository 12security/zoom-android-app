package com.dropbox.core.p005v2.team;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.team.GroupAccessType */
public enum GroupAccessType {
    MEMBER,
    OWNER;

    /* renamed from: com.dropbox.core.v2.team.GroupAccessType$Serializer */
    static class Serializer extends UnionSerializer<GroupAccessType> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(GroupAccessType groupAccessType, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (groupAccessType) {
                case MEMBER:
                    jsonGenerator.writeString("member");
                    return;
                case OWNER:
                    jsonGenerator.writeString("owner");
                    return;
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unrecognized tag: ");
                    sb.append(groupAccessType);
                    throw new IllegalArgumentException(sb.toString());
            }
        }

        public GroupAccessType deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            GroupAccessType groupAccessType;
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
                if ("member".equals(str)) {
                    groupAccessType = GroupAccessType.MEMBER;
                } else if ("owner".equals(str)) {
                    groupAccessType = GroupAccessType.OWNER;
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unknown tag: ");
                    sb.append(str);
                    throw new JsonParseException(jsonParser, sb.toString());
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return groupAccessType;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
