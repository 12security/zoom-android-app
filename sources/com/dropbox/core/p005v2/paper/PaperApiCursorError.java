package com.dropbox.core.p005v2.paper;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.paper.PaperApiCursorError */
public enum PaperApiCursorError {
    EXPIRED_CURSOR,
    INVALID_CURSOR,
    WRONG_USER_IN_CURSOR,
    RESET,
    OTHER;

    /* renamed from: com.dropbox.core.v2.paper.PaperApiCursorError$Serializer */
    static class Serializer extends UnionSerializer<PaperApiCursorError> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(PaperApiCursorError paperApiCursorError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (paperApiCursorError) {
                case EXPIRED_CURSOR:
                    jsonGenerator.writeString("expired_cursor");
                    return;
                case INVALID_CURSOR:
                    jsonGenerator.writeString("invalid_cursor");
                    return;
                case WRONG_USER_IN_CURSOR:
                    jsonGenerator.writeString("wrong_user_in_cursor");
                    return;
                case RESET:
                    jsonGenerator.writeString("reset");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public PaperApiCursorError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            PaperApiCursorError paperApiCursorError;
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
                if ("expired_cursor".equals(str)) {
                    paperApiCursorError = PaperApiCursorError.EXPIRED_CURSOR;
                } else if ("invalid_cursor".equals(str)) {
                    paperApiCursorError = PaperApiCursorError.INVALID_CURSOR;
                } else if ("wrong_user_in_cursor".equals(str)) {
                    paperApiCursorError = PaperApiCursorError.WRONG_USER_IN_CURSOR;
                } else if ("reset".equals(str)) {
                    paperApiCursorError = PaperApiCursorError.RESET;
                } else {
                    paperApiCursorError = PaperApiCursorError.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return paperApiCursorError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
