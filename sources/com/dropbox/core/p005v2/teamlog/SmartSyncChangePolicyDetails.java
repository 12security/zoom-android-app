package com.dropbox.core.p005v2.teamlog;

import com.dropbox.core.p005v2.teampolicies.SmartSyncPolicy;
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

/* renamed from: com.dropbox.core.v2.teamlog.SmartSyncChangePolicyDetails */
public class SmartSyncChangePolicyDetails {
    protected final SmartSyncPolicy newValue;
    protected final SmartSyncPolicy previousValue;

    /* renamed from: com.dropbox.core.v2.teamlog.SmartSyncChangePolicyDetails$Builder */
    public static class Builder {
        protected SmartSyncPolicy newValue = null;
        protected SmartSyncPolicy previousValue = null;

        protected Builder() {
        }

        public Builder withNewValue(SmartSyncPolicy smartSyncPolicy) {
            this.newValue = smartSyncPolicy;
            return this;
        }

        public Builder withPreviousValue(SmartSyncPolicy smartSyncPolicy) {
            this.previousValue = smartSyncPolicy;
            return this;
        }

        public SmartSyncChangePolicyDetails build() {
            return new SmartSyncChangePolicyDetails(this.newValue, this.previousValue);
        }
    }

    /* renamed from: com.dropbox.core.v2.teamlog.SmartSyncChangePolicyDetails$Serializer */
    static class Serializer extends StructSerializer<SmartSyncChangePolicyDetails> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(SmartSyncChangePolicyDetails smartSyncChangePolicyDetails, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            if (smartSyncChangePolicyDetails.newValue != null) {
                jsonGenerator.writeFieldName("new_value");
                StoneSerializers.nullable(com.dropbox.core.p005v2.teampolicies.SmartSyncPolicy.Serializer.INSTANCE).serialize(smartSyncChangePolicyDetails.newValue, jsonGenerator);
            }
            if (smartSyncChangePolicyDetails.previousValue != null) {
                jsonGenerator.writeFieldName("previous_value");
                StoneSerializers.nullable(com.dropbox.core.p005v2.teampolicies.SmartSyncPolicy.Serializer.INSTANCE).serialize(smartSyncChangePolicyDetails.previousValue, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public SmartSyncChangePolicyDetails deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            SmartSyncPolicy smartSyncPolicy = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                SmartSyncPolicy smartSyncPolicy2 = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("new_value".equals(currentName)) {
                        smartSyncPolicy = (SmartSyncPolicy) StoneSerializers.nullable(com.dropbox.core.p005v2.teampolicies.SmartSyncPolicy.Serializer.INSTANCE).deserialize(jsonParser);
                    } else if ("previous_value".equals(currentName)) {
                        smartSyncPolicy2 = (SmartSyncPolicy) StoneSerializers.nullable(com.dropbox.core.p005v2.teampolicies.SmartSyncPolicy.Serializer.INSTANCE).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                SmartSyncChangePolicyDetails smartSyncChangePolicyDetails = new SmartSyncChangePolicyDetails(smartSyncPolicy, smartSyncPolicy2);
                if (!z) {
                    expectEndObject(jsonParser);
                }
                StoneDeserializerLogger.log(smartSyncChangePolicyDetails, smartSyncChangePolicyDetails.toStringMultiline());
                return smartSyncChangePolicyDetails;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public SmartSyncChangePolicyDetails(SmartSyncPolicy smartSyncPolicy, SmartSyncPolicy smartSyncPolicy2) {
        this.newValue = smartSyncPolicy;
        this.previousValue = smartSyncPolicy2;
    }

    public SmartSyncChangePolicyDetails() {
        this(null, null);
    }

    public SmartSyncPolicy getNewValue() {
        return this.newValue;
    }

    public SmartSyncPolicy getPreviousValue() {
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
            com.dropbox.core.v2.teamlog.SmartSyncChangePolicyDetails r5 = (com.dropbox.core.p005v2.teamlog.SmartSyncChangePolicyDetails) r5
            com.dropbox.core.v2.teampolicies.SmartSyncPolicy r2 = r4.newValue
            com.dropbox.core.v2.teampolicies.SmartSyncPolicy r3 = r5.newValue
            if (r2 == r3) goto L_0x0026
            if (r2 == 0) goto L_0x0035
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0035
        L_0x0026:
            com.dropbox.core.v2.teampolicies.SmartSyncPolicy r2 = r4.previousValue
            com.dropbox.core.v2.teampolicies.SmartSyncPolicy r5 = r5.previousValue
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
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.teamlog.SmartSyncChangePolicyDetails.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
