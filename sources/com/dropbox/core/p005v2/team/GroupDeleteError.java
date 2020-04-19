package com.dropbox.core.p005v2.team;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.team.GroupDeleteError */
public enum GroupDeleteError {
    GROUP_NOT_FOUND,
    OTHER,
    SYSTEM_MANAGED_GROUP_DISALLOWED,
    GROUP_ALREADY_DELETED;

    /* renamed from: com.dropbox.core.v2.team.GroupDeleteError$Serializer */
    static class Serializer extends UnionSerializer<GroupDeleteError> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(GroupDeleteError groupDeleteError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (groupDeleteError) {
                case GROUP_NOT_FOUND:
                    jsonGenerator.writeString("group_not_found");
                    return;
                case OTHER:
                    jsonGenerator.writeString("other");
                    return;
                case SYSTEM_MANAGED_GROUP_DISALLOWED:
                    jsonGenerator.writeString("system_managed_group_disallowed");
                    return;
                case GROUP_ALREADY_DELETED:
                    jsonGenerator.writeString("group_already_deleted");
                    return;
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unrecognized tag: ");
                    sb.append(groupDeleteError);
                    throw new IllegalArgumentException(sb.toString());
            }
        }

        public GroupDeleteError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            GroupDeleteError groupDeleteError;
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
                if ("group_not_found".equals(str)) {
                    groupDeleteError = GroupDeleteError.GROUP_NOT_FOUND;
                } else if ("other".equals(str)) {
                    groupDeleteError = GroupDeleteError.OTHER;
                } else if ("system_managed_group_disallowed".equals(str)) {
                    groupDeleteError = GroupDeleteError.SYSTEM_MANAGED_GROUP_DISALLOWED;
                } else if ("group_already_deleted".equals(str)) {
                    groupDeleteError = GroupDeleteError.GROUP_ALREADY_DELETED;
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
                return groupDeleteError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
