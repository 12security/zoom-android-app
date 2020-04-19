package com.dropbox.core.p005v2.teamlog;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.teamlog.SmartSyncOptOutPolicy */
public enum SmartSyncOptOutPolicy {
    DEFAULT,
    OPTED_OUT,
    OTHER;

    /* renamed from: com.dropbox.core.v2.teamlog.SmartSyncOptOutPolicy$Serializer */
    static class Serializer extends UnionSerializer<SmartSyncOptOutPolicy> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(SmartSyncOptOutPolicy smartSyncOptOutPolicy, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (smartSyncOptOutPolicy) {
                case DEFAULT:
                    jsonGenerator.writeString("default");
                    return;
                case OPTED_OUT:
                    jsonGenerator.writeString("opted_out");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public SmartSyncOptOutPolicy deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            SmartSyncOptOutPolicy smartSyncOptOutPolicy;
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
                if ("default".equals(str)) {
                    smartSyncOptOutPolicy = SmartSyncOptOutPolicy.DEFAULT;
                } else if ("opted_out".equals(str)) {
                    smartSyncOptOutPolicy = SmartSyncOptOutPolicy.OPTED_OUT;
                } else {
                    smartSyncOptOutPolicy = SmartSyncOptOutPolicy.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return smartSyncOptOutPolicy;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
