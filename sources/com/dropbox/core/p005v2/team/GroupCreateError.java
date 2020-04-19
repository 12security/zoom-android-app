package com.dropbox.core.p005v2.team;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.team.GroupCreateError */
public enum GroupCreateError {
    GROUP_NAME_ALREADY_USED,
    GROUP_NAME_INVALID,
    EXTERNAL_ID_ALREADY_IN_USE,
    SYSTEM_MANAGED_GROUP_DISALLOWED,
    OTHER;

    /* renamed from: com.dropbox.core.v2.team.GroupCreateError$Serializer */
    static class Serializer extends UnionSerializer<GroupCreateError> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(GroupCreateError groupCreateError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (groupCreateError) {
                case GROUP_NAME_ALREADY_USED:
                    jsonGenerator.writeString("group_name_already_used");
                    return;
                case GROUP_NAME_INVALID:
                    jsonGenerator.writeString("group_name_invalid");
                    return;
                case EXTERNAL_ID_ALREADY_IN_USE:
                    jsonGenerator.writeString("external_id_already_in_use");
                    return;
                case SYSTEM_MANAGED_GROUP_DISALLOWED:
                    jsonGenerator.writeString("system_managed_group_disallowed");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public GroupCreateError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            GroupCreateError groupCreateError;
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
                if ("group_name_already_used".equals(str)) {
                    groupCreateError = GroupCreateError.GROUP_NAME_ALREADY_USED;
                } else if ("group_name_invalid".equals(str)) {
                    groupCreateError = GroupCreateError.GROUP_NAME_INVALID;
                } else if ("external_id_already_in_use".equals(str)) {
                    groupCreateError = GroupCreateError.EXTERNAL_ID_ALREADY_IN_USE;
                } else if ("system_managed_group_disallowed".equals(str)) {
                    groupCreateError = GroupCreateError.SYSTEM_MANAGED_GROUP_DISALLOWED;
                } else {
                    groupCreateError = GroupCreateError.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return groupCreateError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
