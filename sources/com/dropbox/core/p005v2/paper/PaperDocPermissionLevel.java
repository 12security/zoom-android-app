package com.dropbox.core.p005v2.paper;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.paper.PaperDocPermissionLevel */
public enum PaperDocPermissionLevel {
    EDIT,
    VIEW_AND_COMMENT,
    OTHER;

    /* renamed from: com.dropbox.core.v2.paper.PaperDocPermissionLevel$Serializer */
    static class Serializer extends UnionSerializer<PaperDocPermissionLevel> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(PaperDocPermissionLevel paperDocPermissionLevel, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (paperDocPermissionLevel) {
                case EDIT:
                    jsonGenerator.writeString("edit");
                    return;
                case VIEW_AND_COMMENT:
                    jsonGenerator.writeString("view_and_comment");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public PaperDocPermissionLevel deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            PaperDocPermissionLevel paperDocPermissionLevel;
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
                if ("edit".equals(str)) {
                    paperDocPermissionLevel = PaperDocPermissionLevel.EDIT;
                } else if ("view_and_comment".equals(str)) {
                    paperDocPermissionLevel = PaperDocPermissionLevel.VIEW_AND_COMMENT;
                } else {
                    paperDocPermissionLevel = PaperDocPermissionLevel.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return paperDocPermissionLevel;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
