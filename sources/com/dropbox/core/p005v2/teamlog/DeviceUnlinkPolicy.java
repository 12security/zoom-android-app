package com.dropbox.core.p005v2.teamlog;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.teamlog.DeviceUnlinkPolicy */
public enum DeviceUnlinkPolicy {
    REMOVE,
    KEEP,
    OTHER;

    /* renamed from: com.dropbox.core.v2.teamlog.DeviceUnlinkPolicy$Serializer */
    static class Serializer extends UnionSerializer<DeviceUnlinkPolicy> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(DeviceUnlinkPolicy deviceUnlinkPolicy, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (deviceUnlinkPolicy) {
                case REMOVE:
                    jsonGenerator.writeString("remove");
                    return;
                case KEEP:
                    jsonGenerator.writeString("keep");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public DeviceUnlinkPolicy deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            DeviceUnlinkPolicy deviceUnlinkPolicy;
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
                if ("remove".equals(str)) {
                    deviceUnlinkPolicy = DeviceUnlinkPolicy.REMOVE;
                } else if ("keep".equals(str)) {
                    deviceUnlinkPolicy = DeviceUnlinkPolicy.KEEP;
                } else {
                    deviceUnlinkPolicy = DeviceUnlinkPolicy.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return deviceUnlinkPolicy;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
