package com.dropbox.core.p005v2.teamlog;

import com.dropbox.core.stone.StoneDeserializerLogger;
import com.dropbox.core.stone.StructSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.teamlog.DeviceChangeIpDesktopDetails */
public class DeviceChangeIpDesktopDetails {
    protected final DeviceSessionLogInfo deviceSessionInfo;

    /* renamed from: com.dropbox.core.v2.teamlog.DeviceChangeIpDesktopDetails$Serializer */
    static class Serializer extends StructSerializer<DeviceChangeIpDesktopDetails> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(DeviceChangeIpDesktopDetails deviceChangeIpDesktopDetails, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("device_session_info");
            Serializer.INSTANCE.serialize(deviceChangeIpDesktopDetails.deviceSessionInfo, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public DeviceChangeIpDesktopDetails deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            DeviceSessionLogInfo deviceSessionLogInfo = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("device_session_info".equals(currentName)) {
                        deviceSessionLogInfo = (DeviceSessionLogInfo) Serializer.INSTANCE.deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (deviceSessionLogInfo != null) {
                    DeviceChangeIpDesktopDetails deviceChangeIpDesktopDetails = new DeviceChangeIpDesktopDetails(deviceSessionLogInfo);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(deviceChangeIpDesktopDetails, deviceChangeIpDesktopDetails.toStringMultiline());
                    return deviceChangeIpDesktopDetails;
                }
                throw new JsonParseException(jsonParser, "Required field \"device_session_info\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public DeviceChangeIpDesktopDetails(DeviceSessionLogInfo deviceSessionLogInfo) {
        if (deviceSessionLogInfo != null) {
            this.deviceSessionInfo = deviceSessionLogInfo;
            return;
        }
        throw new IllegalArgumentException("Required value for 'deviceSessionInfo' is null");
    }

    public DeviceSessionLogInfo getDeviceSessionInfo() {
        return this.deviceSessionInfo;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.deviceSessionInfo});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        DeviceChangeIpDesktopDetails deviceChangeIpDesktopDetails = (DeviceChangeIpDesktopDetails) obj;
        DeviceSessionLogInfo deviceSessionLogInfo = this.deviceSessionInfo;
        DeviceSessionLogInfo deviceSessionLogInfo2 = deviceChangeIpDesktopDetails.deviceSessionInfo;
        if (deviceSessionLogInfo != deviceSessionLogInfo2 && !deviceSessionLogInfo.equals(deviceSessionLogInfo2)) {
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
