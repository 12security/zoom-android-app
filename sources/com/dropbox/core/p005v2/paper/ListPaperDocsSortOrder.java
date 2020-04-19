package com.dropbox.core.p005v2.paper;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.paper.ListPaperDocsSortOrder */
public enum ListPaperDocsSortOrder {
    ASCENDING,
    DESCENDING,
    OTHER;

    /* renamed from: com.dropbox.core.v2.paper.ListPaperDocsSortOrder$Serializer */
    static class Serializer extends UnionSerializer<ListPaperDocsSortOrder> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(ListPaperDocsSortOrder listPaperDocsSortOrder, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (listPaperDocsSortOrder) {
                case ASCENDING:
                    jsonGenerator.writeString("ascending");
                    return;
                case DESCENDING:
                    jsonGenerator.writeString("descending");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public ListPaperDocsSortOrder deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            ListPaperDocsSortOrder listPaperDocsSortOrder;
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
                if ("ascending".equals(str)) {
                    listPaperDocsSortOrder = ListPaperDocsSortOrder.ASCENDING;
                } else if ("descending".equals(str)) {
                    listPaperDocsSortOrder = ListPaperDocsSortOrder.DESCENDING;
                } else {
                    listPaperDocsSortOrder = ListPaperDocsSortOrder.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return listPaperDocsSortOrder;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
