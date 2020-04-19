package com.dropbox.core.p005v2.team;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.team.GroupUpdateError */
public enum GroupUpdateError {
    GROUP_NOT_FOUND,
    OTHER,
    SYSTEM_MANAGED_GROUP_DISALLOWED,
    GROUP_NAME_ALREADY_USED,
    GROUP_NAME_INVALID,
    EXTERNAL_ID_ALREADY_IN_USE;

    /* renamed from: com.dropbox.core.v2.team.GroupUpdateError$Serializer */
    static class Serializer extends UnionSerializer<GroupUpdateError> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(GroupUpdateError groupUpdateError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (groupUpdateError) {
                case GROUP_NOT_FOUND:
                    jsonGenerator.writeString("group_not_found");
                    return;
                case OTHER:
                    jsonGenerator.writeString("other");
                    return;
                case SYSTEM_MANAGED_GROUP_DISALLOWED:
                    jsonGenerator.writeString("system_managed_group_disallowed");
                    return;
                case GROUP_NAME_ALREADY_USED:
                    jsonGenerator.writeString("group_name_already_used");
                    return;
                case GROUP_NAME_INVALID:
                    jsonGenerator.writeString("group_name_invalid");
                    return;
                case EXTERNAL_ID_ALREADY_IN_USE:
                    jsonGenerator.writeString("external_id_already_in_use");
                    return;
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unrecognized tag: ");
                    sb.append(groupUpdateError);
                    throw new IllegalArgumentException(sb.toString());
            }
        }

        public GroupUpdateError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            GroupUpdateError groupUpdateError;
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
                    groupUpdateError = GroupUpdateError.GROUP_NOT_FOUND;
                } else if ("other".equals(str)) {
                    groupUpdateError = GroupUpdateError.OTHER;
                } else if ("system_managed_group_disallowed".equals(str)) {
                    groupUpdateError = GroupUpdateError.SYSTEM_MANAGED_GROUP_DISALLOWED;
                } else if ("group_name_already_used".equals(str)) {
                    groupUpdateError = GroupUpdateError.GROUP_NAME_ALREADY_USED;
                } else if ("group_name_invalid".equals(str)) {
                    groupUpdateError = GroupUpdateError.GROUP_NAME_INVALID;
                } else if ("external_id_already_in_use".equals(str)) {
                    groupUpdateError = GroupUpdateError.EXTERNAL_ID_ALREADY_IN_USE;
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
                return groupUpdateError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
