package com.dropbox.core.p005v2.sharing;

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
import java.util.regex.Pattern;

/* renamed from: com.dropbox.core.v2.sharing.GetSharedLinkMetadataArg */
class GetSharedLinkMetadataArg {
    protected final String linkPassword;
    protected final String path;
    protected final String url;

    /* renamed from: com.dropbox.core.v2.sharing.GetSharedLinkMetadataArg$Builder */
    public static class Builder {
        protected String linkPassword;
        protected String path;
        protected final String url;

        protected Builder(String str) {
            if (str != null) {
                this.url = str;
                this.path = null;
                this.linkPassword = null;
                return;
            }
            throw new IllegalArgumentException("Required value for 'url' is null");
        }

        public Builder withPath(String str) {
            if (str == null || Pattern.matches("/(.|[\\r\\n])*", str)) {
                this.path = str;
                return this;
            }
            throw new IllegalArgumentException("String 'path' does not match pattern");
        }

        public Builder withLinkPassword(String str) {
            this.linkPassword = str;
            return this;
        }

        public GetSharedLinkMetadataArg build() {
            return new GetSharedLinkMetadataArg(this.url, this.path, this.linkPassword);
        }
    }

    /* renamed from: com.dropbox.core.v2.sharing.GetSharedLinkMetadataArg$Serializer */
    static class Serializer extends StructSerializer<GetSharedLinkMetadataArg> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(GetSharedLinkMetadataArg getSharedLinkMetadataArg, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("url");
            StoneSerializers.string().serialize(getSharedLinkMetadataArg.url, jsonGenerator);
            if (getSharedLinkMetadataArg.path != null) {
                jsonGenerator.writeFieldName("path");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(getSharedLinkMetadataArg.path, jsonGenerator);
            }
            if (getSharedLinkMetadataArg.linkPassword != null) {
                jsonGenerator.writeFieldName("link_password");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(getSharedLinkMetadataArg.linkPassword, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public GetSharedLinkMetadataArg deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            String str2 = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                String str3 = null;
                String str4 = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("url".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("path".equals(currentName)) {
                        str3 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else if ("link_password".equals(currentName)) {
                        str4 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 != null) {
                    GetSharedLinkMetadataArg getSharedLinkMetadataArg = new GetSharedLinkMetadataArg(str2, str3, str4);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(getSharedLinkMetadataArg, getSharedLinkMetadataArg.toStringMultiline());
                    return getSharedLinkMetadataArg;
                }
                throw new JsonParseException(jsonParser, "Required field \"url\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public GetSharedLinkMetadataArg(String str, String str2, String str3) {
        if (str != null) {
            this.url = str;
            if (str2 == null || Pattern.matches("/(.|[\\r\\n])*", str2)) {
                this.path = str2;
                this.linkPassword = str3;
                return;
            }
            throw new IllegalArgumentException("String 'path' does not match pattern");
        }
        throw new IllegalArgumentException("Required value for 'url' is null");
    }

    public GetSharedLinkMetadataArg(String str) {
        this(str, null, null);
    }

    public String getUrl() {
        return this.url;
    }

    public String getPath() {
        return this.path;
    }

    public String getLinkPassword() {
        return this.linkPassword;
    }

    public static Builder newBuilder(String str) {
        return new Builder(str);
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.url, this.path, this.linkPassword});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:21:0x003e, code lost:
        if (r2.equals(r5) == false) goto L_0x0041;
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
            if (r2 == 0) goto L_0x0043
            com.dropbox.core.v2.sharing.GetSharedLinkMetadataArg r5 = (com.dropbox.core.p005v2.sharing.GetSharedLinkMetadataArg) r5
            java.lang.String r2 = r4.url
            java.lang.String r3 = r5.url
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0041
        L_0x0024:
            java.lang.String r2 = r4.path
            java.lang.String r3 = r5.path
            if (r2 == r3) goto L_0x0032
            if (r2 == 0) goto L_0x0041
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0041
        L_0x0032:
            java.lang.String r2 = r4.linkPassword
            java.lang.String r5 = r5.linkPassword
            if (r2 == r5) goto L_0x0042
            if (r2 == 0) goto L_0x0041
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x0041
            goto L_0x0042
        L_0x0041:
            r0 = 0
        L_0x0042:
            return r0
        L_0x0043:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.sharing.GetSharedLinkMetadataArg.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
