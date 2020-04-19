package com.dropbox.core.p005v2.teamlog;

import com.box.androidsdk.content.BoxConstants;
import com.box.androidsdk.content.models.BoxEnterpriseEvent;
import com.dropbox.core.p005v2.team.MobileClientPlatform;
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
import java.util.Date;

/* renamed from: com.dropbox.core.v2.teamlog.MobileDeviceSessionLogInfo */
public class MobileDeviceSessionLogInfo extends DeviceSessionLogInfo {
    protected final MobileClientPlatform clientType;
    protected final String clientVersion;
    protected final String deviceName;
    protected final String lastCarrier;
    protected final String osVersion;
    protected final MobileSessionLogInfo sessionInfo;

    /* renamed from: com.dropbox.core.v2.teamlog.MobileDeviceSessionLogInfo$Builder */
    public static class Builder extends com.dropbox.core.p005v2.teamlog.DeviceSessionLogInfo.Builder {
        protected final MobileClientPlatform clientType;
        protected String clientVersion;
        protected final String deviceName;
        protected String lastCarrier;
        protected String osVersion;
        protected MobileSessionLogInfo sessionInfo;

        protected Builder(String str, MobileClientPlatform mobileClientPlatform) {
            if (str != null) {
                this.deviceName = str;
                if (mobileClientPlatform != null) {
                    this.clientType = mobileClientPlatform;
                    this.sessionInfo = null;
                    this.clientVersion = null;
                    this.osVersion = null;
                    this.lastCarrier = null;
                    return;
                }
                throw new IllegalArgumentException("Required value for 'clientType' is null");
            }
            throw new IllegalArgumentException("Required value for 'deviceName' is null");
        }

        public Builder withSessionInfo(MobileSessionLogInfo mobileSessionLogInfo) {
            this.sessionInfo = mobileSessionLogInfo;
            return this;
        }

        public Builder withClientVersion(String str) {
            this.clientVersion = str;
            return this;
        }

        public Builder withOsVersion(String str) {
            this.osVersion = str;
            return this;
        }

        public Builder withLastCarrier(String str) {
            this.lastCarrier = str;
            return this;
        }

        public Builder withIpAddress(String str) {
            super.withIpAddress(str);
            return this;
        }

        public Builder withCreated(Date date) {
            super.withCreated(date);
            return this;
        }

        public Builder withUpdated(Date date) {
            super.withUpdated(date);
            return this;
        }

        public MobileDeviceSessionLogInfo build() {
            MobileDeviceSessionLogInfo mobileDeviceSessionLogInfo = new MobileDeviceSessionLogInfo(this.deviceName, this.clientType, this.ipAddress, this.created, this.updated, this.sessionInfo, this.clientVersion, this.osVersion, this.lastCarrier);
            return mobileDeviceSessionLogInfo;
        }
    }

    /* renamed from: com.dropbox.core.v2.teamlog.MobileDeviceSessionLogInfo$Serializer */
    static class Serializer extends StructSerializer<MobileDeviceSessionLogInfo> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(MobileDeviceSessionLogInfo mobileDeviceSessionLogInfo, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            writeTag("mobile_device_session", jsonGenerator);
            jsonGenerator.writeFieldName(BoxConstants.KEY_BOX_DEVICE_NAME);
            StoneSerializers.string().serialize(mobileDeviceSessionLogInfo.deviceName, jsonGenerator);
            jsonGenerator.writeFieldName("client_type");
            com.dropbox.core.p005v2.team.MobileClientPlatform.Serializer.INSTANCE.serialize(mobileDeviceSessionLogInfo.clientType, jsonGenerator);
            if (mobileDeviceSessionLogInfo.ipAddress != null) {
                jsonGenerator.writeFieldName(BoxEnterpriseEvent.FIELD_IP_ADDRESS);
                StoneSerializers.nullable(StoneSerializers.string()).serialize(mobileDeviceSessionLogInfo.ipAddress, jsonGenerator);
            }
            if (mobileDeviceSessionLogInfo.created != null) {
                jsonGenerator.writeFieldName("created");
                StoneSerializers.nullable(StoneSerializers.timestamp()).serialize(mobileDeviceSessionLogInfo.created, jsonGenerator);
            }
            if (mobileDeviceSessionLogInfo.updated != null) {
                jsonGenerator.writeFieldName("updated");
                StoneSerializers.nullable(StoneSerializers.timestamp()).serialize(mobileDeviceSessionLogInfo.updated, jsonGenerator);
            }
            if (mobileDeviceSessionLogInfo.sessionInfo != null) {
                jsonGenerator.writeFieldName("session_info");
                StoneSerializers.nullableStruct(Serializer.INSTANCE).serialize(mobileDeviceSessionLogInfo.sessionInfo, jsonGenerator);
            }
            if (mobileDeviceSessionLogInfo.clientVersion != null) {
                jsonGenerator.writeFieldName("client_version");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(mobileDeviceSessionLogInfo.clientVersion, jsonGenerator);
            }
            if (mobileDeviceSessionLogInfo.osVersion != null) {
                jsonGenerator.writeFieldName("os_version");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(mobileDeviceSessionLogInfo.osVersion, jsonGenerator);
            }
            if (mobileDeviceSessionLogInfo.lastCarrier != null) {
                jsonGenerator.writeFieldName("last_carrier");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(mobileDeviceSessionLogInfo.lastCarrier, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public MobileDeviceSessionLogInfo deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
                if ("mobile_device_session".equals(str)) {
                    str = null;
                }
            } else {
                str = null;
            }
            if (str == null) {
                String str2 = null;
                MobileClientPlatform mobileClientPlatform = null;
                String str3 = null;
                Date date = null;
                Date date2 = null;
                MobileSessionLogInfo mobileSessionLogInfo = null;
                String str4 = null;
                String str5 = null;
                String str6 = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if (BoxConstants.KEY_BOX_DEVICE_NAME.equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("client_type".equals(currentName)) {
                        mobileClientPlatform = com.dropbox.core.p005v2.team.MobileClientPlatform.Serializer.INSTANCE.deserialize(jsonParser);
                    } else if (BoxEnterpriseEvent.FIELD_IP_ADDRESS.equals(currentName)) {
                        str3 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else if ("created".equals(currentName)) {
                        date = (Date) StoneSerializers.nullable(StoneSerializers.timestamp()).deserialize(jsonParser);
                    } else if ("updated".equals(currentName)) {
                        date2 = (Date) StoneSerializers.nullable(StoneSerializers.timestamp()).deserialize(jsonParser);
                    } else if ("session_info".equals(currentName)) {
                        mobileSessionLogInfo = (MobileSessionLogInfo) StoneSerializers.nullableStruct(Serializer.INSTANCE).deserialize(jsonParser);
                    } else if ("client_version".equals(currentName)) {
                        str4 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else if ("os_version".equals(currentName)) {
                        str5 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else if ("last_carrier".equals(currentName)) {
                        str6 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"device_name\" missing.");
                } else if (mobileClientPlatform != null) {
                    MobileDeviceSessionLogInfo mobileDeviceSessionLogInfo = new MobileDeviceSessionLogInfo(str2, mobileClientPlatform, str3, date, date2, mobileSessionLogInfo, str4, str5, str6);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(mobileDeviceSessionLogInfo, mobileDeviceSessionLogInfo.toStringMultiline());
                    return mobileDeviceSessionLogInfo;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"client_type\" missing.");
                }
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("No subtype found that matches tag: \"");
                sb.append(str);
                sb.append("\"");
                throw new JsonParseException(jsonParser, sb.toString());
            }
        }
    }

    public MobileDeviceSessionLogInfo(String str, MobileClientPlatform mobileClientPlatform, String str2, Date date, Date date2, MobileSessionLogInfo mobileSessionLogInfo, String str3, String str4, String str5) {
        super(str2, date, date2);
        this.sessionInfo = mobileSessionLogInfo;
        if (str != null) {
            this.deviceName = str;
            if (mobileClientPlatform != null) {
                this.clientType = mobileClientPlatform;
                this.clientVersion = str3;
                this.osVersion = str4;
                this.lastCarrier = str5;
                return;
            }
            throw new IllegalArgumentException("Required value for 'clientType' is null");
        }
        throw new IllegalArgumentException("Required value for 'deviceName' is null");
    }

    public MobileDeviceSessionLogInfo(String str, MobileClientPlatform mobileClientPlatform) {
        this(str, mobileClientPlatform, null, null, null, null, null, null, null);
    }

    public String getDeviceName() {
        return this.deviceName;
    }

    public MobileClientPlatform getClientType() {
        return this.clientType;
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

    public MobileSessionLogInfo getSessionInfo() {
        return this.sessionInfo;
    }

    public String getClientVersion() {
        return this.clientVersion;
    }

    public String getOsVersion() {
        return this.osVersion;
    }

    public String getLastCarrier() {
        return this.lastCarrier;
    }

    public static Builder newBuilder(String str, MobileClientPlatform mobileClientPlatform) {
        return new Builder(str, mobileClientPlatform);
    }

    public int hashCode() {
        return (super.hashCode() * 31) + Arrays.hashCode(new Object[]{this.sessionInfo, this.deviceName, this.clientType, this.clientVersion, this.osVersion, this.lastCarrier});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:53:0x00a2, code lost:
        if (r2.equals(r5) == false) goto L_0x00a5;
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
            if (r2 == 0) goto L_0x00a7
            com.dropbox.core.v2.teamlog.MobileDeviceSessionLogInfo r5 = (com.dropbox.core.p005v2.teamlog.MobileDeviceSessionLogInfo) r5
            java.lang.String r2 = r4.deviceName
            java.lang.String r3 = r5.deviceName
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00a5
        L_0x0024:
            com.dropbox.core.v2.team.MobileClientPlatform r2 = r4.clientType
            com.dropbox.core.v2.team.MobileClientPlatform r3 = r5.clientType
            if (r2 == r3) goto L_0x0030
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00a5
        L_0x0030:
            java.lang.String r2 = r4.ipAddress
            java.lang.String r3 = r5.ipAddress
            if (r2 == r3) goto L_0x0044
            java.lang.String r2 = r4.ipAddress
            if (r2 == 0) goto L_0x00a5
            java.lang.String r2 = r4.ipAddress
            java.lang.String r3 = r5.ipAddress
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00a5
        L_0x0044:
            java.util.Date r2 = r4.created
            java.util.Date r3 = r5.created
            if (r2 == r3) goto L_0x0058
            java.util.Date r2 = r4.created
            if (r2 == 0) goto L_0x00a5
            java.util.Date r2 = r4.created
            java.util.Date r3 = r5.created
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00a5
        L_0x0058:
            java.util.Date r2 = r4.updated
            java.util.Date r3 = r5.updated
            if (r2 == r3) goto L_0x006c
            java.util.Date r2 = r4.updated
            if (r2 == 0) goto L_0x00a5
            java.util.Date r2 = r4.updated
            java.util.Date r3 = r5.updated
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00a5
        L_0x006c:
            com.dropbox.core.v2.teamlog.MobileSessionLogInfo r2 = r4.sessionInfo
            com.dropbox.core.v2.teamlog.MobileSessionLogInfo r3 = r5.sessionInfo
            if (r2 == r3) goto L_0x007a
            if (r2 == 0) goto L_0x00a5
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00a5
        L_0x007a:
            java.lang.String r2 = r4.clientVersion
            java.lang.String r3 = r5.clientVersion
            if (r2 == r3) goto L_0x0088
            if (r2 == 0) goto L_0x00a5
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00a5
        L_0x0088:
            java.lang.String r2 = r4.osVersion
            java.lang.String r3 = r5.osVersion
            if (r2 == r3) goto L_0x0096
            if (r2 == 0) goto L_0x00a5
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00a5
        L_0x0096:
            java.lang.String r2 = r4.lastCarrier
            java.lang.String r5 = r5.lastCarrier
            if (r2 == r5) goto L_0x00a6
            if (r2 == 0) goto L_0x00a5
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x00a5
            goto L_0x00a6
        L_0x00a5:
            r0 = 0
        L_0x00a6:
            return r0
        L_0x00a7:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.teamlog.MobileDeviceSessionLogInfo.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
