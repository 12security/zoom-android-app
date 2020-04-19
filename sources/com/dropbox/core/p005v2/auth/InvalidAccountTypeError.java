package com.dropbox.core.p005v2.auth;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.auth.InvalidAccountTypeError */
public enum InvalidAccountTypeError {
    ENDPOINT,
    FEATURE,
    OTHER;

    /* renamed from: com.dropbox.core.v2.auth.InvalidAccountTypeError$Serializer */
    public static class Serializer extends UnionSerializer<InvalidAccountTypeError> {
        public static final Serializer INSTANCE = null;

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(InvalidAccountTypeError invalidAccountTypeError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (invalidAccountTypeError) {
                case ENDPOINT:
                    jsonGenerator.writeString("endpoint");
                    return;
                case FEATURE:
                    jsonGenerator.writeString("feature");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public InvalidAccountTypeError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            InvalidAccountTypeError invalidAccountTypeError;
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
                if ("endpoint".equals(str)) {
                    invalidAccountTypeError = InvalidAccountTypeError.ENDPOINT;
                } else if ("feature".equals(str)) {
                    invalidAccountTypeError = InvalidAccountTypeError.FEATURE;
                } else {
                    invalidAccountTypeError = InvalidAccountTypeError.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return invalidAccountTypeError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
