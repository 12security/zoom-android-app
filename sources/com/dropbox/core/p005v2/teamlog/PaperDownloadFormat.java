package com.dropbox.core.p005v2.teamlog;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.teamlog.PaperDownloadFormat */
public enum PaperDownloadFormat {
    DOCX,
    HTML,
    MARKDOWN,
    OTHER;

    /* renamed from: com.dropbox.core.v2.teamlog.PaperDownloadFormat$Serializer */
    static class Serializer extends UnionSerializer<PaperDownloadFormat> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(PaperDownloadFormat paperDownloadFormat, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (paperDownloadFormat) {
                case DOCX:
                    jsonGenerator.writeString("docx");
                    return;
                case HTML:
                    jsonGenerator.writeString("html");
                    return;
                case MARKDOWN:
                    jsonGenerator.writeString("markdown");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public PaperDownloadFormat deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            PaperDownloadFormat paperDownloadFormat;
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
                if ("docx".equals(str)) {
                    paperDownloadFormat = PaperDownloadFormat.DOCX;
                } else if ("html".equals(str)) {
                    paperDownloadFormat = PaperDownloadFormat.HTML;
                } else if ("markdown".equals(str)) {
                    paperDownloadFormat = PaperDownloadFormat.MARKDOWN;
                } else {
                    paperDownloadFormat = PaperDownloadFormat.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return paperDownloadFormat;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
