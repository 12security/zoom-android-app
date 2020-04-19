package com.dropbox.core.p005v2.teamlog;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.teamlog.DeviceType */
public enum DeviceType {
    DESKTOP,
    MOBILE,
    OTHER;

    /* renamed from: com.dropbox.core.v2.teamlog.DeviceType$Serializer */
    static class Serializer extends UnionSerializer<DeviceType> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(DeviceType deviceType, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (deviceType) {
                case DESKTOP:
                    jsonGenerator.writeString("desktop");
                    return;
                case MOBILE:
                    jsonGenerator.writeString("mobile");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public DeviceType deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            DeviceType deviceType;
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
                if ("desktop".equals(str)) {
                    deviceType = DeviceType.DESKTOP;
                } else if ("mobile".equals(str)) {
                    deviceType = DeviceType.MOBILE;
                } else {
                    deviceType = DeviceType.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return deviceType;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
