package com.dropbox.core.p005v2.seenstate;

import androidx.core.p002os.EnvironmentCompat;
import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.seenstate.PlatformType */
public enum PlatformType {
    WEB,
    MOBILE,
    DESKTOP,
    UNKNOWN,
    OTHER;

    /* renamed from: com.dropbox.core.v2.seenstate.PlatformType$Serializer */
    public static class Serializer extends UnionSerializer<PlatformType> {
        public static final Serializer INSTANCE = null;

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(PlatformType platformType, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (platformType) {
                case WEB:
                    jsonGenerator.writeString("web");
                    return;
                case MOBILE:
                    jsonGenerator.writeString("mobile");
                    return;
                case DESKTOP:
                    jsonGenerator.writeString("desktop");
                    return;
                case UNKNOWN:
                    jsonGenerator.writeString(EnvironmentCompat.MEDIA_UNKNOWN);
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public PlatformType deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            PlatformType platformType;
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
                if ("web".equals(str)) {
                    platformType = PlatformType.WEB;
                } else if ("mobile".equals(str)) {
                    platformType = PlatformType.MOBILE;
                } else if ("desktop".equals(str)) {
                    platformType = PlatformType.DESKTOP;
                } else if (EnvironmentCompat.MEDIA_UNKNOWN.equals(str)) {
                    platformType = PlatformType.UNKNOWN;
                } else {
                    platformType = PlatformType.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return platformType;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
