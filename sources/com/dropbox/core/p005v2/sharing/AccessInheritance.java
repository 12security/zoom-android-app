package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.sharing.AccessInheritance */
public enum AccessInheritance {
    INHERIT,
    NO_INHERIT,
    OTHER;

    /* renamed from: com.dropbox.core.v2.sharing.AccessInheritance$Serializer */
    static class Serializer extends UnionSerializer<AccessInheritance> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(AccessInheritance accessInheritance, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (accessInheritance) {
                case INHERIT:
                    jsonGenerator.writeString("inherit");
                    return;
                case NO_INHERIT:
                    jsonGenerator.writeString("no_inherit");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public AccessInheritance deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            AccessInheritance accessInheritance;
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
                if ("inherit".equals(str)) {
                    accessInheritance = AccessInheritance.INHERIT;
                } else if ("no_inherit".equals(str)) {
                    accessInheritance = AccessInheritance.NO_INHERIT;
                } else {
                    accessInheritance = AccessInheritance.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return accessInheritance;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
