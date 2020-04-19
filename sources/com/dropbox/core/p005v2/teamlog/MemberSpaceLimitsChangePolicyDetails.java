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

/* renamed from: com.dropbox.core.v2.teamlog.MemberSpaceLimitsChangePolicyDetails */
public class MemberSpaceLimitsChangePolicyDetails {
    protected final Long newValue;
    protected final Long previousValue;

    /* renamed from: com.dropbox.core.v2.teamlog.MemberSpaceLimitsChangePolicyDetails$Builder */
    public static class Builder {
        protected Long newValue = null;
        protected Long previousValue = null;

        protected Builder() {
        }

        public Builder withPreviousValue(Long l) {
            this.previousValue = l;
            return this;
        }

        public Builder withNewValue(Long l) {
            this.newValue = l;
            return this;
        }

        public MemberSpaceLimitsChangePolicyDetails build() {
            return new MemberSpaceLimitsChangePolicyDetails(this.previousValue, this.newValue);
        }
    }

    /* renamed from: com.dropbox.core.v2.teamlog.MemberSpaceLimitsChangePolicyDetails$Serializer */
    static class Serializer extends StructSerializer<MemberSpaceLimitsChangePolicyDetails> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(MemberSpaceLimitsChangePolicyDetails memberSpaceLimitsChangePolicyDetails, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            if (memberSpaceLimitsChangePolicyDetails.previousValue != null) {
                jsonGenerator.writeFieldName("previous_value");
                StoneSerializers.nullable(StoneSerializers.uInt64()).serialize(memberSpaceLimitsChangePolicyDetails.previousValue, jsonGenerator);
            }
            if (memberSpaceLimitsChangePolicyDetails.newValue != null) {
                jsonGenerator.writeFieldName("new_value");
                StoneSerializers.nullable(StoneSerializers.uInt64()).serialize(memberSpaceLimitsChangePolicyDetails.newValue, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public MemberSpaceLimitsChangePolicyDetails deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            Long l = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                Long l2 = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("previous_value".equals(currentName)) {
                        l = (Long) StoneSerializers.nullable(StoneSerializers.uInt64()).deserialize(jsonParser);
                    } else if ("new_value".equals(currentName)) {
                        l2 = (Long) StoneSerializers.nullable(StoneSerializers.uInt64()).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                MemberSpaceLimitsChangePolicyDetails memberSpaceLimitsChangePolicyDetails = new MemberSpaceLimitsChangePolicyDetails(l, l2);
                if (!z) {
                    expectEndObject(jsonParser);
                }
                StoneDeserializerLogger.log(memberSpaceLimitsChangePolicyDetails, memberSpaceLimitsChangePolicyDetails.toStringMultiline());
                return memberSpaceLimitsChangePolicyDetails;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public MemberSpaceLimitsChangePolicyDetails(Long l, Long l2) {
        this.previousValue = l;
        this.newValue = l2;
    }

    public MemberSpaceLimitsChangePolicyDetails() {
        this(null, null);
    }

    public Long getPreviousValue() {
        return this.previousValue;
    }

    public Long getNewValue() {
        return this.newValue;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.previousValue, this.newValue});
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
            com.dropbox.core.v2.teamlog.MemberSpaceLimitsChangePolicyDetails r5 = (com.dropbox.core.p005v2.teamlog.MemberSpaceLimitsChangePolicyDetails) r5
            java.lang.Long r2 = r4.previousValue
            java.lang.Long r3 = r5.previousValue
            if (r2 == r3) goto L_0x0026
            if (r2 == 0) goto L_0x0035
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0035
        L_0x0026:
            java.lang.Long r2 = r4.newValue
            java.lang.Long r5 = r5.newValue
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
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.teamlog.MemberSpaceLimitsChangePolicyDetails.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
