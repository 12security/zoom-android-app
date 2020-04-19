package com.dropbox.core.p005v2.team;

import com.box.androidsdk.content.BoxConstants;
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

/* renamed from: com.dropbox.core.v2.team.MobileClientSession */
public class MobileClientSession extends DeviceSession {
    protected final MobileClientPlatform clientType;
    protected final String clientVersion;
    protected final String deviceName;
    protected final String lastCarrier;
    protected final String osVersion;

    /* renamed from: com.dropbox.core.v2.team.MobileClientSession$Builder */
    public static class Builder extends com.dropbox.core.p005v2.team.DeviceSession.Builder {
        protected final MobileClientPlatform clientType;
        protected String clientVersion;
        protected final String deviceName;
        protected String lastCarrier;
        protected String osVersion;

        protected Builder(String str, String str2, MobileClientPlatform mobileClientPlatform) {
            super(str);
            if (str2 != null) {
                this.deviceName = str2;
                if (mobileClientPlatform != null) {
                    this.clientType = mobileClientPlatform;
                    this.clientVersion = null;
                    this.osVersion = null;
                    this.lastCarrier = null;
                    return;
                }
                throw new IllegalArgumentException("Required value for 'clientType' is null");
            }
            throw new IllegalArgumentException("Required value for 'deviceName' is null");
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

        public MobileClientSession build() {
            MobileClientSession mobileClientSession = new MobileClientSession(this.sessionId, this.deviceName, this.clientType, this.ipAddress, this.country, this.created, this.updated, this.clientVersion, this.osVersion, this.lastCarrier);
            return mobileClientSession;
        }
    }

    /* renamed from: com.dropbox.core.v2.team.MobileClientSession$Serializer */
    static class Serializer extends StructSerializer<MobileClientSession> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(MobileClientSession mobileClientSession, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("session_id");
            StoneSerializers.string().serialize(mobileClientSession.sessionId, jsonGenerator);
            jsonGenerator.writeFieldName(BoxConstants.KEY_BOX_DEVICE_NAME);
            StoneSerializers.string().serialize(mobileClientSession.deviceName, jsonGenerator);
            jsonGenerator.writeFieldName("client_type");
            com.dropbox.core.p005v2.team.MobileClientPlatform.Serializer.INSTANCE.serialize(mobileClientSession.clientType, jsonGenerator);
            if (mobileClientSession.ipAddress != null) {
                jsonGenerator.writeFieldName(BoxEnterpriseEvent.FIELD_IP_ADDRESS);
                StoneSerializers.nullable(StoneSerializers.string()).serialize(mobileClientSession.ipAddress, jsonGenerator);
            }
            if (mobileClientSession.country != null) {
                jsonGenerator.writeFieldName("country");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(mobileClientSession.country, jsonGenerator);
            }
            if (mobileClientSession.created != null) {
                jsonGenerator.writeFieldName("created");
                StoneSerializers.nullable(StoneSerializers.timestamp()).serialize(mobileClientSession.created, jsonGenerator);
            }
            if (mobileClientSession.updated != null) {
                jsonGenerator.writeFieldName("updated");
                StoneSerializers.nullable(StoneSerializers.timestamp()).serialize(mobileClientSession.updated, jsonGenerator);
            }
            if (mobileClientSession.clientVersion != null) {
                jsonGenerator.writeFieldName("client_version");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(mobileClientSession.clientVersion, jsonGenerator);
            }
            if (mobileClientSession.osVersion != null) {
                jsonGenerator.writeFieldName("os_version");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(mobileClientSession.osVersion, jsonGenerator);
            }
            if (mobileClientSession.lastCarrier != null) {
                jsonGenerator.writeFieldName("last_carrier");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(mobileClientSession.lastCarrier, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public MobileClientSession deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                String str2 = null;
                String str3 = null;
                MobileClientPlatform mobileClientPlatform = null;
                String str4 = null;
                String str5 = null;
                Date date = null;
                Date date2 = null;
                String str6 = null;
                String str7 = null;
                String str8 = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("session_id".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if (BoxConstants.KEY_BOX_DEVICE_NAME.equals(currentName)) {
                        str3 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("client_type".equals(currentName)) {
                        mobileClientPlatform = com.dropbox.core.p005v2.team.MobileClientPlatform.Serializer.INSTANCE.deserialize(jsonParser);
                    } else if (BoxEnterpriseEvent.FIELD_IP_ADDRESS.equals(currentName)) {
                        str4 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else if ("country".equals(currentName)) {
                        str5 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else if ("created".equals(currentName)) {
                        date = (Date) StoneSerializers.nullable(StoneSerializers.timestamp()).deserialize(jsonParser);
                    } else if ("updated".equals(currentName)) {
                        date2 = (Date) StoneSerializers.nullable(StoneSerializers.timestamp()).deserialize(jsonParser);
                    } else if ("client_version".equals(currentName)) {
                        str6 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else if ("os_version".equals(currentName)) {
                        str7 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else if ("last_carrier".equals(currentName)) {
                        str8 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"session_id\" missing.");
                } else if (str3 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"device_name\" missing.");
                } else if (mobileClientPlatform != null) {
                    MobileClientSession mobileClientSession = new MobileClientSession(str2, str3, mobileClientPlatform, str4, str5, date, date2, str6, str7, str8);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(mobileClientSession, mobileClientSession.toStringMultiline());
                    return mobileClientSession;
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

    public MobileClientSession(String str, String str2, MobileClientPlatform mobileClientPlatform, String str3, String str4, Date date, Date date2, String str5, String str6, String str7) {
        String str8 = str2;
        MobileClientPlatform mobileClientPlatform2 = mobileClientPlatform;
        super(str, str3, str4, date, date2);
        if (str8 != null) {
            this.deviceName = str8;
            if (mobileClientPlatform2 != null) {
                this.clientType = mobileClientPlatform2;
                this.clientVersion = str5;
                this.osVersion = str6;
                this.lastCarrier = str7;
                return;
            }
            throw new IllegalArgumentException("Required value for 'clientType' is null");
        }
        throw new IllegalArgumentException("Required value for 'deviceName' is null");
    }

    public MobileClientSession(String str, String str2, MobileClientPlatform mobileClientPlatform) {
        this(str, str2, mobileClientPlatform, null, null, null, null, null, null, null);
    }

    public String getSessionId() {
        return this.sessionId;
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

    public String getCountry() {
        return this.country;
    }

    public Date getCreated() {
        return this.created;
    }

    public Date getUpdated() {
        return this.updated;
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

    public static Builder newBuilder(String str, String str2, MobileClientPlatform mobileClientPlatform) {
        return new Builder(str, str2, mobileClientPlatform);
    }

    public int hashCode() {
        return (super.hashCode() * 31) + Arrays.hashCode(new Object[]{this.deviceName, this.clientType, this.clientVersion, this.osVersion, this.lastCarrier});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:58:0x00b8, code lost:
        if (r2.equals(r5) == false) goto L_0x00bb;
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
            if (r2 == 0) goto L_0x00bd
            com.dropbox.core.v2.team.MobileClientSession r5 = (com.dropbox.core.p005v2.team.MobileClientSession) r5
            java.lang.String r2 = r4.sessionId
            java.lang.String r3 = r5.sessionId
            if (r2 == r3) goto L_0x0028
            java.lang.String r2 = r4.sessionId
            java.lang.String r3 = r5.sessionId
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00bb
        L_0x0028:
            java.lang.String r2 = r4.deviceName
            java.lang.String r3 = r5.deviceName
            if (r2 == r3) goto L_0x0034
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00bb
        L_0x0034:
            com.dropbox.core.v2.team.MobileClientPlatform r2 = r4.clientType
            com.dropbox.core.v2.team.MobileClientPlatform r3 = r5.clientType
            if (r2 == r3) goto L_0x0040
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00bb
        L_0x0040:
            java.lang.String r2 = r4.ipAddress
            java.lang.String r3 = r5.ipAddress
            if (r2 == r3) goto L_0x0054
            java.lang.String r2 = r4.ipAddress
            if (r2 == 0) goto L_0x00bb
            java.lang.String r2 = r4.ipAddress
            java.lang.String r3 = r5.ipAddress
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00bb
        L_0x0054:
            java.lang.String r2 = r4.country
            java.lang.String r3 = r5.country
            if (r2 == r3) goto L_0x0068
            java.lang.String r2 = r4.country
            if (r2 == 0) goto L_0x00bb
            java.lang.String r2 = r4.country
            java.lang.String r3 = r5.country
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00bb
        L_0x0068:
            java.util.Date r2 = r4.created
            java.util.Date r3 = r5.created
            if (r2 == r3) goto L_0x007c
            java.util.Date r2 = r4.created
            if (r2 == 0) goto L_0x00bb
            java.util.Date r2 = r4.created
            java.util.Date r3 = r5.created
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00bb
        L_0x007c:
            java.util.Date r2 = r4.updated
            java.util.Date r3 = r5.updated
            if (r2 == r3) goto L_0x0090
            java.util.Date r2 = r4.updated
            if (r2 == 0) goto L_0x00bb
            java.util.Date r2 = r4.updated
            java.util.Date r3 = r5.updated
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00bb
        L_0x0090:
            java.lang.String r2 = r4.clientVersion
            java.lang.String r3 = r5.clientVersion
            if (r2 == r3) goto L_0x009e
            if (r2 == 0) goto L_0x00bb
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00bb
        L_0x009e:
            java.lang.String r2 = r4.osVersion
            java.lang.String r3 = r5.osVersion
            if (r2 == r3) goto L_0x00ac
            if (r2 == 0) goto L_0x00bb
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00bb
        L_0x00ac:
            java.lang.String r2 = r4.lastCarrier
            java.lang.String r5 = r5.lastCarrier
            if (r2 == r5) goto L_0x00bc
            if (r2 == 0) goto L_0x00bb
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x00bb
            goto L_0x00bc
        L_0x00bb:
            r0 = 0
        L_0x00bc:
            return r0
        L_0x00bd:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.team.MobileClientSession.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
