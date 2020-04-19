package com.dropbox.core.p005v2.paper;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.paper.ListPaperDocsSortBy */
public enum ListPaperDocsSortBy {
    ACCESSED,
    MODIFIED,
    CREATED,
    OTHER;

    /* renamed from: com.dropbox.core.v2.paper.ListPaperDocsSortBy$Serializer */
    static class Serializer extends UnionSerializer<ListPaperDocsSortBy> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(ListPaperDocsSortBy listPaperDocsSortBy, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (listPaperDocsSortBy) {
                case ACCESSED:
                    jsonGenerator.writeString("accessed");
                    return;
                case MODIFIED:
                    jsonGenerator.writeString("modified");
                    return;
                case CREATED:
                    jsonGenerator.writeString("created");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public ListPaperDocsSortBy deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            ListPaperDocsSortBy listPaperDocsSortBy;
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
                if ("accessed".equals(str)) {
                    listPaperDocsSortBy = ListPaperDocsSortBy.ACCESSED;
                } else if ("modified".equals(str)) {
                    listPaperDocsSortBy = ListPaperDocsSortBy.MODIFIED;
                } else if ("created".equals(str)) {
                    listPaperDocsSortBy = ListPaperDocsSortBy.CREATED;
                } else {
                    listPaperDocsSortBy = ListPaperDocsSortBy.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return listPaperDocsSortBy;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
