package com.dropbox.core.p005v2.teamlog;

import com.dropbox.core.p005v2.teampolicies.RolloutMethod;
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

/* renamed from: com.dropbox.core.v2.teamlog.DeviceApprovalsChangeOverageActionDetails */
public class DeviceApprovalsChangeOverageActionDetails {
    protected final RolloutMethod newValue;
    protected final RolloutMethod previousValue;

    /* renamed from: com.dropbox.core.v2.teamlog.DeviceApprovalsChangeOverageActionDetails$Builder */
    public static class Builder {
        protected RolloutMethod newValue = null;
        protected RolloutMethod previousValue = null;

        protected Builder() {
        }

        public Builder withNewValue(RolloutMethod rolloutMethod) {
            this.newValue = rolloutMethod;
            return this;
        }

        public Builder withPreviousValue(RolloutMethod rolloutMethod) {
            this.previousValue = rolloutMethod;
            return this;
        }

        public DeviceApprovalsChangeOverageActionDetails build() {
            return new DeviceApprovalsChangeOverageActionDetails(this.newValue, this.previousValue);
        }
    }

    /* renamed from: com.dropbox.core.v2.teamlog.DeviceApprovalsChangeOverageActionDetails$Serializer */
    static class Serializer extends StructSerializer<DeviceApprovalsChangeOverageActionDetails> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(DeviceApprovalsChangeOverageActionDetails deviceApprovalsChangeOverageActionDetails, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            if (deviceApprovalsChangeOverageActionDetails.newValue != null) {
                jsonGenerator.writeFieldName("new_value");
                StoneSerializers.nullable(com.dropbox.core.p005v2.teampolicies.RolloutMethod.Serializer.INSTANCE).serialize(deviceApprovalsChangeOverageActionDetails.newValue, jsonGenerator);
            }
            if (deviceApprovalsChangeOverageActionDetails.previousValue != null) {
                jsonGenerator.writeFieldName("previous_value");
                StoneSerializers.nullable(com.dropbox.core.p005v2.teampolicies.RolloutMethod.Serializer.INSTANCE).serialize(deviceApprovalsChangeOverageActionDetails.previousValue, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public DeviceApprovalsChangeOverageActionDetails deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            RolloutMethod rolloutMethod = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                RolloutMethod rolloutMethod2 = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("new_value".equals(currentName)) {
                        rolloutMethod = (RolloutMethod) StoneSerializers.nullable(com.dropbox.core.p005v2.teampolicies.RolloutMethod.Serializer.INSTANCE).deserialize(jsonParser);
                    } else if ("previous_value".equals(currentName)) {
                        rolloutMethod2 = (RolloutMethod) StoneSerializers.nullable(com.dropbox.core.p005v2.teampolicies.RolloutMethod.Serializer.INSTANCE).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                DeviceApprovalsChangeOverageActionDetails deviceApprovalsChangeOverageActionDetails = new DeviceApprovalsChangeOverageActionDetails(rolloutMethod, rolloutMethod2);
                if (!z) {
                    expectEndObject(jsonParser);
                }
                StoneDeserializerLogger.log(deviceApprovalsChangeOverageActionDetails, deviceApprovalsChangeOverageActionDetails.toStringMultiline());
                return deviceApprovalsChangeOverageActionDetails;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public DeviceApprovalsChangeOverageActionDetails(RolloutMethod rolloutMethod, RolloutMethod rolloutMethod2) {
        this.newValue = rolloutMethod;
        this.previousValue = rolloutMethod2;
    }

    public DeviceApprovalsChangeOverageActionDetails() {
        this(null, null);
    }

    public RolloutMethod getNewValue() {
        return this.newValue;
    }

    public RolloutMethod getPreviousValue() {
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
            com.dropbox.core.v2.teamlog.DeviceApprovalsChangeOverageActionDetails r5 = (com.dropbox.core.p005v2.teamlog.DeviceApprovalsChangeOverageActionDetails) r5
            com.dropbox.core.v2.teampolicies.RolloutMethod r2 = r4.newValue
            com.dropbox.core.v2.teampolicies.RolloutMethod r3 = r5.newValue
            if (r2 == r3) goto L_0x0026
            if (r2 == 0) goto L_0x0035
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0035
        L_0x0026:
            com.dropbox.core.v2.teampolicies.RolloutMethod r2 = r4.previousValue
            com.dropbox.core.v2.teampolicies.RolloutMethod r5 = r5.previousValue
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
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.teamlog.DeviceApprovalsChangeOverageActionDetails.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
