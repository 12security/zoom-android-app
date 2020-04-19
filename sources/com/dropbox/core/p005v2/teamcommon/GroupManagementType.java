package com.dropbox.core.p005v2.teamcommon;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.teamcommon.GroupManagementType */
public enum GroupManagementType {
    USER_MANAGED,
    COMPANY_MANAGED,
    SYSTEM_MANAGED,
    OTHER;

    /* renamed from: com.dropbox.core.v2.teamcommon.GroupManagementType$Serializer */
    public static class Serializer extends UnionSerializer<GroupManagementType> {
        public static final Serializer INSTANCE = null;

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(GroupManagementType groupManagementType, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (groupManagementType) {
                case USER_MANAGED:
                    jsonGenerator.writeString("user_managed");
                    return;
                case COMPANY_MANAGED:
                    jsonGenerator.writeString("company_managed");
                    return;
                case SYSTEM_MANAGED:
                    jsonGenerator.writeString("system_managed");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public GroupManagementType deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            GroupManagementType groupManagementType;
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
                if ("user_managed".equals(str)) {
                    groupManagementType = GroupManagementType.USER_MANAGED;
                } else if ("company_managed".equals(str)) {
                    groupManagementType = GroupManagementType.COMPANY_MANAGED;
                } else if ("system_managed".equals(str)) {
                    groupManagementType = GroupManagementType.SYSTEM_MANAGED;
                } else {
                    groupManagementType = GroupManagementType.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return groupManagementType;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
