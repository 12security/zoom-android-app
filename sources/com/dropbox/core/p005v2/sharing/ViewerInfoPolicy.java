package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.zipow.videobox.AddrBookSettingActivity;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.sharing.ViewerInfoPolicy */
public enum ViewerInfoPolicy {
    ENABLED,
    DISABLED,
    OTHER;

    /* renamed from: com.dropbox.core.v2.sharing.ViewerInfoPolicy$Serializer */
    public static class Serializer extends UnionSerializer<ViewerInfoPolicy> {
        public static final Serializer INSTANCE = null;

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(ViewerInfoPolicy viewerInfoPolicy, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (viewerInfoPolicy) {
                case ENABLED:
                    jsonGenerator.writeString(AddrBookSettingActivity.ARG_RESULT_ENABLED);
                    return;
                case DISABLED:
                    jsonGenerator.writeString("disabled");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public ViewerInfoPolicy deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            ViewerInfoPolicy viewerInfoPolicy;
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
                    viewerInfoPolicy = ViewerInfoPolicy.ENABLED;
                } else if ("disabled".equals(str)) {
                    viewerInfoPolicy = ViewerInfoPolicy.DISABLED;
                } else {
                    viewerInfoPolicy = ViewerInfoPolicy.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return viewerInfoPolicy;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
