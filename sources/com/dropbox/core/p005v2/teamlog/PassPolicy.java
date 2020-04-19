package com.dropbox.core.p005v2.teamlog;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.zipow.videobox.AddrBookSettingActivity;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.teamlog.PassPolicy */
public enum PassPolicy {
    ENABLED,
    ALLOW,
    DISABLED,
    OTHER;

    /* renamed from: com.dropbox.core.v2.teamlog.PassPolicy$Serializer */
    static class Serializer extends UnionSerializer<PassPolicy> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(PassPolicy passPolicy, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (passPolicy) {
                case ENABLED:
                    jsonGenerator.writeString(AddrBookSettingActivity.ARG_RESULT_ENABLED);
                    return;
                case ALLOW:
                    jsonGenerator.writeString("allow");
                    return;
                case DISABLED:
                    jsonGenerator.writeString("disabled");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public PassPolicy deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            PassPolicy passPolicy;
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
                if (AddrBookSettingActivity.ARG_RESULT_ENABLED.equals(str)) {
                    passPolicy = PassPolicy.ENABLED;
                } else if ("allow".equals(str)) {
                    passPolicy = PassPolicy.ALLOW;
                } else if ("disabled".equals(str)) {
                    passPolicy = PassPolicy.DISABLED;
                } else {
                    passPolicy = PassPolicy.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return passPolicy;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
