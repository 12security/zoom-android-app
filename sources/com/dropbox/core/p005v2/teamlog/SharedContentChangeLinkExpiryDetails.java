package com.dropbox.core.p005v2.teamlog;

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

/* renamed from: com.dropbox.core.v2.teamlog.SharedContentChangeLinkExpiryDetails */
public class SharedContentChangeLinkExpiryDetails {
    protected final Date newValue;
    protected final Date previousValue;

    /* renamed from: com.dropbox.core.v2.teamlog.SharedContentChangeLinkExpiryDetails$Builder */
    public static class Builder {
        protected Date newValue = null;
        protected Date previousValue = null;

        protected Builder() {
        }

        public Builder withNewValue(Date date) {
            this.newValue = LangUtil.truncateMillis(date);
            return this;
        }

        public Builder withPreviousValue(Date date) {
            this.previousValue = LangUtil.truncateMillis(date);
            return this;
        }

        public SharedContentChangeLinkExpiryDetails build() {
            return new SharedContentChangeLinkExpiryDetails(this.newValue, this.previousValue);
        }
    }

    /* renamed from: com.dropbox.core.v2.teamlog.SharedContentChangeLinkExpiryDetails$Serializer */
    static class Serializer extends StructSerializer<SharedContentChangeLinkExpiryDetails> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(SharedContentChangeLinkExpiryDetails sharedContentChangeLinkExpiryDetails, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            if (sharedContentChangeLinkExpiryDetails.newValue != null) {
                jsonGenerator.writeFieldName("new_value");
                StoneSerializers.nullable(StoneSerializers.timestamp()).serialize(sharedContentChangeLinkExpiryDetails.newValue, jsonGenerator);
            }
            if (sharedContentChangeLinkExpiryDetails.previousValue != null) {
                jsonGenerator.writeFieldName("previous_value");
                StoneSerializers.nullable(StoneSerializers.timestamp()).serialize(sharedContentChangeLinkExpiryDetails.previousValue, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public SharedContentChangeLinkExpiryDetails deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
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
                    if ("new_value".equals(currentName)) {
                        date = (Date) StoneSerializers.nullable(StoneSerializers.timestamp()).deserialize(jsonParser);
                    } else if ("previous_value".equals(currentName)) {
                        date2 = (Date) StoneSerializers.nullable(StoneSerializers.timestamp()).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                SharedContentChangeLinkExpiryDetails sharedContentChangeLinkExpiryDetails = new SharedContentChangeLinkExpiryDetails(date, date2);
                if (!z) {
                    expectEndObject(jsonParser);
                }
                StoneDeserializerLogger.log(sharedContentChangeLinkExpiryDetails, sharedContentChangeLinkExpiryDetails.toStringMultiline());
                return sharedContentChangeLinkExpiryDetails;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public SharedContentChangeLinkExpiryDetails(Date date, Date date2) {
        this.newValue = LangUtil.truncateMillis(date);
        this.previousValue = LangUtil.truncateMillis(date2);
    }

    public SharedContentChangeLinkExpiryDetails() {
        this(null, null);
    }

    public Date getNewValue() {
        return this.newValue;
    }

    public Date getPreviousValue() {
        return this.previousValue;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.newValue, this.previousValue});
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
            com.dropbox.core.v2.teamlog.SharedContentChangeLinkExpiryDetails r5 = (com.dropbox.core.p005v2.teamlog.SharedContentChangeLinkExpiryDetails) r5
            java.util.Date r2 = r4.newValue
            java.util.Date r3 = r5.newValue
            if (r2 == r3) goto L_0x0026
            if (r2 == 0) goto L_0x0035
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0035
        L_0x0026:
            java.util.Date r2 = r4.previousValue
            java.util.Date r5 = r5.previousValue
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
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.teamlog.SharedContentChangeLinkExpiryDetails.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
