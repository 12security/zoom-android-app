package com.dropbox.core.p005v2.teamlog;

import com.box.androidsdk.content.models.BoxEnterpriseEvent;
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

/* renamed from: com.dropbox.core.v2.teamlog.DeviceSessionLogInfo */
public class DeviceSessionLogInfo {
    protected final Date created;
    protected final String ipAddress;
    protected final Date updated;

    /* renamed from: com.dropbox.core.v2.teamlog.DeviceSessionLogInfo$Builder */
    public static class Builder {
        protected Date created = null;
        protected String ipAddress = null;
        protected Date updated = null;

        protected Builder() {
        }

        public Builder withIpAddress(String str) {
            this.ipAddress = str;
            return this;
        }

        public Builder withCreated(Date date) {
            this.created = LangUtil.truncateMillis(date);
            return this;
        }

        public Builder withUpdated(Date date) {
            this.updated = LangUtil.truncateMillis(date);
            return this;
        }

        public DeviceSessionLogInfo build() {
            return new DeviceSessionLogInfo(this.ipAddress, this.created, this.updated);
        }
    }

    /* renamed from: com.dropbox.core.v2.teamlog.DeviceSessionLogInfo$Serializer */
    static class Serializer extends StructSerializer<DeviceSessionLogInfo> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(DeviceSessionLogInfo deviceSessionLogInfo, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (deviceSessionLogInfo instanceof DesktopDeviceSessionLogInfo) {
                Serializer.INSTANCE.serialize((DesktopDeviceSessionLogInfo) deviceSessionLogInfo, jsonGenerator, z);
            } else if (deviceSessionLogInfo instanceof MobileDeviceSessionLogInfo) {
                Serializer.INSTANCE.serialize((MobileDeviceSessionLogInfo) deviceSessionLogInfo, jsonGenerator, z);
            } else if (deviceSessionLogInfo instanceof WebDeviceSessionLogInfo) {
                Serializer.INSTANCE.serialize((WebDeviceSessionLogInfo) deviceSessionLogInfo, jsonGenerator, z);
            } else if (deviceSessionLogInfo instanceof LegacyDeviceSessionLogInfo) {
                Serializer.INSTANCE.serialize((LegacyDeviceSessionLogInfo) deviceSessionLogInfo, jsonGenerator, z);
            } else {
                if (!z) {
                    jsonGenerator.writeStartObject();
                }
                if (deviceSessionLogInfo.ipAddress != null) {
                    jsonGenerator.writeFieldName(BoxEnterpriseEvent.FIELD_IP_ADDRESS);
                    StoneSerializers.nullable(StoneSerializers.string()).serialize(deviceSessionLogInfo.ipAddress, jsonGenerator);
                }
                if (deviceSessionLogInfo.created != null) {
                    jsonGenerator.writeFieldName("created");
                    StoneSerializers.nullable(StoneSerializers.timestamp()).serialize(deviceSessionLogInfo.created, jsonGenerator);
                }
                if (deviceSessionLogInfo.updated != null) {
                    jsonGenerator.writeFieldName("updated");
                    StoneSerializers.nullable(StoneSerializers.timestamp()).serialize(deviceSessionLogInfo.updated, jsonGenerator);
                }
                if (!z) {
                    jsonGenerator.writeEndObject();
                }
            }
        }

        public DeviceSessionLogInfo deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            DeviceSessionLogInfo deviceSessionLogInfo;
            String str2 = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
                if ("".equals(str)) {
                    str = null;
                }
            } else {
                str = null;
            }
            if (str == null) {
                Date date = null;
                Date date2 = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if (BoxEnterpriseEvent.FIELD_IP_ADDRESS.equals(currentName)) {
                        str2 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else if ("created".equals(currentName)) {
                        date = (Date) StoneSerializers.nullable(StoneSerializers.timestamp()).deserialize(jsonParser);
                    } else if ("updated".equals(currentName)) {
                        date2 = (Date) StoneSerializers.nullable(StoneSerializers.timestamp()).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                deviceSessionLogInfo = new DeviceSessionLogInfo(str2, date, date2);
            } else if ("".equals(str)) {
                deviceSessionLogInfo = INSTANCE.deserialize(jsonParser, true);
            } else if ("desktop_device_session".equals(str)) {
                deviceSessionLogInfo = Serializer.INSTANCE.deserialize(jsonParser, true);
            } else if ("mobile_device_session".equals(str)) {
                deviceSessionLogInfo = Serializer.INSTANCE.deserialize(jsonParser, true);
            } else if ("web_device_session".equals(str)) {
                deviceSessionLogInfo = Serializer.INSTANCE.deserialize(jsonParser, true);
            } else if ("legacy_device_session".equals(str)) {
                deviceSessionLogInfo = Serializer.INSTANCE.deserialize(jsonParser, true);
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("No subtype found that matches tag: \"");
                sb.append(str);
                sb.append("\"");
                throw new JsonParseException(jsonParser, sb.toString());
            }
            if (!z) {
                expectEndObject(jsonParser);
            }
            StoneDeserializerLogger.log(deviceSessionLogInfo, deviceSessionLogInfo.toStringMultiline());
            return deviceSessionLogInfo;
        }
    }

    public DeviceSessionLogInfo(String str, Date date, Date date2) {
        this.ipAddress = str;
        this.created = LangUtil.truncateMillis(date);
        this.updated = LangUtil.truncateMillis(date2);
    }

    public DeviceSessionLogInfo() {
        this(null, null, null);
    }

    public String getIpAddress() {
        return this.ipAddress;
    }

    public Date getCreated() {
        return this.created;
    }

    public Date getUpdated() {
        return this.updated;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.ipAddress, this.created, this.updated});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0040, code lost:
        if (r2.equals(r5) == false) goto L_0x0043;
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
            if (r2 == 0) goto L_0x0045
            com.dropbox.core.v2.teamlog.DeviceSessionLogInfo r5 = (com.dropbox.core.p005v2.teamlog.DeviceSessionLogInfo) r5
            java.lang.String r2 = r4.ipAddress
            java.lang.String r3 = r5.ipAddress
            if (r2 == r3) goto L_0x0026
            if (r2 == 0) goto L_0x0043
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0043
        L_0x0026:
            java.util.Date r2 = r4.created
            java.util.Date r3 = r5.created
            if (r2 == r3) goto L_0x0034
            if (r2 == 0) goto L_0x0043
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0043
        L_0x0034:
            java.util.Date r2 = r4.updated
            java.util.Date r5 = r5.updated
            if (r2 == r5) goto L_0x0044
            if (r2 == 0) goto L_0x0043
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x0043
            goto L_0x0044
        L_0x0043:
            r0 = 0
        L_0x0044:
            return r0
        L_0x0045:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.teamlog.DeviceSessionLogInfo.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
