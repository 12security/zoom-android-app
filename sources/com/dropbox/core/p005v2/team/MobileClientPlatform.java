package com.dropbox.core.p005v2.team;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.team.MobileClientPlatform */
public enum MobileClientPlatform {
    IPHONE,
    IPAD,
    ANDROID,
    WINDOWS_PHONE,
    BLACKBERRY,
    OTHER;

    /* renamed from: com.dropbox.core.v2.team.MobileClientPlatform$Serializer */
    public static class Serializer extends UnionSerializer<MobileClientPlatform> {
        public static final Serializer INSTANCE = null;

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(MobileClientPlatform mobileClientPlatform, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (mobileClientPlatform) {
                case IPHONE:
                    jsonGenerator.writeString("iphone");
                    return;
                case IPAD:
                    jsonGenerator.writeString("ipad");
                    return;
                case ANDROID:
                    jsonGenerator.writeString("android");
                    return;
                case WINDOWS_PHONE:
                    jsonGenerator.writeString("windows_phone");
                    return;
                case BLACKBERRY:
                    jsonGenerator.writeString("blackberry");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public MobileClientPlatform deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            MobileClientPlatform mobileClientPlatform;
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
                if ("iphone".equals(str)) {
                    mobileClientPlatform = MobileClientPlatform.IPHONE;
                } else if ("ipad".equals(str)) {
                    mobileClientPlatform = MobileClientPlatform.IPAD;
                } else if ("android".equals(str)) {
                    mobileClientPlatform = MobileClientPlatform.ANDROID;
                } else if ("windows_phone".equals(str)) {
                    mobileClientPlatform = MobileClientPlatform.WINDOWS_PHONE;
                } else if ("blackberry".equals(str)) {
                    mobileClientPlatform = MobileClientPlatform.BLACKBERRY;
                } else {
                    mobileClientPlatform = MobileClientPlatform.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return mobileClientPlatform;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
