package com.dropbox.core.p005v2.teamlog;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.zipow.videobox.AddrBookSettingActivity;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.teamlog.ShowcaseEnabledPolicy */
public enum ShowcaseEnabledPolicy {
    DISABLED,
    ENABLED,
    OTHER;

    /* renamed from: com.dropbox.core.v2.teamlog.ShowcaseEnabledPolicy$Serializer */
    static class Serializer extends UnionSerializer<ShowcaseEnabledPolicy> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(ShowcaseEnabledPolicy showcaseEnabledPolicy, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (showcaseEnabledPolicy) {
                case DISABLED:
                    jsonGenerator.writeString("disabled");
                    return;
                case ENABLED:
                    jsonGenerator.writeString(AddrBookSettingActivity.ARG_RESULT_ENABLED);
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public ShowcaseEnabledPolicy deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            ShowcaseEnabledPolicy showcaseEnabledPolicy;
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
                    showcaseEnabledPolicy = ShowcaseEnabledPolicy.DISABLED;
                } else if (AddrBookSettingActivity.ARG_RESULT_ENABLED.equals(str)) {
                    showcaseEnabledPolicy = ShowcaseEnabledPolicy.ENABLED;
                } else {
                    showcaseEnabledPolicy = ShowcaseEnabledPolicy.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return showcaseEnabledPolicy;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
