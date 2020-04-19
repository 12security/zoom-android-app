package com.dropbox.core.p005v2.team;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.team.DesktopPlatform */
public enum DesktopPlatform {
    WINDOWS,
    MAC,
    LINUX,
    OTHER;

    /* renamed from: com.dropbox.core.v2.team.DesktopPlatform$Serializer */
    public static class Serializer extends UnionSerializer<DesktopPlatform> {
        public static final Serializer INSTANCE = null;

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(DesktopPlatform desktopPlatform, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (desktopPlatform) {
                case WINDOWS:
                    jsonGenerator.writeString("windows");
                    return;
                case MAC:
                    jsonGenerator.writeString("mac");
                    return;
                case LINUX:
                    jsonGenerator.writeString("linux");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public DesktopPlatform deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            DesktopPlatform desktopPlatform;
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
                if ("windows".equals(str)) {
                    desktopPlatform = DesktopPlatform.WINDOWS;
                } else if ("mac".equals(str)) {
                    desktopPlatform = DesktopPlatform.MAC;
                } else if ("linux".equals(str)) {
                    desktopPlatform = DesktopPlatform.LINUX;
                } else {
                    desktopPlatform = DesktopPlatform.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return desktopPlatform;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
