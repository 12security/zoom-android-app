package com.dropbox.core.p005v2.team;

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

/* renamed from: com.dropbox.core.v2.team.ApiApp */
public class ApiApp {
    protected final String appId;
    protected final String appName;
    protected final boolean isAppFolder;
    protected final Date linked;
    protected final String publisher;
    protected final String publisherUrl;

    /* renamed from: com.dropbox.core.v2.team.ApiApp$Builder */
    public static class Builder {
        protected final String appId;
        protected final String appName;
        protected final boolean isAppFolder;
        protected Date linked;
        protected String publisher;
        protected String publisherUrl;

        protected Builder(String str, String str2, boolean z) {
            if (str != null) {
                this.appId = str;
                if (str2 != null) {
                    this.appName = str2;
                    this.isAppFolder = z;
                    this.publisher = null;
                    this.publisherUrl = null;
                    this.linked = null;
                    return;
                }
                throw new IllegalArgumentException("Required value for 'appName' is null");
            }
            throw new IllegalArgumentException("Required value for 'appId' is null");
        }

        public Builder withPublisher(String str) {
            this.publisher = str;
            return this;
        }

        public Builder withPublisherUrl(String str) {
            this.publisherUrl = str;
            return this;
        }

        public Builder withLinked(Date date) {
            this.linked = LangUtil.truncateMillis(date);
            return this;
        }

        public ApiApp build() {
            ApiApp apiApp = new ApiApp(this.appId, this.appName, this.isAppFolder, this.publisher, this.publisherUrl, this.linked);
            return apiApp;
        }
    }

    /* renamed from: com.dropbox.core.v2.team.ApiApp$Serializer */
    static class Serializer extends StructSerializer<ApiApp> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(ApiApp apiApp, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("app_id");
            StoneSerializers.string().serialize(apiApp.appId, jsonGenerator);
            jsonGenerator.writeFieldName("app_name");
            StoneSerializers.string().serialize(apiApp.appName, jsonGenerator);
            jsonGenerator.writeFieldName("is_app_folder");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(apiApp.isAppFolder), jsonGenerator);
            if (apiApp.publisher != null) {
                jsonGenerator.writeFieldName("publisher");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(apiApp.publisher, jsonGenerator);
            }
            if (apiApp.publisherUrl != null) {
                jsonGenerator.writeFieldName("publisher_url");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(apiApp.publisherUrl, jsonGenerator);
            }
            if (apiApp.linked != null) {
                jsonGenerator.writeFieldName("linked");
                StoneSerializers.nullable(StoneSerializers.timestamp()).serialize(apiApp.linked, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public ApiApp deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
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
                String str4 = null;
                String str5 = null;
                Date date = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("app_id".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("app_name".equals(currentName)) {
                        str3 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("is_app_folder".equals(currentName)) {
                        bool = (Boolean) StoneSerializers.boolean_().deserialize(jsonParser);
                    } else if ("publisher".equals(currentName)) {
                        str4 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else if ("publisher_url".equals(currentName)) {
                        str5 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else if ("linked".equals(currentName)) {
                        date = (Date) StoneSerializers.nullable(StoneSerializers.timestamp()).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"app_id\" missing.");
                } else if (str3 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"app_name\" missing.");
                } else if (bool != null) {
                    ApiApp apiApp = new ApiApp(str2, str3, bool.booleanValue(), str4, str5, date);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(apiApp, apiApp.toStringMultiline());
                    return apiApp;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"is_app_folder\" missing.");
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

    public ApiApp(String str, String str2, boolean z, String str3, String str4, Date date) {
        if (str != null) {
            this.appId = str;
            if (str2 != null) {
                this.appName = str2;
                this.publisher = str3;
                this.publisherUrl = str4;
                this.linked = LangUtil.truncateMillis(date);
                this.isAppFolder = z;
                return;
            }
            throw new IllegalArgumentException("Required value for 'appName' is null");
        }
        throw new IllegalArgumentException("Required value for 'appId' is null");
    }

    public ApiApp(String str, String str2, boolean z) {
        this(str, str2, z, null, null, null);
    }

    public String getAppId() {
        return this.appId;
    }

    public String getAppName() {
        return this.appName;
    }

    public boolean getIsAppFolder() {
        return this.isAppFolder;
    }

    public String getPublisher() {
        return this.publisher;
    }

    public String getPublisherUrl() {
        return this.publisherUrl;
    }

    public Date getLinked() {
        return this.linked;
    }

    public static Builder newBuilder(String str, String str2, boolean z) {
        return new Builder(str, str2, z);
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.appId, this.appName, this.publisher, this.publisherUrl, this.linked, Boolean.valueOf(this.isAppFolder)});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:32:0x005e, code lost:
        if (r2.equals(r5) == false) goto L_0x0061;
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
            if (r2 == 0) goto L_0x0063
            com.dropbox.core.v2.team.ApiApp r5 = (com.dropbox.core.p005v2.team.ApiApp) r5
            java.lang.String r2 = r4.appId
            java.lang.String r3 = r5.appId
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0061
        L_0x0024:
            java.lang.String r2 = r4.appName
            java.lang.String r3 = r5.appName
            if (r2 == r3) goto L_0x0030
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0061
        L_0x0030:
            boolean r2 = r4.isAppFolder
            boolean r3 = r5.isAppFolder
            if (r2 != r3) goto L_0x0061
            java.lang.String r2 = r4.publisher
            java.lang.String r3 = r5.publisher
            if (r2 == r3) goto L_0x0044
            if (r2 == 0) goto L_0x0061
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0061
        L_0x0044:
            java.lang.String r2 = r4.publisherUrl
            java.lang.String r3 = r5.publisherUrl
            if (r2 == r3) goto L_0x0052
            if (r2 == 0) goto L_0x0061
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0061
        L_0x0052:
            java.util.Date r2 = r4.linked
            java.util.Date r5 = r5.linked
            if (r2 == r5) goto L_0x0062
            if (r2 == 0) goto L_0x0061
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x0061
            goto L_0x0062
        L_0x0061:
            r0 = 0
        L_0x0062:
            return r0
        L_0x0063:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.team.ApiApp.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
