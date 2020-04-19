package com.dropbox.core.p005v2.paper;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.paper.PaperDocCreateError */
public enum PaperDocCreateError {
    INSUFFICIENT_PERMISSIONS,
    OTHER,
    CONTENT_MALFORMED,
    FOLDER_NOT_FOUND,
    DOC_LENGTH_EXCEEDED,
    IMAGE_SIZE_EXCEEDED;

    /* renamed from: com.dropbox.core.v2.paper.PaperDocCreateError$Serializer */
    static class Serializer extends UnionSerializer<PaperDocCreateError> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(PaperDocCreateError paperDocCreateError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (paperDocCreateError) {
                case INSUFFICIENT_PERMISSIONS:
                    jsonGenerator.writeString("insufficient_permissions");
                    return;
                case OTHER:
                    jsonGenerator.writeString("other");
                    return;
                case CONTENT_MALFORMED:
                    jsonGenerator.writeString("content_malformed");
                    return;
                case FOLDER_NOT_FOUND:
                    jsonGenerator.writeString("folder_not_found");
                    return;
                case DOC_LENGTH_EXCEEDED:
                    jsonGenerator.writeString("doc_length_exceeded");
                    return;
                case IMAGE_SIZE_EXCEEDED:
                    jsonGenerator.writeString("image_size_exceeded");
                    return;
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unrecognized tag: ");
                    sb.append(paperDocCreateError);
                    throw new IllegalArgumentException(sb.toString());
            }
        }

        public PaperDocCreateError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            PaperDocCreateError paperDocCreateError;
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
                if ("insufficient_permissions".equals(str)) {
                    paperDocCreateError = PaperDocCreateError.INSUFFICIENT_PERMISSIONS;
                } else if ("other".equals(str)) {
                    paperDocCreateError = PaperDocCreateError.OTHER;
                } else if ("content_malformed".equals(str)) {
                    paperDocCreateError = PaperDocCreateError.CONTENT_MALFORMED;
                } else if ("folder_not_found".equals(str)) {
                    paperDocCreateError = PaperDocCreateError.FOLDER_NOT_FOUND;
                } else if ("doc_length_exceeded".equals(str)) {
                    paperDocCreateError = PaperDocCreateError.DOC_LENGTH_EXCEEDED;
                } else if ("image_size_exceeded".equals(str)) {
                    paperDocCreateError = PaperDocCreateError.IMAGE_SIZE_EXCEEDED;
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
                return paperDocCreateError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
