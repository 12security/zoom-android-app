package com.dropbox.core.p005v2.team;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.team.GroupMemberSetAccessTypeError */
public enum GroupMemberSetAccessTypeError {
    GROUP_NOT_FOUND,
    OTHER,
    SYSTEM_MANAGED_GROUP_DISALLOWED,
    MEMBER_NOT_IN_GROUP,
    USER_CANNOT_BE_MANAGER_OF_COMPANY_MANAGED_GROUP;

    /* renamed from: com.dropbox.core.v2.team.GroupMemberSetAccessTypeError$Serializer */
    static class Serializer extends UnionSerializer<GroupMemberSetAccessTypeError> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(GroupMemberSetAccessTypeError groupMemberSetAccessTypeError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (groupMemberSetAccessTypeError) {
                case GROUP_NOT_FOUND:
                    jsonGenerator.writeString("group_not_found");
                    return;
                case OTHER:
                    jsonGenerator.writeString("other");
                    return;
                case SYSTEM_MANAGED_GROUP_DISALLOWED:
                    jsonGenerator.writeString("system_managed_group_disallowed");
                    return;
                case MEMBER_NOT_IN_GROUP:
                    jsonGenerator.writeString("member_not_in_group");
                    return;
                case USER_CANNOT_BE_MANAGER_OF_COMPANY_MANAGED_GROUP:
                    jsonGenerator.writeString("user_cannot_be_manager_of_company_managed_group");
                    return;
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unrecognized tag: ");
                    sb.append(groupMemberSetAccessTypeError);
                    throw new IllegalArgumentException(sb.toString());
            }
        }

        public GroupMemberSetAccessTypeError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            GroupMemberSetAccessTypeError groupMemberSetAccessTypeError;
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
                    groupMemberSetAccessTypeError = GroupMemberSetAccessTypeError.GROUP_NOT_FOUND;
                } else if ("other".equals(str)) {
                    groupMemberSetAccessTypeError = GroupMemberSetAccessTypeError.OTHER;
                } else if ("system_managed_group_disallowed".equals(str)) {
                    groupMemberSetAccessTypeError = GroupMemberSetAccessTypeError.SYSTEM_MANAGED_GROUP_DISALLOWED;
                } else if ("member_not_in_group".equals(str)) {
                    groupMemberSetAccessTypeError = GroupMemberSetAccessTypeError.MEMBER_NOT_IN_GROUP;
                } else if ("user_cannot_be_manager_of_company_managed_group".equals(str)) {
                    groupMemberSetAccessTypeError = GroupMemberSetAccessTypeError.USER_CANNOT_BE_MANAGER_OF_COMPANY_MANAGED_GROUP;
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
                return groupMemberSetAccessTypeError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
