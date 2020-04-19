package com.dropbox.core.p005v2.teamlog;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.teamlog.DownloadPolicyType */
public enum DownloadPolicyType {
    ALLOW,
    DISALLOW,
    OTHER;

    /* renamed from: com.dropbox.core.v2.teamlog.DownloadPolicyType$Serializer */
    static class Serializer extends UnionSerializer<DownloadPolicyType> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(DownloadPolicyType downloadPolicyType, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (downloadPolicyType) {
                case ALLOW:
                    jsonGenerator.writeString("allow");
                    return;
                case DISALLOW:
                    jsonGenerator.writeString("disallow");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public DownloadPolicyType deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            DownloadPolicyType downloadPolicyType;
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
                if ("allow".equals(str)) {
                    downloadPolicyType = DownloadPolicyType.ALLOW;
                } else if ("disallow".equals(str)) {
                    downloadPolicyType = DownloadPolicyType.DISALLOW;
                } else {
                    downloadPolicyType = DownloadPolicyType.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return downloadPolicyType;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
