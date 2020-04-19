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

/* renamed from: com.dropbox.core.v2.team.MembersSetPermissionsResult */
public class MembersSetPermissionsResult {
    protected final AdminTier role;
    protected final String teamMemberId;

    /* renamed from: com.dropbox.core.v2.team.MembersSetPermissionsResult$Serializer */
    static class Serializer extends StructSerializer<MembersSetPermissionsResult> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(MembersSetPermissionsResult membersSetPermissionsResult, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("team_member_id");
            StoneSerializers.string().serialize(membersSetPermissionsResult.teamMemberId, jsonGenerator);
            jsonGenerator.writeFieldName("role");
            Serializer.INSTANCE.serialize(membersSetPermissionsResult.role, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public MembersSetPermissionsResult deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            String str2 = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                AdminTier adminTier = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("team_member_id".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("role".equals(currentName)) {
                        adminTier = Serializer.INSTANCE.deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"team_member_id\" missing.");
                } else if (adminTier != null) {
                    MembersSetPermissionsResult membersSetPermissionsResult = new MembersSetPermissionsResult(str2, adminTier);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(membersSetPermissionsResult, membersSetPermissionsResult.toStringMultiline());
                    return membersSetPermissionsResult;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"role\" missing.");
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

    public MembersSetPermissionsResult(String str, AdminTier adminTier) {
        if (str != null) {
            this.teamMemberId = str;
            if (adminTier != null) {
                this.role = adminTier;
                return;
            }
            throw new IllegalArgumentException("Required value for 'role' is null");
        }
        throw new IllegalArgumentException("Required value for 'teamMemberId' is null");
    }

    public String getTeamMemberId() {
        return this.teamMemberId;
    }

    public AdminTier getRole() {
        return this.role;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.teamMemberId, this.role});
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
            com.dropbox.core.v2.team.MembersSetPermissionsResult r5 = (com.dropbox.core.p005v2.team.MembersSetPermissionsResult) r5
            java.lang.String r2 = r4.teamMemberId
            java.lang.String r3 = r5.teamMemberId
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0031
        L_0x0024:
            com.dropbox.core.v2.team.AdminTier r2 = r4.role
            com.dropbox.core.v2.team.AdminTier r5 = r5.role
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
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.team.MembersSetPermissionsResult.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
