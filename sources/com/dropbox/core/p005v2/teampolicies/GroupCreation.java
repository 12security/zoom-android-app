package com.dropbox.core.p005v2.teampolicies;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.teampolicies.GroupCreation */
public enum GroupCreation {
    ADMINS_AND_MEMBERS,
    ADMINS_ONLY;

    /* renamed from: com.dropbox.core.v2.teampolicies.GroupCreation$Serializer */
    public static class Serializer extends UnionSerializer<GroupCreation> {
        public static final Serializer INSTANCE = null;

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(GroupCreation groupCreation, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (groupCreation) {
                case ADMINS_AND_MEMBERS:
                    jsonGenerator.writeString("admins_and_members");
                    return;
                case ADMINS_ONLY:
                    jsonGenerator.writeString("admins_only");
                    return;
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unrecognized tag: ");
                    sb.append(groupCreation);
                    throw new IllegalArgumentException(sb.toString());
            }
        }

        public GroupCreation deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            GroupCreation groupCreation;
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
                if ("admins_and_members".equals(str)) {
                    groupCreation = GroupCreation.ADMINS_AND_MEMBERS;
                } else if ("admins_only".equals(str)) {
                    groupCreation = GroupCreation.ADMINS_ONLY;
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
                return groupCreation;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
