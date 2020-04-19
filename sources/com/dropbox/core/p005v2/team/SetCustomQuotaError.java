package com.dropbox.core.p005v2.team;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.team.SetCustomQuotaError */
public enum SetCustomQuotaError {
    TOO_MANY_USERS,
    OTHER,
    SOME_USERS_ARE_EXCLUDED;

    /* renamed from: com.dropbox.core.v2.team.SetCustomQuotaError$Serializer */
    static class Serializer extends UnionSerializer<SetCustomQuotaError> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(SetCustomQuotaError setCustomQuotaError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (setCustomQuotaError) {
                case TOO_MANY_USERS:
                    jsonGenerator.writeString("too_many_users");
                    return;
                case OTHER:
                    jsonGenerator.writeString("other");
                    return;
                case SOME_USERS_ARE_EXCLUDED:
                    jsonGenerator.writeString("some_users_are_excluded");
                    return;
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unrecognized tag: ");
                    sb.append(setCustomQuotaError);
                    throw new IllegalArgumentException(sb.toString());
            }
        }

        public SetCustomQuotaError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            SetCustomQuotaError setCustomQuotaError;
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
                if ("too_many_users".equals(str)) {
                    setCustomQuotaError = SetCustomQuotaError.TOO_MANY_USERS;
                } else if ("other".equals(str)) {
                    setCustomQuotaError = SetCustomQuotaError.OTHER;
                } else if ("some_users_are_excluded".equals(str)) {
                    setCustomQuotaError = SetCustomQuotaError.SOME_USERS_ARE_EXCLUDED;
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
                return setCustomQuotaError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
