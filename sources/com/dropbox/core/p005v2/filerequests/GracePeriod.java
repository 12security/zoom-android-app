package com.dropbox.core.p005v2.filerequests;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.filerequests.GracePeriod */
public enum GracePeriod {
    ONE_DAY,
    TWO_DAYS,
    SEVEN_DAYS,
    THIRTY_DAYS,
    ALWAYS,
    OTHER;

    /* renamed from: com.dropbox.core.v2.filerequests.GracePeriod$Serializer */
    static class Serializer extends UnionSerializer<GracePeriod> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(GracePeriod gracePeriod, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (gracePeriod) {
                case ONE_DAY:
                    jsonGenerator.writeString("one_day");
                    return;
                case TWO_DAYS:
                    jsonGenerator.writeString("two_days");
                    return;
                case SEVEN_DAYS:
                    jsonGenerator.writeString("seven_days");
                    return;
                case THIRTY_DAYS:
                    jsonGenerator.writeString("thirty_days");
                    return;
                case ALWAYS:
                    jsonGenerator.writeString("always");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public GracePeriod deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            GracePeriod gracePeriod;
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
                if ("one_day".equals(str)) {
                    gracePeriod = GracePeriod.ONE_DAY;
                } else if ("two_days".equals(str)) {
                    gracePeriod = GracePeriod.TWO_DAYS;
                } else if ("seven_days".equals(str)) {
                    gracePeriod = GracePeriod.SEVEN_DAYS;
                } else if ("thirty_days".equals(str)) {
                    gracePeriod = GracePeriod.THIRTY_DAYS;
                } else if ("always".equals(str)) {
                    gracePeriod = GracePeriod.ALWAYS;
                } else {
                    gracePeriod = GracePeriod.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return gracePeriod;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
