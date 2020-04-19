package com.dropbox.core.p005v2.teampolicies;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.google.android.gms.common.internal.ImagesContract;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.teampolicies.SmartSyncPolicy */
public enum SmartSyncPolicy {
    LOCAL,
    ON_DEMAND,
    OTHER;

    /* renamed from: com.dropbox.core.v2.teampolicies.SmartSyncPolicy$Serializer */
    public static class Serializer extends UnionSerializer<SmartSyncPolicy> {
        public static final Serializer INSTANCE = null;

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(SmartSyncPolicy smartSyncPolicy, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (smartSyncPolicy) {
                case LOCAL:
                    jsonGenerator.writeString(ImagesContract.LOCAL);
                    return;
                case ON_DEMAND:
                    jsonGenerator.writeString("on_demand");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public SmartSyncPolicy deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            SmartSyncPolicy smartSyncPolicy;
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
                if (ImagesContract.LOCAL.equals(str)) {
                    smartSyncPolicy = SmartSyncPolicy.LOCAL;
                } else if ("on_demand".equals(str)) {
                    smartSyncPolicy = SmartSyncPolicy.ON_DEMAND;
                } else {
                    smartSyncPolicy = SmartSyncPolicy.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return smartSyncPolicy;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
