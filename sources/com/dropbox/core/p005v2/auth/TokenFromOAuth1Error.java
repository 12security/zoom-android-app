package com.dropbox.core.p005v2.auth;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.auth.TokenFromOAuth1Error */
public enum TokenFromOAuth1Error {
    INVALID_OAUTH1_TOKEN_INFO,
    APP_ID_MISMATCH,
    OTHER;

    /* renamed from: com.dropbox.core.v2.auth.TokenFromOAuth1Error$Serializer */
    static class Serializer extends UnionSerializer<TokenFromOAuth1Error> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(TokenFromOAuth1Error tokenFromOAuth1Error, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (tokenFromOAuth1Error) {
                case INVALID_OAUTH1_TOKEN_INFO:
                    jsonGenerator.writeString("invalid_oauth1_token_info");
                    return;
                case APP_ID_MISMATCH:
                    jsonGenerator.writeString("app_id_mismatch");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public TokenFromOAuth1Error deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            TokenFromOAuth1Error tokenFromOAuth1Error;
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
                if ("invalid_oauth1_token_info".equals(str)) {
                    tokenFromOAuth1Error = TokenFromOAuth1Error.INVALID_OAUTH1_TOKEN_INFO;
                } else if ("app_id_mismatch".equals(str)) {
                    tokenFromOAuth1Error = TokenFromOAuth1Error.APP_ID_MISMATCH;
                } else {
                    tokenFromOAuth1Error = TokenFromOAuth1Error.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return tokenFromOAuth1Error;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
