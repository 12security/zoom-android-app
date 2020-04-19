package com.dropbox.core.p005v2.paper;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.paper.PaperDocUpdatePolicy */
public enum PaperDocUpdatePolicy {
    APPEND,
    PREPEND,
    OVERWRITE_ALL,
    OTHER;

    /* renamed from: com.dropbox.core.v2.paper.PaperDocUpdatePolicy$Serializer */
    static class Serializer extends UnionSerializer<PaperDocUpdatePolicy> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(PaperDocUpdatePolicy paperDocUpdatePolicy, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (paperDocUpdatePolicy) {
                case APPEND:
                    jsonGenerator.writeString("append");
                    return;
                case PREPEND:
                    jsonGenerator.writeString("prepend");
                    return;
                case OVERWRITE_ALL:
                    jsonGenerator.writeString("overwrite_all");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public PaperDocUpdatePolicy deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            PaperDocUpdatePolicy paperDocUpdatePolicy;
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
                if ("append".equals(str)) {
                    paperDocUpdatePolicy = PaperDocUpdatePolicy.APPEND;
                } else if ("prepend".equals(str)) {
                    paperDocUpdatePolicy = PaperDocUpdatePolicy.PREPEND;
                } else if ("overwrite_all".equals(str)) {
                    paperDocUpdatePolicy = PaperDocUpdatePolicy.OVERWRITE_ALL;
                } else {
                    paperDocUpdatePolicy = PaperDocUpdatePolicy.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return paperDocUpdatePolicy;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
