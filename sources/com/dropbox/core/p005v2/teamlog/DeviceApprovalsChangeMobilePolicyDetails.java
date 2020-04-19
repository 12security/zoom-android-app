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

/* renamed from: com.dropbox.core.v2.teamlog.DeviceApprovalsChangeMobilePolicyDetails */
public class DeviceApprovalsChangeMobilePolicyDetails {
    protected final DeviceApprovalsPolicy newValue;
    protected final DeviceApprovalsPolicy previousValue;

    /* renamed from: com.dropbox.core.v2.teamlog.DeviceApprovalsChangeMobilePolicyDetails$Builder */
    public static class Builder {
        protected DeviceApprovalsPolicy newValue = null;
        protected DeviceApprovalsPolicy previousValue = null;

        protected Builder() {
        }

        public Builder withNewValue(DeviceApprovalsPolicy deviceApprovalsPolicy) {
            this.newValue = deviceApprovalsPolicy;
            return this;
        }

        public Builder withPreviousValue(DeviceApprovalsPolicy deviceApprovalsPolicy) {
            this.previousValue = deviceApprovalsPolicy;
            return this;
        }

        public DeviceApprovalsChangeMobilePolicyDetails build() {
            return new DeviceApprovalsChangeMobilePolicyDetails(this.newValue, this.previousValue);
        }
    }

    /* renamed from: com.dropbox.core.v2.teamlog.DeviceApprovalsChangeMobilePolicyDetails$Serializer */
    static class Serializer extends StructSerializer<DeviceApprovalsChangeMobilePolicyDetails> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(DeviceApprovalsChangeMobilePolicyDetails deviceApprovalsChangeMobilePolicyDetails, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            if (deviceApprovalsChangeMobilePolicyDetails.newValue != null) {
                jsonGenerator.writeFieldName("new_value");
                StoneSerializers.nullable(Serializer.INSTANCE).serialize(deviceApprovalsChangeMobilePolicyDetails.newValue, jsonGenerator);
            }
            if (deviceApprovalsChangeMobilePolicyDetails.previousValue != null) {
                jsonGenerator.writeFieldName("previous_value");
                StoneSerializers.nullable(Serializer.INSTANCE).serialize(deviceApprovalsChangeMobilePolicyDetails.previousValue, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public DeviceApprovalsChangeMobilePolicyDetails deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            DeviceApprovalsPolicy deviceApprovalsPolicy = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                DeviceApprovalsPolicy deviceApprovalsPolicy2 = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("new_value".equals(currentName)) {
                        deviceApprovalsPolicy = (DeviceApprovalsPolicy) StoneSerializers.nullable(Serializer.INSTANCE).deserialize(jsonParser);
                    } else if ("previous_value".equals(currentName)) {
                        deviceApprovalsPolicy2 = (DeviceApprovalsPolicy) StoneSerializers.nullable(Serializer.INSTANCE).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                DeviceApprovalsChangeMobilePolicyDetails deviceApprovalsChangeMobilePolicyDetails = new DeviceApprovalsChangeMobilePolicyDetails(deviceApprovalsPolicy, deviceApprovalsPolicy2);
                if (!z) {
                    expectEndObject(jsonParser);
                }
                StoneDeserializerLogger.log(deviceApprovalsChangeMobilePolicyDetails, deviceApprovalsChangeMobilePolicyDetails.toStringMultiline());
                return deviceApprovalsChangeMobilePolicyDetails;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public DeviceApprovalsChangeMobilePolicyDetails(DeviceApprovalsPolicy deviceApprovalsPolicy, DeviceApprovalsPolicy deviceApprovalsPolicy2) {
        this.newValue = deviceApprovalsPolicy;
        this.previousValue = deviceApprovalsPolicy2;
    }

    public DeviceApprovalsChangeMobilePolicyDetails() {
        this(null, null);
    }

    public DeviceApprovalsPolicy getNewValue() {
        return this.newValue;
    }

    public DeviceApprovalsPolicy getPreviousValue() {
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
            com.dropbox.core.v2.teamlog.DeviceApprovalsChangeMobilePolicyDetails r5 = (com.dropbox.core.p005v2.teamlog.DeviceApprovalsChangeMobilePolicyDetails) r5
            com.dropbox.core.v2.teamlog.DeviceApprovalsPolicy r2 = r4.newValue
            com.dropbox.core.v2.teamlog.DeviceApprovalsPolicy r3 = r5.newValue
            if (r2 == r3) goto L_0x0026
            if (r2 == 0) goto L_0x0035
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0035
        L_0x0026:
            com.dropbox.core.v2.teamlog.DeviceApprovalsPolicy r2 = r4.previousValue
            com.dropbox.core.v2.teamlog.DeviceApprovalsPolicy r5 = r5.previousValue
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
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.teamlog.DeviceApprovalsChangeMobilePolicyDetails.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
