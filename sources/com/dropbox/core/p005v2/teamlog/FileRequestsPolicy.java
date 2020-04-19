package com.dropbox.core.p005v2.teamlog;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.zipow.videobox.AddrBookSettingActivity;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.teamlog.FileRequestsPolicy */
public enum FileRequestsPolicy {
    DISABLED,
    ENABLED,
    OTHER;

    /* renamed from: com.dropbox.core.v2.teamlog.FileRequestsPolicy$Serializer */
    static class Serializer extends UnionSerializer<FileRequestsPolicy> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(FileRequestsPolicy fileRequestsPolicy, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (fileRequestsPolicy) {
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

        public FileRequestsPolicy deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            FileRequestsPolicy fileRequestsPolicy;
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
                    fileRequestsPolicy = FileRequestsPolicy.DISABLED;
                } else if (AddrBookSettingActivity.ARG_RESULT_ENABLED.equals(str)) {
                    fileRequestsPolicy = FileRequestsPolicy.ENABLED;
                } else {
                    fileRequestsPolicy = FileRequestsPolicy.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return fileRequestsPolicy;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
