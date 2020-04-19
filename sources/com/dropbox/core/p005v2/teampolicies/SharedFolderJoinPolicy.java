package com.dropbox.core.p005v2.teampolicies;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.teampolicies.SharedFolderJoinPolicy */
public enum SharedFolderJoinPolicy {
    FROM_TEAM_ONLY,
    FROM_ANYONE,
    OTHER;

    /* renamed from: com.dropbox.core.v2.teampolicies.SharedFolderJoinPolicy$Serializer */
    static class Serializer extends UnionSerializer<SharedFolderJoinPolicy> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(SharedFolderJoinPolicy sharedFolderJoinPolicy, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (sharedFolderJoinPolicy) {
                case FROM_TEAM_ONLY:
                    jsonGenerator.writeString("from_team_only");
                    return;
                case FROM_ANYONE:
                    jsonGenerator.writeString("from_anyone");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public SharedFolderJoinPolicy deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            SharedFolderJoinPolicy sharedFolderJoinPolicy;
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
                if ("from_team_only".equals(str)) {
                    sharedFolderJoinPolicy = SharedFolderJoinPolicy.FROM_TEAM_ONLY;
                } else if ("from_anyone".equals(str)) {
                    sharedFolderJoinPolicy = SharedFolderJoinPolicy.FROM_ANYONE;
                } else {
                    sharedFolderJoinPolicy = SharedFolderJoinPolicy.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return sharedFolderJoinPolicy;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
