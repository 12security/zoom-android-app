package com.dropbox.core.p005v2.teampolicies;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.teampolicies.SharedFolderMemberPolicy */
public enum SharedFolderMemberPolicy {
    TEAM,
    ANYONE,
    OTHER;

    /* renamed from: com.dropbox.core.v2.teampolicies.SharedFolderMemberPolicy$Serializer */
    static class Serializer extends UnionSerializer<SharedFolderMemberPolicy> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(SharedFolderMemberPolicy sharedFolderMemberPolicy, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (sharedFolderMemberPolicy) {
                case TEAM:
                    jsonGenerator.writeString("team");
                    return;
                case ANYONE:
                    jsonGenerator.writeString("anyone");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public SharedFolderMemberPolicy deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            SharedFolderMemberPolicy sharedFolderMemberPolicy;
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
                    sharedFolderMemberPolicy = SharedFolderMemberPolicy.TEAM;
                } else if ("anyone".equals(str)) {
                    sharedFolderMemberPolicy = SharedFolderMemberPolicy.ANYONE;
                } else {
                    sharedFolderMemberPolicy = SharedFolderMemberPolicy.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return sharedFolderMemberPolicy;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
