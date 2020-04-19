package com.dropbox.core.p005v2.team;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.team.RevokeLinkedAppError */
public enum RevokeLinkedAppError {
    APP_NOT_FOUND,
    MEMBER_NOT_FOUND,
    OTHER;

    /* renamed from: com.dropbox.core.v2.team.RevokeLinkedAppError$Serializer */
    static class Serializer extends UnionSerializer<RevokeLinkedAppError> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(RevokeLinkedAppError revokeLinkedAppError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (revokeLinkedAppError) {
                case APP_NOT_FOUND:
                    jsonGenerator.writeString("app_not_found");
                    return;
                case MEMBER_NOT_FOUND:
                    jsonGenerator.writeString("member_not_found");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public RevokeLinkedAppError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            RevokeLinkedAppError revokeLinkedAppError;
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
                if ("app_not_found".equals(str)) {
                    revokeLinkedAppError = RevokeLinkedAppError.APP_NOT_FOUND;
                } else if ("member_not_found".equals(str)) {
                    revokeLinkedAppError = RevokeLinkedAppError.MEMBER_NOT_FOUND;
                } else {
                    revokeLinkedAppError = RevokeLinkedAppError.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return revokeLinkedAppError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
