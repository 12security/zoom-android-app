package com.dropbox.core.p005v2.teamlog;

import com.box.androidsdk.content.models.BoxEnterpriseEvent;
import com.dropbox.core.p005v2.team.DesktopPlatform;
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

/* renamed from: com.dropbox.core.v2.teamlog.DesktopDeviceSessionLogInfo */
public class DesktopDeviceSessionLogInfo extends DeviceSessionLogInfo {
    protected final DesktopPlatform clientType;
    protected final String clientVersion;
    protected final String hostName;
    protected final boolean isDeleteOnUnlinkSupported;
    protected final String platform;
    protected final DesktopSessionLogInfo sessionInfo;

    /* renamed from: com.dropbox.core.v2.teamlog.DesktopDeviceSessionLogInfo$Builder */
    public static class Builder extends com.dropbox.core.p005v2.teamlog.DeviceSessionLogInfo.Builder {
        protected final DesktopPlatform clientType;
        protected String clientVersion;
        protected final String hostName;
        protected final boolean isDeleteOnUnlinkSupported;
        protected final String platform;
        protected DesktopSessionLogInfo sessionInfo;

        protected Builder(String str, DesktopPlatform desktopPlatform, String str2, boolean z) {
            if (str != null) {
                this.hostName = str;
                if (desktopPlatform != null) {
                    this.clientType = desktopPlatform;
                    if (str2 != null) {
                        this.platform = str2;
                        this.isDeleteOnUnlinkSupported = z;
                        this.sessionInfo = null;
                        this.clientVersion = null;
                        return;
                    }
                    throw new IllegalArgumentException("Required value for 'platform' is null");
                }
                throw new IllegalArgumentException("Required value for 'clientType' is null");
            }
            throw new IllegalArgumentException("Required value for 'hostName' is null");
        }

        public Builder withSessionInfo(DesktopSessionLogInfo desktopSessionLogInfo) {
            this.sessionInfo = desktopSessionLogInfo;
            return this;
        }

        public Builder withClientVersion(String str) {
            this.clientVersion = str;
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

        public DesktopDeviceSessionLogInfo build() {
            DesktopDeviceSessionLogInfo desktopDeviceSessionLogInfo = new DesktopDeviceSessionLogInfo(this.hostName, this.clientType, this.platform, this.isDeleteOnUnlinkSupported, this.ipAddress, this.created, this.updated, this.sessionInfo, this.clientVersion);
            return desktopDeviceSessionLogInfo;
        }
    }

    /* renamed from: com.dropbox.core.v2.teamlog.DesktopDeviceSessionLogInfo$Serializer */
    static class Serializer extends StructSerializer<DesktopDeviceSessionLogInfo> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(DesktopDeviceSessionLogInfo desktopDeviceSessionLogInfo, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            writeTag("desktop_device_session", jsonGenerator);
            jsonGenerator.writeFieldName("host_name");
            StoneSerializers.string().serialize(desktopDeviceSessionLogInfo.hostName, jsonGenerator);
            jsonGenerator.writeFieldName("client_type");
            com.dropbox.core.p005v2.team.DesktopPlatform.Serializer.INSTANCE.serialize(desktopDeviceSessionLogInfo.clientType, jsonGenerator);
            jsonGenerator.writeFieldName("platform");
            StoneSerializers.string().serialize(desktopDeviceSessionLogInfo.platform, jsonGenerator);
            jsonGenerator.writeFieldName("is_delete_on_unlink_supported");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(desktopDeviceSessionLogInfo.isDeleteOnUnlinkSupported), jsonGenerator);
            if (desktopDeviceSessionLogInfo.ipAddress != null) {
                jsonGenerator.writeFieldName(BoxEnterpriseEvent.FIELD_IP_ADDRESS);
                StoneSerializers.nullable(StoneSerializers.string()).serialize(desktopDeviceSessionLogInfo.ipAddress, jsonGenerator);
            }
            if (desktopDeviceSessionLogInfo.created != null) {
                jsonGenerator.writeFieldName("created");
                StoneSerializers.nullable(StoneSerializers.timestamp()).serialize(desktopDeviceSessionLogInfo.created, jsonGenerator);
            }
            if (desktopDeviceSessionLogInfo.updated != null) {
                jsonGenerator.writeFieldName("updated");
                StoneSerializers.nullable(StoneSerializers.timestamp()).serialize(desktopDeviceSessionLogInfo.updated, jsonGenerator);
            }
            if (desktopDeviceSessionLogInfo.sessionInfo != null) {
                jsonGenerator.writeFieldName("session_info");
                StoneSerializers.nullableStruct(Serializer.INSTANCE).serialize(desktopDeviceSessionLogInfo.sessionInfo, jsonGenerator);
            }
            if (desktopDeviceSessionLogInfo.clientVersion != null) {
                jsonGenerator.writeFieldName("client_version");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(desktopDeviceSessionLogInfo.clientVersion, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public DesktopDeviceSessionLogInfo deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            Boolean bool = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
                if ("desktop_device_session".equals(str)) {
                    str = null;
                }
            } else {
                str = null;
            }
            if (str == null) {
                String str2 = null;
                DesktopPlatform desktopPlatform = null;
                String str3 = null;
                String str4 = null;
                Date date = null;
                Date date2 = null;
                DesktopSessionLogInfo desktopSessionLogInfo = null;
                String str5 = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("host_name".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("client_type".equals(currentName)) {
                        desktopPlatform = com.dropbox.core.p005v2.team.DesktopPlatform.Serializer.INSTANCE.deserialize(jsonParser);
                    } else if ("platform".equals(currentName)) {
                        str3 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("is_delete_on_unlink_supported".equals(currentName)) {
                        bool = (Boolean) StoneSerializers.boolean_().deserialize(jsonParser);
                    } else if (BoxEnterpriseEvent.FIELD_IP_ADDRESS.equals(currentName)) {
                        str4 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else if ("created".equals(currentName)) {
                        date = (Date) StoneSerializers.nullable(StoneSerializers.timestamp()).deserialize(jsonParser);
                    } else if ("updated".equals(currentName)) {
                        date2 = (Date) StoneSerializers.nullable(StoneSerializers.timestamp()).deserialize(jsonParser);
                    } else if ("session_info".equals(currentName)) {
                        desktopSessionLogInfo = (DesktopSessionLogInfo) StoneSerializers.nullableStruct(Serializer.INSTANCE).deserialize(jsonParser);
                    } else if ("client_version".equals(currentName)) {
                        str5 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"host_name\" missing.");
                } else if (desktopPlatform == null) {
                    throw new JsonParseException(jsonParser, "Required field \"client_type\" missing.");
                } else if (str3 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"platform\" missing.");
                } else if (bool != null) {
                    DesktopDeviceSessionLogInfo desktopDeviceSessionLogInfo = new DesktopDeviceSessionLogInfo(str2, desktopPlatform, str3, bool.booleanValue(), str4, date, date2, desktopSessionLogInfo, str5);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(desktopDeviceSessionLogInfo, desktopDeviceSessionLogInfo.toStringMultiline());
                    return desktopDeviceSessionLogInfo;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"is_delete_on_unlink_supported\" missing.");
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

    public DesktopDeviceSessionLogInfo(String str, DesktopPlatform desktopPlatform, String str2, boolean z, String str3, Date date, Date date2, DesktopSessionLogInfo desktopSessionLogInfo, String str4) {
        super(str3, date, date2);
        this.sessionInfo = desktopSessionLogInfo;
        if (str != null) {
            this.hostName = str;
            if (desktopPlatform != null) {
                this.clientType = desktopPlatform;
                this.clientVersion = str4;
                if (str2 != null) {
                    this.platform = str2;
                    this.isDeleteOnUnlinkSupported = z;
                    return;
                }
                throw new IllegalArgumentException("Required value for 'platform' is null");
            }
            throw new IllegalArgumentException("Required value for 'clientType' is null");
        }
        throw new IllegalArgumentException("Required value for 'hostName' is null");
    }

    public DesktopDeviceSessionLogInfo(String str, DesktopPlatform desktopPlatform, String str2, boolean z) {
        this(str, desktopPlatform, str2, z, null, null, null, null, null);
    }

    public String getHostName() {
        return this.hostName;
    }

    public DesktopPlatform getClientType() {
        return this.clientType;
    }

    public String getPlatform() {
        return this.platform;
    }

    public boolean getIsDeleteOnUnlinkSupported() {
        return this.isDeleteOnUnlinkSupported;
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

    public DesktopSessionLogInfo getSessionInfo() {
        return this.sessionInfo;
    }

    public String getClientVersion() {
        return this.clientVersion;
    }

    public static Builder newBuilder(String str, DesktopPlatform desktopPlatform, String str2, boolean z) {
        return new Builder(str, desktopPlatform, str2, z);
    }

    public int hashCode() {
        return (super.hashCode() * 31) + Arrays.hashCode(new Object[]{this.sessionInfo, this.hostName, this.clientType, this.clientVersion, this.platform, Boolean.valueOf(this.isDeleteOnUnlinkSupported)});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:49:0x0098, code lost:
        if (r2.equals(r5) == false) goto L_0x009b;
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
            if (r2 == 0) goto L_0x009d
            com.dropbox.core.v2.teamlog.DesktopDeviceSessionLogInfo r5 = (com.dropbox.core.p005v2.teamlog.DesktopDeviceSessionLogInfo) r5
            java.lang.String r2 = r4.hostName
            java.lang.String r3 = r5.hostName
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x009b
        L_0x0024:
            com.dropbox.core.v2.team.DesktopPlatform r2 = r4.clientType
            com.dropbox.core.v2.team.DesktopPlatform r3 = r5.clientType
            if (r2 == r3) goto L_0x0030
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x009b
        L_0x0030:
            java.lang.String r2 = r4.platform
            java.lang.String r3 = r5.platform
            if (r2 == r3) goto L_0x003c
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x009b
        L_0x003c:
            boolean r2 = r4.isDeleteOnUnlinkSupported
            boolean r3 = r5.isDeleteOnUnlinkSupported
            if (r2 != r3) goto L_0x009b
            java.lang.String r2 = r4.ipAddress
            java.lang.String r3 = r5.ipAddress
            if (r2 == r3) goto L_0x0056
            java.lang.String r2 = r4.ipAddress
            if (r2 == 0) goto L_0x009b
            java.lang.String r2 = r4.ipAddress
            java.lang.String r3 = r5.ipAddress
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x009b
        L_0x0056:
            java.util.Date r2 = r4.created
            java.util.Date r3 = r5.created
            if (r2 == r3) goto L_0x006a
            java.util.Date r2 = r4.created
            if (r2 == 0) goto L_0x009b
            java.util.Date r2 = r4.created
            java.util.Date r3 = r5.created
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x009b
        L_0x006a:
            java.util.Date r2 = r4.updated
            java.util.Date r3 = r5.updated
            if (r2 == r3) goto L_0x007e
            java.util.Date r2 = r4.updated
            if (r2 == 0) goto L_0x009b
            java.util.Date r2 = r4.updated
            java.util.Date r3 = r5.updated
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x009b
        L_0x007e:
            com.dropbox.core.v2.teamlog.DesktopSessionLogInfo r2 = r4.sessionInfo
            com.dropbox.core.v2.teamlog.DesktopSessionLogInfo r3 = r5.sessionInfo
            if (r2 == r3) goto L_0x008c
            if (r2 == 0) goto L_0x009b
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x009b
        L_0x008c:
            java.lang.String r2 = r4.clientVersion
            java.lang.String r5 = r5.clientVersion
            if (r2 == r5) goto L_0x009c
            if (r2 == 0) goto L_0x009b
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x009b
            goto L_0x009c
        L_0x009b:
            r0 = 0
        L_0x009c:
            return r0
        L_0x009d:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.teamlog.DesktopDeviceSessionLogInfo.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
