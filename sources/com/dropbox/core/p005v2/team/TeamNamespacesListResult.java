package com.dropbox.core.p005v2.team;

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
import java.util.List;

/* renamed from: com.dropbox.core.v2.team.TeamNamespacesListResult */
public class TeamNamespacesListResult {
    protected final String cursor;
    protected final boolean hasMore;
    protected final List<NamespaceMetadata> namespaces;

    /* renamed from: com.dropbox.core.v2.team.TeamNamespacesListResult$Serializer */
    static class Serializer extends StructSerializer<TeamNamespacesListResult> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(TeamNamespacesListResult teamNamespacesListResult, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("namespaces");
            StoneSerializers.list(Serializer.INSTANCE).serialize(teamNamespacesListResult.namespaces, jsonGenerator);
            jsonGenerator.writeFieldName("cursor");
            StoneSerializers.string().serialize(teamNamespacesListResult.cursor, jsonGenerator);
            jsonGenerator.writeFieldName("has_more");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(teamNamespacesListResult.hasMore), jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public TeamNamespacesListResult deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            List list = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                String str2 = null;
                Boolean bool = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("namespaces".equals(currentName)) {
                        list = (List) StoneSerializers.list(Serializer.INSTANCE).deserialize(jsonParser);
                    } else if ("cursor".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("has_more".equals(currentName)) {
                        bool = (Boolean) StoneSerializers.boolean_().deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (list == null) {
                    throw new JsonParseException(jsonParser, "Required field \"namespaces\" missing.");
                } else if (str2 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"cursor\" missing.");
                } else if (bool != null) {
                    TeamNamespacesListResult teamNamespacesListResult = new TeamNamespacesListResult(list, str2, bool.booleanValue());
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(teamNamespacesListResult, teamNamespacesListResult.toStringMultiline());
                    return teamNamespacesListResult;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"has_more\" missing.");
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

    public TeamNamespacesListResult(List<NamespaceMetadata> list, String str, boolean z) {
        if (list != null) {
            for (NamespaceMetadata namespaceMetadata : list) {
                if (namespaceMetadata == null) {
                    throw new IllegalArgumentException("An item in list 'namespaces' is null");
                }
            }
            this.namespaces = list;
            if (str != null) {
                this.cursor = str;
                this.hasMore = z;
                return;
            }
            throw new IllegalArgumentException("Required value for 'cursor' is null");
        }
        throw new IllegalArgumentException("Required value for 'namespaces' is null");
    }

    public List<NamespaceMetadata> getNamespaces() {
        return this.namespaces;
    }

    public String getCursor() {
        return this.cursor;
    }

    public boolean getHasMore() {
        return this.hasMore;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.namespaces, this.cursor, Boolean.valueOf(this.hasMore)});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x002e, code lost:
        if (r2.equals(r3) == false) goto L_0x0037;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0034, code lost:
        if (r4.hasMore != r5.hasMore) goto L_0x0037;
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
            if (r2 == 0) goto L_0x0039
            com.dropbox.core.v2.team.TeamNamespacesListResult r5 = (com.dropbox.core.p005v2.team.TeamNamespacesListResult) r5
            java.util.List<com.dropbox.core.v2.team.NamespaceMetadata> r2 = r4.namespaces
            java.util.List<com.dropbox.core.v2.team.NamespaceMetadata> r3 = r5.namespaces
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0037
        L_0x0024:
            java.lang.String r2 = r4.cursor
            java.lang.String r3 = r5.cursor
            if (r2 == r3) goto L_0x0030
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0037
        L_0x0030:
            boolean r2 = r4.hasMore
            boolean r5 = r5.hasMore
            if (r2 != r5) goto L_0x0037
            goto L_0x0038
        L_0x0037:
            r0 = 0
        L_0x0038:
            return r0
        L_0x0039:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.team.TeamNamespacesListResult.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
