package com.dropbox.core.p005v2.teamlog;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.teamlog.TimeUnit */
public enum TimeUnit {
    MILLISECONDS,
    SECONDS,
    MINUTES,
    HOURS,
    DAYS,
    WEEKS,
    MONTHS,
    YEARS,
    OTHER;

    /* renamed from: com.dropbox.core.v2.teamlog.TimeUnit$Serializer */
    static class Serializer extends UnionSerializer<TimeUnit> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(TimeUnit timeUnit, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (timeUnit) {
                case MILLISECONDS:
                    jsonGenerator.writeString("milliseconds");
                    return;
                case SECONDS:
                    jsonGenerator.writeString("seconds");
                    return;
                case MINUTES:
                    jsonGenerator.writeString("minutes");
                    return;
                case HOURS:
                    jsonGenerator.writeString("hours");
                    return;
                case DAYS:
                    jsonGenerator.writeString("days");
                    return;
                case WEEKS:
                    jsonGenerator.writeString("weeks");
                    return;
                case MONTHS:
                    jsonGenerator.writeString("months");
                    return;
                case YEARS:
                    jsonGenerator.writeString("years");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public TimeUnit deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            TimeUnit timeUnit;
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
                if ("milliseconds".equals(str)) {
                    timeUnit = TimeUnit.MILLISECONDS;
                } else if ("seconds".equals(str)) {
                    timeUnit = TimeUnit.SECONDS;
                } else if ("minutes".equals(str)) {
                    timeUnit = TimeUnit.MINUTES;
                } else if ("hours".equals(str)) {
                    timeUnit = TimeUnit.HOURS;
                } else if ("days".equals(str)) {
                    timeUnit = TimeUnit.DAYS;
                } else if ("weeks".equals(str)) {
                    timeUnit = TimeUnit.WEEKS;
                } else if ("months".equals(str)) {
                    timeUnit = TimeUnit.MONTHS;
                } else if ("years".equals(str)) {
                    timeUnit = TimeUnit.YEARS;
                } else {
                    timeUnit = TimeUnit.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return timeUnit;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
