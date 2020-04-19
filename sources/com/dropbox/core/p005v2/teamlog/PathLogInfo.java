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

/* renamed from: com.dropbox.core.v2.teamlog.PathLogInfo */
public class PathLogInfo {
    protected final String contextual;
    protected final NamespaceRelativePathLogInfo namespaceRelative;

    /* renamed from: com.dropbox.core.v2.teamlog.PathLogInfo$Serializer */
    static class Serializer extends StructSerializer<PathLogInfo> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(PathLogInfo pathLogInfo, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("namespace_relative");
            Serializer.INSTANCE.serialize(pathLogInfo.namespaceRelative, jsonGenerator);
            if (pathLogInfo.contextual != null) {
                jsonGenerator.writeFieldName("contextual");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(pathLogInfo.contextual, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public PathLogInfo deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            NamespaceRelativePathLogInfo namespaceRelativePathLogInfo = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                String str2 = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("namespace_relative".equals(currentName)) {
                        namespaceRelativePathLogInfo = (NamespaceRelativePathLogInfo) Serializer.INSTANCE.deserialize(jsonParser);
                    } else if ("contextual".equals(currentName)) {
                        str2 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (namespaceRelativePathLogInfo != null) {
                    PathLogInfo pathLogInfo = new PathLogInfo(namespaceRelativePathLogInfo, str2);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(pathLogInfo, pathLogInfo.toStringMultiline());
                    return pathLogInfo;
                }
                throw new JsonParseException(jsonParser, "Required field \"namespace_relative\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public PathLogInfo(NamespaceRelativePathLogInfo namespaceRelativePathLogInfo, String str) {
        this.contextual = str;
        if (namespaceRelativePathLogInfo != null) {
            this.namespaceRelative = namespaceRelativePathLogInfo;
            return;
        }
        throw new IllegalArgumentException("Required value for 'namespaceRelative' is null");
    }

    public PathLogInfo(NamespaceRelativePathLogInfo namespaceRelativePathLogInfo) {
        this(namespaceRelativePathLogInfo, null);
    }

    public NamespaceRelativePathLogInfo getNamespaceRelative() {
        return this.namespaceRelative;
    }

    public String getContextual() {
        return this.contextual;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.contextual, this.namespaceRelative});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0030, code lost:
        if (r2.equals(r5) == false) goto L_0x0033;
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
            if (r2 == 0) goto L_0x0035
            com.dropbox.core.v2.teamlog.PathLogInfo r5 = (com.dropbox.core.p005v2.teamlog.PathLogInfo) r5
            com.dropbox.core.v2.teamlog.NamespaceRelativePathLogInfo r2 = r4.namespaceRelative
            com.dropbox.core.v2.teamlog.NamespaceRelativePathLogInfo r3 = r5.namespaceRelative
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0033
        L_0x0024:
            java.lang.String r2 = r4.contextual
            java.lang.String r5 = r5.contextual
            if (r2 == r5) goto L_0x0034
            if (r2 == 0) goto L_0x0033
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x0033
            goto L_0x0034
        L_0x0033:
            r0 = 0
        L_0x0034:
            return r0
        L_0x0035:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.teamlog.PathLogInfo.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
