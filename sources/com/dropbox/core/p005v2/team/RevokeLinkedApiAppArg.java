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

/* renamed from: com.dropbox.core.v2.team.RevokeLinkedApiAppArg */
public class RevokeLinkedApiAppArg {
    protected final String appId;
    protected final boolean keepAppFolder;
    protected final String teamMemberId;

    /* renamed from: com.dropbox.core.v2.team.RevokeLinkedApiAppArg$Serializer */
    static class Serializer extends StructSerializer<RevokeLinkedApiAppArg> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(RevokeLinkedApiAppArg revokeLinkedApiAppArg, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("app_id");
            StoneSerializers.string().serialize(revokeLinkedApiAppArg.appId, jsonGenerator);
            jsonGenerator.writeFieldName("team_member_id");
            StoneSerializers.string().serialize(revokeLinkedApiAppArg.teamMemberId, jsonGenerator);
            jsonGenerator.writeFieldName("keep_app_folder");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(revokeLinkedApiAppArg.keepAppFolder), jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public RevokeLinkedApiAppArg deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            String str2 = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                Boolean valueOf = Boolean.valueOf(true);
                String str3 = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("app_id".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("team_member_id".equals(currentName)) {
                        str3 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("keep_app_folder".equals(currentName)) {
                        valueOf = (Boolean) StoneSerializers.boolean_().deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"app_id\" missing.");
                } else if (str3 != null) {
                    RevokeLinkedApiAppArg revokeLinkedApiAppArg = new RevokeLinkedApiAppArg(str2, str3, valueOf.booleanValue());
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(revokeLinkedApiAppArg, revokeLinkedApiAppArg.toStringMultiline());
                    return revokeLinkedApiAppArg;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"team_member_id\" missing.");
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

    public RevokeLinkedApiAppArg(String str, String str2, boolean z) {
        if (str != null) {
            this.appId = str;
            if (str2 != null) {
                this.teamMemberId = str2;
                this.keepAppFolder = z;
                return;
            }
            throw new IllegalArgumentException("Required value for 'teamMemberId' is null");
        }
        throw new IllegalArgumentException("Required value for 'appId' is null");
    }

    public RevokeLinkedApiAppArg(String str, String str2) {
        this(str, str2, true);
    }

    public String getAppId() {
        return this.appId;
    }

    public String getTeamMemberId() {
        return this.teamMemberId;
    }

    public boolean getKeepAppFolder() {
        return this.keepAppFolder;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.appId, this.teamMemberId, Boolean.valueOf(this.keepAppFolder)});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x002e, code lost:
        if (r2.equals(r3) == false) goto L_0x0037;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0034, code lost:
        if (r4.keepAppFolder != r5.keepAppFolder) goto L_0x0037;
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
            com.dropbox.core.v2.team.RevokeLinkedApiAppArg r5 = (com.dropbox.core.p005v2.team.RevokeLinkedApiAppArg) r5
            java.lang.String r2 = r4.appId
            java.lang.String r3 = r5.appId
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0037
        L_0x0024:
            java.lang.String r2 = r4.teamMemberId
            java.lang.String r3 = r5.teamMemberId
            if (r2 == r3) goto L_0x0030
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0037
        L_0x0030:
            boolean r2 = r4.keepAppFolder
            boolean r5 = r5.keepAppFolder
            if (r2 != r5) goto L_0x0037
            goto L_0x0038
        L_0x0037:
            r0 = 0
        L_0x0038:
            return r0
        L_0x0039:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.team.RevokeLinkedApiAppArg.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
