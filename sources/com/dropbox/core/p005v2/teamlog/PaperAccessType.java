package com.dropbox.core.p005v2.teamlog;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.teamlog.PaperAccessType */
public enum PaperAccessType {
    VIEWER,
    COMMENTER,
    EDITOR,
    OTHER;

    /* renamed from: com.dropbox.core.v2.teamlog.PaperAccessType$Serializer */
    static class Serializer extends UnionSerializer<PaperAccessType> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(PaperAccessType paperAccessType, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (paperAccessType) {
                case VIEWER:
                    jsonGenerator.writeString("viewer");
                    return;
                case COMMENTER:
                    jsonGenerator.writeString("commenter");
                    return;
                case EDITOR:
                    jsonGenerator.writeString("editor");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public PaperAccessType deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            PaperAccessType paperAccessType;
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
                if ("viewer".equals(str)) {
                    paperAccessType = PaperAccessType.VIEWER;
                } else if ("commenter".equals(str)) {
                    paperAccessType = PaperAccessType.COMMENTER;
                } else if ("editor".equals(str)) {
                    paperAccessType = PaperAccessType.EDITOR;
                } else {
                    paperAccessType = PaperAccessType.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return paperAccessType;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
