package com.dropbox.core.p005v2.teamlog;

import com.dropbox.core.stone.StoneDeserializerLogger;
import com.dropbox.core.stone.StoneSerializers;
import com.dropbox.core.stone.StructSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.teamlog.DurationLogInfo */
public class DurationLogInfo {
    protected final long amount;
    protected final TimeUnit unit;

    /* renamed from: com.dropbox.core.v2.teamlog.DurationLogInfo$Serializer */
    static class Serializer extends StructSerializer<DurationLogInfo> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(DurationLogInfo durationLogInfo, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("unit");
            Serializer.INSTANCE.serialize(durationLogInfo.unit, jsonGenerator);
            jsonGenerator.writeFieldName("amount");
            StoneSerializers.uInt64().serialize(Long.valueOf(durationLogInfo.amount), jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public DurationLogInfo deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            TimeUnit timeUnit = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                Long l = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("unit".equals(currentName)) {
                        timeUnit = Serializer.INSTANCE.deserialize(jsonParser);
                    } else if ("amount".equals(currentName)) {
                        l = (Long) StoneSerializers.uInt64().deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (timeUnit == null) {
                    throw new JsonParseException(jsonParser, "Required field \"unit\" missing.");
                } else if (l != null) {
                    DurationLogInfo durationLogInfo = new DurationLogInfo(timeUnit, l.longValue());
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(durationLogInfo, durationLogInfo.toStringMultiline());
                    return durationLogInfo;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"amount\" missing.");
                }
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("No subtype found that matches tag: \"");
                sb.append(str);
                sb.append("\"");
                throw new JsonParseException(jsonParser, sb.toString());
            }
        }
    }

    public DurationLogInfo(TimeUnit timeUnit, long j) {
        if (timeUnit != null) {
            this.unit = timeUnit;
            this.amount = j;
            return;
        }
        throw new IllegalArgumentException("Required value for 'unit' is null");
    }

    public TimeUnit getUnit() {
        return this.unit;
    }

    public long getAmount() {
        return this.amount;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.unit, Long.valueOf(this.amount)});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        DurationLogInfo durationLogInfo = (DurationLogInfo) obj;
        TimeUnit timeUnit = this.unit;
        TimeUnit timeUnit2 = durationLogInfo.unit;
        if ((timeUnit != timeUnit2 && !timeUnit.equals(timeUnit2)) || this.amount != durationLogInfo.amount) {
            z = false;
        }
        return z;
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
