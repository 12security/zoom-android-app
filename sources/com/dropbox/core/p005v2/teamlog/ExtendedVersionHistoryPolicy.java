package com.dropbox.core.p005v2.teamlog;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.teamlog.ExtendedVersionHistoryPolicy */
public enum ExtendedVersionHistoryPolicy {
    EXPLICITLY_LIMITED,
    EXPLICITLY_UNLIMITED,
    IMPLICITLY_LIMITED,
    IMPLICITLY_UNLIMITED,
    OTHER;

    /* renamed from: com.dropbox.core.v2.teamlog.ExtendedVersionHistoryPolicy$Serializer */
    static class Serializer extends UnionSerializer<ExtendedVersionHistoryPolicy> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(ExtendedVersionHistoryPolicy extendedVersionHistoryPolicy, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (extendedVersionHistoryPolicy) {
                case EXPLICITLY_LIMITED:
                    jsonGenerator.writeString("explicitly_limited");
                    return;
                case EXPLICITLY_UNLIMITED:
                    jsonGenerator.writeString("explicitly_unlimited");
                    return;
                case IMPLICITLY_LIMITED:
                    jsonGenerator.writeString("implicitly_limited");
                    return;
                case IMPLICITLY_UNLIMITED:
                    jsonGenerator.writeString("implicitly_unlimited");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public ExtendedVersionHistoryPolicy deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            ExtendedVersionHistoryPolicy extendedVersionHistoryPolicy;
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
                if ("explicitly_limited".equals(str)) {
                    extendedVersionHistoryPolicy = ExtendedVersionHistoryPolicy.EXPLICITLY_LIMITED;
                } else if ("explicitly_unlimited".equals(str)) {
                    extendedVersionHistoryPolicy = ExtendedVersionHistoryPolicy.EXPLICITLY_UNLIMITED;
                } else if ("implicitly_limited".equals(str)) {
                    extendedVersionHistoryPolicy = ExtendedVersionHistoryPolicy.IMPLICITLY_LIMITED;
                } else if ("implicitly_unlimited".equals(str)) {
                    extendedVersionHistoryPolicy = ExtendedVersionHistoryPolicy.IMPLICITLY_UNLIMITED;
                } else {
                    extendedVersionHistoryPolicy = ExtendedVersionHistoryPolicy.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return extendedVersionHistoryPolicy;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
