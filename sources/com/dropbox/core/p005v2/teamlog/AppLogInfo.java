package com.dropbox.core.p005v2.teamlog;

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

/* renamed from: com.dropbox.core.v2.teamlog.AppLogInfo */
public class AppLogInfo {
    protected final String appId;
    protected final String displayName;

    /* renamed from: com.dropbox.core.v2.teamlog.AppLogInfo$Builder */
    public static class Builder {
        protected String appId = null;
        protected String displayName = null;

        protected Builder() {
        }

        public Builder withAppId(String str) {
            this.appId = str;
            return this;
        }

        public Builder withDisplayName(String str) {
            this.displayName = str;
            return this;
        }

        public AppLogInfo build() {
            return new AppLogInfo(this.appId, this.displayName);
        }
    }

    /* renamed from: com.dropbox.core.v2.teamlog.AppLogInfo$Serializer */
    static class Serializer extends StructSerializer<AppLogInfo> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(AppLogInfo appLogInfo, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (appLogInfo instanceof UserOrTeamLinkedAppLogInfo) {
                Serializer.INSTANCE.serialize((UserOrTeamLinkedAppLogInfo) appLogInfo, jsonGenerator, z);
            } else if (appLogInfo instanceof UserLinkedAppLogInfo) {
                Serializer.INSTANCE.serialize((UserLinkedAppLogInfo) appLogInfo, jsonGenerator, z);
            } else if (appLogInfo instanceof TeamLinkedAppLogInfo) {
                Serializer.INSTANCE.serialize((TeamLinkedAppLogInfo) appLogInfo, jsonGenerator, z);
            } else {
                if (!z) {
                    jsonGenerator.writeStartObject();
                }
                if (appLogInfo.appId != null) {
                    jsonGenerator.writeFieldName("app_id");
                    StoneSerializers.nullable(StoneSerializers.string()).serialize(appLogInfo.appId, jsonGenerator);
                }
                if (appLogInfo.displayName != null) {
                    jsonGenerator.writeFieldName("display_name");
                    StoneSerializers.nullable(StoneSerializers.string()).serialize(appLogInfo.displayName, jsonGenerator);
                }
                if (!z) {
                    jsonGenerator.writeEndObject();
                }
            }
        }

        public AppLogInfo deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            AppLogInfo appLogInfo;
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
                String str3 = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("app_id".equals(currentName)) {
                        str2 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else if ("display_name".equals(currentName)) {
                        str3 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                appLogInfo = new AppLogInfo(str2, str3);
            } else if ("".equals(str)) {
                appLogInfo = INSTANCE.deserialize(jsonParser, true);
            } else if ("user_or_team_linked_app".equals(str)) {
                appLogInfo = Serializer.INSTANCE.deserialize(jsonParser, true);
            } else if ("user_linked_app".equals(str)) {
                appLogInfo = Serializer.INSTANCE.deserialize(jsonParser, true);
            } else if ("team_linked_app".equals(str)) {
                appLogInfo = Serializer.INSTANCE.deserialize(jsonParser, true);
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
            StoneDeserializerLogger.log(appLogInfo, appLogInfo.toStringMultiline());
            return appLogInfo;
        }
    }

    public AppLogInfo(String str, String str2) {
        this.appId = str;
        this.displayName = str2;
    }

    public AppLogInfo() {
        this(null, null);
    }

    public String getAppId() {
        return this.appId;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.appId, this.displayName});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0032, code lost:
        if (r2.equals(r5) == false) goto L_0x0035;
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
            if (r2 == 0) goto L_0x0037
            com.dropbox.core.v2.teamlog.AppLogInfo r5 = (com.dropbox.core.p005v2.teamlog.AppLogInfo) r5
            java.lang.String r2 = r4.appId
            java.lang.String r3 = r5.appId
            if (r2 == r3) goto L_0x0026
            if (r2 == 0) goto L_0x0035
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0035
        L_0x0026:
            java.lang.String r2 = r4.displayName
            java.lang.String r5 = r5.displayName
            if (r2 == r5) goto L_0x0036
            if (r2 == 0) goto L_0x0035
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x0035
            goto L_0x0036
        L_0x0035:
            r0 = 0
        L_0x0036:
            return r0
        L_0x0037:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.teamlog.AppLogInfo.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
