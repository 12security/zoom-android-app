package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.sharing.SharedLinkPolicy */
public enum SharedLinkPolicy {
    ANYONE,
    TEAM,
    MEMBERS,
    OTHER;

    /* renamed from: com.dropbox.core.v2.sharing.SharedLinkPolicy$Serializer */
    public static class Serializer extends UnionSerializer<SharedLinkPolicy> {
        public static final Serializer INSTANCE = null;

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(SharedLinkPolicy sharedLinkPolicy, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (sharedLinkPolicy) {
                case ANYONE:
                    jsonGenerator.writeString("anyone");
                    return;
                case TEAM:
                    jsonGenerator.writeString("team");
                    return;
                case MEMBERS:
                    jsonGenerator.writeString("members");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public SharedLinkPolicy deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            SharedLinkPolicy sharedLinkPolicy;
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
                if ("anyone".equals(str)) {
                    sharedLinkPolicy = SharedLinkPolicy.ANYONE;
                } else if ("team".equals(str)) {
                    sharedLinkPolicy = SharedLinkPolicy.TEAM;
                } else if ("members".equals(str)) {
                    sharedLinkPolicy = SharedLinkPolicy.MEMBERS;
                } else {
                    sharedLinkPolicy = SharedLinkPolicy.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return sharedLinkPolicy;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
