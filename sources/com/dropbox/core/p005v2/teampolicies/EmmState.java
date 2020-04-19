package com.dropbox.core.p005v2.teampolicies;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.teampolicies.EmmState */
public enum EmmState {
    DISABLED,
    OPTIONAL,
    REQUIRED,
    OTHER;

    /* renamed from: com.dropbox.core.v2.teampolicies.EmmState$Serializer */
    public static class Serializer extends UnionSerializer<EmmState> {
        public static final Serializer INSTANCE = null;

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(EmmState emmState, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (emmState) {
                case DISABLED:
                    jsonGenerator.writeString("disabled");
                    return;
                case OPTIONAL:
                    jsonGenerator.writeString("optional");
                    return;
                case REQUIRED:
                    jsonGenerator.writeString("required");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public EmmState deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            EmmState emmState;
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
                if ("disabled".equals(str)) {
                    emmState = EmmState.DISABLED;
                } else if ("optional".equals(str)) {
                    emmState = EmmState.OPTIONAL;
                } else if ("required".equals(str)) {
                    emmState = EmmState.REQUIRED;
                } else {
                    emmState = EmmState.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return emmState;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
