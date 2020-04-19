package com.dropbox.core.p005v2.common;

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

/* renamed from: com.dropbox.core.v2.common.RootInfo */
public class RootInfo {
    protected final String homeNamespaceId;
    protected final String rootNamespaceId;

    /* renamed from: com.dropbox.core.v2.common.RootInfo$Serializer */
    public static class Serializer extends StructSerializer<RootInfo> {
        public static final Serializer INSTANCE = new Serializer();

        public void serialize(RootInfo rootInfo, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (rootInfo instanceof TeamRootInfo) {
                Serializer.INSTANCE.serialize((TeamRootInfo) rootInfo, jsonGenerator, z);
            } else if (rootInfo instanceof UserRootInfo) {
                Serializer.INSTANCE.serialize((UserRootInfo) rootInfo, jsonGenerator, z);
            } else {
                if (!z) {
                    jsonGenerator.writeStartObject();
                }
                jsonGenerator.writeFieldName("root_namespace_id");
                StoneSerializers.string().serialize(rootInfo.rootNamespaceId, jsonGenerator);
                jsonGenerator.writeFieldName("home_namespace_id");
                StoneSerializers.string().serialize(rootInfo.homeNamespaceId, jsonGenerator);
                if (!z) {
                    jsonGenerator.writeEndObject();
                }
            }
        }

        public RootInfo deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            RootInfo rootInfo;
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
                    if ("root_namespace_id".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("home_namespace_id".equals(currentName)) {
                        str3 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"root_namespace_id\" missing.");
                } else if (str3 != null) {
                    rootInfo = new RootInfo(str2, str3);
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"home_namespace_id\" missing.");
                }
            } else if ("".equals(str)) {
                rootInfo = INSTANCE.deserialize(jsonParser, true);
            } else if ("team".equals(str)) {
                rootInfo = Serializer.INSTANCE.deserialize(jsonParser, true);
            } else if ("user".equals(str)) {
                rootInfo = Serializer.INSTANCE.deserialize(jsonParser, true);
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
            StoneDeserializerLogger.log(rootInfo, rootInfo.toStringMultiline());
            return rootInfo;
        }
    }

    public RootInfo(String str, String str2) {
        if (str == null) {
            throw new IllegalArgumentException("Required value for 'rootNamespaceId' is null");
        } else if (Pattern.matches("[-_0-9a-zA-Z:]+", str)) {
            this.rootNamespaceId = str;
            if (str2 == null) {
                throw new IllegalArgumentException("Required value for 'homeNamespaceId' is null");
            } else if (Pattern.matches("[-_0-9a-zA-Z:]+", str2)) {
                this.homeNamespaceId = str2;
            } else {
                throw new IllegalArgumentException("String 'homeNamespaceId' does not match pattern");
            }
        } else {
            throw new IllegalArgumentException("String 'rootNamespaceId' does not match pattern");
        }
    }

    public String getRootNamespaceId() {
        return this.rootNamespaceId;
    }

    public String getHomeNamespaceId() {
        return this.homeNamespaceId;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.rootNamespaceId, this.homeNamespaceId});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x002e, code lost:
        if (r2.equals(r5) == false) goto L_0x0031;
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
            if (r2 == 0) goto L_0x0033
            com.dropbox.core.v2.common.RootInfo r5 = (com.dropbox.core.p005v2.common.RootInfo) r5
            java.lang.String r2 = r4.rootNamespaceId
            java.lang.String r3 = r5.rootNamespaceId
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0031
        L_0x0024:
            java.lang.String r2 = r4.homeNamespaceId
            java.lang.String r5 = r5.homeNamespaceId
            if (r2 == r5) goto L_0x0032
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x0031
            goto L_0x0032
        L_0x0031:
            r0 = 0
        L_0x0032:
            return r0
        L_0x0033:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.common.RootInfo.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
