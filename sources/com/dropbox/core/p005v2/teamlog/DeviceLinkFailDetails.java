package com.dropbox.core.p005v2.teamlog;

import com.box.androidsdk.content.models.BoxEnterpriseEvent;
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

/* renamed from: com.dropbox.core.v2.teamlog.DeviceLinkFailDetails */
public class DeviceLinkFailDetails {
    protected final DeviceType deviceType;
    protected final String ipAddress;

    /* renamed from: com.dropbox.core.v2.teamlog.DeviceLinkFailDetails$Serializer */
    static class Serializer extends StructSerializer<DeviceLinkFailDetails> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(DeviceLinkFailDetails deviceLinkFailDetails, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("device_type");
            Serializer.INSTANCE.serialize(deviceLinkFailDetails.deviceType, jsonGenerator);
            if (deviceLinkFailDetails.ipAddress != null) {
                jsonGenerator.writeFieldName(BoxEnterpriseEvent.FIELD_IP_ADDRESS);
                StoneSerializers.nullable(StoneSerializers.string()).serialize(deviceLinkFailDetails.ipAddress, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public DeviceLinkFailDetails deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            DeviceType deviceType = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                String str2 = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("device_type".equals(currentName)) {
                        deviceType = Serializer.INSTANCE.deserialize(jsonParser);
                    } else if (BoxEnterpriseEvent.FIELD_IP_ADDRESS.equals(currentName)) {
                        str2 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (deviceType != null) {
                    DeviceLinkFailDetails deviceLinkFailDetails = new DeviceLinkFailDetails(deviceType, str2);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(deviceLinkFailDetails, deviceLinkFailDetails.toStringMultiline());
                    return deviceLinkFailDetails;
                }
                throw new JsonParseException(jsonParser, "Required field \"device_type\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public DeviceLinkFailDetails(DeviceType deviceType2, String str) {
        this.ipAddress = str;
        if (deviceType2 != null) {
            this.deviceType = deviceType2;
            return;
        }
        throw new IllegalArgumentException("Required value for 'deviceType' is null");
    }

    public DeviceLinkFailDetails(DeviceType deviceType2) {
        this(deviceType2, null);
    }

    public DeviceType getDeviceType() {
        return this.deviceType;
    }

    public String getIpAddress() {
        return this.ipAddress;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.ipAddress, this.deviceType});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0030, code lost:
        if (r2.equals(r5) == false) goto L_0x0033;
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
            if (r2 == 0) goto L_0x0035
            com.dropbox.core.v2.teamlog.DeviceLinkFailDetails r5 = (com.dropbox.core.p005v2.teamlog.DeviceLinkFailDetails) r5
            com.dropbox.core.v2.teamlog.DeviceType r2 = r4.deviceType
            com.dropbox.core.v2.teamlog.DeviceType r3 = r5.deviceType
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0033
        L_0x0024:
            java.lang.String r2 = r4.ipAddress
            java.lang.String r5 = r5.ipAddress
            if (r2 == r5) goto L_0x0034
            if (r2 == 0) goto L_0x0033
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x0033
            goto L_0x0034
        L_0x0033:
            r0 = 0
        L_0x0034:
            return r0
        L_0x0035:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.teamlog.DeviceLinkFailDetails.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
