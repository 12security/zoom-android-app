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
import org.apache.http.cookie.ClientCookie;

/* renamed from: com.dropbox.core.v2.team.ActiveWebSession */
public class ActiveWebSession extends DeviceSession {
    protected final String browser;
    protected final Date expires;

    /* renamed from: os */
    protected final String f133os;
    protected final String userAgent;

    /* renamed from: com.dropbox.core.v2.team.ActiveWebSession$Builder */
    public static class Builder extends com.dropbox.core.p005v2.team.DeviceSession.Builder {
        protected final String browser;
        protected Date expires;

        /* renamed from: os */
        protected final String f134os;
        protected final String userAgent;

        protected Builder(String str, String str2, String str3, String str4) {
            super(str);
            if (str2 != null) {
                this.userAgent = str2;
                if (str3 != null) {
                    this.f134os = str3;
                    if (str4 != null) {
                        this.browser = str4;
                        this.expires = null;
                        return;
                    }
                    throw new IllegalArgumentException("Required value for 'browser' is null");
                }
                throw new IllegalArgumentException("Required value for 'os' is null");
            }
            throw new IllegalArgumentException("Required value for 'userAgent' is null");
        }

        public Builder withExpires(Date date) {
            this.expires = LangUtil.truncateMillis(date);
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

        public ActiveWebSession build() {
            ActiveWebSession activeWebSession = new ActiveWebSession(this.sessionId, this.userAgent, this.f134os, this.browser, this.ipAddress, this.country, this.created, this.updated, this.expires);
            return activeWebSession;
        }
    }

    /* renamed from: com.dropbox.core.v2.team.ActiveWebSession$Serializer */
    static class Serializer extends StructSerializer<ActiveWebSession> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(ActiveWebSession activeWebSession, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("session_id");
            StoneSerializers.string().serialize(activeWebSession.sessionId, jsonGenerator);
            jsonGenerator.writeFieldName("user_agent");
            StoneSerializers.string().serialize(activeWebSession.userAgent, jsonGenerator);
            jsonGenerator.writeFieldName("os");
            StoneSerializers.string().serialize(activeWebSession.f133os, jsonGenerator);
            jsonGenerator.writeFieldName("browser");
            StoneSerializers.string().serialize(activeWebSession.browser, jsonGenerator);
            if (activeWebSession.ipAddress != null) {
                jsonGenerator.writeFieldName(BoxEnterpriseEvent.FIELD_IP_ADDRESS);
                StoneSerializers.nullable(StoneSerializers.string()).serialize(activeWebSession.ipAddress, jsonGenerator);
            }
            if (activeWebSession.country != null) {
                jsonGenerator.writeFieldName("country");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(activeWebSession.country, jsonGenerator);
            }
            if (activeWebSession.created != null) {
                jsonGenerator.writeFieldName("created");
                StoneSerializers.nullable(StoneSerializers.timestamp()).serialize(activeWebSession.created, jsonGenerator);
            }
            if (activeWebSession.updated != null) {
                jsonGenerator.writeFieldName("updated");
                StoneSerializers.nullable(StoneSerializers.timestamp()).serialize(activeWebSession.updated, jsonGenerator);
            }
            if (activeWebSession.expires != null) {
                jsonGenerator.writeFieldName(ClientCookie.EXPIRES_ATTR);
                StoneSerializers.nullable(StoneSerializers.timestamp()).serialize(activeWebSession.expires, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public ActiveWebSession deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
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
                String str5 = null;
                String str6 = null;
                String str7 = null;
                Date date = null;
                Date date2 = null;
                Date date3 = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("session_id".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("user_agent".equals(currentName)) {
                        str3 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("os".equals(currentName)) {
                        str4 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("browser".equals(currentName)) {
                        str5 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if (BoxEnterpriseEvent.FIELD_IP_ADDRESS.equals(currentName)) {
                        str6 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else if ("country".equals(currentName)) {
                        str7 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else if ("created".equals(currentName)) {
                        date = (Date) StoneSerializers.nullable(StoneSerializers.timestamp()).deserialize(jsonParser);
                    } else if ("updated".equals(currentName)) {
                        date2 = (Date) StoneSerializers.nullable(StoneSerializers.timestamp()).deserialize(jsonParser);
                    } else if (ClientCookie.EXPIRES_ATTR.equals(currentName)) {
                        date3 = (Date) StoneSerializers.nullable(StoneSerializers.timestamp()).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"session_id\" missing.");
                } else if (str3 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"user_agent\" missing.");
                } else if (str4 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"os\" missing.");
                } else if (str5 != null) {
                    ActiveWebSession activeWebSession = new ActiveWebSession(str2, str3, str4, str5, str6, str7, date, date2, date3);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(activeWebSession, activeWebSession.toStringMultiline());
                    return activeWebSession;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"browser\" missing.");
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

    public ActiveWebSession(String str, String str2, String str3, String str4, String str5, String str6, Date date, Date date2, Date date3) {
        super(str, str5, str6, date, date2);
        if (str2 != null) {
            this.userAgent = str2;
            if (str3 != null) {
                this.f133os = str3;
                if (str4 != null) {
                    this.browser = str4;
                    this.expires = LangUtil.truncateMillis(date3);
                    return;
                }
                throw new IllegalArgumentException("Required value for 'browser' is null");
            }
            throw new IllegalArgumentException("Required value for 'os' is null");
        }
        throw new IllegalArgumentException("Required value for 'userAgent' is null");
    }

    public ActiveWebSession(String str, String str2, String str3, String str4) {
        this(str, str2, str3, str4, null, null, null, null, null);
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public String getUserAgent() {
        return this.userAgent;
    }

    public String getOs() {
        return this.f133os;
    }

    public String getBrowser() {
        return this.browser;
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

    public Date getExpires() {
        return this.expires;
    }

    public static Builder newBuilder(String str, String str2, String str3, String str4) {
        return new Builder(str, str2, str3, str4);
    }

    public int hashCode() {
        return (super.hashCode() * 31) + Arrays.hashCode(new Object[]{this.userAgent, this.f133os, this.browser, this.expires});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:52:0x00a8, code lost:
        if (r2.equals(r5) == false) goto L_0x00ab;
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
            if (r2 == 0) goto L_0x00ad
            com.dropbox.core.v2.team.ActiveWebSession r5 = (com.dropbox.core.p005v2.team.ActiveWebSession) r5
            java.lang.String r2 = r4.sessionId
            java.lang.String r3 = r5.sessionId
            if (r2 == r3) goto L_0x0028
            java.lang.String r2 = r4.sessionId
            java.lang.String r3 = r5.sessionId
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00ab
        L_0x0028:
            java.lang.String r2 = r4.userAgent
            java.lang.String r3 = r5.userAgent
            if (r2 == r3) goto L_0x0034
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00ab
        L_0x0034:
            java.lang.String r2 = r4.f133os
            java.lang.String r3 = r5.f133os
            if (r2 == r3) goto L_0x0040
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00ab
        L_0x0040:
            java.lang.String r2 = r4.browser
            java.lang.String r3 = r5.browser
            if (r2 == r3) goto L_0x004c
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00ab
        L_0x004c:
            java.lang.String r2 = r4.ipAddress
            java.lang.String r3 = r5.ipAddress
            if (r2 == r3) goto L_0x0060
            java.lang.String r2 = r4.ipAddress
            if (r2 == 0) goto L_0x00ab
            java.lang.String r2 = r4.ipAddress
            java.lang.String r3 = r5.ipAddress
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00ab
        L_0x0060:
            java.lang.String r2 = r4.country
            java.lang.String r3 = r5.country
            if (r2 == r3) goto L_0x0074
            java.lang.String r2 = r4.country
            if (r2 == 0) goto L_0x00ab
            java.lang.String r2 = r4.country
            java.lang.String r3 = r5.country
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00ab
        L_0x0074:
            java.util.Date r2 = r4.created
            java.util.Date r3 = r5.created
            if (r2 == r3) goto L_0x0088
            java.util.Date r2 = r4.created
            if (r2 == 0) goto L_0x00ab
            java.util.Date r2 = r4.created
            java.util.Date r3 = r5.created
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00ab
        L_0x0088:
            java.util.Date r2 = r4.updated
            java.util.Date r3 = r5.updated
            if (r2 == r3) goto L_0x009c
            java.util.Date r2 = r4.updated
            if (r2 == 0) goto L_0x00ab
            java.util.Date r2 = r4.updated
            java.util.Date r3 = r5.updated
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00ab
        L_0x009c:
            java.util.Date r2 = r4.expires
            java.util.Date r5 = r5.expires
            if (r2 == r5) goto L_0x00ac
            if (r2 == 0) goto L_0x00ab
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x00ab
            goto L_0x00ac
        L_0x00ab:
            r0 = 0
        L_0x00ac:
            return r0
        L_0x00ad:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.team.ActiveWebSession.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
