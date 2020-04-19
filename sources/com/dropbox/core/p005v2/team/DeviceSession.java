package com.dropbox.core.p005v2.team;

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

/* renamed from: com.dropbox.core.v2.team.DeviceSession */
public class DeviceSession {
    protected final String country;
    protected final Date created;
    protected final String ipAddress;
    protected final String sessionId;
    protected final Date updated;

    /* renamed from: com.dropbox.core.v2.team.DeviceSession$Builder */
    public static class Builder {
        protected String country;
        protected Date created;
        protected String ipAddress;
        protected final String sessionId;
        protected Date updated;

        protected Builder(String str) {
            if (str != null) {
                this.sessionId = str;
                this.ipAddress = null;
                this.country = null;
                this.created = null;
                this.updated = null;
                return;
            }
            throw new IllegalArgumentException("Required value for 'sessionId' is null");
        }

        public Builder withIpAddress(String str) {
            this.ipAddress = str;
            return this;
        }

        public Builder withCountry(String str) {
            this.country = str;
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

        public DeviceSession build() {
            DeviceSession deviceSession = new DeviceSession(this.sessionId, this.ipAddress, this.country, this.created, this.updated);
            return deviceSession;
        }
    }

    /* renamed from: com.dropbox.core.v2.team.DeviceSession$Serializer */
    private static class Serializer extends StructSerializer<DeviceSession> {
        public static final Serializer INSTANCE = new Serializer();

        private Serializer() {
        }

        public void serialize(DeviceSession deviceSession, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("session_id");
            StoneSerializers.string().serialize(deviceSession.sessionId, jsonGenerator);
            if (deviceSession.ipAddress != null) {
                jsonGenerator.writeFieldName(BoxEnterpriseEvent.FIELD_IP_ADDRESS);
                StoneSerializers.nullable(StoneSerializers.string()).serialize(deviceSession.ipAddress, jsonGenerator);
            }
            if (deviceSession.country != null) {
                jsonGenerator.writeFieldName("country");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(deviceSession.country, jsonGenerator);
            }
            if (deviceSession.created != null) {
                jsonGenerator.writeFieldName("created");
                StoneSerializers.nullable(StoneSerializers.timestamp()).serialize(deviceSession.created, jsonGenerator);
            }
            if (deviceSession.updated != null) {
                jsonGenerator.writeFieldName("updated");
                StoneSerializers.nullable(StoneSerializers.timestamp()).serialize(deviceSession.updated, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public DeviceSession deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
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
                String str4 = null;
                Date date = null;
                Date date2 = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("session_id".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if (BoxEnterpriseEvent.FIELD_IP_ADDRESS.equals(currentName)) {
                        str3 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else if ("country".equals(currentName)) {
                        str4 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else if ("created".equals(currentName)) {
                        date = (Date) StoneSerializers.nullable(StoneSerializers.timestamp()).deserialize(jsonParser);
                    } else if ("updated".equals(currentName)) {
                        date2 = (Date) StoneSerializers.nullable(StoneSerializers.timestamp()).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 != null) {
                    DeviceSession deviceSession = new DeviceSession(str2, str3, str4, date, date2);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(deviceSession, deviceSession.toStringMultiline());
                    return deviceSession;
                }
                throw new JsonParseException(jsonParser, "Required field \"session_id\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public DeviceSession(String str, String str2, String str3, Date date, Date date2) {
        if (str != null) {
            this.sessionId = str;
            this.ipAddress = str2;
            this.country = str3;
            this.created = LangUtil.truncateMillis(date);
            this.updated = LangUtil.truncateMillis(date2);
            return;
        }
        throw new IllegalArgumentException("Required value for 'sessionId' is null");
    }

    public DeviceSession(String str) {
        this(str, null, null, null, null);
    }

    public String getSessionId() {
        return this.sessionId;
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

    public static Builder newBuilder(String str) {
        return new Builder(str);
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.sessionId, this.ipAddress, this.country, this.created, this.updated});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:31:0x005a, code lost:
        if (r2.equals(r5) == false) goto L_0x005d;
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
            if (r2 == 0) goto L_0x005f
            com.dropbox.core.v2.team.DeviceSession r5 = (com.dropbox.core.p005v2.team.DeviceSession) r5
            java.lang.String r2 = r4.sessionId
            java.lang.String r3 = r5.sessionId
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x005d
        L_0x0024:
            java.lang.String r2 = r4.ipAddress
            java.lang.String r3 = r5.ipAddress
            if (r2 == r3) goto L_0x0032
            if (r2 == 0) goto L_0x005d
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x005d
        L_0x0032:
            java.lang.String r2 = r4.country
            java.lang.String r3 = r5.country
            if (r2 == r3) goto L_0x0040
            if (r2 == 0) goto L_0x005d
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x005d
        L_0x0040:
            java.util.Date r2 = r4.created
            java.util.Date r3 = r5.created
            if (r2 == r3) goto L_0x004e
            if (r2 == 0) goto L_0x005d
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x005d
        L_0x004e:
            java.util.Date r2 = r4.updated
            java.util.Date r5 = r5.updated
            if (r2 == r5) goto L_0x005e
            if (r2 == 0) goto L_0x005d
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x005d
            goto L_0x005e
        L_0x005d:
            r0 = 0
        L_0x005e:
            return r0
        L_0x005f:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.team.DeviceSession.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
