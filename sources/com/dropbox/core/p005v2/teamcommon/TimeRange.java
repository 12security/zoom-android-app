package com.dropbox.core.p005v2.teamcommon;

import com.dropbox.core.stone.StoneDeserializerLogger;
import com.dropbox.core.stone.StoneSerializers;
import com.dropbox.core.stone.StructSerializer;
import com.dropbox.core.util.LangUtil;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

/* renamed from: com.dropbox.core.v2.teamcommon.TimeRange */
public class TimeRange {
    protected final Date endTime;
    protected final Date startTime;

    /* renamed from: com.dropbox.core.v2.teamcommon.TimeRange$Builder */
    public static class Builder {
        protected Date endTime = null;
        protected Date startTime = null;

        protected Builder() {
        }

        public Builder withStartTime(Date date) {
            this.startTime = LangUtil.truncateMillis(date);
            return this;
        }

        public Builder withEndTime(Date date) {
            this.endTime = LangUtil.truncateMillis(date);
            return this;
        }

        public TimeRange build() {
            return new TimeRange(this.startTime, this.endTime);
        }
    }

    /* renamed from: com.dropbox.core.v2.teamcommon.TimeRange$Serializer */
    public static class Serializer extends StructSerializer<TimeRange> {
        public static final Serializer INSTANCE = new Serializer();

        public void serialize(TimeRange timeRange, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            if (timeRange.startTime != null) {
                jsonGenerator.writeFieldName("start_time");
                StoneSerializers.nullable(StoneSerializers.timestamp()).serialize(timeRange.startTime, jsonGenerator);
            }
            if (timeRange.endTime != null) {
                jsonGenerator.writeFieldName("end_time");
                StoneSerializers.nullable(StoneSerializers.timestamp()).serialize(timeRange.endTime, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public TimeRange deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            Date date = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                Date date2 = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("start_time".equals(currentName)) {
                        date = (Date) StoneSerializers.nullable(StoneSerializers.timestamp()).deserialize(jsonParser);
                    } else if ("end_time".equals(currentName)) {
                        date2 = (Date) StoneSerializers.nullable(StoneSerializers.timestamp()).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                TimeRange timeRange = new TimeRange(date, date2);
                if (!z) {
                    expectEndObject(jsonParser);
                }
                StoneDeserializerLogger.log(timeRange, timeRange.toStringMultiline());
                return timeRange;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public TimeRange(Date date, Date date2) {
        this.startTime = LangUtil.truncateMillis(date);
        this.endTime = LangUtil.truncateMillis(date2);
    }

    public TimeRange() {
        this(null, null);
    }

    public Date getStartTime() {
        return this.startTime;
    }

    public Date getEndTime() {
        return this.endTime;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.startTime, this.endTime});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0032, code lost:
        if (r2.equals(r5) == false) goto L_0x0035;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean equals(java.lang.Object r5) {
        /*
            r4 = this;
            r0 = 1
            if (r5 != r4) goto L_0x0004
            return r0
        L_0x0004:
            r1 = 0
            if (r5 != 0) goto L_0x0008
            return r1
        L_0x0008:
            java.lang.Class r2 = r5.getClass()
            java.lang.Class r3 = r4.getClass()
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0037
            com.dropbox.core.v2.teamcommon.TimeRange r5 = (com.dropbox.core.p005v2.teamcommon.TimeRange) r5
            java.util.Date r2 = r4.startTime
            java.util.Date r3 = r5.startTime
            if (r2 == r3) goto L_0x0026
            if (r2 == 0) goto L_0x0035
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0035
        L_0x0026:
            java.util.Date r2 = r4.endTime
            java.util.Date r5 = r5.endTime
            if (r2 == r5) goto L_0x0036
            if (r2 == 0) goto L_0x0035
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x0035
            goto L_0x0036
        L_0x0035:
            r0 = 0
        L_0x0036:
            return r0
        L_0x0037:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.teamcommon.TimeRange.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
