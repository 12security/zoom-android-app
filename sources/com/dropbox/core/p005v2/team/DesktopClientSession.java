package com.dropbox.core.p005v2.team;

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

/* renamed from: com.dropbox.core.v2.team.DesktopClientSession */
public class DesktopClientSession extends DeviceSession {
    protected final DesktopPlatform clientType;
    protected final String clientVersion;
    protected final String hostName;
    protected final boolean isDeleteOnUnlinkSupported;
    protected final String platform;

    /* renamed from: com.dropbox.core.v2.team.DesktopClientSession$Builder */
    public static class Builder extends com.dropbox.core.p005v2.team.DeviceSession.Builder {
        protected final DesktopPlatform clientType;
        protected final String clientVersion;
        protected final String hostName;
        protected final boolean isDeleteOnUnlinkSupported;
        protected final String platform;

        protected Builder(String str, String str2, DesktopPlatform desktopPlatform, String str3, String str4, boolean z) {
            super(str);
            if (str2 != null) {
                this.hostName = str2;
                if (desktopPlatform != null) {
                    this.clientType = desktopPlatform;
                    if (str3 != null) {
                        this.clientVersion = str3;
                        if (str4 != null) {
                            this.platform = str4;
                            this.isDeleteOnUnlinkSupported = z;
                            return;
                        }
                        throw new IllegalArgumentException("Required value for 'platform' is null");
                    }
                    throw new IllegalArgumentException("Required value for 'clientVersion' is null");
                }
                throw new IllegalArgumentException("Required value for 'clientType' is null");
            }
            throw new IllegalArgumentException("Required value for 'hostName' is null");
        }

        public Builder withIpAddress(String str) {
            super.withIpAddress(str);
            return this;
        }

        public Builder withCountry(String str) {
            super.withCountry(str);
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

        public DesktopClientSession build() {
            DesktopClientSession desktopClientSession = new DesktopClientSession(this.sessionId, this.hostName, this.clientType, this.clientVersion, this.platform, this.isDeleteOnUnlinkSupported, this.ipAddress, this.country, this.created, this.updated);
            return desktopClientSession;
        }
    }

    /* renamed from: com.dropbox.core.v2.team.DesktopClientSession$Serializer */
    static class Serializer extends StructSerializer<DesktopClientSession> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(DesktopClientSession desktopClientSession, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("session_id");
            StoneSerializers.string().serialize(desktopClientSession.sessionId, jsonGenerator);
            jsonGenerator.writeFieldName("host_name");
            StoneSerializers.string().serialize(desktopClientSession.hostName, jsonGenerator);
            jsonGenerator.writeFieldName("client_type");
            com.dropbox.core.p005v2.team.DesktopPlatform.Serializer.INSTANCE.serialize(desktopClientSession.clientType, jsonGenerator);
            jsonGenerator.writeFieldName("client_version");
            StoneSerializers.string().serialize(desktopClientSession.clientVersion, jsonGenerator);
            jsonGenerator.writeFieldName("platform");
            StoneSerializers.string().serialize(desktopClientSession.platform, jsonGenerator);
            jsonGenerator.writeFieldName("is_delete_on_unlink_supported");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(desktopClientSession.isDeleteOnUnlinkSupported), jsonGenerator);
            if (desktopClientSession.ipAddress != null) {
                jsonGenerator.writeFieldName(BoxEnterpriseEvent.FIELD_IP_ADDRESS);
                StoneSerializers.nullable(StoneSerializers.string()).serialize(desktopClientSession.ipAddress, jsonGenerator);
            }
            if (desktopClientSession.country != null) {
                jsonGenerator.writeFieldName("country");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(desktopClientSession.country, jsonGenerator);
            }
            if (desktopClientSession.created != null) {
                jsonGenerator.writeFieldName("created");
                StoneSerializers.nullable(StoneSerializers.timestamp()).serialize(desktopClientSession.created, jsonGenerator);
            }
            if (desktopClientSession.updated != null) {
                jsonGenerator.writeFieldName("updated");
                StoneSerializers.nullable(StoneSerializers.timestamp()).serialize(desktopClientSession.updated, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public DesktopClientSession deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            Boolean bool = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                String str2 = null;
                String str3 = null;
                DesktopPlatform desktopPlatform = null;
                String str4 = null;
                String str5 = null;
                String str6 = null;
                String str7 = null;
                Date date = null;
                Date date2 = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("session_id".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("host_name".equals(currentName)) {
                        str3 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("client_type".equals(currentName)) {
                        desktopPlatform = com.dropbox.core.p005v2.team.DesktopPlatform.Serializer.INSTANCE.deserialize(jsonParser);
                    } else if ("client_version".equals(currentName)) {
                        str4 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("platform".equals(currentName)) {
                        str5 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("is_delete_on_unlink_supported".equals(currentName)) {
                        bool = (Boolean) StoneSerializers.boolean_().deserialize(jsonParser);
                    } else if (BoxEnterpriseEvent.FIELD_IP_ADDRESS.equals(currentName)) {
                        str6 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else if ("country".equals(currentName)) {
                        str7 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else if ("created".equals(currentName)) {
                        date = (Date) StoneSerializers.nullable(StoneSerializers.timestamp()).deserialize(jsonParser);
                    } else if ("updated".equals(currentName)) {
                        date2 = (Date) StoneSerializers.nullable(StoneSerializers.timestamp()).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"session_id\" missing.");
                } else if (str3 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"host_name\" missing.");
                } else if (desktopPlatform == null) {
                    throw new JsonParseException(jsonParser, "Required field \"client_type\" missing.");
                } else if (str4 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"client_version\" missing.");
                } else if (str5 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"platform\" missing.");
                } else if (bool != null) {
                    DesktopClientSession desktopClientSession = new DesktopClientSession(str2, str3, desktopPlatform, str4, str5, bool.booleanValue(), str6, str7, date, date2);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(desktopClientSession, desktopClientSession.toStringMultiline());
                    return desktopClientSession;
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

    public DesktopClientSession(String str, String str2, DesktopPlatform desktopPlatform, String str3, String str4, boolean z, String str5, String str6, Date date, Date date2) {
        String str7 = str2;
        DesktopPlatform desktopPlatform2 = desktopPlatform;
        String str8 = str3;
        String str9 = str4;
        super(str, str5, str6, date, date2);
        if (str7 != null) {
            this.hostName = str7;
            if (desktopPlatform2 != null) {
                this.clientType = desktopPlatform2;
                if (str8 != null) {
                    this.clientVersion = str8;
                    if (str9 != null) {
                        this.platform = str9;
                        this.isDeleteOnUnlinkSupported = z;
                        return;
                    }
                    throw new IllegalArgumentException("Required value for 'platform' is null");
                }
                throw new IllegalArgumentException("Required value for 'clientVersion' is null");
            }
            throw new IllegalArgumentException("Required value for 'clientType' is null");
        }
        throw new IllegalArgumentException("Required value for 'hostName' is null");
    }

    public DesktopClientSession(String str, String str2, DesktopPlatform desktopPlatform, String str3, String str4, boolean z) {
        this(str, str2, desktopPlatform, str3, str4, z, null, null, null, null);
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public String getHostName() {
        return this.hostName;
    }

    public DesktopPlatform getClientType() {
        return this.clientType;
    }

    public String getClientVersion() {
        return this.clientVersion;
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

    public String getCountry() {
        return this.country;
    }

    public Date getCreated() {
        return this.created;
    }

    public Date getUpdated() {
        return this.updated;
    }

    public static Builder newBuilder(String str, String str2, DesktopPlatform desktopPlatform, String str3, String str4, boolean z) {
        Builder builder = new Builder(str, str2, desktopPlatform, str3, str4, z);
        return builder;
    }

    public int hashCode() {
        return (super.hashCode() * 31) + Arrays.hashCode(new Object[]{this.hostName, this.clientType, this.clientVersion, this.platform, Boolean.valueOf(this.isDeleteOnUnlinkSupported)});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0056, code lost:
        if (r2.equals(r3) == false) goto L_0x00af;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x0070, code lost:
        if (r4.ipAddress.equals(r5.ipAddress) == false) goto L_0x00af;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x0084, code lost:
        if (r4.country.equals(r5.country) == false) goto L_0x00af;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:47:0x0098, code lost:
        if (r4.created.equals(r5.created) == false) goto L_0x00af;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:0x00ac, code lost:
        if (r4.updated.equals(r5.updated) == false) goto L_0x00af;
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
            if (r2 == 0) goto L_0x00b1
            com.dropbox.core.v2.team.DesktopClientSession r5 = (com.dropbox.core.p005v2.team.DesktopClientSession) r5
            java.lang.String r2 = r4.sessionId
            java.lang.String r3 = r5.sessionId
            if (r2 == r3) goto L_0x0028
            java.lang.String r2 = r4.sessionId
            java.lang.String r3 = r5.sessionId
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00af
        L_0x0028:
            java.lang.String r2 = r4.hostName
            java.lang.String r3 = r5.hostName
            if (r2 == r3) goto L_0x0034
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00af
        L_0x0034:
            com.dropbox.core.v2.team.DesktopPlatform r2 = r4.clientType
            com.dropbox.core.v2.team.DesktopPlatform r3 = r5.clientType
            if (r2 == r3) goto L_0x0040
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00af
        L_0x0040:
            java.lang.String r2 = r4.clientVersion
            java.lang.String r3 = r5.clientVersion
            if (r2 == r3) goto L_0x004c
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00af
        L_0x004c:
            java.lang.String r2 = r4.platform
            java.lang.String r3 = r5.platform
            if (r2 == r3) goto L_0x0058
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00af
        L_0x0058:
            boolean r2 = r4.isDeleteOnUnlinkSupported
            boolean r3 = r5.isDeleteOnUnlinkSupported
            if (r2 != r3) goto L_0x00af
            java.lang.String r2 = r4.ipAddress
            java.lang.String r3 = r5.ipAddress
            if (r2 == r3) goto L_0x0072
            java.lang.String r2 = r4.ipAddress
            if (r2 == 0) goto L_0x00af
            java.lang.String r2 = r4.ipAddress
            java.lang.String r3 = r5.ipAddress
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00af
        L_0x0072:
            java.lang.String r2 = r4.country
            java.lang.String r3 = r5.country
            if (r2 == r3) goto L_0x0086
            java.lang.String r2 = r4.country
            if (r2 == 0) goto L_0x00af
            java.lang.String r2 = r4.country
            java.lang.String r3 = r5.country
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00af
        L_0x0086:
            java.util.Date r2 = r4.created
            java.util.Date r3 = r5.created
            if (r2 == r3) goto L_0x009a
            java.util.Date r2 = r4.created
            if (r2 == 0) goto L_0x00af
            java.util.Date r2 = r4.created
            java.util.Date r3 = r5.created
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00af
        L_0x009a:
            java.util.Date r2 = r4.updated
            java.util.Date r3 = r5.updated
            if (r2 == r3) goto L_0x00b0
            java.util.Date r2 = r4.updated
            if (r2 == 0) goto L_0x00af
            java.util.Date r2 = r4.updated
            java.util.Date r5 = r5.updated
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x00af
            goto L_0x00b0
        L_0x00af:
            r0 = 0
        L_0x00b0:
            return r0
        L_0x00b1:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.team.DesktopClientSession.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
