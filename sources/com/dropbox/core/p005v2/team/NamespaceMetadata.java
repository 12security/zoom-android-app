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
import java.util.regex.Pattern;

/* renamed from: com.dropbox.core.v2.team.NamespaceMetadata */
public class NamespaceMetadata {
    protected final String name;
    protected final String namespaceId;
    protected final NamespaceType namespaceType;
    protected final String teamMemberId;

    /* renamed from: com.dropbox.core.v2.team.NamespaceMetadata$Serializer */
    static class Serializer extends StructSerializer<NamespaceMetadata> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(NamespaceMetadata namespaceMetadata, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("name");
            StoneSerializers.string().serialize(namespaceMetadata.name, jsonGenerator);
            jsonGenerator.writeFieldName("namespace_id");
            StoneSerializers.string().serialize(namespaceMetadata.namespaceId, jsonGenerator);
            jsonGenerator.writeFieldName("namespace_type");
            Serializer.INSTANCE.serialize(namespaceMetadata.namespaceType, jsonGenerator);
            if (namespaceMetadata.teamMemberId != null) {
                jsonGenerator.writeFieldName("team_member_id");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(namespaceMetadata.teamMemberId, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public NamespaceMetadata deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
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
                NamespaceType namespaceType = null;
                String str4 = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("name".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("namespace_id".equals(currentName)) {
                        str3 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("namespace_type".equals(currentName)) {
                        namespaceType = Serializer.INSTANCE.deserialize(jsonParser);
                    } else if ("team_member_id".equals(currentName)) {
                        str4 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"name\" missing.");
                } else if (str3 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"namespace_id\" missing.");
                } else if (namespaceType != null) {
                    NamespaceMetadata namespaceMetadata = new NamespaceMetadata(str2, str3, namespaceType, str4);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(namespaceMetadata, namespaceMetadata.toStringMultiline());
                    return namespaceMetadata;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"namespace_type\" missing.");
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

    public NamespaceMetadata(String str, String str2, NamespaceType namespaceType2, String str3) {
        if (str != null) {
            this.name = str;
            if (str2 == null) {
                throw new IllegalArgumentException("Required value for 'namespaceId' is null");
            } else if (Pattern.matches("[-_0-9a-zA-Z:]+", str2)) {
                this.namespaceId = str2;
                if (namespaceType2 != null) {
                    this.namespaceType = namespaceType2;
                    this.teamMemberId = str3;
                    return;
                }
                throw new IllegalArgumentException("Required value for 'namespaceType' is null");
            } else {
                throw new IllegalArgumentException("String 'namespaceId' does not match pattern");
            }
        } else {
            throw new IllegalArgumentException("Required value for 'name' is null");
        }
    }

    public NamespaceMetadata(String str, String str2, NamespaceType namespaceType2) {
        this(str, str2, namespaceType2, null);
    }

    public String getName() {
        return this.name;
    }

    public String getNamespaceId() {
        return this.namespaceId;
    }

    public NamespaceType getNamespaceType() {
        return this.namespaceType;
    }

    public String getTeamMemberId() {
        return this.teamMemberId;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.name, this.namespaceId, this.namespaceType, this.teamMemberId});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0048, code lost:
        if (r2.equals(r5) == false) goto L_0x004b;
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
            if (r2 == 0) goto L_0x004d
            com.dropbox.core.v2.team.NamespaceMetadata r5 = (com.dropbox.core.p005v2.team.NamespaceMetadata) r5
            java.lang.String r2 = r4.name
            java.lang.String r3 = r5.name
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x004b
        L_0x0024:
            java.lang.String r2 = r4.namespaceId
            java.lang.String r3 = r5.namespaceId
            if (r2 == r3) goto L_0x0030
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x004b
        L_0x0030:
            com.dropbox.core.v2.team.NamespaceType r2 = r4.namespaceType
            com.dropbox.core.v2.team.NamespaceType r3 = r5.namespaceType
            if (r2 == r3) goto L_0x003c
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x004b
        L_0x003c:
            java.lang.String r2 = r4.teamMemberId
            java.lang.String r5 = r5.teamMemberId
            if (r2 == r5) goto L_0x004c
            if (r2 == 0) goto L_0x004b
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x004b
            goto L_0x004c
        L_0x004b:
            r0 = 0
        L_0x004c:
            return r0
        L_0x004d:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.team.NamespaceMetadata.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
