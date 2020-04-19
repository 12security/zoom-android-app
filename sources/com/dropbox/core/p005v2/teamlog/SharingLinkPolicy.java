package com.dropbox.core.p005v2.teamlog;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.teamlog.SharingLinkPolicy */
public enum SharingLinkPolicy {
    DEFAULT_PRIVATE,
    DEFAULT_PUBLIC,
    ONLY_PRIVATE,
    OTHER;

    /* renamed from: com.dropbox.core.v2.teamlog.SharingLinkPolicy$Serializer */
    static class Serializer extends UnionSerializer<SharingLinkPolicy> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(SharingLinkPolicy sharingLinkPolicy, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (sharingLinkPolicy) {
                case DEFAULT_PRIVATE:
                    jsonGenerator.writeString("default_private");
                    return;
                case DEFAULT_PUBLIC:
                    jsonGenerator.writeString("default_public");
                    return;
                case ONLY_PRIVATE:
                    jsonGenerator.writeString("only_private");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public SharingLinkPolicy deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            SharingLinkPolicy sharingLinkPolicy;
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
                if ("default_private".equals(str)) {
                    sharingLinkPolicy = SharingLinkPolicy.DEFAULT_PRIVATE;
                } else if ("default_public".equals(str)) {
                    sharingLinkPolicy = SharingLinkPolicy.DEFAULT_PUBLIC;
                } else if ("only_private".equals(str)) {
                    sharingLinkPolicy = SharingLinkPolicy.ONLY_PRIVATE;
                } else {
                    sharingLinkPolicy = SharingLinkPolicy.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return sharingLinkPolicy;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
