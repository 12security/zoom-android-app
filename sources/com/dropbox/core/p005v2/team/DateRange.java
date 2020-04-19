package com.dropbox.core.p005v2.team;

import com.dropbox.core.stone.StoneDeserializerLogger;
import com.dropbox.core.stone.StoneSerializers;
import com.dropbox.core.stone.StructSerializer;
import com.dropbox.core.util.LangUtil;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.google.firebase.analytics.FirebaseAnalytics.Param;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

/* renamed from: com.dropbox.core.v2.team.DateRange */
class DateRange {
    protected final Date endDate;
    protected final Date startDate;

    /* renamed from: com.dropbox.core.v2.team.DateRange$Builder */
    public static class Builder {
        protected Date endDate = null;
        protected Date startDate = null;

        protected Builder() {
        }

        public Builder withStartDate(Date date) {
            this.startDate = LangUtil.truncateMillis(date);
            return this;
        }

        public Builder withEndDate(Date date) {
            this.endDate = LangUtil.truncateMillis(date);
            return this;
        }

        public DateRange build() {
            return new DateRange(this.startDate, this.endDate);
        }
    }

    /* renamed from: com.dropbox.core.v2.team.DateRange$Serializer */
    static class Serializer extends StructSerializer<DateRange> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(DateRange dateRange, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            if (dateRange.startDate != null) {
                jsonGenerator.writeFieldName(Param.START_DATE);
                StoneSerializers.nullable(StoneSerializers.timestamp()).serialize(dateRange.startDate, jsonGenerator);
            }
            if (dateRange.endDate != null) {
                jsonGenerator.writeFieldName(Param.END_DATE);
                StoneSerializers.nullable(StoneSerializers.timestamp()).serialize(dateRange.endDate, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public DateRange deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
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
                    if (Param.START_DATE.equals(currentName)) {
                        date = (Date) StoneSerializers.nullable(StoneSerializers.timestamp()).deserialize(jsonParser);
                    } else if (Param.END_DATE.equals(currentName)) {
                        date2 = (Date) StoneSerializers.nullable(StoneSerializers.timestamp()).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                DateRange dateRange = new DateRange(date, date2);
                if (!z) {
                    expectEndObject(jsonParser);
                }
                StoneDeserializerLogger.log(dateRange, dateRange.toStringMultiline());
                return dateRange;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public DateRange(Date date, Date date2) {
        this.startDate = LangUtil.truncateMillis(date);
        this.endDate = LangUtil.truncateMillis(date2);
    }

    public DateRange() {
        this(null, null);
    }

    public Date getStartDate() {
        return this.startDate;
    }

    public Date getEndDate() {
        return this.endDate;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.startDate, this.endDate});
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
            com.dropbox.core.v2.team.DateRange r5 = (com.dropbox.core.p005v2.team.DateRange) r5
            java.util.Date r2 = r4.startDate
            java.util.Date r3 = r5.startDate
            if (r2 == r3) goto L_0x0026
            if (r2 == 0) goto L_0x0035
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0035
        L_0x0026:
            java.util.Date r2 = r4.endDate
            java.util.Date r5 = r5.endDate
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
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.team.DateRange.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
