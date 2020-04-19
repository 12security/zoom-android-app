package com.dropbox.core.p005v2.team;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.team.RevokeDeviceSessionError */
public enum RevokeDeviceSessionError {
    DEVICE_SESSION_NOT_FOUND,
    MEMBER_NOT_FOUND,
    OTHER;

    /* renamed from: com.dropbox.core.v2.team.RevokeDeviceSessionError$Serializer */
    static class Serializer extends UnionSerializer<RevokeDeviceSessionError> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(RevokeDeviceSessionError revokeDeviceSessionError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (revokeDeviceSessionError) {
                case DEVICE_SESSION_NOT_FOUND:
                    jsonGenerator.writeString("device_session_not_found");
                    return;
                case MEMBER_NOT_FOUND:
                    jsonGenerator.writeString("member_not_found");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public RevokeDeviceSessionError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            RevokeDeviceSessionError revokeDeviceSessionError;
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
                if ("device_session_not_found".equals(str)) {
                    revokeDeviceSessionError = RevokeDeviceSessionError.DEVICE_SESSION_NOT_FOUND;
                } else if ("member_not_found".equals(str)) {
                    revokeDeviceSessionError = RevokeDeviceSessionError.MEMBER_NOT_FOUND;
                } else {
                    revokeDeviceSessionError = RevokeDeviceSessionError.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return revokeDeviceSessionError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
