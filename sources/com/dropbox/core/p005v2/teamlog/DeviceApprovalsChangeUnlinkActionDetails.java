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

/* renamed from: com.dropbox.core.v2.teamlog.DeviceApprovalsChangeUnlinkActionDetails */
public class DeviceApprovalsChangeUnlinkActionDetails {
    protected final DeviceUnlinkPolicy newValue;
    protected final DeviceUnlinkPolicy previousValue;

    /* renamed from: com.dropbox.core.v2.teamlog.DeviceApprovalsChangeUnlinkActionDetails$Builder */
    public static class Builder {
        protected DeviceUnlinkPolicy newValue = null;
        protected DeviceUnlinkPolicy previousValue = null;

        protected Builder() {
        }

        public Builder withNewValue(DeviceUnlinkPolicy deviceUnlinkPolicy) {
            this.newValue = deviceUnlinkPolicy;
            return this;
        }

        public Builder withPreviousValue(DeviceUnlinkPolicy deviceUnlinkPolicy) {
            this.previousValue = deviceUnlinkPolicy;
            return this;
        }

        public DeviceApprovalsChangeUnlinkActionDetails build() {
            return new DeviceApprovalsChangeUnlinkActionDetails(this.newValue, this.previousValue);
        }
    }

    /* renamed from: com.dropbox.core.v2.teamlog.DeviceApprovalsChangeUnlinkActionDetails$Serializer */
    static class Serializer extends StructSerializer<DeviceApprovalsChangeUnlinkActionDetails> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(DeviceApprovalsChangeUnlinkActionDetails deviceApprovalsChangeUnlinkActionDetails, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            if (deviceApprovalsChangeUnlinkActionDetails.newValue != null) {
                jsonGenerator.writeFieldName("new_value");
                StoneSerializers.nullable(Serializer.INSTANCE).serialize(deviceApprovalsChangeUnlinkActionDetails.newValue, jsonGenerator);
            }
            if (deviceApprovalsChangeUnlinkActionDetails.previousValue != null) {
                jsonGenerator.writeFieldName("previous_value");
                StoneSerializers.nullable(Serializer.INSTANCE).serialize(deviceApprovalsChangeUnlinkActionDetails.previousValue, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public DeviceApprovalsChangeUnlinkActionDetails deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            DeviceUnlinkPolicy deviceUnlinkPolicy = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                DeviceUnlinkPolicy deviceUnlinkPolicy2 = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("new_value".equals(currentName)) {
                        deviceUnlinkPolicy = (DeviceUnlinkPolicy) StoneSerializers.nullable(Serializer.INSTANCE).deserialize(jsonParser);
                    } else if ("previous_value".equals(currentName)) {
                        deviceUnlinkPolicy2 = (DeviceUnlinkPolicy) StoneSerializers.nullable(Serializer.INSTANCE).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                DeviceApprovalsChangeUnlinkActionDetails deviceApprovalsChangeUnlinkActionDetails = new DeviceApprovalsChangeUnlinkActionDetails(deviceUnlinkPolicy, deviceUnlinkPolicy2);
                if (!z) {
                    expectEndObject(jsonParser);
                }
                StoneDeserializerLogger.log(deviceApprovalsChangeUnlinkActionDetails, deviceApprovalsChangeUnlinkActionDetails.toStringMultiline());
                return deviceApprovalsChangeUnlinkActionDetails;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public DeviceApprovalsChangeUnlinkActionDetails(DeviceUnlinkPolicy deviceUnlinkPolicy, DeviceUnlinkPolicy deviceUnlinkPolicy2) {
        this.newValue = deviceUnlinkPolicy;
        this.previousValue = deviceUnlinkPolicy2;
    }

    public DeviceApprovalsChangeUnlinkActionDetails() {
        this(null, null);
    }

    public DeviceUnlinkPolicy getNewValue() {
        return this.newValue;
    }

    public DeviceUnlinkPolicy getPreviousValue() {
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
            com.dropbox.core.v2.teamlog.DeviceApprovalsChangeUnlinkActionDetails r5 = (com.dropbox.core.p005v2.teamlog.DeviceApprovalsChangeUnlinkActionDetails) r5
            com.dropbox.core.v2.teamlog.DeviceUnlinkPolicy r2 = r4.newValue
            com.dropbox.core.v2.teamlog.DeviceUnlinkPolicy r3 = r5.newValue
            if (r2 == r3) goto L_0x0026
            if (r2 == 0) goto L_0x0035
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0035
        L_0x0026:
            com.dropbox.core.v2.teamlog.DeviceUnlinkPolicy r2 = r4.previousValue
            com.dropbox.core.v2.teamlog.DeviceUnlinkPolicy r5 = r5.previousValue
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
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.teamlog.DeviceApprovalsChangeUnlinkActionDetails.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
