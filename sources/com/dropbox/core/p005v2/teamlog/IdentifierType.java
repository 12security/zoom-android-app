package com.dropbox.core.p005v2.teamlog;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.teamlog.IdentifierType */
public enum IdentifierType {
    EMAIL,
    FACEBOOK_PROFILE_NAME,
    OTHER;

    /* renamed from: com.dropbox.core.v2.teamlog.IdentifierType$Serializer */
    static class Serializer extends UnionSerializer<IdentifierType> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(IdentifierType identifierType, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (identifierType) {
                case EMAIL:
                    jsonGenerator.writeString("email");
                    return;
                case FACEBOOK_PROFILE_NAME:
                    jsonGenerator.writeString("facebook_profile_name");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public IdentifierType deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            IdentifierType identifierType;
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
                if ("email".equals(str)) {
                    identifierType = IdentifierType.EMAIL;
                } else if ("facebook_profile_name".equals(str)) {
                    identifierType = IdentifierType.FACEBOOK_PROFILE_NAME;
                } else {
                    identifierType = IdentifierType.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return identifierType;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
