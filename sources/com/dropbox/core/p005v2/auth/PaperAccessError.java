package com.dropbox.core.p005v2.auth;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.auth.PaperAccessError */
public enum PaperAccessError {
    PAPER_DISABLED,
    NOT_PAPER_USER,
    OTHER;

    /* renamed from: com.dropbox.core.v2.auth.PaperAccessError$Serializer */
    public static class Serializer extends UnionSerializer<PaperAccessError> {
        public static final Serializer INSTANCE = null;

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(PaperAccessError paperAccessError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (paperAccessError) {
                case PAPER_DISABLED:
                    jsonGenerator.writeString("paper_disabled");
                    return;
                case NOT_PAPER_USER:
                    jsonGenerator.writeString("not_paper_user");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public PaperAccessError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            PaperAccessError paperAccessError;
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
                if ("paper_disabled".equals(str)) {
                    paperAccessError = PaperAccessError.PAPER_DISABLED;
                } else if ("not_paper_user".equals(str)) {
                    paperAccessError = PaperAccessError.NOT_PAPER_USER;
                } else {
                    paperAccessError = PaperAccessError.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return paperAccessError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
