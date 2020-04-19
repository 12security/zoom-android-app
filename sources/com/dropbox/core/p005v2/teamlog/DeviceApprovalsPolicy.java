package com.dropbox.core.p005v2.teamlog;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.teamlog.DeviceApprovalsPolicy */
public enum DeviceApprovalsPolicy {
    UNLIMITED,
    LIMITED,
    OTHER;

    /* renamed from: com.dropbox.core.v2.teamlog.DeviceApprovalsPolicy$Serializer */
    static class Serializer extends UnionSerializer<DeviceApprovalsPolicy> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(DeviceApprovalsPolicy deviceApprovalsPolicy, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (deviceApprovalsPolicy) {
                case UNLIMITED:
                    jsonGenerator.writeString("unlimited");
                    return;
                case LIMITED:
                    jsonGenerator.writeString("limited");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public DeviceApprovalsPolicy deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            DeviceApprovalsPolicy deviceApprovalsPolicy;
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
                if ("unlimited".equals(str)) {
                    deviceApprovalsPolicy = DeviceApprovalsPolicy.UNLIMITED;
                } else if ("limited".equals(str)) {
                    deviceApprovalsPolicy = DeviceApprovalsPolicy.LIMITED;
                } else {
                    deviceApprovalsPolicy = DeviceApprovalsPolicy.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return deviceApprovalsPolicy;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
