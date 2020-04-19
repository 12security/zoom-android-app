package com.dropbox.core.p005v2.teampolicies;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.teampolicies.SharedLinkCreatePolicy */
public enum SharedLinkCreatePolicy {
    DEFAULT_PUBLIC,
    DEFAULT_TEAM_ONLY,
    TEAM_ONLY,
    OTHER;

    /* renamed from: com.dropbox.core.v2.teampolicies.SharedLinkCreatePolicy$Serializer */
    static class Serializer extends UnionSerializer<SharedLinkCreatePolicy> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(SharedLinkCreatePolicy sharedLinkCreatePolicy, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (sharedLinkCreatePolicy) {
                case DEFAULT_PUBLIC:
                    jsonGenerator.writeString("default_public");
                    return;
                case DEFAULT_TEAM_ONLY:
                    jsonGenerator.writeString("default_team_only");
                    return;
                case TEAM_ONLY:
                    jsonGenerator.writeString("team_only");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public SharedLinkCreatePolicy deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            SharedLinkCreatePolicy sharedLinkCreatePolicy;
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
                if ("default_public".equals(str)) {
                    sharedLinkCreatePolicy = SharedLinkCreatePolicy.DEFAULT_PUBLIC;
                } else if ("default_team_only".equals(str)) {
                    sharedLinkCreatePolicy = SharedLinkCreatePolicy.DEFAULT_TEAM_ONLY;
                } else if ("team_only".equals(str)) {
                    sharedLinkCreatePolicy = SharedLinkCreatePolicy.TEAM_ONLY;
                } else {
                    sharedLinkCreatePolicy = SharedLinkCreatePolicy.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return sharedLinkCreatePolicy;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
