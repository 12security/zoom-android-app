package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.sharing.AccessLevel */
public enum AccessLevel {
    OWNER,
    EDITOR,
    VIEWER,
    VIEWER_NO_COMMENT,
    OTHER;

    /* renamed from: com.dropbox.core.v2.sharing.AccessLevel$Serializer */
    public static class Serializer extends UnionSerializer<AccessLevel> {
        public static final Serializer INSTANCE = null;

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(AccessLevel accessLevel, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (accessLevel) {
                case OWNER:
                    jsonGenerator.writeString("owner");
                    return;
                case EDITOR:
                    jsonGenerator.writeString("editor");
                    return;
                case VIEWER:
                    jsonGenerator.writeString("viewer");
                    return;
                case VIEWER_NO_COMMENT:
                    jsonGenerator.writeString("viewer_no_comment");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public AccessLevel deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            AccessLevel accessLevel;
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
                if ("owner".equals(str)) {
                    accessLevel = AccessLevel.OWNER;
                } else if ("editor".equals(str)) {
                    accessLevel = AccessLevel.EDITOR;
                } else if ("viewer".equals(str)) {
                    accessLevel = AccessLevel.VIEWER;
                } else if ("viewer_no_comment".equals(str)) {
                    accessLevel = AccessLevel.VIEWER_NO_COMMENT;
                } else {
                    accessLevel = AccessLevel.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return accessLevel;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
