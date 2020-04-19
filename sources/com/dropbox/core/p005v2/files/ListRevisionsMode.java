package com.dropbox.core.p005v2.files;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.files.ListRevisionsMode */
public enum ListRevisionsMode {
    PATH,
    ID,
    OTHER;

    /* renamed from: com.dropbox.core.v2.files.ListRevisionsMode$Serializer */
    static class Serializer extends UnionSerializer<ListRevisionsMode> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(ListRevisionsMode listRevisionsMode, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (listRevisionsMode) {
                case PATH:
                    jsonGenerator.writeString("path");
                    return;
                case ID:
                    jsonGenerator.writeString("id");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public ListRevisionsMode deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            ListRevisionsMode listRevisionsMode;
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
                if ("path".equals(str)) {
                    listRevisionsMode = ListRevisionsMode.PATH;
                } else if ("id".equals(str)) {
                    listRevisionsMode = ListRevisionsMode.ID;
                } else {
                    listRevisionsMode = ListRevisionsMode.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return listRevisionsMode;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
