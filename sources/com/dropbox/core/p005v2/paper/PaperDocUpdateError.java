package com.dropbox.core.p005v2.paper;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.paper.PaperDocUpdateError */
public enum PaperDocUpdateError {
    INSUFFICIENT_PERMISSIONS,
    OTHER,
    DOC_NOT_FOUND,
    CONTENT_MALFORMED,
    REVISION_MISMATCH,
    DOC_LENGTH_EXCEEDED,
    IMAGE_SIZE_EXCEEDED,
    DOC_ARCHIVED,
    DOC_DELETED;

    /* renamed from: com.dropbox.core.v2.paper.PaperDocUpdateError$Serializer */
    static class Serializer extends UnionSerializer<PaperDocUpdateError> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(PaperDocUpdateError paperDocUpdateError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (paperDocUpdateError) {
                case INSUFFICIENT_PERMISSIONS:
                    jsonGenerator.writeString("insufficient_permissions");
                    return;
                case OTHER:
                    jsonGenerator.writeString("other");
                    return;
                case DOC_NOT_FOUND:
                    jsonGenerator.writeString("doc_not_found");
                    return;
                case CONTENT_MALFORMED:
                    jsonGenerator.writeString("content_malformed");
                    return;
                case REVISION_MISMATCH:
                    jsonGenerator.writeString("revision_mismatch");
                    return;
                case DOC_LENGTH_EXCEEDED:
                    jsonGenerator.writeString("doc_length_exceeded");
                    return;
                case IMAGE_SIZE_EXCEEDED:
                    jsonGenerator.writeString("image_size_exceeded");
                    return;
                case DOC_ARCHIVED:
                    jsonGenerator.writeString("doc_archived");
                    return;
                case DOC_DELETED:
                    jsonGenerator.writeString("doc_deleted");
                    return;
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unrecognized tag: ");
                    sb.append(paperDocUpdateError);
                    throw new IllegalArgumentException(sb.toString());
            }
        }

        public PaperDocUpdateError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            PaperDocUpdateError paperDocUpdateError;
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
                    paperDocUpdateError = PaperDocUpdateError.INSUFFICIENT_PERMISSIONS;
                } else if ("other".equals(str)) {
                    paperDocUpdateError = PaperDocUpdateError.OTHER;
                } else if ("doc_not_found".equals(str)) {
                    paperDocUpdateError = PaperDocUpdateError.DOC_NOT_FOUND;
                } else if ("content_malformed".equals(str)) {
                    paperDocUpdateError = PaperDocUpdateError.CONTENT_MALFORMED;
                } else if ("revision_mismatch".equals(str)) {
                    paperDocUpdateError = PaperDocUpdateError.REVISION_MISMATCH;
                } else if ("doc_length_exceeded".equals(str)) {
                    paperDocUpdateError = PaperDocUpdateError.DOC_LENGTH_EXCEEDED;
                } else if ("image_size_exceeded".equals(str)) {
                    paperDocUpdateError = PaperDocUpdateError.IMAGE_SIZE_EXCEEDED;
                } else if ("doc_archived".equals(str)) {
                    paperDocUpdateError = PaperDocUpdateError.DOC_ARCHIVED;
                } else if ("doc_deleted".equals(str)) {
                    paperDocUpdateError = PaperDocUpdateError.DOC_DELETED;
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
                return paperDocUpdateError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
