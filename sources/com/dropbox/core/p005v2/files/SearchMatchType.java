package com.dropbox.core.p005v2.files;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.google.firebase.analytics.FirebaseAnalytics.Param;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.files.SearchMatchType */
public enum SearchMatchType {
    FILENAME,
    CONTENT,
    BOTH;

    /* renamed from: com.dropbox.core.v2.files.SearchMatchType$Serializer */
    static class Serializer extends UnionSerializer<SearchMatchType> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(SearchMatchType searchMatchType, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (searchMatchType) {
                case FILENAME:
                    jsonGenerator.writeString("filename");
                    return;
                case CONTENT:
                    jsonGenerator.writeString(Param.CONTENT);
                    return;
                case BOTH:
                    jsonGenerator.writeString("both");
                    return;
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unrecognized tag: ");
                    sb.append(searchMatchType);
                    throw new IllegalArgumentException(sb.toString());
            }
        }

        public SearchMatchType deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            SearchMatchType searchMatchType;
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
                    searchMatchType = SearchMatchType.FILENAME;
                } else if (Param.CONTENT.equals(str)) {
                    searchMatchType = SearchMatchType.CONTENT;
                } else if ("both".equals(str)) {
                    searchMatchType = SearchMatchType.BOTH;
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
                return searchMatchType;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
