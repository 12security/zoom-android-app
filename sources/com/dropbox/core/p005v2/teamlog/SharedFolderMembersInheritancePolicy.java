package com.dropbox.core.p005v2.teamlog;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.teamlog.SharedFolderMembersInheritancePolicy */
public enum SharedFolderMembersInheritancePolicy {
    INHERIT_MEMBERS,
    DONT_INHERIT_MEMBERS,
    OTHER;

    /* renamed from: com.dropbox.core.v2.teamlog.SharedFolderMembersInheritancePolicy$Serializer */
    static class Serializer extends UnionSerializer<SharedFolderMembersInheritancePolicy> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(SharedFolderMembersInheritancePolicy sharedFolderMembersInheritancePolicy, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (sharedFolderMembersInheritancePolicy) {
                case INHERIT_MEMBERS:
                    jsonGenerator.writeString("inherit_members");
                    return;
                case DONT_INHERIT_MEMBERS:
                    jsonGenerator.writeString("dont_inherit_members");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public SharedFolderMembersInheritancePolicy deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            SharedFolderMembersInheritancePolicy sharedFolderMembersInheritancePolicy;
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
                if ("inherit_members".equals(str)) {
                    sharedFolderMembersInheritancePolicy = SharedFolderMembersInheritancePolicy.INHERIT_MEMBERS;
                } else if ("dont_inherit_members".equals(str)) {
                    sharedFolderMembersInheritancePolicy = SharedFolderMembersInheritancePolicy.DONT_INHERIT_MEMBERS;
                } else {
                    sharedFolderMembersInheritancePolicy = SharedFolderMembersInheritancePolicy.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return sharedFolderMembersInheritancePolicy;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
