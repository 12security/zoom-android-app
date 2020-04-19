package com.dropbox.core.p005v2.paper;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.paper.ImportFormat */
public enum ImportFormat {
    HTML,
    MARKDOWN,
    PLAIN_TEXT,
    OTHER;

    /* renamed from: com.dropbox.core.v2.paper.ImportFormat$Serializer */
    static class Serializer extends UnionSerializer<ImportFormat> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(ImportFormat importFormat, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (importFormat) {
                case HTML:
                    jsonGenerator.writeString("html");
                    return;
                case MARKDOWN:
                    jsonGenerator.writeString("markdown");
                    return;
                case PLAIN_TEXT:
                    jsonGenerator.writeString("plain_text");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public ImportFormat deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            ImportFormat importFormat;
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
                if ("html".equals(str)) {
                    importFormat = ImportFormat.HTML;
                } else if ("markdown".equals(str)) {
                    importFormat = ImportFormat.MARKDOWN;
                } else if ("plain_text".equals(str)) {
                    importFormat = ImportFormat.PLAIN_TEXT;
                } else {
                    importFormat = ImportFormat.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return importFormat;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
