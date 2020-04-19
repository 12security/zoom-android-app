package com.dropbox.core.p005v2.files;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.files.SearchMode */
public enum SearchMode {
    FILENAME,
    FILENAME_AND_CONTENT,
    DELETED_FILENAME;

    /* renamed from: com.dropbox.core.v2.files.SearchMode$Serializer */
    static class Serializer extends UnionSerializer<SearchMode> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(SearchMode searchMode, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (searchMode) {
                case FILENAME:
                    jsonGenerator.writeString("filename");
                    return;
                case FILENAME_AND_CONTENT:
                    jsonGenerator.writeString("filename_and_content");
                    return;
                case DELETED_FILENAME:
                    jsonGenerator.writeString("deleted_filename");
                    return;
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unrecognized tag: ");
                    sb.append(searchMode);
                    throw new IllegalArgumentException(sb.toString());
            }
        }

        public SearchMode deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            SearchMode searchMode;
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
                if ("filename".equals(str)) {
                    searchMode = SearchMode.FILENAME;
                } else if ("filename_and_content".equals(str)) {
                    searchMode = SearchMode.FILENAME_AND_CONTENT;
                } else if ("deleted_filename".equals(str)) {
                    searchMode = SearchMode.DELETED_FILENAME;
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
                return searchMode;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
