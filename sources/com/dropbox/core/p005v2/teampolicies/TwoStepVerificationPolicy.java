package com.dropbox.core.p005v2.teampolicies;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.teampolicies.TwoStepVerificationPolicy */
public enum TwoStepVerificationPolicy {
    REQUIRE_TFA_ENABLE,
    REQUIRE_TFA_DISABLE,
    OTHER;

    /* renamed from: com.dropbox.core.v2.teampolicies.TwoStepVerificationPolicy$Serializer */
    public static class Serializer extends UnionSerializer<TwoStepVerificationPolicy> {
        public static final Serializer INSTANCE = null;

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(TwoStepVerificationPolicy twoStepVerificationPolicy, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (twoStepVerificationPolicy) {
                case REQUIRE_TFA_ENABLE:
                    jsonGenerator.writeString("require_tfa_enable");
                    return;
                case REQUIRE_TFA_DISABLE:
                    jsonGenerator.writeString("require_tfa_disable");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public TwoStepVerificationPolicy deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            TwoStepVerificationPolicy twoStepVerificationPolicy;
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
                if ("require_tfa_enable".equals(str)) {
                    twoStepVerificationPolicy = TwoStepVerificationPolicy.REQUIRE_TFA_ENABLE;
                } else if ("require_tfa_disable".equals(str)) {
                    twoStepVerificationPolicy = TwoStepVerificationPolicy.REQUIRE_TFA_DISABLE;
                } else {
                    twoStepVerificationPolicy = TwoStepVerificationPolicy.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return twoStepVerificationPolicy;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
