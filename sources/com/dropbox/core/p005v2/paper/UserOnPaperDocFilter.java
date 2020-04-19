package com.dropbox.core.p005v2.paper;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.paper.UserOnPaperDocFilter */
public enum UserOnPaperDocFilter {
    VISITED,
    SHARED,
    OTHER;

    /* renamed from: com.dropbox.core.v2.paper.UserOnPaperDocFilter$Serializer */
    static class Serializer extends UnionSerializer<UserOnPaperDocFilter> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(UserOnPaperDocFilter userOnPaperDocFilter, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (userOnPaperDocFilter) {
                case VISITED:
                    jsonGenerator.writeString("visited");
                    return;
                case SHARED:
                    jsonGenerator.writeString("shared");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public UserOnPaperDocFilter deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            UserOnPaperDocFilter userOnPaperDocFilter;
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
                if ("visited".equals(str)) {
                    userOnPaperDocFilter = UserOnPaperDocFilter.VISITED;
                } else if ("shared".equals(str)) {
                    userOnPaperDocFilter = UserOnPaperDocFilter.SHARED;
                } else {
                    userOnPaperDocFilter = UserOnPaperDocFilter.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return userOnPaperDocFilter;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
