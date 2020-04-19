package com.dropbox.core.p005v2.teamlog;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.zipow.videobox.AddrBookSettingActivity;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.teamlog.TfaConfiguration */
public enum TfaConfiguration {
    DISABLED,
    ENABLED,
    SMS,
    AUTHENTICATOR,
    OTHER;

    /* renamed from: com.dropbox.core.v2.teamlog.TfaConfiguration$Serializer */
    static class Serializer extends UnionSerializer<TfaConfiguration> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(TfaConfiguration tfaConfiguration, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (tfaConfiguration) {
                case DISABLED:
                    jsonGenerator.writeString("disabled");
                    return;
                case ENABLED:
                    jsonGenerator.writeString(AddrBookSettingActivity.ARG_RESULT_ENABLED);
                    return;
                case SMS:
                    jsonGenerator.writeString("sms");
                    return;
                case AUTHENTICATOR:
                    jsonGenerator.writeString("authenticator");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public TfaConfiguration deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            TfaConfiguration tfaConfiguration;
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
                    tfaConfiguration = TfaConfiguration.DISABLED;
                } else if (AddrBookSettingActivity.ARG_RESULT_ENABLED.equals(str)) {
                    tfaConfiguration = TfaConfiguration.ENABLED;
                } else if ("sms".equals(str)) {
                    tfaConfiguration = TfaConfiguration.SMS;
                } else if ("authenticator".equals(str)) {
                    tfaConfiguration = TfaConfiguration.AUTHENTICATOR;
                } else {
                    tfaConfiguration = TfaConfiguration.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return tfaConfiguration;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
