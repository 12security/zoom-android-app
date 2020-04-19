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

/* renamed from: com.dropbox.core.v2.common.TeamRootInfo */
public class TeamRootInfo extends RootInfo {
    protected final String homePath;

    /* renamed from: com.dropbox.core.v2.common.TeamRootInfo$Serializer */
    static class Serializer extends StructSerializer<TeamRootInfo> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(TeamRootInfo teamRootInfo, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            writeTag("team", jsonGenerator);
            jsonGenerator.writeFieldName("root_namespace_id");
            StoneSerializers.string().serialize(teamRootInfo.rootNamespaceId, jsonGenerator);
            jsonGenerator.writeFieldName("home_namespace_id");
            StoneSerializers.string().serialize(teamRootInfo.homeNamespaceId, jsonGenerator);
            jsonGenerator.writeFieldName("home_path");
            StoneSerializers.string().serialize(teamRootInfo.homePath, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public TeamRootInfo deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            String str2 = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
                if ("team".equals(str)) {
                    str = null;
                }
            } else {
                str = null;
            }
            if (str == null) {
                String str3 = null;
                String str4 = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("root_namespace_id".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("home_namespace_id".equals(currentName)) {
                        str3 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("home_path".equals(currentName)) {
                        str4 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"root_namespace_id\" missing.");
                } else if (str3 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"home_namespace_id\" missing.");
                } else if (str4 != null) {
                    TeamRootInfo teamRootInfo = new TeamRootInfo(str2, str3, str4);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(teamRootInfo, teamRootInfo.toStringMultiline());
                    return teamRootInfo;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"home_path\" missing.");
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

    public TeamRootInfo(String str, String str2, String str3) {
        super(str, str2);
        if (str3 != null) {
            this.homePath = str3;
            return;
        }
        throw new IllegalArgumentException("Required value for 'homePath' is null");
    }

    public String getRootNamespaceId() {
        return this.rootNamespaceId;
    }

    public String getHomeNamespaceId() {
        return this.homeNamespaceId;
    }

    public String getHomePath() {
        return this.homePath;
    }

    public int hashCode() {
        return (super.hashCode() * 31) + Arrays.hashCode(new Object[]{this.homePath});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0042, code lost:
        if (r2.equals(r5) == false) goto L_0x0045;
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
            if (r2 == 0) goto L_0x0047
            com.dropbox.core.v2.common.TeamRootInfo r5 = (com.dropbox.core.p005v2.common.TeamRootInfo) r5
            java.lang.String r2 = r4.rootNamespaceId
            java.lang.String r3 = r5.rootNamespaceId
            if (r2 == r3) goto L_0x0028
            java.lang.String r2 = r4.rootNamespaceId
            java.lang.String r3 = r5.rootNamespaceId
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0045
        L_0x0028:
            java.lang.String r2 = r4.homeNamespaceId
            java.lang.String r3 = r5.homeNamespaceId
            if (r2 == r3) goto L_0x0038
            java.lang.String r2 = r4.homeNamespaceId
            java.lang.String r3 = r5.homeNamespaceId
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0045
        L_0x0038:
            java.lang.String r2 = r4.homePath
            java.lang.String r5 = r5.homePath
            if (r2 == r5) goto L_0x0046
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x0045
            goto L_0x0046
        L_0x0045:
            r0 = 0
        L_0x0046:
            return r0
        L_0x0047:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.common.TeamRootInfo.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
