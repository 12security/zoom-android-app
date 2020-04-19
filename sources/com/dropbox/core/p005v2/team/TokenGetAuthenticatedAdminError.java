package com.dropbox.core.p005v2.team;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.team.TokenGetAuthenticatedAdminError */
public enum TokenGetAuthenticatedAdminError {
    MAPPING_NOT_FOUND,
    ADMIN_NOT_ACTIVE,
    OTHER;

    /* renamed from: com.dropbox.core.v2.team.TokenGetAuthenticatedAdminError$Serializer */
    static class Serializer extends UnionSerializer<TokenGetAuthenticatedAdminError> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(TokenGetAuthenticatedAdminError tokenGetAuthenticatedAdminError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (tokenGetAuthenticatedAdminError) {
                case MAPPING_NOT_FOUND:
                    jsonGenerator.writeString("mapping_not_found");
                    return;
                case ADMIN_NOT_ACTIVE:
                    jsonGenerator.writeString("admin_not_active");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public TokenGetAuthenticatedAdminError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            TokenGetAuthenticatedAdminError tokenGetAuthenticatedAdminError;
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
                if ("mapping_not_found".equals(str)) {
                    tokenGetAuthenticatedAdminError = TokenGetAuthenticatedAdminError.MAPPING_NOT_FOUND;
                } else if ("admin_not_active".equals(str)) {
                    tokenGetAuthenticatedAdminError = TokenGetAuthenticatedAdminError.ADMIN_NOT_ACTIVE;
                } else {
                    tokenGetAuthenticatedAdminError = TokenGetAuthenticatedAdminError.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return tokenGetAuthenticatedAdminError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
