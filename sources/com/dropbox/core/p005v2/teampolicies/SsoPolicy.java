package com.dropbox.core.p005v2.teampolicies;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.teampolicies.SsoPolicy */
public enum SsoPolicy {
    DISABLED,
    OPTIONAL,
    REQUIRED,
    OTHER;

    /* renamed from: com.dropbox.core.v2.teampolicies.SsoPolicy$Serializer */
    public static class Serializer extends UnionSerializer<SsoPolicy> {
        public static final Serializer INSTANCE = null;

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(SsoPolicy ssoPolicy, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (ssoPolicy) {
                case DISABLED:
                    jsonGenerator.writeString("disabled");
                    return;
                case OPTIONAL:
                    jsonGenerator.writeString("optional");
                    return;
                case REQUIRED:
                    jsonGenerator.writeString("required");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public SsoPolicy deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            SsoPolicy ssoPolicy;
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
                if ("disabled".equals(str)) {
                    ssoPolicy = SsoPolicy.DISABLED;
                } else if ("optional".equals(str)) {
                    ssoPolicy = SsoPolicy.OPTIONAL;
                } else if ("required".equals(str)) {
                    ssoPolicy = SsoPolicy.REQUIRED;
                } else {
                    ssoPolicy = SsoPolicy.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return ssoPolicy;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
