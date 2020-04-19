package com.dropbox.core.p005v2.teampolicies;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.zipow.videobox.AddrBookSettingActivity;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.teampolicies.PaperEnabledPolicy */
public enum PaperEnabledPolicy {
    DISABLED,
    ENABLED,
    UNSPECIFIED,
    OTHER;

    /* renamed from: com.dropbox.core.v2.teampolicies.PaperEnabledPolicy$Serializer */
    public static class Serializer extends UnionSerializer<PaperEnabledPolicy> {
        public static final Serializer INSTANCE = null;

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(PaperEnabledPolicy paperEnabledPolicy, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (paperEnabledPolicy) {
                case DISABLED:
                    jsonGenerator.writeString("disabled");
                    return;
                case ENABLED:
                    jsonGenerator.writeString(AddrBookSettingActivity.ARG_RESULT_ENABLED);
                    return;
                case UNSPECIFIED:
                    jsonGenerator.writeString("unspecified");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public PaperEnabledPolicy deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            PaperEnabledPolicy paperEnabledPolicy;
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
                    paperEnabledPolicy = PaperEnabledPolicy.DISABLED;
                } else if (AddrBookSettingActivity.ARG_RESULT_ENABLED.equals(str)) {
                    paperEnabledPolicy = PaperEnabledPolicy.ENABLED;
                } else if ("unspecified".equals(str)) {
                    paperEnabledPolicy = PaperEnabledPolicy.UNSPECIFIED;
                } else {
                    paperEnabledPolicy = PaperEnabledPolicy.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return paperEnabledPolicy;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
