package com.dropbox.core.p005v2.paper;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.paper.DocLookupError */
public enum DocLookupError {
    INSUFFICIENT_PERMISSIONS,
    OTHER,
    DOC_NOT_FOUND;

    /* renamed from: com.dropbox.core.v2.paper.DocLookupError$Serializer */
    static class Serializer extends UnionSerializer<DocLookupError> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(DocLookupError docLookupError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (docLookupError) {
                case INSUFFICIENT_PERMISSIONS:
                    jsonGenerator.writeString("insufficient_permissions");
                    return;
                case OTHER:
                    jsonGenerator.writeString("other");
                    return;
                case DOC_NOT_FOUND:
                    jsonGenerator.writeString("doc_not_found");
                    return;
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unrecognized tag: ");
                    sb.append(docLookupError);
                    throw new IllegalArgumentException(sb.toString());
            }
        }

        public DocLookupError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            DocLookupError docLookupError;
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
                    docLookupError = DocLookupError.INSUFFICIENT_PERMISSIONS;
                } else if ("other".equals(str)) {
                    docLookupError = DocLookupError.OTHER;
                } else if ("doc_not_found".equals(str)) {
                    docLookupError = DocLookupError.DOC_NOT_FOUND;
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
                return docLookupError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
