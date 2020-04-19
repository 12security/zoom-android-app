package com.dropbox.core.p005v2.paper;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.paper.ListPaperDocsFilterBy */
public enum ListPaperDocsFilterBy {
    DOCS_ACCESSED,
    DOCS_CREATED,
    OTHER;

    /* renamed from: com.dropbox.core.v2.paper.ListPaperDocsFilterBy$Serializer */
    static class Serializer extends UnionSerializer<ListPaperDocsFilterBy> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(ListPaperDocsFilterBy listPaperDocsFilterBy, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (listPaperDocsFilterBy) {
                case DOCS_ACCESSED:
                    jsonGenerator.writeString("docs_accessed");
                    return;
                case DOCS_CREATED:
                    jsonGenerator.writeString("docs_created");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public ListPaperDocsFilterBy deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            ListPaperDocsFilterBy listPaperDocsFilterBy;
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
                if ("docs_accessed".equals(str)) {
                    listPaperDocsFilterBy = ListPaperDocsFilterBy.DOCS_ACCESSED;
                } else if ("docs_created".equals(str)) {
                    listPaperDocsFilterBy = ListPaperDocsFilterBy.DOCS_CREATED;
                } else {
                    listPaperDocsFilterBy = ListPaperDocsFilterBy.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return listPaperDocsFilterBy;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
