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
import java.util.Date;

/* renamed from: com.dropbox.core.v2.teamlog.LegacyDeviceSessionLogInfo */
public class LegacyDeviceSessionLogInfo extends DeviceSessionLogInfo {
    protected final String clientVersion;
    protected final String deviceType;
    protected final String displayName;
    protected final Boolean isEmmManaged;
    protected final String legacyUniqId;
    protected final String macAddress;
    protected final String osVersion;
    protected final String platform;
    protected final SessionLogInfo sessionInfo;

    /* renamed from: com.dropbox.core.v2.teamlog.LegacyDeviceSessionLogInfo$Builder */
    public static class Builder extends com.dropbox.core.p005v2.teamlog.DeviceSessionLogInfo.Builder {
        protected String clientVersion = null;
        protected String deviceType = null;
        protected String displayName = null;
        protected Boolean isEmmManaged = null;
        protected String legacyUniqId = null;
        protected String macAddress = null;
        protected String osVersion = null;
        protected String platform = null;
        protected SessionLogInfo sessionInfo = null;

        protected Builder() {
        }

        public Builder withSessionInfo(SessionLogInfo sessionLogInfo) {
            this.sessionInfo = sessionLogInfo;
            return this;
        }

        public Builder withDisplayName(String str) {
            this.displayName = str;
            return this;
        }

        public Builder withIsEmmManaged(Boolean bool) {
            this.isEmmManaged = bool;
            return this;
        }

        public Builder withPlatform(String str) {
            this.platform = str;
            return this;
        }

        public Builder withMacAddress(String str) {
            this.macAddress = str;
            return this;
        }

        public Builder withOsVersion(String str) {
            this.osVersion = str;
            return this;
        }

        public Builder withDeviceType(String str) {
            this.deviceType = str;
            return this;
        }

        public Builder withClientVersion(String str) {
            this.clientVersion = str;
            return this;
        }

        public Builder withLegacyUniqId(String str) {
            this.legacyUniqId = str;
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

        public LegacyDeviceSessionLogInfo build() {
            LegacyDeviceSessionLogInfo legacyDeviceSessionLogInfo = new LegacyDeviceSessionLogInfo(this.ipAddress, this.created, this.updated, this.sessionInfo, this.displayName, this.isEmmManaged, this.platform, this.macAddress, this.osVersion, this.deviceType, this.clientVersion, this.legacyUniqId);
            return legacyDeviceSessionLogInfo;
        }
    }

    /* renamed from: com.dropbox.core.v2.teamlog.LegacyDeviceSessionLogInfo$Serializer */
    static class Serializer extends StructSerializer<LegacyDeviceSessionLogInfo> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(LegacyDeviceSessionLogInfo legacyDeviceSessionLogInfo, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            writeTag("legacy_device_session", jsonGenerator);
            if (legacyDeviceSessionLogInfo.ipAddress != null) {
                jsonGenerator.writeFieldName(BoxEnterpriseEvent.FIELD_IP_ADDRESS);
                StoneSerializers.nullable(StoneSerializers.string()).serialize(legacyDeviceSessionLogInfo.ipAddress, jsonGenerator);
            }
            if (legacyDeviceSessionLogInfo.created != null) {
                jsonGenerator.writeFieldName("created");
                StoneSerializers.nullable(StoneSerializers.timestamp()).serialize(legacyDeviceSessionLogInfo.created, jsonGenerator);
            }
            if (legacyDeviceSessionLogInfo.updated != null) {
                jsonGenerator.writeFieldName("updated");
                StoneSerializers.nullable(StoneSerializers.timestamp()).serialize(legacyDeviceSessionLogInfo.updated, jsonGenerator);
            }
            if (legacyDeviceSessionLogInfo.sessionInfo != null) {
                jsonGenerator.writeFieldName("session_info");
                StoneSerializers.nullableStruct(Serializer.INSTANCE).serialize(legacyDeviceSessionLogInfo.sessionInfo, jsonGenerator);
            }
            if (legacyDeviceSessionLogInfo.displayName != null) {
                jsonGenerator.writeFieldName("display_name");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(legacyDeviceSessionLogInfo.displayName, jsonGenerator);
            }
            if (legacyDeviceSessionLogInfo.isEmmManaged != null) {
                jsonGenerator.writeFieldName("is_emm_managed");
                StoneSerializers.nullable(StoneSerializers.boolean_()).serialize(legacyDeviceSessionLogInfo.isEmmManaged, jsonGenerator);
            }
            if (legacyDeviceSessionLogInfo.platform != null) {
                jsonGenerator.writeFieldName("platform");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(legacyDeviceSessionLogInfo.platform, jsonGenerator);
            }
            if (legacyDeviceSessionLogInfo.macAddress != null) {
                jsonGenerator.writeFieldName("mac_address");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(legacyDeviceSessionLogInfo.macAddress, jsonGenerator);
            }
            if (legacyDeviceSessionLogInfo.osVersion != null) {
                jsonGenerator.writeFieldName("os_version");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(legacyDeviceSessionLogInfo.osVersion, jsonGenerator);
            }
            if (legacyDeviceSessionLogInfo.deviceType != null) {
                jsonGenerator.writeFieldName("device_type");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(legacyDeviceSessionLogInfo.deviceType, jsonGenerator);
            }
            if (legacyDeviceSessionLogInfo.clientVersion != null) {
                jsonGenerator.writeFieldName("client_version");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(legacyDeviceSessionLogInfo.clientVersion, jsonGenerator);
            }
            if (legacyDeviceSessionLogInfo.legacyUniqId != null) {
                jsonGenerator.writeFieldName("legacy_uniq_id");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(legacyDeviceSessionLogInfo.legacyUniqId, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public LegacyDeviceSessionLogInfo deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            JsonParser jsonParser2 = jsonParser;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
                if ("legacy_device_session".equals(str)) {
                    str = null;
                }
            } else {
                str = null;
            }
            if (str == null) {
                String str2 = null;
                Date date = null;
                Date date2 = null;
                SessionLogInfo sessionLogInfo = null;
                String str3 = null;
                Boolean bool = null;
                String str4 = null;
                String str5 = null;
                String str6 = null;
                String str7 = null;
                String str8 = null;
                String str9 = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if (BoxEnterpriseEvent.FIELD_IP_ADDRESS.equals(currentName)) {
                        str2 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser2);
                    } else if ("created".equals(currentName)) {
                        date = (Date) StoneSerializers.nullable(StoneSerializers.timestamp()).deserialize(jsonParser2);
                    } else if ("updated".equals(currentName)) {
                        date2 = (Date) StoneSerializers.nullable(StoneSerializers.timestamp()).deserialize(jsonParser2);
                    } else if ("session_info".equals(currentName)) {
                        sessionLogInfo = (SessionLogInfo) StoneSerializers.nullableStruct(Serializer.INSTANCE).deserialize(jsonParser2);
                    } else if ("display_name".equals(currentName)) {
                        str3 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser2);
                    } else if ("is_emm_managed".equals(currentName)) {
                        bool = (Boolean) StoneSerializers.nullable(StoneSerializers.boolean_()).deserialize(jsonParser2);
                    } else if ("platform".equals(currentName)) {
                        str4 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser2);
                    } else if ("mac_address".equals(currentName)) {
                        str5 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser2);
                    } else if ("os_version".equals(currentName)) {
                        str6 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser2);
                    } else if ("device_type".equals(currentName)) {
                        str7 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser2);
                    } else if ("client_version".equals(currentName)) {
                        str8 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser2);
                    } else if ("legacy_uniq_id".equals(currentName)) {
                        str9 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser2);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                LegacyDeviceSessionLogInfo legacyDeviceSessionLogInfo = new LegacyDeviceSessionLogInfo(str2, date, date2, sessionLogInfo, str3, bool, str4, str5, str6, str7, str8, str9);
                if (!z) {
                    expectEndObject(jsonParser);
                }
                StoneDeserializerLogger.log(legacyDeviceSessionLogInfo, legacyDeviceSessionLogInfo.toStringMultiline());
                return legacyDeviceSessionLogInfo;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser2, sb.toString());
        }
    }

    public LegacyDeviceSessionLogInfo(String str, Date date, Date date2, SessionLogInfo sessionLogInfo, String str2, Boolean bool, String str3, String str4, String str5, String str6, String str7, String str8) {
        super(str, date, date2);
        this.sessionInfo = sessionLogInfo;
        this.displayName = str2;
        this.isEmmManaged = bool;
        this.platform = str3;
        this.macAddress = str4;
        this.osVersion = str5;
        this.deviceType = str6;
        this.clientVersion = str7;
        this.legacyUniqId = str8;
    }

    public LegacyDeviceSessionLogInfo() {
        this(null, null, null, null, null, null, null, null, null, null, null, null);
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

    public SessionLogInfo getSessionInfo() {
        return this.sessionInfo;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public Boolean getIsEmmManaged() {
        return this.isEmmManaged;
    }

    public String getPlatform() {
        return this.platform;
    }

    public String getMacAddress() {
        return this.macAddress;
    }

    public String getOsVersion() {
        return this.osVersion;
    }

    public String getDeviceType() {
        return this.deviceType;
    }

    public String getClientVersion() {
        return this.clientVersion;
    }

    public String getLegacyUniqId() {
        return this.legacyUniqId;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public int hashCode() {
        return (super.hashCode() * 31) + Arrays.hashCode(new Object[]{this.sessionInfo, this.displayName, this.isEmmManaged, this.platform, this.macAddress, this.osVersion, this.deviceType, this.clientVersion, this.legacyUniqId});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:70:0x00d0, code lost:
        if (r2.equals(r5) == false) goto L_0x00d3;
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
            if (r2 == 0) goto L_0x00d5
            com.dropbox.core.v2.teamlog.LegacyDeviceSessionLogInfo r5 = (com.dropbox.core.p005v2.teamlog.LegacyDeviceSessionLogInfo) r5
            java.lang.String r2 = r4.ipAddress
            java.lang.String r3 = r5.ipAddress
            if (r2 == r3) goto L_0x002c
            java.lang.String r2 = r4.ipAddress
            if (r2 == 0) goto L_0x00d3
            java.lang.String r2 = r4.ipAddress
            java.lang.String r3 = r5.ipAddress
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00d3
        L_0x002c:
            java.util.Date r2 = r4.created
            java.util.Date r3 = r5.created
            if (r2 == r3) goto L_0x0040
            java.util.Date r2 = r4.created
            if (r2 == 0) goto L_0x00d3
            java.util.Date r2 = r4.created
            java.util.Date r3 = r5.created
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00d3
        L_0x0040:
            java.util.Date r2 = r4.updated
            java.util.Date r3 = r5.updated
            if (r2 == r3) goto L_0x0054
            java.util.Date r2 = r4.updated
            if (r2 == 0) goto L_0x00d3
            java.util.Date r2 = r4.updated
            java.util.Date r3 = r5.updated
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00d3
        L_0x0054:
            com.dropbox.core.v2.teamlog.SessionLogInfo r2 = r4.sessionInfo
            com.dropbox.core.v2.teamlog.SessionLogInfo r3 = r5.sessionInfo
            if (r2 == r3) goto L_0x0062
            if (r2 == 0) goto L_0x00d3
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00d3
        L_0x0062:
            java.lang.String r2 = r4.displayName
            java.lang.String r3 = r5.displayName
            if (r2 == r3) goto L_0x0070
            if (r2 == 0) goto L_0x00d3
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00d3
        L_0x0070:
            java.lang.Boolean r2 = r4.isEmmManaged
            java.lang.Boolean r3 = r5.isEmmManaged
            if (r2 == r3) goto L_0x007e
            if (r2 == 0) goto L_0x00d3
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00d3
        L_0x007e:
            java.lang.String r2 = r4.platform
            java.lang.String r3 = r5.platform
            if (r2 == r3) goto L_0x008c
            if (r2 == 0) goto L_0x00d3
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00d3
        L_0x008c:
            java.lang.String r2 = r4.macAddress
            java.lang.String r3 = r5.macAddress
            if (r2 == r3) goto L_0x009a
            if (r2 == 0) goto L_0x00d3
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00d3
        L_0x009a:
            java.lang.String r2 = r4.osVersion
            java.lang.String r3 = r5.osVersion
            if (r2 == r3) goto L_0x00a8
            if (r2 == 0) goto L_0x00d3
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00d3
        L_0x00a8:
            java.lang.String r2 = r4.deviceType
            java.lang.String r3 = r5.deviceType
            if (r2 == r3) goto L_0x00b6
            if (r2 == 0) goto L_0x00d3
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00d3
        L_0x00b6:
            java.lang.String r2 = r4.clientVersion
            java.lang.String r3 = r5.clientVersion
            if (r2 == r3) goto L_0x00c4
            if (r2 == 0) goto L_0x00d3
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00d3
        L_0x00c4:
            java.lang.String r2 = r4.legacyUniqId
            java.lang.String r5 = r5.legacyUniqId
            if (r2 == r5) goto L_0x00d4
            if (r2 == 0) goto L_0x00d3
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x00d3
            goto L_0x00d4
        L_0x00d3:
            r0 = 0
        L_0x00d4:
            return r0
        L_0x00d5:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.teamlog.LegacyDeviceSessionLogInfo.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
