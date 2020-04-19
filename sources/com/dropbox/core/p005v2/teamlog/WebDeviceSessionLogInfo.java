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

/* renamed from: com.dropbox.core.v2.teamlog.WebDeviceSessionLogInfo */
public class WebDeviceSessionLogInfo extends DeviceSessionLogInfo {
    protected final String browser;

    /* renamed from: os */
    protected final String f156os;
    protected final WebSessionLogInfo sessionInfo;
    protected final String userAgent;

    /* renamed from: com.dropbox.core.v2.teamlog.WebDeviceSessionLogInfo$Builder */
    public static class Builder extends com.dropbox.core.p005v2.teamlog.DeviceSessionLogInfo.Builder {
        protected final String browser;

        /* renamed from: os */
        protected final String f157os;
        protected WebSessionLogInfo sessionInfo;
        protected final String userAgent;

        protected Builder(String str, String str2, String str3) {
            if (str != null) {
                this.userAgent = str;
                if (str2 != null) {
                    this.f157os = str2;
                    if (str3 != null) {
                        this.browser = str3;
                        this.sessionInfo = null;
                        return;
                    }
                    throw new IllegalArgumentException("Required value for 'browser' is null");
                }
                throw new IllegalArgumentException("Required value for 'os' is null");
            }
            throw new IllegalArgumentException("Required value for 'userAgent' is null");
        }

        public Builder withSessionInfo(WebSessionLogInfo webSessionLogInfo) {
            this.sessionInfo = webSessionLogInfo;
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

        public WebDeviceSessionLogInfo build() {
            WebDeviceSessionLogInfo webDeviceSessionLogInfo = new WebDeviceSessionLogInfo(this.userAgent, this.f157os, this.browser, this.ipAddress, this.created, this.updated, this.sessionInfo);
            return webDeviceSessionLogInfo;
        }
    }

    /* renamed from: com.dropbox.core.v2.teamlog.WebDeviceSessionLogInfo$Serializer */
    static class Serializer extends StructSerializer<WebDeviceSessionLogInfo> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(WebDeviceSessionLogInfo webDeviceSessionLogInfo, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            writeTag("web_device_session", jsonGenerator);
            jsonGenerator.writeFieldName("user_agent");
            StoneSerializers.string().serialize(webDeviceSessionLogInfo.userAgent, jsonGenerator);
            jsonGenerator.writeFieldName("os");
            StoneSerializers.string().serialize(webDeviceSessionLogInfo.f156os, jsonGenerator);
            jsonGenerator.writeFieldName("browser");
            StoneSerializers.string().serialize(webDeviceSessionLogInfo.browser, jsonGenerator);
            if (webDeviceSessionLogInfo.ipAddress != null) {
                jsonGenerator.writeFieldName(BoxEnterpriseEvent.FIELD_IP_ADDRESS);
                StoneSerializers.nullable(StoneSerializers.string()).serialize(webDeviceSessionLogInfo.ipAddress, jsonGenerator);
            }
            if (webDeviceSessionLogInfo.created != null) {
                jsonGenerator.writeFieldName("created");
                StoneSerializers.nullable(StoneSerializers.timestamp()).serialize(webDeviceSessionLogInfo.created, jsonGenerator);
            }
            if (webDeviceSessionLogInfo.updated != null) {
                jsonGenerator.writeFieldName("updated");
                StoneSerializers.nullable(StoneSerializers.timestamp()).serialize(webDeviceSessionLogInfo.updated, jsonGenerator);
            }
            if (webDeviceSessionLogInfo.sessionInfo != null) {
                jsonGenerator.writeFieldName("session_info");
                StoneSerializers.nullableStruct(Serializer.INSTANCE).serialize(webDeviceSessionLogInfo.sessionInfo, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public WebDeviceSessionLogInfo deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
                if ("web_device_session".equals(str)) {
                    str = null;
                }
            } else {
                str = null;
            }
            if (str == null) {
                String str2 = null;
                String str3 = null;
                String str4 = null;
                String str5 = null;
                Date date = null;
                Date date2 = null;
                WebSessionLogInfo webSessionLogInfo = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("user_agent".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("os".equals(currentName)) {
                        str3 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("browser".equals(currentName)) {
                        str4 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if (BoxEnterpriseEvent.FIELD_IP_ADDRESS.equals(currentName)) {
                        str5 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else if ("created".equals(currentName)) {
                        date = (Date) StoneSerializers.nullable(StoneSerializers.timestamp()).deserialize(jsonParser);
                    } else if ("updated".equals(currentName)) {
                        date2 = (Date) StoneSerializers.nullable(StoneSerializers.timestamp()).deserialize(jsonParser);
                    } else if ("session_info".equals(currentName)) {
                        webSessionLogInfo = (WebSessionLogInfo) StoneSerializers.nullableStruct(Serializer.INSTANCE).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"user_agent\" missing.");
                } else if (str3 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"os\" missing.");
                } else if (str4 != null) {
                    WebDeviceSessionLogInfo webDeviceSessionLogInfo = new WebDeviceSessionLogInfo(str2, str3, str4, str5, date, date2, webSessionLogInfo);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(webDeviceSessionLogInfo, webDeviceSessionLogInfo.toStringMultiline());
                    return webDeviceSessionLogInfo;
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

    public WebDeviceSessionLogInfo(String str, String str2, String str3, String str4, Date date, Date date2, WebSessionLogInfo webSessionLogInfo) {
        super(str4, date, date2);
        this.sessionInfo = webSessionLogInfo;
        if (str != null) {
            this.userAgent = str;
            if (str2 != null) {
                this.f156os = str2;
                if (str3 != null) {
                    this.browser = str3;
                    return;
                }
                throw new IllegalArgumentException("Required value for 'browser' is null");
            }
            throw new IllegalArgumentException("Required value for 'os' is null");
        }
        throw new IllegalArgumentException("Required value for 'userAgent' is null");
    }

    public WebDeviceSessionLogInfo(String str, String str2, String str3) {
        this(str, str2, str3, null, null, null, null);
    }

    public String getUserAgent() {
        return this.userAgent;
    }

    public String getOs() {
        return this.f156os;
    }

    public String getBrowser() {
        return this.browser;
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

    public WebSessionLogInfo getSessionInfo() {
        return this.sessionInfo;
    }

    public static Builder newBuilder(String str, String str2, String str3) {
        return new Builder(str, str2, str3);
    }

    public int hashCode() {
        return (super.hashCode() * 31) + Arrays.hashCode(new Object[]{this.sessionInfo, this.userAgent, this.f156os, this.browser});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:42:0x0084, code lost:
        if (r2.equals(r5) == false) goto L_0x0087;
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
            if (r2 == 0) goto L_0x0089
            com.dropbox.core.v2.teamlog.WebDeviceSessionLogInfo r5 = (com.dropbox.core.p005v2.teamlog.WebDeviceSessionLogInfo) r5
            java.lang.String r2 = r4.userAgent
            java.lang.String r3 = r5.userAgent
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0087
        L_0x0024:
            java.lang.String r2 = r4.f156os
            java.lang.String r3 = r5.f156os
            if (r2 == r3) goto L_0x0030
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0087
        L_0x0030:
            java.lang.String r2 = r4.browser
            java.lang.String r3 = r5.browser
            if (r2 == r3) goto L_0x003c
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0087
        L_0x003c:
            java.lang.String r2 = r4.ipAddress
            java.lang.String r3 = r5.ipAddress
            if (r2 == r3) goto L_0x0050
            java.lang.String r2 = r4.ipAddress
            if (r2 == 0) goto L_0x0087
            java.lang.String r2 = r4.ipAddress
            java.lang.String r3 = r5.ipAddress
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0087
        L_0x0050:
            java.util.Date r2 = r4.created
            java.util.Date r3 = r5.created
            if (r2 == r3) goto L_0x0064
            java.util.Date r2 = r4.created
            if (r2 == 0) goto L_0x0087
            java.util.Date r2 = r4.created
            java.util.Date r3 = r5.created
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0087
        L_0x0064:
            java.util.Date r2 = r4.updated
            java.util.Date r3 = r5.updated
            if (r2 == r3) goto L_0x0078
            java.util.Date r2 = r4.updated
            if (r2 == 0) goto L_0x0087
            java.util.Date r2 = r4.updated
            java.util.Date r3 = r5.updated
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0087
        L_0x0078:
            com.dropbox.core.v2.teamlog.WebSessionLogInfo r2 = r4.sessionInfo
            com.dropbox.core.v2.teamlog.WebSessionLogInfo r5 = r5.sessionInfo
            if (r2 == r5) goto L_0x0088
            if (r2 == 0) goto L_0x0087
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x0087
            goto L_0x0088
        L_0x0087:
            r0 = 0
        L_0x0088:
            return r0
        L_0x0089:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.teamlog.WebDeviceSessionLogInfo.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
