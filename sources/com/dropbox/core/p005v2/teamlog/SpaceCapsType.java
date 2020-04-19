package com.dropbox.core.p005v2.teamlog;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.teamlog.SpaceCapsType */
public enum SpaceCapsType {
    HARD,
    OFF,
    SOFT,
    OTHER;

    /* renamed from: com.dropbox.core.v2.teamlog.SpaceCapsType$Serializer */
    static class Serializer extends UnionSerializer<SpaceCapsType> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(SpaceCapsType spaceCapsType, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (spaceCapsType) {
                case HARD:
                    jsonGenerator.writeString("hard");
                    return;
                case OFF:
                    jsonGenerator.writeString("off");
                    return;
                case SOFT:
                    jsonGenerator.writeString("soft");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public SpaceCapsType deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            SpaceCapsType spaceCapsType;
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
                if ("hard".equals(str)) {
                    spaceCapsType = SpaceCapsType.HARD;
                } else if ("off".equals(str)) {
                    spaceCapsType = SpaceCapsType.OFF;
                } else if ("soft".equals(str)) {
                    spaceCapsType = SpaceCapsType.SOFT;
                } else {
                    spaceCapsType = SpaceCapsType.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return spaceCapsType;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
